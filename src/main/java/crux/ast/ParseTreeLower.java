package crux.ast;

import crux.ast.*;
import crux.ast.OpExpr.Operation;
import crux.ir.Function;
import crux.ir.insts.BinaryOperator;
import crux.pt.CruxBaseVisitor;
import crux.pt.CruxParser;
import crux.ast.types.*;
import crux.ast.SymbolTable.Symbol;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class will convert the parse tree generated by ANTLR to AST It follows the visitor pattern
 * where decls will be by DeclVisitor Class Stmts will be resolved by StmtVisitor Class Exprs will
 * be resolved by ExprVisitor Class
 */

public final class ParseTreeLower {
  private final DeclVisitor declVisitor = new DeclVisitor();
  private final StmtVisitor stmtVisitor = new StmtVisitor();
  private final ExprVisitor exprVisitor = new ExprVisitor();

  private final SymbolTable symTab;

  public ParseTreeLower(PrintStream err) {
    symTab = new SymbolTable(err);
  }

  private static Position makePosition(ParserRuleContext ctx) {
    var start = ctx.start;
    return new Position(start.getLine());
  }

  /**
   *
   * @return True if any errors
   */
  public boolean hasEncounteredError() {
    return symTab.hasEncounteredError();
  }


  /**
   * Lower top-level parse tree to AST
   *
   * @return a {@link DeclList} object representing the top-level AST.
   */

  public DeclarationList lower(CruxParser.ProgramContext program)
  {
    List<Declaration> decList = new ArrayList<>();
    var decListCtx = program.declList().decl();
    for(var declCtx: decListCtx)
    {
      decList.add(declCtx.accept(declVisitor));
    }
    return new DeclarationList(makePosition(program), decList);
  }

  /**
   * Lower stmt list by lower individual stmt into AST.
   *
   * @return a {@link StmtList} AST object.
   */

  private StatementList lower(CruxParser.StmtListContext stmtList)
  {
    List<Statement> stmtCtxArray = new ArrayList<>();
    var stmtListCtx = stmtList.stmt();
    for(var stmtCtx : stmtListCtx)
    {
      stmtCtxArray.add(stmtCtx.accept(stmtVisitor));
    }
    return new StatementList(makePosition(stmtList), stmtCtxArray);
  }


  /**
   * Similar to {@link #lower(CruxParser.StmtListContext)}, but handles symbol table as well.
   *
   * @return a {@link StmtList} AST object.
   */
  private StatementList lower(CruxParser.StmtBlockContext stmtBlock)
  {
    symTab.enter();
    var stmtBlk = lower(stmtBlock.stmtList());
    symTab.exit();
    return stmtBlk;
  }

  public Type getType(String ctx)
  {
      switch(ctx)
      {
          case("void"):
              return new VoidType();
          case("int"):
              return new IntType();
          case("bool"):
              return new BoolType();
          default:
              return new ErrorType("error: prs lower(119)");
      }
  }

  /**
   * A parse tree visitor to create AST nodes derived from {@link Declaration}
   */
  private final class DeclVisitor extends CruxBaseVisitor<Declaration> {
    /**
     * Visit a parse tree var decl and create an AST {@link VarariableDeclaration}
     *
     * @return an AST {@link VariableDeclaration}
     */
    @Override
    public VariableDeclaration visitVarDecl(CruxParser.VarDeclContext ctx)
    {
      Type ctxType = getType(ctx.type().Identifier().getText());
      Symbol sym = symTab.add(makePosition(ctx), ctx.Identifier().getText(), ctxType);
      return new VariableDeclaration(makePosition(ctx), sym);
    }


    /**
     * Visit a parse tree array decl and creates an AST {@link ArrayDeclaration}
     *
     * @return an AST {@link ArrayDeclaration}
     */
     public Declaration visitArrayDecl(CruxParser.ArrayDeclContext ctx)
     {
       Type ctxType = getType(ctx.type().Identifier().getText());
       Symbol sym = symTab.add(makePosition(ctx), ctx.Identifier().getText(), ctxType);
       return new ArrayDeclaration(makePosition(ctx), sym);
     }

    /**
     * Visit a parse tree function definition and create an AST {@link FunctionDefinition}
     *
     * @return an AST {@link FunctionDefinition}
     */

     @Override
     public Declaration visitFunctionDefn(CruxParser.FunctionDefnContext ctx)
     {
       Type ctxreturnType = getType(ctx.type().Identifier().getText());
       TypeList args = new TypeList();

       Type ctxType;
       List<CruxParser.ParamContext> paramCtxts = ctx.paramList().param();
       for (var paramCtx : paramCtxts)
       {
         ctxType = getType(paramCtx.type().Identifier().getText());
         args.append(ctxType);
       }

       FuncType ctxFuncType = new FuncType(args, ctxreturnType);
       Symbol sym = symTab.add(makePosition(ctx), ctx.Identifier().getText(), ctxFuncType);

       List<Symbol> paramList = new ArrayList<>();
       symTab.enter();
       for (var paramCtx : paramCtxts)
       {
         ctxType = getType(paramCtx.type().Identifier().getText());
         paramList.add(symTab.add(makePosition(paramCtx), paramCtx.Identifier().getText(), ctxType));
       }
       StatementList stmtL = lower(ctx.stmtBlock().stmtList());
       symTab.exit();

       return new FunctionDefinition(makePosition(ctx), sym, paramList, stmtL);
     }

  }

  /**
   * A parse tree visitor to create AST nodes derived from {@link Stmt}
   */

  private final class StmtVisitor extends CruxBaseVisitor<Statement> {
    /**
     * Visit a parse tree var decl and create an AST {@link VariableDeclaration}. Since
     * {@link VariableDeclaration} is both {@link Declaration} and {@link Statement}, we simply
     * delegate this to {@link DeclVisitor#visitArrayDecl(CruxParser.ArrayDeclContext)} which we
     * implement earlier.
     *
     * @return an AST {@link VariableDeclaration}
     */

    @Override
    public Statement visitVarDecl(CruxParser.VarDeclContext ctx)
    {
      DeclVisitor declVisitor1 = new DeclVisitor();
      return declVisitor1.visitVarDecl(ctx);
    }

    /**
     * Visit a parse tree assignment stmt and create an AST {@link Assignment}
     *
     * @return an AST {@link Assignment}
     */

    @Override
    public Statement visitAssignStmt(CruxParser.AssignStmtContext ctx)
    {
      Expression location = ctx.designator().accept(exprVisitor);
      Expression value = ctx.expr0().accept(exprVisitor);
      return new Assignment(makePosition(ctx), location, value);
    }


    /**
     * Visit a parse tree call stmt and create an AST {@link Call}. Since {@link Call} is both
     * {@link Expression} and {@link Statementt}, we simply delegate this to
     * {@link ExprVisitor#visitCallExpr(CruxParser.CallExprContext)} that we will implement later.
     *
     * @return an AST {@link Call}
     */
     @Override
     public Statement visitCallStmt(CruxParser.CallStmtContext ctx)
     {
       return ctx.callExpr().accept(stmtVisitor);
     }


    /**
     * Visit a parse tree if-else branch and create an AST {@link IfElseBranch}. The template code
     * shows partial implementations that visit the then block and else block recursively before
     * using those returned AST nodes to construct {@link IfElseBranch} object.
     *
     * @return an AST {@link IfElseBranch}
     */

     @Override
     public Statement visitIfStmt(CruxParser.IfStmtContext ctx)
     {
       Expression condition = ctx.expr0().accept(exprVisitor);
       StatementList thenBlock = lower(ctx.stmtBlock(0));
       StatementList elseBlock = lower(ctx.stmtBlock(1));

       return new IfElseBranch(makePosition(ctx), condition, thenBlock, elseBlock);
     }


    /**
     * Visit a parse tree for loop and create an AST {@link Loop}. You'll going to use a similar
     * techniques as {@link #visitIfStmt(CruxParser.IfStmtContext)} to decompose this construction.
     *
     * @return an AST {@link Loop}
     */

     @Override
     public Statement visitLoopStmt(CruxParser.LoopStmtContext ctx)
     {
       StatementList body = lower(ctx.stmtBlock());
       return new Loop(makePosition(ctx), body);
     }


    /**
     * Visit a parse tree return stmt and create an AST {@link Return}. Here we show a simple
     * example of how to lower a simple parse tree construction.
     *
     * @return an AST {@link Return}
     */
    @Override
    public Statement visitReturnStmt(CruxParser.ReturnStmtContext ctx)
    {
      Expression value = ctx.expr0().accept(exprVisitor);
      return new Return(makePosition(ctx), value);
    }

    /**
     * Creates a Break node
     */
    @Override
    public Statement visitBreakStmt(CruxParser.BreakStmtContext ctx)
    {
      return new Break(makePosition(ctx));
    }

    /**
     * Creates a Continue node
     */
    @Override
    public Statement visitContinueStmt(CruxParser.ContinueStmtContext ctx)
    {
      return new Continue(makePosition(ctx));
    }
  }

  private final class ExprVisitor extends CruxBaseVisitor<Expression> {
    /**
     * Parse Expr0 to OpExpr Node Parsing the expr should be exactly as described in the grammar
     */
    @Override
    public Expression visitExpr0(CruxParser.Expr0Context ctx)
    {
        if (ctx.op0() == null)
        {
            return ctx.expr1(0).accept(exprVisitor);
        }

        Operation op;
        switch (ctx.op0().getText())
        {
            case(">="):
                op = Operation.GE;
                break;
            case("<="):
                op = Operation.LE;
                break;
            case("!="):
                op = Operation.NE;
                break;
            case("=="):
                op = Operation.EQ;
                break;
            case(">"):
                op = Operation.GT;
                break;
            case("<"):
                op = Operation.LT;
                break;
            default:
                op = null;
        }
        Expression left = ctx.expr1(0).accept(exprVisitor);
        Expression right = ctx.expr1(1).accept(exprVisitor);
        return new OpExpr(makePosition(ctx), op, left, right);
    }

    /**
     * Parse Expr1 to OpExpr Node Parsing the expr should be exactly as described in the grammar
     */
    @Override
    public Expression visitExpr1(CruxParser.Expr1Context ctx)
    {
        if (ctx.op1() == null)
        {
            return ctx.expr2().accept(exprVisitor);
        } else
        {
            Expression left = ctx.expr1().accept(exprVisitor);
            Expression right = ctx.expr2().accept(exprVisitor);
            Operation op;
            switch (ctx.op1().getText())
            {
                case("+"):
                    op = Operation.ADD;
                case("-"):
                    op = Operation.SUB;
                case("||"):
                    op = Operation.LOGIC_OR;
                default:
                    op = null;
            }
            return new OpExpr(makePosition(ctx), op, left, right);
        }
    }


    /**
     * Parse Expr2 to OpExpr Node Parsing the expr should be exactly as described in the grammar
     */
    @Override
    public Expression visitExpr2(CruxParser.Expr2Context ctx)
    {
        if (ctx.op2() == null)
        {
            return ctx.expr3().accept(exprVisitor);
        } else
        {
            Expression left = ctx.expr2().accept(exprVisitor);
            Expression right = ctx.expr3().accept(exprVisitor);
            Operation op;
            switch (ctx.op2().getText())
            {
                case("*"):
                    op = Operation.MULT;
                case("/"):
                    op = Operation.DIV;
                case("&&"):
                    op = Operation.LOGIC_AND;
                default:
                    op = null;
            }
            return new OpExpr(makePosition(ctx), op, left, right);
        }
    }

    /**
     * Parse Expr3 to OpExpr Node Parsing the expr should be exactly as described in the grammar
     */
    @Override
    public Expression visitExpr3(CruxParser.Expr3Context ctx)
    {
      if (ctx.designator() != null)
      {
        return ctx.designator().accept(exprVisitor);
      } else if (ctx.callExpr() != null)
      {
        return ctx.callExpr().accept(exprVisitor);
      } else if (ctx.literal() != null)
      {
        return ctx.literal().accept(exprVisitor);
      } else if (ctx.expr0() != null)
      {
        return ctx.expr0().accept(exprVisitor);
      } else
      {
        return ctx.expr3().accept(exprVisitor);
      }
    }

    /**
     * Create an Call Node
     */
    @Override
    public Call visitCallExpr(CruxParser.CallExprContext ctx)
    {
      Symbol callee = symTab.lookup(makePosition(ctx), ctx.Identifier().getText());
      List<CruxParser.Expr0Context> argCtxList = ctx.exprList().expr0();
      List<Expression> args = new ArrayList<>();
      for (var argCtx : argCtxList)
      {
        args.add(argCtx.accept(exprVisitor));
      }
      return new Call(makePosition(ctx), callee, args);
    }

    /**
     * visitDesignator will check for a name or ArrayAccess FYI it should account for the case when
     * the designator was dereferenced
     */
    @Override
    public Expression visitDesignator(CruxParser.DesignatorContext ctx)
    {
        Symbol base = symTab.lookup(makePosition(ctx), ctx.Identifier().getText());
        if (ctx.expr0() != null)
        {
            Expression index = ctx.expr0().accept(exprVisitor);
            return new ArrayAccess(makePosition(ctx), base, index);
        } else
        {
            return new VarAccess(makePosition(ctx), base);
        }
    }

    /**
     * Create an Literal Node
     */
    @Override
    public Expression visitLiteral(CruxParser.LiteralContext ctx)
    {
      if (ctx.getText() == "int")
      {
          long valueInt = Long.parseLong(ctx.getText());
          return new LiteralInt(makePosition(ctx), valueInt);
      } else
      {
        boolean valueBool = Boolean.parseBoolean(ctx.getText());
        return  new LiteralBool(makePosition(ctx), valueBool);
      }
    }
  }
}
