package lab02Timing;

public class Main {

    public static void main(String[] args) {
        System.out.println("TimeMillis time in milliseconds?: " + System.currentTimeMillis());
        System.out.println("NanoTime time in nanoSeconds?: " + System.nanoTime());
        //both longs
        //nano time more precise? use System.nanoTime();
        //most computers run 3-4 CPU instructions per nanoSecond
        //dont use print statments, between starting and stopping, kills time
        //average timing over 10's of thousands for small inputs, and 1000 for large



    }
}


