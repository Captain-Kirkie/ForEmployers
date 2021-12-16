public import std.stdio;
public import std.range;
public import std.array;
public import std.algorithm;
public import std.traits;
public import std.math;
public import std.datetime.stopwatch;
public import std.container.binaryheap;

//Use like Point!dimension.  See a 2-D example below
struct Point(size_t N)
{ // N is a compile-time parameter
    float[N] data;
    alias data this; //you can treat a Point like it's a float[]

    Point opBinary(string op)(float r)
    {
        Point ret;
        foreach (i; 0 .. N)
        {
            ret[i] = mixin("data[i] " ~ op ~ "r");
        }
        return ret;
    }

    Point opBinary(string op)(Point p)
    {
        Point ret;
        foreach (i; 0 .. N)
        {
            ret[i] = mixin("data[i] " ~ op ~ "p.data[i]");
        }
        return ret;
    }
}

//For the bucketing approach.  This represents the integer coordinates of a bucket
struct Indices(size_t Dim)
{
    size_t[Dim] data;
    alias data this;
}

//sqrt( (a0 - b0)^2 + (a1 - b1)^2 ...)
float distance(T)(T a, T b) if (isInstanceOf!(Point, T))
{
    return sqrt(a[].zip(b[]) //group the elemnts of a and b
            .map!(x => (x[0] - x[1]) * (x[0] - x[1])) //get the squared differences
            .sum); //and sum them
}

//I encourage you to write unittest blocks in your code.  You can run them all
//with `dub test` from the top level of the project
unittest
{
    auto x = Point!2([3, 0]); // this is 2-d struct point
    auto y = Point!2([0, -4]);
    assert(distance(x, y) == 5);

}

//Axis Aligned Bounding Box
//this stores the "bottom left" and "top right" corners of a box that's parallel to the axes
//Works for any dimension
struct AABB(size_t N)
{
    Point!N min, max; // has two poknts, a min and a max
}

//get the coordinates on the min and max corners of a list of points
AABB!N boundingBox(size_t N)(Point!N[] points)
{
    AABB!N ret;
    ret.min[] = float.infinity;
    ret.max[] = -float.infinity;
    foreach (const ref p; points)
    {
        foreach (i; 0 .. N)
        {
            ret.min[i] = min(ret.min[i], p[i]);
            ret.max[i] = max(ret.max[i], p[i]);
        }
    }
    return ret;
}

//return the point in an AABB to p
//This will be useful for the KNN methods of the tree data structures
Point!N closest(size_t N)(AABB!N aabb, Point!N p)
{
    foreach (i; 0 .. N)
    {
        p[i] = clamp(p[i], aabb.min[i], aabb.max[i]);
    }
    return p;
}

unittest
{
    auto points = [Point!2([1, 2]), Point!2([-2, 5])];
    auto aabb = boundingBox(points);
    assert(aabb.min == Point!2([-2, 2]));
    assert(aabb.max == Point!2([1, 5]));

    assert(closest(aabb, Point!2([0, 0])) == Point!2([0, 2])); //call closest using the normal function syntax
    assert(aabb.closest(Point!2([0.5, 3])) == Point!2([0.5, 3])); //call it using the method-like syntax... does the same thing
}

//Used for the BucketKNN, so you can probably ignore this!
//
//takes in the indices of the bottom left/top right corners of a box
//and returns all the buckets in that box/cube
//You'll want to use this to loop through all the buckets in a piece of your
//bucketed data structure
//You can probably safely ignore looking at this implementation... it's ugly
//look at the unitttest below that uses it
auto getIndicesRange(size_t Dim)(Indices!Dim start, Indices!Dim stop)
{
    auto helper(size_t N)()
    {
        auto thisIota = iota(start[N], stop[N] + 1).map!(x => [x]);
        static if (N == Dim - 1)
        {
            return thisIota;
        }
        else
        {
            return cartesianProduct(thisIota, helper!(N + 1)()).map!(function(x) {
                return x[0] ~ x[1]; // ~ appending to a list
            });
        }
    }

    return helper!0().map!(function Indices!Dim(x) {
        return Indices!Dim(x[0 .. Dim]);
    });
}

unittest
{
    // in para == run time parameters???
    // after ! == compile time parameters
    auto bottomLeft = Indices!3([0, 2, 3]);
    auto topRight = Indices!3([2, 3, 5]);
    writeln("indices between ", bottomLeft, " and ", topRight);
    foreach (ind; getIndicesRange(bottomLeft, topRight))
    {
        writeln(ind);
    }

}

//Partition the list of points so that the median is in the right place and the
//left half has all points with a smaller coordinate in SortingDim
//and the right half has all poitns with greater coordinate in sortingDim
auto medianByDimension(size_t SortingDim, size_t PointDim)(Point!PointDim[] points)
{
    return points.topN!((a, b) => a[SortingDim] < b[SortingDim])(points.length / 2);
}

unittest
{
    auto points = [Point!2([1, 2]), Point!2([3, 1]), Point!2([2, 3])];
    points.medianByDimension!0; //partition based on x coordinate
    //"median" had x coordinate 2,
    //"left half" has the point with x coordinate less than 2
    //"righ thalf" has the point with x coordinate greater than 2
    assert(points == [Point!2([1, 2]), Point!2([2, 3]), Point!2([3, 1])]);

    points.medianByDimension!1; //partition based on y coordinate
    //point with y coordinate of 2 is in the middle
    //y coordinate 1 on the left side, y coorindate 3 on the right side
    assert(points == [Point!2([3, 1]), Point!2([1, 2]), Point!2([2, 3])]);
}

//partition the list of points so the front part has all the points with a smaller coordinate
//in the sortingDim, and the right part has all the points with greater coordinate in the sortingDim
//this returns the "right half".  See the unitTest for how to work with this
auto partitionByDimension(size_t sortingDim, size_t PointDim)(
        Point!PointDim[] points, float splitValue)
{
    return points.partition!(x => x[sortingDim] < splitValue);
}

unittest
{
    auto points = [Point!2([1, 2]), Point!2([3, 1]), Point!2([2, 3])];
    auto rightHalf = points.partitionByDimension!0(2.5); // half of 5
    //$ means points.length, so all the points that aren't on the right half
    auto leftHalf = points[0 .. $ - rightHalf.length];
    assert(rightHalf == [Point!2([3, 1])]);
    assert(leftHalf.length == 2); //not sure what order they'll be in

    //to partition by y coordinate, you'd use partitionByDimension!1 instead of 0
}

//returns a "max heap" keyed by distance to p.  pq.front is the point farthest from p
//see below for how you might use it
auto makePriorityQueue(size_t Dim)(Point!Dim p)
{
    Point!Dim[] storage;
    return BinaryHeap!(Point!Dim[], (a, b) => distance(a, p) < distance(b, p))(storage);
}

unittest
{
    auto points = [Point!2([1, 2]), Point!2([3, 1]), Point!2([2, 3])];
    auto pq = makePriorityQueue(Point!2([0, 0]));
    foreach (p; points)
        pq.insert(p);
    assert(pq.front == Point!2([2, 3])); //it's the farthest away
    pq.popFront; //remove (2,3)
    //use release to get the array out of the pq
    assert(pq.release == [Point!2([3, 1]), Point!2([1, 2])]); // the farthest is still furthest
}

//reorders points so that the closes to p comes first, and the farthest from p is last
void sortByDistance(size_t Dim)(Point!Dim[] points, Point!Dim p)
{
    points.sort!((a, b) => distance(a, p) < distance(b, p));
}

//similar to sort.  Array will be reordered so the k closest to p will come first
//there are no guarantees of order though, so they won't be sorted, and the rest won't be sorted
//this will be a little bit faster than sort if you don't care about the ordering for part of the list
void topNByDistance(size_t Dim)(Point!Dim[] points, Point!Dim p, int k)
{
    points.topN!((a, b) => distance(a, p) < distance(b, p))(k);
}

//get n points with coordinates uniformly distributed between 0 and 1
//when Dim == 2, this is points in the "unit squre", Dim == 3 -> unit cube, etc
Point!Dim[] getUniformPoints(size_t Dim)(size_t n)
{

    import std.random : uniform01;

    auto ret = new Point!Dim[n];
    foreach (ref p; ret)
    {
        foreach (i; 0 .. Dim)
        {
            p[i] = uniform01!float;
        }
    }
    return ret;
}

//get points with coordinates that come from a normal distribution with mean = 0
//variance = 1.  Unlike the uniform points, these will NOT be evenly spread out.
//There will be more points closer to the origin than further away.
//Also note, there is not bound on how far away the points may be from the origin
//The further from the origin, the lower the probability of sampling them, but
//you could get points very far away if you're "lucky"
Point!Dim[] getGaussianPoints(size_t Dim)(size_t n)
{
    import std.mathspecial : normalDistributionInverse;

    return getUniformPoints!Dim(n).map!(function(Point!Dim x) {
        Point!Dim ret;
        foreach (i; 0 .. Dim)
        {
            ret[i] = normalDistributionInverse(x[i]);
        }
        return ret;
    }).array;
}

unittest
{
    writeln("In ");
    auto uPoints = getUniformPoints!2(1000); //1000 2D points in the [0,1] square
    auto uBounds = boundingBox(uPoints);
    //they should all be within the unit square
    assert(uBounds.min[0] >= 0);
    assert(uBounds.min[1] >= 0);
    assert(uBounds.max[0] <= 1);
    assert(uBounds.max[1] <= 1);

    auto gPoints = getGaussianPoints!3(10000);
    //no guarantees here...
    writeln("gaussian points bounding box: ", boundingBox(gPoints));
     
}
