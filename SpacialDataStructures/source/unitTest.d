module unitTest;
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

unittest
{
    writeln("Comparison Test");
    alias P2 = Point!2;
    auto numTestPts = 1000;
    auto uPoints = getUniformPoints!2(numTestPts); //1000 2D points in the [0,1] square
    auto K = 5;
    auto radius = 5;

    // grab random num
    Mt19937 gen;
    gen.seed(unpredictableSeed);
    auto randNum = gen.front % numTestPts; // same for each run

    // make QuadTree
    auto tree = new QuadTree(uPoints);
    P2 testPoint = uPoints[randNum]; // take the 10th point
    auto quadRet = tree.rangeQeury(testPoint, radius);

    // make KD Tree
    auto KDtree = new KDTree!2(uPoints);
    auto KDRet = KDtree.rangeQeury(testPoint, radius);

    // make dumb knn and bucket knn, verify all range querys return same points, and all 
    // knn quearys return same list of point, 
    // isPermutation
    auto dumb = DumbKNN!2(uPoints);
    auto dumbRet = dumb.rangeQuery(testPoint, radius);

    assert(isPermutation(quadRet, KDRet));
    assert(isPermutation(dumbRet, KDRet));
    assert(isPermutation(dumbRet, quadRet));
    writeln("Dumb ", dumbRet.length);
    writeln("Quad", quadRet.length);

}
