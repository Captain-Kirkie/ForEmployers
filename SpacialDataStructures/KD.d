module KD;
import common;
import std.range;
import std.algorithm;
import std.container;
import std.stdio;
import std.array;
import std.algorithm;
import std.random;

/*
KD tree == generalized bst
compare points:
    a.x < b.x
    a.y < b.y

    Key each level based off eitherx or y
    swap every level
    https://www.geeksforgeeks.org/k-dimensional-tree/
    https://www.youtube.com/watch?v=hBKqtB9T-F8

    Node!0 = x
    Node!1 = y
*/

struct KDTree(size_t Dim)
{
    alias PT = Point!Dim;

    Node!0 root; // hardcode root to be x

    this(PT[] points)
    { // always start at 0 dim
        root = new Node!0(points); // new x node
    }

    //An x-split node and a y-split node are different types
    class Node(size_t splitDimension)
    {
        enum thisLevel = splitDimension; //this lets you refer to a node's split level with theNode.thisLevel
        enum nextLevel = (splitDimension + 1) % Dim;
        Node!nextLevel left, right; //child nodes split by the next level
        PT splitPoint; // value of node

        this(PT[] points)
        { // constructor for node
            points.medianByDimension!thisLevel; // split the points based on median
            int medianIndex = cast(int)(points.length / 2); // maybe check if odd or even
            auto leftList = points[0 .. medianIndex]; // left half of list
            auto rightList = points[medianIndex + 1 .. $]; // grab right half
            splitPoint = points[medianIndex]; // set the value of the node
           // points = points[0 .. medianIndex] ~ points[medianIndex + 1 .. $]; // remove median node

            if (leftList.length > 0)
            {
                this.left = new Node!nextLevel(leftList);
            }
            if (rightList.length > 0)
            {
                this.right = new Node!nextLevel(rightList);
            }
        }
    }

    PT[] rangeQeury(PT p, float r)
    { // range query
        PT[] ret;

        void recurse(Node)(Node n)
        {
            if (distance(n.splitPoint, p) < r)
            { // if within range, add to list
                ret ~= n.splitPoint;
            }

            // if qp.x - r < sp.x
            if (n.left !is null && (p[n.thisLevel] - r) < n.splitPoint[n.thisLevel])
            {
                recurse(n.left);
            }
            // if qp.x + r > sp.x   y is the same
            if (n.right !is null && (p[n.thisLevel] + r) > n.splitPoint[n.thisLevel])
            {
                recurse(n.right);
            }
        }

        recurse(root);
        return ret;
    }

    PT[] knnQuery(PT p, int k)
    {
        // make an infinite bounding box
        AABB!Dim infiniteBox = AABB!Dim();
        infiniteBox.min[] = -float.infinity;
        infiniteBox.max[] = float.infinity;
        auto pq = makePriorityQueue(p); // make a priority q based off of p

        void recurse(Node)(Node n, AABB!Dim box)
        {
            if (pq.length < k)
            {
                pq.insert(n.splitPoint);
            }
            else if (distance(p, n.splitPoint) < distance(p, pq.front)) // check point in node
            {
                pq.replaceFront(n.splitPoint);
            }
            // now we decide if we recurse

            // if list size < k || closet point in left is closer to p than worst in list
            AABB!Dim leftBox = AABB!Dim(); // 
            leftBox.min = box.min.dup; // making a duplicate
            leftBox.max = box.max.dup; // copy everything
            leftBox.max[n.thisLevel] = n.splitPoint[n.thisLevel];
           

            AABB!Dim rightBox = AABB!Dim();
            rightBox.max = box.max.dup; // copy everything
            rightBox.min = box.min.dup;
            rightBox.min[n.thisLevel] = n.splitPoint[n.thisLevel];
        
            // recurse left

            if (n.left !is null && (pq.length < k || distance(closest(leftBox, p),
                    p) < distance(pq.front, p)))
            {
                recurse(n.left, leftBox);
            }

            if (n.right !is null && (pq.length < k || distance(closest(rightBox,
                    p), p) < distance(pq.front, p)))
            {
                recurse(n.right, rightBox);
            }
        }

        recurse(this.root, infiniteBox);
        return pq.release;
    }

}

unittest
{
    writeln("\n\nRunning KD Tree Unit Test-SMALL TEST");
    auto KDtree = KDTree!2([
            Point!2([.5, .5]), Point!2([1, 1]), Point!2([0.75, 0.4]),
            Point!2([0.4, 0.74]), Point!2([0.56, 0.73]), Point!2([0.6, 0.73])
            ]);
    auto KDRange = KDtree.rangeQeury(Point!2([1, 1]), 2);
}

unittest
{
    writeln("\n\nRunning KD Tree RANGE QUEARY Test-BIG TEST");
    auto numTestPts = 1000;
    auto uPoints = getUniformPoints!2(numTestPts); //1000 2D points in the [0,1] square
    auto radius = 5;
    auto testNum = 100;

    for (int i = 0; i < testNum; i++)
    {
        // grab random num
        Mt19937 gen;
        gen.seed(unpredictableSeed);
        auto randNum = gen.front % numTestPts; // same for each run

        auto KDtree = new KDTree!2(uPoints);
        Point!2 testPoint = uPoints[randNum]; // take the random-th point
        auto testRQ = KDtree.rangeQeury(testPoint, radius);

        foreach (Point!2 p; testRQ)
        {
            assert(distance(p, testPoint) < radius);
        }
    }
}

unittest
{
    writeln("\n\nRUNNING KDTREE KNN TEST");
    auto numTestPts = 1000;
    auto uPoints = getUniformPoints!2(numTestPts); //1000 2D points in the [0,1] square
    auto K = 5;

    // grab random num
    Mt19937 gen;
    gen.seed(unpredictableSeed);
    auto randNum = gen.front % numTestPts; // same for each run

    auto KDtree = new KDTree!2(uPoints);

    Point!2 testPoint = uPoints[randNum]; // take the random-th point
    auto testKNN = KDtree.knnQuery(testPoint, K);
    writeln(testKNN);
}
