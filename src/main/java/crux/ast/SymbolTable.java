package crux.ast;

import crux.ast.Position;
import crux.ast.types.*;


import javax.management.openmbean.ArrayType;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Symbol table will map each symbol from Crux source code to its declaration or appearance in the
 * source. The symbol table is made up of scopes, Each scope is a map which maps an identifier to
 * it's symbol. Scopes are inserted to the table starting from the first scope (Global Scope). The
 * Global scope is the first scope in each Crux program and it contains all the built in functions
 * and names. The symbol table is an ArrayList of scopes.
 */
public final class SymbolTable {

  /**
   * Symbol is used to record the name and type of names in the code. Names include function names,
   * global variables, global arrays, and local variables.
   */
  static public final class Symbol implements java.io.Serializable {
    static final long serialVersionUID = 12022L;
    private final String name;
    private final Type type;
    private final String error;

    /**
     *
     * @param name String
     * @param type the Type
     */
    private Symbol(String name, Type type) {
      this.name = name;
      this.type = type;
      this.error = null;
    }

    private Symbol(String name, String error) {
      this.name = name;
      this.type = null;
      this.error = error;
    }

    /**
     *
     * @return String the name
     */
    public String getName() {
      return name;
    }

    /**
     *
     * @return the type
     */
    public Type getType() {
      return type;
    }

    @Override
    public String toString() {
      if (error != null) {
        return String.format("Symbol(%s:%s)", name, error);
      }
      return String.format("Symbol(%s:%s)", name, type);
    }

    public String toString(boolean includeType) {
      if (error != null) {
        return toString();
      }
      return includeType ? toString() : String.format("Symbol(%s)", name);
    }
  }

  private final PrintStream err;
  private final ArrayList<Map<String, Symbol>> symbolScopes = new ArrayList<>();

  private boolean encounteredError = false;

  SymbolTable(PrintStream err) {
    this.err = err;
    //TODO
    Map<String, Symbol> globalScope = new HashMap<>();
    symbolScopes.add(globalScope);
    add(null, "readInt",    new FuncType(TypeList.of(), new IntType()));
    add(null, "readChar",   new FuncType(TypeList.of(), new IntType()));
    add(null, "printBool",  new FuncType(TypeList.of(new BoolType()), new VoidType()));
    add(null, "printInt",   new FuncType(TypeList.of(new IntType()), new VoidType()));
    add(null, "printChar",  new FuncType(TypeList.of(new IntType()), new VoidType()));
    add(null, "println",    new FuncType(TypeList.of(), new VoidType()));
  }

  boolean hasEncounteredError() {
    return encounteredError;
  }

  /**
   * Called to tell symbol table we entered a new scope.
   */

  void enter() {
    //TODO
    Map<String, Symbol> newScope = new HashMap<>();
    symbolScopes.add(newScope); //add a new scope
  }

  /**
   * Called to tell symbol table we are exiting a scope.
   */

  void exit() {
    //TODO
    symbolScopes.remove(symbolScopes.size()-1); //delete the last scope
  }

  /**
   * Insert a symbol to the table at the most recent scope. if the name already exists in the
   * current scope that's a declaration error.
   */
  Symbol add(Position pos, String name, Type type) {
    //TODO
    Map<String, Symbol> currentScope = symbolScopes.get(symbolScopes.size()-1);
    if (currentScope.containsKey(name))
    {
      err.printf("AlreadyDefinedSymbolError%s[Already defined %s.]%n", pos, name);
      encounteredError = true;
      return new Symbol(name, "AlreadyDefinedSymbolError");
    }
    Symbol newSymbol = new Symbol(name, type);
    currentScope.put(name, newSymbol);
    return newSymbol;
  }

  /**
   * lookup a name in the SymbolTable, if the name not found in the table it should encounter an
   * error and return a symbol with ResolveSymbolError error. if the symbol is found then return it.
   */
  Symbol lookup(Position pos, String name) {
    var symbol = find(name);
    if (symbol == null) {
      err.printf("ResolveSymbolError%s[Could not find %s.]%n", pos, name);
      encounteredError = true;
      return new Symbol(name, "ResolveSymbolError");
    } else {
      return symbol;
    }
  }

  /**
   * Try to find a symbol in the table starting from the most recent scope.
   */
  private Symbol find(String name) {
    //TODO
    for (int i = symbolScopes.size()-1; i >= 0; i--)
    {
      Map<String, Symbol> currentScope = symbolScopes.get(i);
      if (currentScope.containsKey(name))
        return currentScope.get(name);
    }
    return null;
  }
}
