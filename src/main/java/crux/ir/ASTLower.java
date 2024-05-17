package crux.ir;

import crux.ast.SymbolTable.Symbol;
import crux.ast.*;
import crux.ast.OpExpr.Operation;
import crux.ast.traversal.NodeVisitor;
import crux.ast.types.*;
import crux.ir.insts.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class InstPair
{
  Instruction start, end;
  Value val;
  public InstPair(Instruction start, Instruction end, Value val)
  {
    this.start = start;
    this.end = end;
    this.val = val;
  }

  public InstPair(Instruction one, Value val)
  {
    this.start = one;
    this.end = one;
    this.val = val;
  }

  public InstPair(Instruction start, Instruction end)
  {
    this.start = start;
    this.end = end;
    this.val = null;
  }

  public InstPair(Instruction one)
  {
    this.start = one;
    this.end = one;
    this.val = null;
  }

  public Instruction getStart() {
    return start;
  }

  public Instruction getEnd() {
    return end;
  }

  public void setVal(Value val) {
    this.val = val;
  }

  public Value getVal() {
    return val;
  }
}


/**
 * Convert AST to IR and build the CFG
 */
public final class ASTLower implements NodeVisitor<InstPair> {
  private Program mCurrentProgram = null;
  private Function mCurrentFunction = null;

  private Map<Symbol, LocalVar> mCurrentLocalVarMap = null;

  /**
   * A constructor to initialize member variables
   */
  public ASTLower() {}

  public Program lower(DeclarationList ast) {
    ast.accept(this);
    return mCurrentProgram;
  }

  public InstPair connect(InstPair startPair, InstPair endPair)
  {
    startPair.getEnd().setNext(0, endPair.getStart());
    return new InstPair(startPair.getStart(), endPair.getEnd());
  }

  public InstPair connect(InstPair startPair, Instruction inst)
  {
    startPair.getEnd().setNext(0, inst);
    return new InstPair(startPair.getStart(), inst);
  }

  public InstPair connect(Instruction inst, InstPair endPair)
  {
    inst.setNext(0, endPair.getStart());
    return new InstPair(inst, endPair.getEnd());
  }

  public InstPair connect(Instruction start, Instruction end)
  {
    start.setNext(0, end);
    return new InstPair(start, end);
  }


  @Override
  public InstPair visit(DeclarationList declarationList)
  {
    mCurrentProgram = new Program();
    for (var declaration : declarationList.getChildren())
    {
      declaration.accept(this);
    }
    return null;
  }

  /**
   * This visitor should create a Function instance for the functionDefinition node, add parameters
   * to the localVarMap, add the function to the program, and init the function start Instruction.
   */
  @Override
  public InstPair visit(FunctionDefinition functionDefinition)
  {
    mCurrentFunction = new Function(functionDefinition.getSymbol().getName(),
                                    (FuncType) functionDefinition.getSymbol().getType());
    mCurrentLocalVarMap = new HashMap<Symbol, LocalVar>();
    List<LocalVar> args = new ArrayList<>();
    for (var argument : functionDefinition.getParameters())
    {
      var localVar = mCurrentFunction.getTempVar(argument.getType());
      mCurrentLocalVarMap.put(argument, localVar);
      args.add(localVar);
    }
    mCurrentFunction.setArguments(args);
    mCurrentProgram.addFunction(mCurrentFunction);
    var inst = functionDefinition.getStatements().accept(this);
    mCurrentFunction.setStart(inst.getStart());
    mCurrentFunction = null;
    mCurrentLocalVarMap = null;
    return null;
  }

  @Override
  public InstPair visit(StatementList statementList)
  {
    var startInst = new NopInst();
    Instruction currentInst = startInst;
    for (var stmt : statementList.getChildren())
    {
      var currentInstPair = stmt.accept(this);
      currentInst.setNext(0, currentInstPair.getStart());
      currentInst = currentInstPair.getEnd();
    }
    return new InstPair(startInst, currentInst); //value is null
  }

  /**
   * Declarations, could be either local or Global
   */
  @Override
  public InstPair visit(VariableDeclaration variableDeclaration)
  {
    Symbol varDeclSym = variableDeclaration.getSymbol();
    if (mCurrentFunction == null)
    {
      IntegerConstant value;
      if (varDeclSym.getType().toString().startsWith("array"))
      {
        var arraySym = (ArrayType) varDeclSym.getType();
        value = IntegerConstant.get(mCurrentProgram, arraySym.getExtent());
      } else
      {
        value = IntegerConstant.get(mCurrentProgram, 1);
      }
      mCurrentProgram.addGlobalVar(new GlobalDecl(varDeclSym, value));
    }
    else
    {
      var tempVar = mCurrentFunction.getTempVar(varDeclSym.getType(), varDeclSym.getName());
      mCurrentLocalVarMap.put(varDeclSym, tempVar);
    }
    return new InstPair(new NopInst());
  }

  /**
   * Create a declaration for array and connected it to the CFG
   */
  @Override
  public InstPair visit(ArrayDeclaration arrayDeclaration)
  {
    var varDecl = new VariableDeclaration(arrayDeclaration.getPosition(), arrayDeclaration.getSymbol());
    varDecl.accept(this); //arrayDecl are varDecl but with ArrayType
    return null;
  }

  /**
   * LookUp the name in the map(s). For globals, we should do a load to get the value to load into a
   * LocalVar.
   */
  @Override
  public InstPair visit(VarAccess name)
  {
    var tempType = name.getSymbol().getType();
    if (mCurrentLocalVarMap.containsKey(name.getSymbol())) //scope is local
    {
      var localVar = mCurrentLocalVarMap.get(name.getSymbol());
      return new InstPair(new NopInst(), localVar);
    }
    else //scope is global
    {
      var localVar = mCurrentFunction.getTempVar(tempType);
      var addressVar = mCurrentFunction.getTempAddressVar(tempType);
      var address = new AddressAt(addressVar, name.getSymbol());
      var load = new LoadInst(localVar, addressVar);
      connect(address, load);
      return new InstPair(address, load, localVar);
    }
  }

  /**
   * If the location is a VarAccess to a LocalVar, copy the value to it. If the location is a
   * VarAccess to a global, store the value. If the location is ArrayAccess, store the value.
   */
  @Override
  public InstPair visit(Assignment assignment)
  {
      var lhs = assignment.getLocation(); //don't visit lhs
      var rhs = assignment.getValue().accept(this); //visit rhs

      if (lhs instanceof VarAccess)
      {
          if (mCurrentLocalVarMap.containsKey(((VarAccess) lhs).getSymbol())) //scope is local
          {
              var destVar = mCurrentLocalVarMap.get(((VarAccess) lhs).getSymbol());
              var copy = new CopyInst(destVar, rhs.getVal());
              connect(rhs, copy);
              return new InstPair(rhs.getStart(), copy);
          }
          else //scope is global
          {
              var addressVar = mCurrentFunction.getTempAddressVar(((VarAccess) lhs).getType());
              var addressAt = new AddressAt(addressVar, ((VarAccess) lhs).getSymbol());
              var store = new StoreInst((LocalVar) rhs.getVal(), addressVar);
              connect(addressAt, rhs);
              connect(rhs, store);
              return new InstPair(addressAt, store);
          }
      } else //lhs is ArrayAccess
      {
          var index = ((ArrayAccess) lhs).getIndex().accept(this);
          var addressVar = mCurrentFunction.getTempAddressVar(((ArrayAccess) lhs).getType());
          var addressAt = new AddressAt(addressVar, ((ArrayAccess) lhs).getBase(), (LocalVar) index.getVal());
          var store = new StoreInst((LocalVar) rhs.getVal(), addressVar);
          connect(index, addressAt);
          connect(addressAt, rhs);
          connect(rhs, store);
          return new InstPair(index.getStart(), store);
      }
  }

  /**
   * Lower a Call.
   */
  @Override
  public InstPair visit(Call call)
  {
    var startInstPair = new InstPair(new NopInst());
    InstPair prevInstPair = startInstPair;
    List<LocalVar> params = new ArrayList<>();
    for (var arg : call.getArguments())
    {
      var argInstPair = arg.accept(this);
      connect(prevInstPair, argInstPair);

      var argLocalVar = (LocalVar) argInstPair.getVal();
      params.add(argLocalVar);

      prevInstPair = argInstPair;
    }

    CallInst callInst;
    var calleeType = call.getCallee().getType();
    var funcType = (FuncType) calleeType;
    if (funcType.getRet() instanceof VoidType)
    {
      callInst = new CallInst(call.getCallee(), params);
    }
    else
    {
      var localVar = mCurrentFunction.getTempVar(funcType.getRet());
      callInst = new CallInst(localVar, call.getCallee(), params);
    }
    connect(prevInstPair, callInst);
    return new InstPair (startInstPair.getStart(), callInst);
  }

  /**
   * Handle operations like arithmetics and comparisons. Also handle logical operations (and,
   * or, not).
   */
  @Override
  public InstPair visit(OpExpr operation) {
      Operation op = operation.getOp();
      InstPair lhs = operation.getLeft().accept(this);
      InstPair rhs = null;
      if(operation.getRight()!=null) //if rhs exists
          rhs = operation.getRight().accept(this);

      LocalVar dest = mCurrentFunction.getTempVar(operation.getType());
      BinaryOperator bo;
      CompareInst co;
      UnaryNotInst uo;

      switch (op.name())
      {
          case "ADD":
              bo = new BinaryOperator(BinaryOperator.Op.Add,
                      dest,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              connect(lhs, rhs);
              connect(rhs, bo);
              return new InstPair(lhs.getStart(), bo, dest);
          case "SUB":
              bo = new BinaryOperator(BinaryOperator.Op.Sub,
                      dest,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,bo);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), bo, dest);
          case "MUL":
              bo = new BinaryOperator(BinaryOperator.Op.Mul,
                      dest,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,bo);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), bo, dest);
          case "DIV":
              bo = new BinaryOperator(BinaryOperator.Op.Div,
                      dest,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,bo);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), bo, dest);
          case "GE":
              co = new CompareInst(
                      dest,
                      CompareInst.Predicate.GE,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,co);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), co, dest);
          case "GT":
              co = new CompareInst(dest,
                      CompareInst.Predicate.GT,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,co);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), co, dest);
          case "LE":
              co = new CompareInst(dest,
                      CompareInst.Predicate.LE,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,co);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), co, dest);
          case "LT":
              co = new CompareInst(dest,
                      CompareInst.Predicate.LT,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,co);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), co, dest);
          case "EQ":
              co = new CompareInst(dest,
                      CompareInst.Predicate.EQ,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,co);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), co, dest);
          case "NE":
              co = new CompareInst(dest,
                      CompareInst.Predicate.NE,
                      (LocalVar)lhs.getVal(),
                      (LocalVar)rhs.getVal() );
              rhs.getEnd().setNext(0,co);
              lhs.getEnd().setNext(0, rhs.getStart());
              return new InstPair(lhs.getStart(), co, dest);
          case "LOGIC_NOT":
              uo = new UnaryNotInst(dest,(LocalVar)lhs.getVal());
              lhs.getEnd().setNext(0, uo);
              return new InstPair(lhs.getStart(), uo, dest);
          default:
              return new InstPair(new NopInst());
      }
  }

  /**
   * It should compute the address into the array, do the load, and return the value in a LocalVar.
   */
  @Override
  public InstPair visit(ArrayAccess access)
  {
    var arrayType = (ArrayType) access.getBase().getType();
    var baseType = arrayType.getBase(); //get the array's type
    var tempLocalVar = mCurrentFunction.getTempVar(baseType);
    var index = access.getIndex().accept(this);
    var addressVar = mCurrentFunction.getTempAddressVar(baseType);
    var addressInst = new AddressAt(addressVar, access.getBase(), (LocalVar) index.getVal());
    var load = new LoadInst(tempLocalVar, addressVar);
    connect(index, addressInst);
    connect(addressInst, load);
    return new InstPair(index.getStart(), load, tempLocalVar);
  }

  /**
   * Copy the literal into a tempVar
   */
  @Override
  public InstPair visit(LiteralBool literalBool)
  {
    var destVar = mCurrentFunction.getTempVar(literalBool.getType());
    var boolConst = BooleanConstant.get(mCurrentProgram, literalBool.getValue());
    var copy = new CopyInst(destVar, boolConst);
    return new InstPair(copy, destVar);
  }

  /**
   * Copy the literal into a tempVar
   */
  @Override
  public InstPair visit(LiteralInt literalInt)
  {
    var destVar = mCurrentFunction.getTempVar(literalInt.getType());
    var intConst = IntegerConstant.get(mCurrentProgram, literalInt.getValue());
    var copy = new CopyInst(destVar, intConst);
    return new InstPair(copy, destVar);
  }

  /**
   * Lower a Return.
   */
  @Override
  public InstPair visit(Return ret)
  {
    var index = ret.getValue().accept(this);
    var retInst =  new ReturnInst((LocalVar) index.getVal());
    connect(index, retInst);
    return new InstPair(index.getStart(), retInst);
  }

  /**
   * Break Node
   */
  @Override
  public InstPair visit(Break brk)
  {
    return new InstPair(loopExit, new NopInst());
  }

  /**
   * Continue Node
   */
  @Override
  public InstPair visit(Continue cont)
  {
    return new InstPair(loopHead, new NopInst());
  }

  /**
   * Implement If Then Else statements.
   */
  @Override
  public InstPair visit(IfElseBranch ifElseBranch)
  {
    var cond = ifElseBranch.getCondition().accept(this);
    var jump = new JumpInst((LocalVar) cond.getVal());
    var thenBlock = ifElseBranch.getThenBlock().accept(this);
    var elseBlock = ifElseBranch.getElseBlock().accept(this);
    jump.setNext(1, thenBlock.getStart()); //true
    jump.setNext(0, elseBlock.getStart()); //false
    var nop = new NopInst();
    connect(thenBlock, nop);
    connect(elseBlock, nop);
    return new InstPair(cond.getStart(), nop);
  }

  private Instruction loopHead;
  private Instruction loopExit;

  /**
   * Implement loops.
   */
  @Override
  public InstPair visit(Loop loop)
  {
    loopHead = new NopInst();
    var body = loop.getBody().accept(this);
    connect(body.getEnd(), body.getStart());
    return new InstPair(loopHead, loopExit);
  }
}
