package imult;

import java.io.*;
import java.lang.Math;
import java.util.Arrays;
/*
 * Class BigIntMul: Handles the multiplication and analysis 
 * helper methods:
 *   mulTest()
 *   getRunTimes()
 *   getRatios()
 *   mulVerify()
 *   plotRunTimes()
 * (See coursework handout for full method documentation)
 */
public class BigIntMul {

  public static final double lgPwr_ = Math.log(3) / Math.log(2);
  private static double max_ratio_ = 0; // to store max c value from getRatios()
  private static double avg_ratio_ = 0; // to store avg c value from getRatios()

  public static void mulTest(Unsigned m, Unsigned n) {
    int pairs = m.intValue();
    // generate m pairs of BigInts of length n
    BigInt[][] bigint = new BigInt[pairs][3];
    for (int i = 0; i < pairs; i++) {
      bigint[i][0] = new BigInt(n.intValue());
      bigint[i][1] = new BigInt(n.intValue());
    }
    // multiply them using koMul
    for (int i = 0; i < pairs; i++) {
      bigint[i][2] = StudentCode.koMul(bigint[i][0], bigint[i][1]);
      //bigint[i][2] = Arithmetic.schoolMul(bigint[i][0], bigint[i][1]);
      if (!mulVerify(bigint[i][0], bigint[i][1], bigint[i][2])) {
        System.err.print("ERROR. In BigIntMul.MulTest():\n\t");
        bigint[i][0].print();
        System.err.print("*\t");
        bigint[i][1].print();
        System.err.print("is not equal to  ");
        bigint[i][2].print();
        System.err.println("Exiting...");
        System.exit(1);
      }
    }
    // all results correct
    System.err.println("BigIntMul.MulTest(): All " + pairs + " results correct.");
  }

  public static boolean mulVerify(BigInt A, BigInt B, BigInt prod) {
    BigInt answer = Arithmetic.fastCheckMul(A, B);
    int alen = answer.length();
    if (alen != prod.length()) return false;
    for (int i = 0; i < alen; i++) {
      if(answer.getDigit(i).value() != prod.getDigit(i).value())
        return false;
    }
    return true;
  }

  public static void getRunTimes(Unsigned m, Unsigned n, Unsigned t, 
      File fout, boolean opt) {
    // random System.currentTimeMillis() 
    //MemoryFarm.initMem();
    BigInt.setRandSeed(1231538606138l);  
    int pairs = m.intValue();
    long[][] worstCaseRunTimes = new long[t.intValue()][2];
    for (int j = 1; j <= t.intValue(); j++) {
      // generate m pairs of BigInts of length n*t
      int len = n.intValue() * j;
      BigInt[][] bigints = new BigInt[pairs][2];
      for(int i = 0; i < pairs; i++) {
        bigints[i][0] = new BigInt(len);
        bigints[i][1] = new BigInt(len);
      }
      // storage for execution times
      long[] koTimes = new long[pairs];
      long[] schoolTimes = new long[pairs];
      long start = 0; // record start time
      for (int i = 0; i < pairs; i++) {
        System.out.print("BigIntMul.getRunTimes(): computing pair " + (i + 1));
        System.out.print(" of " +  pairs + " with " + len + " digits.\n");
        // multiple them using schoolMul
        start = System.currentTimeMillis(); 
        Arithmetic.schoolMul(bigints[i][0], bigints[i][1]);
        // record exe time
        schoolTimes[i] = System.currentTimeMillis() - start; 
        // multiple them using koMul
        // don't want to record time for if statement 
        if(!opt) {
          start = System.currentTimeMillis(); 
          StudentCode.koMul(bigints[i][0], bigints[i][1]);
        }
        else {
          start = System.currentTimeMillis(); 
          StudentCode.koMulOpt(bigints[i][0], bigints[i][1]);
        }
        // record exe time
        koTimes[i] = System.currentTimeMillis() - start;
        //reset scratch space
        //MemoryFarm.dealloc();
      }
      // sort arrays in ascending order
      Arrays.sort(schoolTimes);
      Arrays.sort(koTimes); 
      // store worst case runtime
      worstCaseRunTimes[j - 1][0] = schoolTimes[pairs - 1];
      worstCaseRunTimes[j - 1][1] = koTimes[pairs - 1];
    }
    // print runtimes to output file
    try {
      BufferedWriter sOut = new BufferedWriter(new FileWriter(fout));
      for(int j = 0; j < t.intValue(); j++) {
        // print in columns "integer-size       schoolTimes     koTimes"
        sOut.write(((j + 1) * n.intValue()) + "\t" +
                    worstCaseRunTimes[j][0] + "\t" +
                    worstCaseRunTimes[j][1] + "\n");
      }
      sOut.close();
      System.err.println("BigIntMul.getRunTimes() results printed to " + fout.getPath());
    }
    catch (Exception e) {//Catch exception if any
      System.err.println("ERROR. BigIntMul.getRunTimes() IO error " + e.getMessage());
    }
  }

  public static void getRatios(Unsigned m, Unsigned n, Unsigned t, 
      File fout, Unsigned xOver) {
    BigInt.setRandSeed(1231538606138l);
    int pairs = m.intValue();
    double[] ratios = new double[t.intValue()];
    for (int j = 1; j <= t.intValue(); j++) {
      // generate m pairs of BigInts of length n*t
      int len = n.intValue() * j;
      BigInt[][] bigints = new BigInt[pairs][2];
      for (int i = 0; i < pairs; i++) {
        bigints[i][0] = new BigInt(len);
        bigints[i][1] = new BigInt(len);
      }
      // storage for execution times
      long[] koTimes = new long[pairs];
      long start = 0; // to record start time
      for (int i = 0; i < pairs; i++) {
        System.out.print("BigIntMul.getRatios(): computing pair " + (i + 1));
        System.out.print(" of " +  pairs + " with " + len + " digits.\n");
        // multiple using koMul
        start = System.currentTimeMillis(); 
        StudentCode.koMulOpt(bigints[i][0], bigints[i][1]);
        // record exe time
        koTimes[i] = System.currentTimeMillis() - start;
      }
      // sort array in ascending order
      Arrays.sort(koTimes); 
      // store ratio = runtime / n^(lg_3)
      if(len >= xOver.intValue())
        ratios[j - 1] = ((double)koTimes[pairs - 1] / Math.pow(len, lgPwr_));
      else ratios[j - 1] = 0;
    }
    // print runtimes to output file
    try {
      BufferedWriter sOut = new BufferedWriter(new FileWriter(fout));
      for(int j = (xOver.intValue()/n.intValue()) - 1;
          j < t.intValue(); j++) {
        // print in columns "integer-size ratio" 
        sOut.write(((j + 1) * n.intValue()) + "\t" +
                    ratios[j] + "\n");
      }
      // record max and average ratios and print the sorted ratios in the file
      Arrays.sort(ratios); // sort ratio array
	  sOut.write ("Ignoring ratios before cross over point: "+(xOver.intValue()/n.intValue())+"\n");
	  sOut.write ("Sorted ratios are: ");
	  for (int i = (xOver.intValue()/n.intValue()) - 1;
		  i < t.intValue(); i++){
		  sOut.write(ratios[i]+", ");
	  }
	  sOut.write("\n");
	  sOut.write("Maximum ratio is: " + ratios[t.intValue() - 1] + "\n");
      sOut.write("Average ratio is: " + Arithmetic.average(ratios,(xOver.intValue()/n.intValue()) - 1));
	  sOut.close();
      System.err.println("BigIntMul.getRatios() results printed to " + fout.getPath());
    }
    catch (Exception e) {//Catch exception if any
      System.err.println("ERROR. BigIntMul.getRatios() IO error " + e.getMessage());
    }
    // record ratios for later use! 
    max_ratio_ = ratios[t.intValue() - 1];
    avg_ratio_ = Arithmetic.average(ratios,(xOver.intValue()/n.intValue()) - 1);
  }

  public static void plotRunTimes(double c, double a, File fin) throws java.io.FileNotFoundException {
    Utils.plotRunTimes(c, a, fin);
  }

  public static void main(String argv[]) {

    //write your code here to generate the plot
  }
}
