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

  public void addToEnd(InstPair child)
  {
    this.end.setNext(0, child.start);
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
    var inst = functionDefinition.getStatements().accept(this);
    mCurrentFunction.setStart(inst.getStart());
    mCurrentFunction = null;
    mCurrentLocalVarMap = null;
    return null;
  }

  @Override
  public InstPair visit(StatementList statementList)
  {

    return null;
  }

  /**
   * Declarations, could be either local or Global
   */
  @Override
  public InstPair visit(VariableDeclaration variableDeclaration) {
    return null;
  }

  /**
   * Create a declaration for array and connected it to the CFG
   */
  @Override
  public InstPair visit(ArrayDeclaration arrayDeclaration) {
    return null;
  }

  /**
   * LookUp the name in the map(s). For globals, we should do a load to get the value to load into a
   * LocalVar.
   */
  @Override
  public InstPair visit(VarAccess name) {
    return null;
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
  public InstPair visit(ArrayAccess access) {
    return null;
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
