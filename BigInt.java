package imult;

import java.io.PrintWriter;
import java.lang.Math;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Vector;
/*
 * Class BigInt: Represents large positive integers in base = 10^4 
 * stored from least to most significant digits. 
 * (See coursework handout for full method documentation)
 */
public class BigInt {
  private static final int power_ = 4; // base digit length
  private static final int base_ = (int) Math.pow(10, power_);  // base
  public static final int MAX_LENGTH_ = 5000; // max digits

  private Vector<Unsigned> digits_ = null;
  private static Random rand = new Random();
  public static Unsigned Zero_ = new Unsigned(0);

  // constructors
  public BigInt() { // init empty BigInt 
    digits_ = new Vector<Unsigned>();
  }

  public BigInt(int n) { // init with random digits 
    digits_ = new Vector<Unsigned>();
    randomDigits(n);
  }

  public BigInt(String sval) { // init with digits in string 
    digits_ = new Vector<Unsigned>();
    inputDigits(sval);
  }

  public BigInt(List<Unsigned> ivals) { // init with list of digits
    digits_ = new Vector<Unsigned>(ivals);
  }

  public BigInt split(int r, int s) {
    assert((0 <= r) && (r <= s));
    while (s + 1 >= digits_.size())
      digits_.add(Zero_);
    return new BigInt(digits_.subList(r, s + 1));
  }

  public void setDigit(int index, Unsigned digit) {
    assert(index < MAX_LENGTH_);
    assert(digit.value() < base_);
    if (index < digits_.size())
      digits_.set(index, digit);
    else
      digits_.add(index, digit);
  }

  public Unsigned getDigit(int index) {
    assert(index < MAX_LENGTH_);
    if (index < digits_.size())
      return digits_.get(index);
    else
      return Zero_;
  }

  public int length() {
    // remove trailing zeroes
    for (int i = digits_.size() - 1; i >= 0; i--) {
      if (getDigit(i).value() == 0)
        digits_.remove(i);
      else break;
    }
    return digits_.size();
  }

  public void lshift(int n) {
    assert(n >= 0);
    for (int i = 0; i < n; i++) {
      digits_.add(0, Zero_);
    }
  }

  public void lshift(Unsigned n) {
    this.lshift(n.intValue());
  }

  public void clear() {
    digits_.clear();
  }

  public void randomDigits(int n) {
    assert(n > 0 && n <= MAX_LENGTH_);
    if(n >= MAX_LENGTH_ / 2) {
      System.err.println("WARNING: Using a " + n + " digit BigInt may cause overflow when multiplied.");
    }
    this.clear(); // clear all current digits
    for (int i = 0; i < n; i++) {
      setDigit(i, new Unsigned(rand.nextInt(base_)));  
    }
    if (getDigit(n - 1).value() == 0)
      setDigit(n - 1, new Unsigned(1)); 
  }

  public void inputDigits(String sval) {
    this.clear(); // clear all current digits
    Vector<Unsigned> vtmp = new Vector<Unsigned>();
    // parse string for spaces
    Scanner scan = new Scanner(sval);
    while (scan.hasNext()) {
      try {
        int n = scan.nextInt();
        if (n >= base_) {
          throw new NumberFormatException("ERROR: Digit " + n + " too large for base " + base_);
        }
        vtmp.add(new Unsigned(n));
      }
      catch(InputMismatchException e) {
        throw new NumberFormatException("ERROR: Invalid symbol while parsing digits \"" + scan.next() + "\"");
      }
    }
    scan.close();
    // store with lowest base digits first
    int cnt = vtmp.size();
    for (int i = 0; i < cnt; i++) {
      setDigit(i, vtmp.get(cnt - i - 1));
    }
    if (this.length() >= MAX_LENGTH_ / 2)
      System.err.println("WARNING: Using a " + this.length() + " digit BigInt may cause overflow when multiplied.");
  }

  public static int base() {
    return base_;
  }

  public void print() {
    // prints the BigInt as an integer
    int len = this.length();
    if (len == 0) {
      System.out.println("0");
      return;
    }
    for (int i = len - 1; i >= 0; i--) {
      String zfill = "";
      for (int j = power_ - 1; j > 0; j--) {
        if (getDigit(i).value() < (long)Math.pow(10, j)) {
          if (i < len - 1)
            zfill += "0";
        }
        else break;
      }
      System.out.print(zfill + getDigit(i).value());
    }
    System.out.print("\n");
  }

  public void print(PrintWriter outFile) {
    // prints the BigInt as an integer into output file
    int len = this.length();
    if (len == 0) {
      outFile.print("0\n");
      return;
    }
    for (int i = len - 1; i >= 0; i--) {
      String zfill = "";
      for (int j = power_ - 1; j > 0; j--) {
        if (getDigit(i).value() < (long)Math.pow(10, j)) {
          if (i < len - 1)
            zfill += "0";
        }
        else break;
      }
      outFile.print(zfill + getDigit(i).value());
    }
    outFile.print("\n");
  }

  public void dump() {
    // prints from least to most significant 
    // digits as stored. 
    int len = this.length();
    for (int i = 0; i < len; i++)
      System.err.print(digits_.get(i).value() + " | ");
    System.err.print("\n");
  }

  public long value() {
    // returns the long representation of this BigInt
    long value = 0;
    int len = this.length();
    for (int i = 0; i < len; i++) {
      value += digits_.get(i).value() * (long)Math.pow(base_, i);
    }
    return value;
  }

  public static void setRandSeed(long seed) {
    rand.setSeed(seed);
  }

} //end class BigInt
