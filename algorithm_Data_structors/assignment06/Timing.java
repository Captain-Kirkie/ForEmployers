package assignment06;

public class Timing {
    public static void main(String[] args) {
        long startTime, midPointTime, stopTime;
        ReallyBadHashFunctor badHashFunctor = new ReallyBadHashFunctor();
        GoodHashFunctor goodHashFunctor = new GoodHashFunctor();
        GoodHashFunctor2 goodHashFunctor2 = new GoodHashFunctor2();
        MediocreHashFunctor mediocreHashFunctor = new MediocreHashFunctor();

        String hashString = "hashString";
        startTime = System.nanoTime();
        while (System.nanoTime() - startTime < 1000000000) { // empty block
        }

        // Now, run the test.

        long timesToLoop = 10000 * 100000;

        startTime = System.nanoTime();

        for (long i = 0; i < timesToLoop; i++)
            for (double d = 1; d <= 10; d++)
               badHashFunctor.hash(hashString);


        midPointTime = System.nanoTime();

        // Run an empty loop to capture the cost of running the loop.

        for (long i = 0; i < timesToLoop; i++) { // empty block
        }

        stopTime = System.nanoTime();

        // Compute the time, subtract the cost of running the loop
        // from the cost of running the loop and computing square roots.
        // Average it over the number of runs.

        double averageTime = ((midPointTime - startTime) - (stopTime - midPointTime)) / timesToLoop;

        System.out.println(
                "It takes exactly " + averageTime + " nanoseconds to compute the hash of " + hashString);
    }

}
