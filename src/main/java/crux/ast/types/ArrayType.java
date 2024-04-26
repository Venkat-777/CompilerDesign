package crux.ast.types;

import java.lang.reflect.Array;

/**
 * The variable base is the type of the array element. This could be int or bool. The extent
 * variable is number of elements in the array.
 *
 */
public final class ArrayType extends Type implements java.io.Serializable {
  static final long serialVersionUID = 12022L;
  private final Type base;
  private final long extent;

  public ArrayType(long extent, Type base) {
    this.extent = extent;
    this.base = base;
  }

  public Type getBase() {
    return base;
  }

  public long getExtent() {
    return extent;
  }

  @Override
  public String toString() {
    return String.format("array[%d,%s]", extent, base);
  }

  @Override
  public Type index(Type that)
  {
    if (that.getClass() == IntType.class)
    {
      return this.base;
    } else
    {
      return super.index(that);
    }
  }

  @Override
  public boolean equivalent(Type that)
  {
    if (this.getClass() == that.getClass())
    {
      ArrayType newThat = (ArrayType) that;
      return (this.base.equivalent(newThat.base) && this.extent == newThat.extent);
    } else {
      return false;
    }
  }
}
