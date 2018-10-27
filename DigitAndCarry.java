package imult;
/*
 * Class DigitAndCarry: Represents a digit and carry pair
 * (See coursework handout for full method documentation)
 */

class DigitAndCarry {
  private Unsigned digit_ = new Unsigned(0);
  private Unsigned carry_ = new Unsigned(0);
  public Unsigned getDigit() {
    return digit_;
  }
  public Unsigned getCarry() {
    return carry_;
  }
  public void setDigit(long d) {
    digit_.setValue(d);
  }
  public void setCarry(long c) {
    carry_.setValue(c);
  }
  public void setDigit(int d) {
    digit_.setValue(d);
  }
  public void setCarry(int c) {
    carry_.setValue(c);
  }
} // end DigitAndCarry
