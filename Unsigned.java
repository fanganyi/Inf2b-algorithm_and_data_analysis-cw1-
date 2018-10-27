package imult;
/*
 * Class Unsigned: Uses type long to represent and ensure unsigned 
 * 32-bit values since Java has no base type for this. 
 */
public class Unsigned extends Number {
  private long unsigned = 0;

  public Unsigned(int n) {
    setValue(n); 
  }

  public Unsigned(long n) {
    setValue(n);
  }

  // returns class value
  public long value() {
    return longValue();
  }

  public int intValue() {
    return (int) unsigned;
  }

  public void setValue(int n) {
    unsigned = n & 0xFFFFFFFFL;
  }

  public void setValue(long n) {
    if (n >= 0)
      unsigned = n;
    else
      throw new IllegalArgumentException("Invalid representation for unsigned integer.");
  }

  public void setValue(Unsigned n) {
    unsigned = n.value();
  }

  public long longValue() {
    return unsigned;
  }

  // unused abstract methods in Number class
  public double doubleValue() {
    throw new ClassCastException("Invalid representation for unsigned integer.");
  }

  public float floatValue() {
    throw new ClassCastException("Invalid representation for unsigned integer.");
  }
}
