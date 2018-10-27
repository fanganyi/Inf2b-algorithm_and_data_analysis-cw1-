package imult;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/*
 * Class Utils: Helper methods.
 * readDigitFile(): reads a list of BigInts from file and returns 
 *   them in a BigInt array.
 * plotRunTimes():  handles the parsing of the file from
 *   BigIntMul.getRunTimes() method for the showGraph() method
 * showGraph(): plots the data from the experiments 
 */
public class Utils {

  public static BigInt[] readDigitFile(File fin) throws java.io.FileNotFoundException {
    assert(fin.exists() && fin.isFile());
    ArrayList<BigInt> objList = new ArrayList<BigInt>();
    String sval = null;
    Scanner sc = new Scanner(fin).useDelimiter(";");
    while (sc.hasNext()) {
      sval = sc.next().trim();
      if(sval.length() > 0) {
        objList.add(new BigInt(sval));
        //System.out.println(sval);
      }
    }
    sc.close();
    BigInt[] bints = new BigInt[objList.size()];
    for (int i = 0; i < objList.size(); i++) {
      bints[i] = objList.get(i);
    }
    return bints; 
  }

  public static void plotRunTimes(double c, double a, File fin) throws java.io.FileNotFoundException {
    ArrayList<String> times = new ArrayList<String>();
    Scanner sc = new Scanner(fin).useDelimiter("\n");
    while(sc.hasNext()) {
      String sval = sc.next().trim();
      if(sval.length() > 0) times.add(sval);
    }
    sc.close();
    // save n, school run times, ko runtimes
    double[][] runTimes = new double[times.size()][3];
    for(int i = 0; i < times.size(); i++) {
      int idx = 0;
      Scanner lineScan = new Scanner(times.get(i)); 
      while(lineScan.hasNext()) {
        runTimes[i][idx] = Double.valueOf(lineScan.next());
        idx++;
      }
    }
    showGraph(c, a, runTimes);
  }

  public static void showGraph(double c, double a, double[][] runTimes) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GraphingData gd = new GraphingData(c, a, runTimes);
    f.add(gd);
    JMenuBar menu = new JMenuBar();
    f.setJMenuBar(menu);
    JMenuItem saveItem = new JMenuItem("Save Graph");
    saveItem.addActionListener(new SaveGraph(gd));
    menu.add(saveItem);
    f.setSize(600,600);
    f.setLocation(200,200);
    f.setVisible(true);
  }

  public static void main(String argv[]) throws java.io.FileNotFoundException {
    plotRunTimes(0.02887, 0.02887, new File("../IO/optKoMulTimes"));
    //BigInt[] bints = readDigitFile(new File("test.input"));
    //for(int i = 0; i < bints.length; i++)
     // bints[0].print();
  }
}

class SaveGraph implements ActionListener {
  GraphingData gd;

  public SaveGraph(GraphingData g) {
    gd = g;
  }

  public void actionPerformed(ActionEvent e) {
    gd.save(); 
  }
}
