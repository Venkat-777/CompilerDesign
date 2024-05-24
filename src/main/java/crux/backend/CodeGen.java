package crux.backend;

import crux.ast.SymbolTable.Symbol;
import crux.ir.*;
import crux.ir.insts.*;
import crux.printing.IRValueFormatter;

import java.util.*;

/**
 * Convert the CFG into Assembly Instructions
 */
public final class CodeGen extends InstVisitor {
  private final Program p;
  private final CodePrinter out;

  public CodeGen(Program p) {
    this.p = p;
    // Do not change the file name that is outputted or it will
    // break the grader!

    out = new CodePrinter("a.s");
  }

  private HashMap<Variable, Integer> varIndexMap;
  private int varIndex;

  public int getVarIndex(Variable var) //returns Integer value
  {
      return varIndexMap.get(var);
  }

  public void storeVar(Variable var) //stores to bottom of map
  {
    varIndexMap.put(var, varIndexMap.size());
  }

  private String registers[] = {"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};



  /**
   * It should allocate space for globals call genCode for each Function
   */
  public void genCode()
  {
    //TODO
    for (Iterator<GlobalDecl> glob_it = p.getGlobals(); glob_it.hasNext();)
    {
      GlobalDecl g = glob_it.next();
      var name = g.getSymbol().getName();
      var size = g.getNumElement().getValue() * 8 + ""; //multiply by 8
      out.printCode(".comm " + name + ", " + size + ", 8");
    }

    int count[] = new int[1];
    for (Iterator<Function> func_it = p.getFunctions(); func_it.hasNext();)
    {
      Function f = func_it.next();
      genCode(f, count);
    }
    
    out.close();
  }

  private void genCode(Function f, int count[])
  {
    f.assignLabels(count);

    out.printCode(".globl " + f.getName());
    out.printLabel(f.getName() + ":");

    var numslots = f.getNumTempVars() + f.getNumTempAddressVars();
    out.printCode("enter $(8 * " + numslots + "), $0");


  }

  public void visit(AddressAt i) {}

  public void visit(BinaryOperator i) {}

  public void visit(CompareInst i) {}

  public void visit(CopyInst i) {}

  public void visit(JumpInst i) {}

  public void visit(LoadInst i) {}

  public void visit(NopInst i) {}

  public void visit(StoreInst i) {}

  public void visit(ReturnInst i) {}

  public void visit(CallInst i)
  {


    out.printCode("call " + i.getCallee().getName());
  }

  public void visit(UnaryNotInst i) {}
}
