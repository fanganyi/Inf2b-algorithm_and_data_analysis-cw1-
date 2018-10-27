package imult;

/*
 * Class Arithmetic: Handles addition, subtraction, and
 *   multiplication over digits in BigInt base. Includes methods:
 *   addDigits()
 *   subDigits()
 *   mulDigits()
 *   add()
 *   sub()
 */
class Arithmetic {

  public static DigitAndCarry addDigits(Unsigned a, Unsigned b,
                                        Unsigned c) {
    long sum = a.value() + b.value() + c.value();
    // force into base
    DigitAndCarry dc = new DigitAndCarry();
    dc.setDigit(sum % BigInt.base());
    dc.setCarry(sum / BigInt.base());
    return dc;
  }

  public static DigitAndCarry subDigits(Unsigned a, Unsigned b,
                                        Unsigned c) {
    DigitAndCarry dc = new DigitAndCarry();
    long sum;
    if(a.value() >= (b.value() + c.value())) {
      sum = a.value() - (b.value() + c.value());
      // force into base
      dc.setDigit(sum % BigInt.base());
      dc.setCarry(sum / BigInt.base());
    } else {
      sum = (BigInt.base() + a.value()) - (b.value() + c.value());
      dc.setDigit(sum);
      dc.setCarry(1);
    }
    //System.out.println("* " + a.value() + " - " + b.value() + " = " + dc.setDigit().value());
    return dc;
  }

  public static DigitAndCarry mulDigits(Unsigned a, Unsigned b) {
    long prod = a.value() * b.value();
    DigitAndCarry dc = new DigitAndCarry();
    dc.setDigit(prod % BigInt.base());
    dc.setCarry(prod / BigInt.base());
    return dc;
  }

  public static BigInt schoolMul(BigInt A, BigInt B) {
    assert(A.length() == B.length());
    int alen = A.length(), blen = B.length();
    int column = 0;
    BigInt product = new BigInt(); // answer
    for(int b = 0; b < blen; b++) { // for each digit in B
      BigInt row = new BigInt();
      Unsigned carry = new Unsigned(0);
      // Explicitly set zeros in columns
      for(int i = 0; i < b; i++) row.setDigit(i, BigInt.Zero_);

      for(int a = 0; a < alen; a++) { // for each digit in A
        column = a + b;
        // multiple 
        DigitAndCarry dc1 = Arithmetic.mulDigits(B.getDigit(b), A.getDigit(a));
        // add carry to current value
        DigitAndCarry dc2 = Arithmetic.addDigits(dc1.getDigit(), carry, BigInt.Zero_);
        // set digit in this column
        row.setDigit(column, dc2.getDigit());
        // save carry
        carry.setValue(dc1.getCarry().value() + dc2.getCarry().value());
      }
      // add final carry
      row.setDigit(column + 1, carry);
      // add to final product
      BigInt sum = new BigInt();
      DigitAndCarry dcAdd = new DigitAndCarry();
      int len = product.length() > row.length() ? product.length() : row.length();
      for(int i = 0; i < len; i++) {
        dcAdd = Arithmetic.addDigits(product.getDigit(i), row.getDigit(i), dcAdd.getCarry());
        sum.setDigit(i, dcAdd.getDigit());
      }
      sum.setDigit(len, dcAdd.getCarry());
      product = sum;
    }
    return product;
  }

  public static BigInt fastCheckMul(BigInt A, BigInt B) {
    int alen = A.length(), blen = B.length();
    BigInt product = new BigInt(); // answer
    int col = 0;
    for(int b = 0; b < blen; b++) { // for each digit in B
      Unsigned carry = new Unsigned(0);
      col = b; // current column
      for(int a = 0; a < alen; a++) { // for each digit in A
        // multiple 
        DigitAndCarry dc1 = Arithmetic.mulDigits(B.getDigit(b), A.getDigit(a));        
        // add to this column with carry
        DigitAndCarry dc2 = Arithmetic.addDigits(product.getDigit(col), dc1.getDigit(), carry);
        // set new value in current column
        product.setDigit(col++, dc2.getDigit());
        // save carry for next iteration
        carry.setValue(dc1.getCarry().value() + dc2.getCarry().value());
      }
      //put carry into answer here
      product.setDigit(col, carry);
    }
    return product;
  }

  public static double average(double[] a,int StartIndex) { 
    double sum = 0.0;

    for (int i = StartIndex; i < a.length; i++)
        sum = sum + a[i];
    return sum / (a.length-StartIndex);
  }

  public static void main(String argv[]) {
  }
} // end class Arithmetic
