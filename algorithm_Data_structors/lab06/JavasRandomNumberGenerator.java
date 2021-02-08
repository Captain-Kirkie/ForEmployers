package lab06;

/**
 * Builds a RNG simply by wrapping the java.util.Random class.
 */
public class JavasRandomNumberGenerator implements RandomNumberGenerator {

  /*
   * Create a Java.util.Random object to do the actual "work" of this class.
   */

  private java.util.Random random_generator_ = new java.util.Random();

  public int nextInt(int max) {
    return this.random_generator_.nextInt(max);
  }

  public void setSeed(long seed) {
    this.random_generator_.setSeed(seed);
  }

  public void setConstants(long _const1, long _const2) {
    // not needed
  }
}
