package crux.backend;

import crux.ast.SymbolTable.Symbol;
import crux.ast.types.IntType;
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

  private final IRValueFormatter irFormat = new IRValueFormatter();
  private void printInstructionInfo(Instruction i)
  {
    var info = String.format("/* %s */", i.format(irFormat));
    out.printCode(info);
  }

  public CodeGen(Program p) {
    this.p = p;
    // Do not change the file name that is outputted or it will
    // break the grader!

    out = new CodePrinter("a.s");
  }

  private HashMap<Variable, Integer> varIndexMap = new HashMap<>();
  private int varIndex;

  public Integer getVarIndex(Variable var) //returns Integer value
  {
      return varIndexMap.get(var);
  }

  public void storeVar(Variable var) //stores to bottom of map
  {
    varIndexMap.put(var, varIndexMap.size());
  }

  private String registers[] = {"%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9"};

  public void printVarToReg(String var, int offset, String register)
  {
    String s = String.format("movq -%d(%s), %s", offset, var, register);
    out.printCode(s);
  }

  public void printIntToReg(Integer Int, String register)
  {
    out.printCode("movq $" + Int + ", " + register);
  }

  public void printRegToVar(String register, String var, int offset)
  {
    String s = String.format("movq %s, -%d(%s)", register, offset, var);
    out.printCode(s);
  }

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

    for (int i = 1; i <= f.getArguments().size(); ++i)
    {
      var temp = f.getTempVar(f.getArguments().get(i-1).getType());
      Integer offset = i*8;
      varIndexMap.put(temp, offset); //offset is stored as positive
      printRegToVar(registers[i-1], "%rbp", i*8);
    }

    // visit function body and generate code
    var currentInst = f.getStart();
    while(currentInst != null)
    {
      currentInst.accept(this);
      currentInst = currentInst.getNext(0); //get next Inst
    }

    out.printCode("leave");
    out.printCode("ret");
  }

  public void visit(AddressAt i)
  {
    out.printCode("reached AddressAt");
  }

  public void visit(BinaryOperator i)
  {
    out.printCode("reached BinaryOperator");
  }

  public void visit(CompareInst i)
  {
    out.printCode("reached cmpInst");
  }

  public void visit(CopyInst i)
  {
    out.printCode("reached copyInst");

    var iType = i.getSrcValue().getType();
    if (iType instanceof IntType)
    {
      var temp = (IntegerConstant) i.getSrcValue();
      out.printCode(String.valueOf(temp.getValue()));
    } else //is BoolType
    {
      var temp = (BooleanConstant) i.getSrcValue();
      out.printCode(String.valueOf(temp.getValue()));
    }

    int src = 0;
    String dest = "%r10";
    printIntToReg(src, dest);
  }

  public void visit(JumpInst i)
  {
    out.printCode("reached jumpInst");
  }

  public void visit(LoadInst i)
  {
    out.printCode("reached LoadInst");
  }

  public void visit(NopInst i)
  {
    out.printCode("reached nopInst");
  }

  public void visit(StoreInst i)
  {
    out.printCode("reached storeInst");
  }

  public void visit(ReturnInst i)
  {
    out.printCode("reached returnInst");
  }

  public void visit(CallInst i)
  {
    for (int j = 1; j <= varIndexMap.size(); ++j)
    {
      printVarToReg("%rbp", j*8, registers[j]);
    }
    out.printCode("call " + i.getCallee().getName());
  }

  public void visit(UnaryNotInst i)
  {
    out.printCode("reached UnaryNotInst");
  }
}
