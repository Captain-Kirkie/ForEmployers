package lab06;

public class BetterRandomNumberGenerator implements RandomNumberGenerator {

/*
   long previous = 0;
   long c = 11;
   long a = 25214903917L;

    @Override
    public int nextInt(int max) {
        int next = (int) ((a * previous  + c ) % max);
        previous = next;
        return next;
   */

    long a = 5231;
    long c = 1272;
    long m = System.nanoTime();
    long time = System.nanoTime();
    long seed;

    RandomNumberGenerator randomNumberGenerator;
    // X_{n+1}=\left(aX_{n}+c\right){\bmod {m}
    @Override
    public int nextInt(int max) {
        int next = (int) ((a * seed  + c ) % max);
        seed = next;
        return next;

//       long number = ((a * seed) + c) % max;
//       c += (System.nanoTime() - time);
//
//       return (int) number;

    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public void setConstants(long const1, long const2) {

    }
}
