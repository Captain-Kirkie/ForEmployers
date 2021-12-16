module quad;
import common;
import std.random;

/*
whole box:
    bottom left == min cordinate
    top right == max of all dimensions

    figure out what row and collumn in, just divide by size of cell
    col = (P.x - grid min y)/ cell width
    vid for QuadTree:
    https://www.youtube.com/watch?v=RQ-IIdtLg8c
*/
alias P2 = Point!2; // now points are called P2
struct QuadTree
{
    // member variable for quad tree
    Node root;
    // Quad Tree Constructor

    this(P2[] points)
    {
        this.root = new Node(points, boundingBox(points));
    }

    class Node
    { // internal node class
        // NODE member variables
        P2[] nodePoints; // list of points
        Node[4] children; // 4 children
        AABB!2 aabb; // AABB bounding box that explains what area this node covers
        bool leaf = false;

        // get bool is leaf
        bool isLeaf()
        {
            return leaf;
        }

        this(P2[] points, AABB!2 aabb) // NODE constructor
        {
            // writeln("Node Constructor");
            if (points.length < 64)
            { // we are at a leaf node
                nodePoints = points;
                children = null;
                aabb = boundingBox(points);
                leaf = true;
            }
            else
            { // look around 52:59 in partitioning vid
                // split points into 4 quadrants
                // make four children
                // partition array, NE, NW, SE, SW

                // find mind points
                auto midX = (aabb.max[0] + aabb.min[0]) / 2;
                auto midY = (aabb.max[1] + aabb.min[1]) / 2;

                //$ means points.length, so all the points that aren't on the right half
                // splitting into east and west
                auto east = points.partitionByDimension!0(midX); // get the right half
                auto west = points[0 .. $ - east.length]; // get left

                // now that i have east and west, i can split into NE, NW, SE, SW
                // not sure if these are right
                auto NE = east.partitionByDimension!1(midY);
                auto SE = east[0 .. $ - NE.length];
                auto NW = west.partitionByDimension!1(midY);
                auto SW = west[0 .. $ - NW.length]; //????

                children[0] = new Node(NE, boundingBox(NE));
                children[1] = new Node(NW, boundingBox(NW));
                children[2] = new Node(SE, boundingBox(SE));
                children[3] = new Node(SW, boundingBox(SW));
            }
        }

    }

    // end of node class
    P2[] rangeQeury(P2 p, float r)
    {
        P2[] ret;
        void recurse(Node n)
        {
            // if leaf node, we want to check the points and add it
            if (n.isLeaf())
            {
                // writeln("Inside isleaf");
                // writeln(n.nodePoints);
                foreach (P2 point; n.nodePoints) // search through all points
                {
                    if (distance(point, p) < r)
                        ret ~= point; // add to list
                }
            }
            else // otherwise we want to recuse only if neccessary
            {
                foreach (Node i; n.children)
                {
                    // if the closest point in the box to p is <= r away from p
                    auto closestPoint = closest(i.aabb, p);
                    if (distance(closestPoint, p) < r)
                    { // recurse
                        recurse(i);
                    }
                }
            }
        }

        recurse(this.root);
        return ret;
    }

    // around 1 : 10 
    P2[] knnQuery(P2 p, int k) // https://www.youtube.com/watch?v=RQ-IIdtLg8c
    {
        // auto pq = makePriorityQueue!k(p);
        auto pq = makePriorityQueue(p); // make a priority q based off of p

        void recurse(Node n)
        {
            // if leaf node, we want to check the points and add it
            if (n.isLeaf())
            {
                foreach (P2 point; n.nodePoints) // search through all points
                {
                    // if list length less than k
                    if (pq.length < k)
                    {
                        pq.insert(point);
                    }
                    else if (distance(p, point) < distance(p, pq.front))
                    { // add it
                        pq.replaceFront(point);
                    }
                }
            }
            else // otherwise we want to recuse only if neccessary
            {
                foreach (Node i; n.children)
                {
                    if (pq.length < k || distance(closest(i.aabb, p), p) < distance(p, pq.front))
                    {
                        recurse(i);
                    }
                }
            }
        }
        recurse(root);
        return pq.release;
    }
}

unittest
{
    writeln("Range Query Quad tree ");

    auto numTestPts = 100;
    auto uPoints = getUniformPoints!2(numTestPts); //1000 2D points in the [0,1] square
    auto radius = 5;
    auto testNum = 100;

    for (int i = 0; i < testNum; i++)
    {
        // grab random num
        Mt19937 gen;
        gen.seed(unpredictableSeed);
        auto randNum = gen.front % numTestPts; // same for each run

        auto tree = new QuadTree(uPoints);
        P2 testPoint = uPoints[randNum]; // take the 10th point
        auto testRQ = tree.rangeQeury(testPoint, radius);

        foreach (P2 p; testRQ)
        {
            assert(distance(p, testPoint) < radius);
        }
    }
}

unittest
{
    writeln("Quad Tree KNN queary");
    auto numTestPts = 10000;
    auto uPoints = getUniformPoints!2(numTestPts); //1000 2D points in the [0,1] square
    // grab random point
    Mt19937 gen;
    gen.seed(unpredictableSeed);

    for (int i = 0; i < 10; i++)
    {
        auto randNum = gen.front % numTestPts; // same for each run
        auto randK = gen.front % numTestPts;

        auto tree = new QuadTree(uPoints);
        P2 testPoint = uPoints[randNum]; // take the 10th point
        auto testKNN = tree.knnQuery(testPoint, randK);
        assert(testKNN.length == randK);
    }
    
}


