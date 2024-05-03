package crux.ast.types;

import crux.ast.SymbolTable.Symbol;
import crux.ast.*;
import crux.ast.traversal.NullNodeVisitor;
import crux.ir.Variable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will associate types with the AST nodes from Stage 2
 */
public final class TypeChecker {
  private final ArrayList<String> errors = new ArrayList<>();

  public ArrayList<String> getErrors() {
    return errors;
  }

  public void check(DeclarationList ast) {
    var inferenceVisitor = new TypeInferenceVisitor();
    inferenceVisitor.visit(ast);
  }

  /**
   * Helper function, should be used to add error into the errors array
   */
  private void addTypeError(Node n, String message) {
    errors.add(String.format("TypeError%s[%s]", n.getPosition(), message));
  }

  /**
   * Helper function, should be used to record Types if the Type is an ErrorType then it will call
   * addTypeError
   */
  private void setNodeType(Node n, Type ty) {
    ((BaseNode) n).setType(ty);
    if (ty.getClass() == ErrorType.class) {
      var error = (ErrorType) ty;
      addTypeError(n, error.getMessage());
    }
  }

  /**
   * Helper to retrieve Type from the map
   */
  public Type getType(Node n) {
    return ((BaseNode) n).getType();
  }

  public Symbol currentFunctionSymbol;
  public boolean lastStatementReturns, hasBreak;

  /**
   * This calls will visit each AST node and try to resolve it's type with the help of the
   * symbolTable.
   */
  private final class TypeInferenceVisitor extends NullNodeVisitor<Void> {

    @Override
    public Void visit(VarAccess vaccess)
    {
      setNodeType(vaccess, vaccess.getSymbol().getType());
      return null;
    }

    @Override
    public Void visit(ArrayDeclaration arrayDeclaration)
    {
      var arrayType = (ArrayType) arrayDeclaration.getSymbol().getType();
      if (arrayType.getBase() instanceof BoolType ||
          arrayType.getBase() instanceof IntType)
        lastStatementReturns = false;
      else
        addTypeError(arrayDeclaration, "Not an int or bool type (arraydecl)");
      return null;
    }

    @Override
    public Void visit(Assignment assignment)
    {
        assignment.getLocation().accept(this);
        assignment.getValue().accept(this);

        var locationType = getType(assignment.getLocation());
        var valueType = getType(assignment.getValue());

        locationType.assign(valueType);
        setNodeType(assignment, locationType);

        lastStatementReturns = false;
        return null;
    }

    @Override
    public Void visit(Break brk)
    {
      hasBreak = true;
      lastStatementReturns = false;
      return null;
    }

    @Override
    public Void visit(Call call)
    {
        TypeList argumentListType = new TypeList();
        for (var child: call.getChildren())
        {
            child.accept(this);
            argumentListType.append(getType(child));
        }
        var calleType = call.getCallee().getType();
        calleType.call(argumentListType);
        lastStatementReturns = false;
        return null;
    }

    @Override
    public Void visit(Continue cont)
    {
        lastStatementReturns = false;
        return null;
    }
    
    @Override
    public Void visit(DeclarationList declarationList)
    {
      for (var child : declarationList.getChildren())
      {
        child.accept(this);
      }
      return null;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition)
    {
      currentFunctionSymbol = functionDefinition.getSymbol();
      var currentFunctionType = (FuncType) functionDefinition.getSymbol().getType();
      if (currentFunctionSymbol.getName().equals("main"))
      {
          if (!(currentFunctionType.getRet() instanceof VoidType))
              addTypeError(functionDefinition, "main type is not void");
          if (!currentFunctionType.getArgs().isEmpty())
              addTypeError(functionDefinition, "main should have no arguments");
      } else
      {
          if (!currentFunctionType.getArgs().isEmpty()) //if there are parameters
          {
              for (var args : functionDefinition.getParameters())
              {
                  if (!(args.getType() instanceof IntType || args.getType() instanceof BoolType))
                      addTypeError(functionDefinition, "arguments is not Int or Bool");
              }
          }
      }
      functionDefinition.getStatements().accept(this);
      if (!(currentFunctionType.getRet() instanceof VoidType) && !lastStatementReturns)
        addTypeError(functionDefinition, "return not stated");
      return null;
    }

    @Override
    public Void visit(IfElseBranch ifElseBranch)
    {
      ifElseBranch.getCondition().accept(this);
      var conditionType = getType(ifElseBranch.getCondition());
      if (!(conditionType instanceof BoolType))
          addTypeError(ifElseBranch, "Condition is not BoolType");
      ifElseBranch.getThenBlock().accept(this);
      boolean thenReturns = lastStatementReturns;
      if (ifElseBranch.getElseBlock() != null)
          ifElseBranch.getElseBlock().accept(this);

      lastStatementReturns &= thenReturns;
      return null;
    }

    @Override
    public Void visit(ArrayAccess access)
    {
      access.getIndex().accept(this);
      var arrayType = access.getBase().getType();
      var indexType = getType(access.getIndex());
      arrayType.index(indexType);
      setNodeType(access, indexType);
      return null;
    }

    @Override
    public Void visit(LiteralBool literalBool)
    {
      setNodeType(literalBool, new BoolType());
      return null;
    }

    @Override
    public Void visit(LiteralInt literalInt)
    {
      setNodeType(literalInt, new IntType());
      return null;
    }

    @Override
    public Void visit(Loop loop)
    {
      hasBreak = false;
      loop.getBody().accept(this);
      if (!(hasBreak && lastStatementReturns))
          addTypeError(loop, "infinite loop");
      return null;
    }

    @Override
    public Void visit(OpExpr op)
    {
        op.getLeft().accept(this);
        var leftType = getType(op.getLeft());
        if (op.getOp() != OpExpr.Operation.LOGIC_NOT)
        {
            Type resultType;
            op.getRight().accept(this);
            var rightType = getType(op.getRight());
            switch (op.getOp())
            {
                case LE:
                case EQ:
                case GE:
                case GT:
                case LT:
                case NE:
                case LOGIC_OR:
                case LOGIC_AND:
                    resultType = leftType.compare(rightType);
                    break;
                case ADD:
                    resultType = leftType.add(rightType);
                    break;
                case SUB:
                    resultType = leftType.sub(rightType);
                    break;
                case MULT:
                    resultType = leftType.mul(rightType);
                    break;
                case DIV:
                    resultType = leftType.div(rightType);
                    break;
                default:
                    addTypeError(op, "no operator");
                    resultType = null;
            }
            setNodeType(op, resultType);
        } else
        {
            Type resultType = leftType.not();
            setNodeType(op, resultType);
        }
        return null;
    }

    @Override
    public Void visit(Return ret)
    {
      ret.getValue().accept(this);
      var returnExprType = getType(ret.getValue());

      var curentFunction = (FuncType) currentFunctionSymbol.getType();
      var currentFunctionReturnType = curentFunction.getRet();

      if (!returnExprType.equivalent(currentFunctionReturnType))
        addTypeError(ret, "return expression type does not match function return type");

      lastStatementReturns = true;
      return null;
    }

    @Override
    public Void visit(StatementList statementList)
    {
      lastStatementReturns = false;
      for (var child : statementList.getChildren())
      {
        if (lastStatementReturns)
        {
          addTypeError(statementList, "unreachable statement");
          break;
        }
        child.accept(this);
      }
      return null;
    }

    @Override
    public Void visit(VariableDeclaration variableDeclaration)
    {
      var variableType = variableDeclaration.getSymbol().getType();
      if (variableType instanceof BoolType ||
          variableType instanceof IntType)
        lastStatementReturns = false;
      else
        addTypeError(variableDeclaration, "Not a int or bool type (vardecl)");
      return null;
    }
  }
}
