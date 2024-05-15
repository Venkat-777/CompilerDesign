package crux.ir;

import crux.ast.SymbolTable.Symbol;
import crux.ast.*;
import crux.ast.OpExpr.Operation;
import crux.ast.traversal.NodeVisitor;
import crux.ast.types.*;
import crux.ir.insts.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    visit(ast);
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
    visit(varDecl); //arrayDecl are varDecl but with ArrayType
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
    var localVar = mCurrentFunction.getTempVar(tempType);

    if (mCurrentLocalVarMap.containsKey(name.getSymbol())) //scope is local
    {
      return new InstPair(new NopInst(), localVar);
    }
    else //scope is global
    {
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
  public InstPair visit(Assignment assignment) {
    return null;
  }

  /**
   * Lower a Call.
   */
  @Override
  public InstPair visit(Call call) {
    return null;
  }

  /**
   * Handle operations like arithmetics and comparisons. Also handle logical operations (and,
   * or, not).
   */
  @Override
  public InstPair visit(OpExpr operation) {
    return null;
  }

  private InstPair visit(Expression expression) {
    return null;
  }

  /**
   * It should compute the address into the array, do the load, and return the value in a LocalVar.
   */
  @Override
  public InstPair visit(ArrayAccess access)
  {
    var baseType = access.getBase().getType();
    var tempLocalVar = mCurrentFunction.getTempVar(baseType);
    var index = access.getIndex().accept(this);
    var addressVar = mCurrentFunction.getTempAddressVar(baseType);
    var addressInst = new AddressAt(addressVar, access.getBase(), tempLocalVar);
    var load = new LoadInst(tempLocalVar, addressVar);
    connect(index, addressInst);
    connect(addressInst, load);
    return new InstPair(index.getStart(), load, tempLocalVar);
  }

  /**
   * Copy the literal into a tempVar
   */
  @Override
  public InstPair visit(LiteralBool literalBool) {
    return null;
  }

  /**
   * Copy the literal into a tempVar
   */
  @Override
  public InstPair visit(LiteralInt literalInt) {
    return null;
  }

  /**
   * Lower a Return.
   */
  @Override
  public InstPair visit(Return ret) {
    return null;
  }

  /**
   * Break Node
   */
  @Override
  public InstPair visit(Break brk) {
    return null;
  }

  /**
   * Continue Node
   */
  @Override
  public InstPair visit(Continue cont) {
    return null;
  }

  /**
   * Implement If Then Else statements.
   */
  @Override
  public InstPair visit(IfElseBranch ifElseBranch) {
    return null;
  }

  /**
   * Implement loops.
   */
  @Override
  public InstPair visit(Loop loop) {
    return null;
  }
}
