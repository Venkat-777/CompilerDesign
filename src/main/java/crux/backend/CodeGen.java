package crux.backend;

import crux.ast.SymbolTable.Symbol;
import crux.ast.types.BoolType;
import crux.ast.types.FuncType;
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
    numslots = (numslots + 1) & ~1; //round to nearest even
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
    printInstructionInfo(i);
  }

  public void visit(BinaryOperator i)
  {
    printInstructionInfo(i);

    var left = i.getLeftOperand();
    var right = i.getRightOperand();
    var src = i.getDst();
    int leftIndex = varIndexMap.get(left);
    int rightIndex = varIndexMap.get(right);

    if (!varIndexMap.containsKey(src))
    {
      varIndex++;
      varIndexMap.put(src, varIndex);
    }
    int srcIndex = varIndexMap.get(src);

    printVarToReg("%rbp", (rightIndex+1)*8, "%r10"); //add right operand to r10

    if (i.getOperator() == BinaryOperator.Op.Add)
    {
      out.printCode("addq -" + (leftIndex+1)*8 + "(%rbp)" + ", %r10");
    }

    printRegToVar("%r10", "%rbp", srcIndex*8);
  }

  public void visit(CompareInst i)
  {
    printInstructionInfo(i);
  }

  public void visit(CopyInst i)
  {
    printInstructionInfo(i);

    var iType = i.getSrcValue().getType();

    String dest = "%r10";
    if (iType instanceof IntType)
    {
      var iCast = (IntegerConstant) i.getSrcValue();
      int src = (int) iCast.getValue();
      varIndexMap.put(i.getDstVar(), src);
      varIndex++;
      printIntToReg(src, dest); //$x to %r10
    } else if (iType instanceof BoolType) {
      var iCast = (BooleanConstant) i.getSrcValue();
      int src = iCast.getValue() ? 1 : 0;
      varIndexMap.put(i.getDstVar(), src);
      varIndex++;
      printIntToReg(src, dest); //$x to %r10
    }

    printRegToVar("%r10", "%rbp", varIndex*8); //%r10 to LocalVar
  }

  public void visit(JumpInst i)
  {
    printInstructionInfo(i);
  }

  public void visit(LoadInst i)
  {
    printInstructionInfo(i);
  }

  public void visit(NopInst i)
  {
    printInstructionInfo(i);
  }

  public void visit(StoreInst i)
  {
    printInstructionInfo(i);
  }

  public void visit(ReturnInst i)
  {
    printInstructionInfo(i);
  }

  public void visit(CallInst i)
  {
    printInstructionInfo(i);

    printVarToReg("%rbp", varIndex*8, "%rdi");
    out.printCode("call " + i.getCallee().getName());
  }

  public void visit(UnaryNotInst i)
  {
    printInstructionInfo(i);
  }
}
