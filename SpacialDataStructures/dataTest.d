module dataTest;

import quad;
import KD;
import dumbknn;
import common;
import std.range;
import std.algorithm;
import std.container;
import std.stdio;
import std.array;
import std.algorithm;
import std.random;
import bucketknn;
import std.csv;
import std.file;

static int DATAFRAMES = 25; // this is how many data ponts i want
static long TESTING_SIZE = 100;
static const int numBuckets = 5; // this is just a guess currently
static const int N = 100000;
static const int K = 5;
static const int update_K = 50;
static const int update_N = 5000;
// https://dlang.org/phobos/std_csv.html
static void create_CSV()
{
    writeln("\nRunning timeing test");
    TEST();
}

auto TEST()
{ // quad trees are only 2-d
    writeln("Creating CSV");
    // create csv
    auto headers = "dim,N,K,usecs,Type";
    string file_Name = "Results.csv";
    auto file = File(file_Name, "w");
    file.writeln(headers); // write the headers

    // write csv header
    // do it where we change N
    int Nchanging = N; // this updates
    int KChanging = K; // this updates

    testN(Nchanging, K, file);
    testK(N, KChanging, file);
    testDim(N, K, file);
    testNandK(N, K, file);
}

// only changin N here *********************************************
auto testN(int Nchanging, int K, File file)
{
    writeln("Writing N Data");
    
    foreach (i; 0 .. DATAFRAMES)
    {
        if (Nchanging <= K)
        { // an error check because i am dumb
        return;
        }
        // create points
        auto trainingPoints = getGaussianPoints!2(Nchanging);
        auto testingPoints = getUniformPoints!2(TESTING_SIZE); // this is size

        // creeate all relevant data structures
        auto quadTree = new QuadTree(trainingPoints);
        auto KDTree = new KDTree!2(trainingPoints);
        auto dumb = new DumbKNN!2(trainingPoints); // now have dumb
        auto bknn = BucketKNN!2(trainingPoints, cast(int) pow(Nchanging / 64, 1.0 / 2));

        // create all relevant stopwatches
        auto sw_quad = StopWatch(AutoStart.no);
        auto sw_KD = StopWatch(AutoStart.no);
        auto sw_dumb = StopWatch(AutoStart.no);
        auto sw_bucket = StopWatch(AutoStart.no);

        // quad timing
        sw_quad.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto quadTest = quadTree.knnQuery(qp, K);
        }
        sw_quad.stop;
        long avg_Quad = sw_quad.peek.total!"usecs" / TESTING_SIZE; // get the average
        // write to CSV
        file.writeln(2, ',', Nchanging, ',', K, ',', avg_Quad, ',', "QuadTree_N"); // Write to quad

        // KD Timing
        sw_KD.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto KDTest = KDTree.knnQuery(qp, K);
        }
        sw_KD.stop;
        long avg_KD = sw_KD.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', Nchanging, ',', K, ',', avg_KD, ',', "KDTree_N"); // Write to quad

        // dumb timeing
        sw_dumb.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto dumbTest = dumb.knnQuery(qp, K);
        }
        sw_dumb.stop;
        long avg_dumb = sw_dumb.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', Nchanging, ',', K, ',', avg_dumb, ',', "Dumb_N"); // Write to quad

        // bucket timing
        sw_bucket.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto bknnTest = bknn.knnQuery(qp, K);
        }
        sw_bucket.stop;
        long avg_bucket = sw_bucket.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', Nchanging, ',', K, ',', avg_bucket, ',', "Bucket_N"); // Write to quad

        Nchanging += update_N; // Update N
    }
}
// only changin K here *********************************************
auto testK(int N, int KChanging, File file)
{
    writeln("Writing K Data");
   
    foreach (i; 0 .. DATAFRAMES)
    {
        if (N <= KChanging)
        { // an error check because i am dumb
        return;
        }

        // create points
        auto trainingPoints = getGaussianPoints!2(N); // this should be a stable N
        auto testingPoints = getUniformPoints!2(TESTING_SIZE); // this is size

        // creeate all relevant data structures
        auto quadTree = new QuadTree(trainingPoints);
        auto KDTree = new KDTree!2(trainingPoints);
        auto dumb = new DumbKNN!2(trainingPoints); // now have dumb
        auto bknn = BucketKNN!2(trainingPoints, cast(int) pow(N / 64, 1.0 / 2));

        // create all relevant stopwatches
        auto sw_quad = StopWatch(AutoStart.no);
        auto sw_KD = StopWatch(AutoStart.no);
        auto sw_dumb = StopWatch(AutoStart.no);
        auto sw_bucket = StopWatch(AutoStart.no);

        // quad timing
        sw_quad.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto quadTest = quadTree.knnQuery(qp, KChanging);
        }

        sw_quad.stop;
        long avg_Quad = sw_quad.peek.total!"usecs" / TESTING_SIZE; // get the average
        // write to CSV
        file.writeln(2, ',', N, ',', KChanging, ',', avg_Quad, ',', "QuadTree_K"); // Write to quad

        // KD Timing

        sw_KD.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto KDTest = KDTree.knnQuery(qp, KChanging);
        }
        sw_KD.stop;
        long avg_KD = sw_KD.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', N, ',', KChanging, ',', avg_KD, ',', "KDTree_K"); // Write to quad

        // dumb timing

        sw_dumb.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto dumbTest = dumb.knnQuery(qp, KChanging);
        }

        sw_dumb.stop;

        long avg_dumb = sw_dumb.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', N, ',', KChanging, ',', avg_dumb, ',', "Dumb_K"); // Write to quad

        // bucket

        sw_bucket.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto bknnTest = bknn.knnQuery(qp, KChanging);
        }

        sw_bucket.stop;

        long avg_bucket = sw_bucket.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', N, ',', KChanging, ',', avg_bucket, ',', "Bucket_K");

        KChanging += update_K; // Update K
    }
}

// only changin Dim here *********************************************
auto testDim(int N, int K, File file)
{
    writeln("Writing Dim Data");
    if (N <= K)
    { // an error check because i am dumb
        return;
    }
    static foreach (dim; 1 .. 8)
    {
        { // THIS SHOULD BE THE SAME AS DATAFRAMES
            auto trainingPoints = getGaussianPoints!dim(N);
            auto testingPoints = getUniformPoints!dim(TESTING_SIZE); // this is size

            // make relevant structures
            auto KDTree = new KDTree!dim(trainingPoints);
            auto dumb = new DumbKNN!dim(trainingPoints); // now have dumb
            auto bknn = BucketKNN!dim(trainingPoints, cast(int) pow(N / 64, 1.0 / dim));

            // create all relevant stopwatches
            auto sw_quad = StopWatch(AutoStart.no);
            auto sw_KD = StopWatch(AutoStart.no);
            auto sw_dumb = StopWatch(AutoStart.no);
            auto sw_bucket = StopWatch(AutoStart.no);

            // KD Dim Timing
            sw_KD.start;
            foreach (const ref qp; testingPoints)
            { // go through every testing point
                auto testKD = KDTree.knnQuery(qp, K);
            }
            sw_KD.stop;
            long avg_KD_Dim = sw_KD.peek.total!"usecs" / TESTING_SIZE; // get the average
            file.writeln(dim, ',', N, ',', K, ',', avg_KD_Dim, ',', "KDTree_Dim");

            //dumb timing
            sw_dumb.start;
            foreach (const ref qp; testingPoints)
            { // go through every testing point
                auto dumbTest = dumb.knnQuery(qp, K);
            }
            sw_dumb.stop;
            long avg_dumb = sw_dumb.peek.total!"usecs" / TESTING_SIZE;
            file.writeln(dim, ',', N, ',', K, ',', avg_dumb, ',', "Dumb_Dim");

            //bucket timing
            sw_bucket.start;
            foreach (const ref qp; testingPoints)
            { // go through every testing point
                auto bucketTest = bknn.knnQuery(qp, K);
            }
            sw_bucket.stop;
            long avg_bucket = sw_bucket.peek.total!"usecs" / TESTING_SIZE;
            file.writeln(dim, ',', N, ',', K, ',', avg_bucket, ',', "Bucket_Dim");
        }
    }
}

// only changin VARY N AND K here *********************************************
auto testNandK(int N, int K, File file)
{
    writeln("Testing both N and K");

    int newK = K;
    int newN = N;
    for (int i = 0; i < DATAFRAMES; i++)
    {
        if (newK >= newN)
        { // bandaid
            break;
        }
        // create points
        auto trainingPoints = getGaussianPoints!2(newN); // this should be a stable N
        auto testingPoints = getUniformPoints!2(TESTING_SIZE); // this is size

        // creeate all relevant data structures
        auto quadTree = new QuadTree(trainingPoints);
        auto KDTree = new KDTree!2(trainingPoints);
        auto dumb = new DumbKNN!2(trainingPoints); // now have dumb
        auto bknn = BucketKNN!2(trainingPoints, cast(int) pow(newN / 64, 1.0 / 2));

        // create all relevant stopwatches
        auto sw_quad = StopWatch(AutoStart.no);
        auto sw_KD = StopWatch(AutoStart.no);
        auto sw_dumb = StopWatch(AutoStart.no);
        auto sw_bucket = StopWatch(AutoStart.no);

        // quad timing
        sw_quad.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto quadTest = quadTree.knnQuery(qp, newK);
        }

        sw_quad.stop;
        long avg_Quad = sw_quad.peek.total!"usecs" / TESTING_SIZE; // get the average
        // write to CSV
        file.writeln(2, ',', newN, ',', newK, ',', avg_Quad, ',', "QuadTree_K_N"); // Write to quad

        // KD Timing
        sw_KD.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto KDTest = KDTree.knnQuery(qp, newK);
        }
        sw_KD.stop;
        long avg_KD = sw_KD.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', newN, ',', newK, ',', avg_KD, ',', "KDTree_K_N"); // Write to quad

        // dumb timing
        sw_dumb.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto dumbTest = dumb.knnQuery(qp, newK);
        }

        sw_dumb.stop;

        long avg_dumb = sw_dumb.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', newN, ',', newK, ',', avg_dumb, ',', "Dumb_K_N"); // Write to quad

        // bucket
        sw_bucket.start;
        foreach (const ref qp; testingPoints)
        { // go through every testing point
            auto bknnTest = bknn.knnQuery(qp, newK);
        }

        sw_bucket.stop;

        long avg_bucket = sw_bucket.peek.total!"usecs" / TESTING_SIZE;
        file.writeln(2, ',', newN, ',', newK, ',', avg_bucket, ',', "Bucket_K_N");
        // update new k
        newK += update_K;

        // update new n
        newN += update_N;

    }
}
