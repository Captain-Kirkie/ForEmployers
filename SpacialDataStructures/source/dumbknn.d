import common;

//this struct depends on the dimension of points we're working with
//to make an actual DumbKNN object, you'd do
//auto myDKNN = DumbKNN!3(arrayOf3DPoints)
struct DumbKNN(size_t Dim)
{

    alias PT = Point!Dim; //this is a "nickname" added for convenience

    private PT[] points; //a member variable

    //constructor
    this(PT[] points)
    {
        //copy the incoming array.  Without dup we're referencing the same array that was passed in.
        this.points = points.dup;
    }

    //these are methods.  They look/work like they do in C++.  Since we're in a struct
    //everything defaults to public
    PT[] rangeQuery(PT p, float r)
    {

        PT[] ret; //empty array
        foreach (const ref q; points)
        { //foreach loop
            if (distance(p, q) < r)
            {
                ret ~= q; //append to the array
            }
        }
        return ret;
    }

    PT[] knnQuery(PT p, int k)
    {
        points.topNByDistance(p, k);
        //here I'm "slicing" my array.  This works pretty much the same as it does in python
        //except we use .. instead of colon.  $ is a shorcut for the length of the array, so
        //arr[0 .. $] means the whole thing and [1 .. $ -1] would refer to the array except for its
        //first and last elements.  Slicing does not copy the array!
        return points[0 .. k].dup; //return a copy so people don't modify the original
    }

}

unittest
{
    //I'd include unitttesting code for each of your data structures to test with
    //use a small # of points and manually check that you get the answers you expect
    auto points = [
        Point!2([.5, .5]), Point!2([1, 1]), Point!2([0.75, 0.4]),
        Point!2([0.4, 0.74])
    ];
    //since the points are 2D, the data structure is a DumbKNN!2
    auto dumb = DumbKNN!2(points);

    writeln("dumbknn rq");
    foreach (p; dumb.rangeQuery(Point!2([1, 1]), .7))
    {
        writeln(p);
    }
    assert(dumb.rangeQuery(Point!2([1, 1]), .7).length == 3);

    writeln("dumb knn");
    foreach (p; dumb.knnQuery(Point!2([1, 1]), 3))
    {
        writeln(p);
    }
}
