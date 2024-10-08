package crux.backend;

import crux.ast.LiteralBool;
import crux.ast.OpExpr;
import crux.ast.SymbolTable.Symbol;
import crux.ast.types.BoolType;
import crux.ast.types.FuncType;
import crux.ast.types.IntType;
import crux.ast.types.VoidType;
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

  public HashMap<Instruction, String> labelMap = new HashMap<>();

  public Stack<Instruction> tovisit = new Stack<>();
  public HashSet<Instruction> visitedLabels = new HashSet<>();

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
    labelMap = f.assignLabels(count);

    out.printCode(".globl " + f.getName());
    out.printLabel(f.getName() + ":");

    var numslots = f.getNumTempVars() + f.getNumTempAddressVars();
    numslots = (numslots + 1) & ~1; //rounds to nearest even
    varIndex = 0;
    out.printCode("enter $(8 * " + numslots + "), $0");

    var argNums = Math.min(f.getArguments().size(), 6);
    for (int i = 0; i < argNums; ++i)
    {
      var temp = f.getArguments().get(i);
      varIndex++;
      varIndexMap.put(temp, varIndex); //offset is stored as positive
      printRegToVar(registers[i], "%rbp", varIndex*8);
    }

    int stackIndex = -1;
    for (int i = 6; i < f.getArguments().size(); ++i)
    {
      var temp = f.getArguments().get(i);
      varIndexMap.put(temp, stackIndex);
      stackIndex--;
    }

    // visit function body and generate code
    tovisit.push(f.getStart());
    while (!tovisit.isEmpty())
    {
      var currentInst = tovisit.pop();
      if (visitedLabels.contains(currentInst))
      {
        currentInst.accept(this);
        out.printCode("jmp " + labelMap.get(currentInst));
      }
      else
      {
        if (labelMap.containsKey(currentInst)) //inst is a jump target
        {
          out.printLabel(labelMap.get(currentInst) + ":");
        }
        if (currentInst != null)
        {
          currentInst.accept(this);
          visitedLabels.add(currentInst);
          if (currentInst.getNext(1) != null)
          {
            tovisit.push(currentInst.getNext(1));
          }
          if (currentInst.getNext(0) != null)
          {
            tovisit.push(currentInst.getNext(0));
          }
          else
          {
            out.printCode("leave");
            out.printCode("ret");
          }
        }
      }
    }
  }

  public void visit(AddressAt i)
  {
    //printInstructionInfo(i);
    //out.printCode("/* AddressAt */");

    var varName = i.getBase().getName();
    out.printCode("movq " + varName + "@GOTPCREL(%rip), %r11");

    if (i.getOffset() != null)
    {
      var offset = varIndexMap.get(i.getOffset());
      printVarToReg("%rbp", offset*8, "%r10");
      out.printCode("imulq $8, %r10");
      out.printCode("addq %r10, %r11");
    }

    varIndex++;
    varIndexMap.put(i.getDst(), varIndex);
    //move result %r11 to destination
    printRegToVar("%r11", "%rbp", varIndex*8);
  }

  public void visit(BinaryOperator i)
  {
    //printInstructionInfo(i);

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

    if (i.getOperator() == BinaryOperator.Op.Add)
    {
      printVarToReg("%rbp", (leftIndex)*8, "%r10"); //add right operand to r10
      out.printCode("addq -" + (rightIndex)*8 + "(%rbp)" + ", %r10");
      printRegToVar("%r10", "%rbp", srcIndex*8);
    } else if (i.getOperator() == BinaryOperator.Op.Sub)
    {
      printVarToReg("%rbp", (leftIndex)*8, "%r10"); //add right operand to r10
      out.printCode("subq -" + (rightIndex)*8 + "(%rbp)" + ", %r10");
      printRegToVar("%r10", "%rbp", srcIndex*8);
    } else if (i.getOperator() == BinaryOperator.Op.Mul)
    {
      printVarToReg("%rbp", (leftIndex)*8, "%r10"); //add right operand to r10
      out.printCode("imul -" + (rightIndex)*8 + "(%rbp)" + ", %r10");
      printRegToVar("%r10", "%rbp", srcIndex*8);
    } else
    {
      out.printCode("movq -"+leftIndex*8+"(%rbp), %rax");
      out.printCode("cqto");
      out.printCode("idivq -" +rightIndex*8+ "(%rbp)");
      out.printCode("movq %rax, -"+srcIndex*8+"(%rbp)");
    }
  }

  public void visit(CompareInst i)
  {
    //printInstructionInfo(i);

    var dest = i.getDst();
    var left = i.getLeftOperand();
    var right = i.getRightOperand();
    if (!varIndexMap.containsKey(dest))
    {
      varIndex++;
      varIndexMap.put(dest, varIndex);
    }
    int dstslot = varIndexMap.get(dest);
    int lhsslot = varIndexMap.get(left);
    int rhsslot = varIndexMap.get(right);
    out.printCode("movq $0, %rax");
    out.printCode("movq $1, %r10");
    printVarToReg("%rbp",lhsslot*8,"%r11");
    out.printCode("cmp -"+ rhsslot*8 + "(%rbp) , %r11");

    switch (i.getPredicate())
    {
      case GT:
        out.printCode("cmovg %r10, %rax");
        break;
      case GE:
        out.printCode("cmovge %r10, %rax");
        break;
      case LT:
        out.printCode("cmovl %r10, %rax");
        break;
      case LE:
        out.printCode("cmovle %r10, %rax");
        break;
      case EQ:
        out.printCode("cmove %r10, %rax");
        break;
      case NE:
        out.printCode("cmovne %r10, %rax");
        break;
    }

    printRegToVar("%rax","%rbp",dstslot*8);

  }

  public void visit(CopyInst i)
  {
    //printInstructionInfo(i);
    out.printCode("/* CopyInst */");

    var dest = "%r10";

    if (i.getSrcValue() instanceof LocalVar) //src is a LocalVar
    {
      var srcVar = i.getSrcValue();
      var srcOffset = varIndexMap.get(srcVar);
      printVarToReg("%rbp", srcOffset*8, "%r10");
      if (!varIndexMap.containsKey(i.getDstVar()))
      {
        varIndex++;
        varIndexMap.put(i.getDstVar(), varIndex);
      }
    } else //src is a constant
    {
      var iType = i.getSrcValue().getType();
      if (iType instanceof IntType)
      {
        var iCast = (IntegerConstant) i.getSrcValue();
        int src = (int) iCast.getValue();
        if (!varIndexMap.containsKey(i.getDstVar()))
        {
          varIndex++;
          varIndexMap.put(i.getDstVar(), varIndex);
        }
        printIntToReg(src, dest); //$x to %r10
      } else if (iType instanceof BoolType) {
        var iCast = (BooleanConstant) i.getSrcValue();
        int src = iCast.getValue() ? 1 : 0;
        if (!varIndexMap.containsKey(i.getDstVar()))
        {
          varIndex++;
          varIndexMap.put(i.getDstVar(), varIndex);
        }
        printIntToReg(src, dest); //$x to %r10
      }
    }
    printRegToVar("%r10", "%rbp", varIndexMap.get(i.getDstVar())*8); //%r10 to LocalVar
  }

  public void visit(JumpInst i)
  {
    //printInstructionInfo(i);

    var predicate = i.getPredicate();
    if (!varIndexMap.containsKey(predicate))
    {
      varIndex++;
      varIndexMap.put(predicate, varIndex);
    }
    var offset = varIndexMap.get(predicate);
    printVarToReg("%rbp", offset*8, "%r10");

    out.printCode("cmp $1, %r10");

    var thenBlock = i.getNext(1);
    var elseBlock = i.getNext(0);

    out.printCode("je " + labelMap.get(thenBlock));

    if (labelMap.get(elseBlock) != null)
    {
      out.printCode("jmp " + labelMap.get(elseBlock));
    }

  }

  public void visit(LoadInst i)
  {
    //printInstructionInfo(i);
    //out.printCode("/* LoadInst */");

    var srcAddress = i.getSrcAddress();
    var srcOffset = varIndexMap.get(srcAddress);
    printVarToReg("%rbp", srcOffset*8, "%r10"); //localVar to %r10

    out.printCode("movq 0(%r10), %r11");

    varIndex++;
    varIndexMap.put(i.getDst(), varIndex);
    printRegToVar("%r11", "%rbp", varIndex*8);
  }

  public void visit(NopInst i)
  {
    //printInstructionInfo(i);
  }

  public void visit(StoreInst i)
  {
    //printInstructionInfo(i);

    out.printCode("/* StoreInst */");

    var srcVar = i.getSrcValue();
    var srcOffset = varIndexMap.get(srcVar);
    printVarToReg("%rbp", srcOffset*8, "%r10"); //localVar to %r10

    var destAddress = i.getDestAddress();
    var destOffset = varIndexMap.get(destAddress);
    printVarToReg("%rbp", destOffset*8, "%r11"); //destAddress to %r11

    out.printCode("movq %r10, 0(%r11)");
  }

  public void visit(ReturnInst i)
  {
    //printInstructionInfo(i);

    if (i.getReturnValue() != null)
    {
      var localVar = i.getReturnValue();
      var offset = varIndexMap.get(localVar);
      printVarToReg("%rbp" , offset*8, "%rax");
    }

    out.printCode("leave");
    out.printCode("ret");
  }

  public void visit(CallInst i)
  {
    //printInstructionInfo(i);

    int numParams = i.getParams().size();
    if (numParams <= 6)
    {
      for (int j = numParams; j > 0; j--)
      {
        if (!varIndexMap.containsKey(i.getParams().get(j-1)))
        {
          varIndex++;
          varIndexMap.put(i.getParams().get(j-1), varIndex);
        }
        var offset = varIndexMap.get(i.getParams().get(j-1));
        if (offset > 0)
        {
          printVarToReg("%rbp", offset*8, registers[j-1]);
        } else
        {
          offset--;
          out.printCode("movq " + offset*-8 + "(%rbp)" + ", " + registers[j-1]);
        }
      }
    } else //when there is 7-n arguments
    {
      for (int j = 0; j < 6; j++) //handle arguments 1~6
      {
        var param = i.getParams().get(j);
        if (!varIndexMap.containsKey(param))
        {
          varIndex++;
          varIndexMap.put(param, varIndex);
        }
        var offset = varIndexMap.get(param);
        printVarToReg("%rbp", offset*8, registers[j]);
      }

      //handle 7-n arguments
      var paramsLeft = numParams - 6;
      paramsLeft = (paramsLeft + 1) & ~1; //rounds to nearest even
      out.printCode("subq $" + paramsLeft*8 + ", %rsp");

      for (int j = numParams-6; j > 0; j--)
      {
        printVarToReg("%rbp", j*8 + 48, "%r10");
        out.printCode("movq %r10, " + (j-1)*8 + "(%rsp)");
      }
    }

    out.printCode("call " + i.getCallee().getName());

    var func = (FuncType) i.getCallee().getType();
    if (!(func.getRet() instanceof VoidType))
    {
      varIndex++;
      varIndexMap.put(i.getDst(), varIndex);
      var offset = varIndexMap.get(i.getDst());
      printRegToVar("%rax", "%rbp", offset*8);
    }

    if (i.getParams().size() > 6)
    {
      var numOnStack = numParams - 6;
      numOnStack = (numOnStack + 1) & ~1; //rounds to nearest even
      out.printCode("addq $" + numOnStack*8 + ", %rsp");
    }
  }

  public void visit(UnaryNotInst i)
  {
    printInstructionInfo(i);

    var inner = i.getInner();
    var val = (Value) inner;
    int UnaryNotSlot = varIndexMap.get(i.getInner());
    printVarToReg("%rbp",UnaryNotSlot*8,"%r10");
    out.printCode("movq $1, %r11");
    out.printCode("subq %r10, %r11");
    varIndex++;
    varIndexMap.put(i.getDst(),varIndex);
    printRegToVar("%r11","%rbp",varIndex*8);
  }
}
