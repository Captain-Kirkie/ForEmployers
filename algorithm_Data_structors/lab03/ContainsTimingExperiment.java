//package lab03;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Random;
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//public class ContainsTimingExperiment {
//
//  private static final int ITER_COUNT = 100;
//
//  public static void main(String[] args) {
//    // you spin me round baby, right round
//    long startTime = System.nanoTime();
//    while (System.nanoTime() - startTime < 1_000_000_000)
//      ;
//
//    try (FileWriter fw = new FileWriter(new File("contains_experiment.tsv"))) { // open up a file writer so we can write
//                                                                                // to file.
//      Random random = new Random();
//      for (int exp = 10; exp <= 20; exp++) { // This is used as the exponent to calculate the size of the set.
//        int size = (int) Math.pow(2, exp); // or ..
//
//        // Do the experiment multiple times, and average out the results
//        long totalTime = 0;
//
//        for (int iter = 0; iter < ITER_COUNT; iter++) {
//          // SET UP!
//          SortedSet<Integer> set = new TreeSet<>();
//          for (int i = 0; i < size; i++) {
//            set.add(i);
//          }
//          int findElement = random.nextInt(size); // This gets me a random int between 0 and size;
//
//          // TIME IT!
//          long start = System.nanoTime();
//          set.remove(findElement);
//          long stop = System.nanoTime();
//          totalTime += stop - start;
//        }
//        double averageTime = totalTime / (double) ITER_COUNT;
//        System.out.println(size + "\t" + averageTime); // print to console
//        fw.write(size + "\t" + averageTime + "\n"); // write to file.
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
////    Charter charter = new Charter();
////    charter.createChart("contains_experiment.tsv", "chart.png");
////  }
//}
