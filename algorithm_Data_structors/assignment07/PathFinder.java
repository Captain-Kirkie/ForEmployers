package assignment07;
//to visualize maze
// /usr/bin/python pacman.py -l src/assignment07/mazes/demoMaze.txt

//solution
// python pacman.py -l demoMazeSol.txt -p auto

//zooming
// python pacman.py -l demoMazeSol.txt -p auto -z 0.5
// python pacman.py -l demoMazeSol.txt -p auto -z 2

//https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
//https://www.youtube.com/watch?v=TIbUeeksXcI
//depth first check 3:51
//breadth first

//depth first goes deep
//breadth goes out layer by layer, shock wave

//Good description
//https://medium.com/omarelgabrys-blog/path-finding-algorithms-f65a8902eb40

//https://hurna.io/academy/algorithms/maze_pathfinder/bfs.html

// this one is just cool http://www.questionablyartificial.com/2015---path-planning-mazes--pacman.html
//https://www.khanacademy.org/computing/computer-science/algorithms/intro-to-algorithms/a/route-finding
//TODO: This one is good!
//https://www.geeksforgeeks.org/shortest-distance-two-cells-matrix-grid/
// A*
//http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {


    /**
     * @param inputFile  maze to search
     * @param outPutFile solution to searched maze
     *                   TODO: You must use the filenames exactly as is (do not change the directory or path). Figure this out?
     *                   We will provide the full path to files we want to read/write in our tests.
     */

    public static void solveMaze(String inputFile, String outPutFile) throws IOException {
        //get the dimensions of the maze
        File file = new File(inputFile);
        Graph graph = new Graph(file); //create a new graph that is of this type
        //perform breadth first search... return an array of nodes with shortest path
        ArrayList<Node> shortPath = getShortestPathBFS(graph.nodes, graph.startPositionRow, graph.startPositionColumn, graph.goal);

        for (Node n : shortPath) {
            n.data = '.';
        }

        System.out.println();
        System.out.println("Solved BFS maze");
        graph.printGraph();

        printOutputFile(graph, outPutFile);

    }


    /**
     *
     * @param inputFile file name of maze input
     * @param outPutFile file name of outputted maze file
     *                   solves maze using Depthfirst search
     *                   this is not always the shortest path
     */
    public static void solveMazeDepthFirst(String inputFile, String outPutFile) {
        File file = new File(inputFile); //create a file
        Graph graph = new Graph(file);

        //do depth first search hear
        ArrayList<Node> path = new ArrayList<>();
        int startRow = graph.start.row;
        int startColumn = graph.start.column;

        DFS(graph.nodes[startRow][startColumn], graph.goal, graph.nodes);

        Node currentNode = graph.goal;
        while (currentNode.cameFrom != null) {
            if (currentNode.cameFrom.data == 'S')
                break;

            path.add(currentNode.cameFrom);
            currentNode = currentNode.cameFrom;
        }

        for (Node n : path) {
            n.data = '.';
        }

        System.out.println();
        System.out.println("Solved DFS maze");
        graph.printGraph();

        printOutputFile(graph, outPutFile);

    }

    /**
     * DFS search algorithim, goes DEEEP
     * @param curr current node
     * @param goal node we are searching for
     * @param nodes 2D array of nodes
     *              looks at neighbors, if not null and not visited, visits neighbors recursively
     */

    private static void DFS(Node curr, Node goal, Node[][] nodes) {
        curr.visited = true;

        if (curr.equals(goal))
            return;

        //look up
        if (nodes[curr.row - 1][curr.column] != null && !nodes[curr.row - 1][curr.column].visited) {
            nodes[curr.row - 1][curr.column].cameFrom = curr;
            DFS(nodes[curr.row - 1][curr.column], goal, nodes);
        }


        //look down
        if (nodes[curr.row + 1][curr.column] != null && !nodes[curr.row + 1][curr.column].visited) {
            nodes[curr.row + 1][curr.column].cameFrom = curr;
            DFS(nodes[curr.row + 1][curr.column], goal, nodes);
        }


        //look left
        if (nodes[curr.row][curr.column - 1] != null && !nodes[curr.row][curr.column - 1].visited) {
            nodes[curr.row][curr.column - 1].cameFrom = curr;
            DFS(nodes[curr.row][curr.column - 1], goal, nodes);
        }


        //look right
        if (nodes[curr.row][curr.column + 1] != null && !nodes[curr.row][curr.column + 1].visited) {
            nodes[curr.row][curr.column + 1].cameFrom = curr;
            DFS(nodes[curr.row][curr.column + 1], goal, nodes);
        }

    }

    /**
     *
     * @param nodes 2D array of nodes
     * @param startRow starting Row
     * @param startColumn starting column
     * @param goal goal we are searching for
     * @return returns an arraylist of nodes, indicating shortest path from S -> G
     * gets current node from Queue
     * checks if current node is the goal
     * Checks neighbors to verify they are not null and not visited
     * if both are true, adds neighbors to back of queue
     * continues iteratively until goal is found, if cant find goal, returns empty array List
     *
     * while !Q.empty()
     * deque current node
     *   for each unvisited current node
     *      mark neighbor as visited
     *      put neighbor on back of Q
     */


    //Get shortest path using Breadth First Search
    private static ArrayList<Node> getShortestPathBFS(Node[][] nodes, int startRow, int startColumn, Node goal) {
        Queue<Node> queue = new LinkedList<>(); //make a queue
        ArrayList<Node> shortestPath = new ArrayList<>(); //array list to get the shortest path


        queue.add(nodes[startRow][startColumn]); //add start node to the queue
        nodes[startRow][startColumn].visited = true; //mark it as visited
        nodes[startRow][startColumn].cameFrom = null; //set start nodes came from to null

        while (!queue.isEmpty()) { //while the queue is not empty
            Node curr = queue.poll(); //pop the first item off the queue

            //if the destination is found
            if (nodes[curr.row][curr.column] == goal) {
                break;
            }

            //look up row - 1
            if (nodes[curr.row - 1][curr.column] != null && !nodes[curr.row - 1][curr.column].visited) {
                queue.add(nodes[curr.row - 1][curr.column]); //add to queue
                nodes[curr.row - 1][curr.column].visited = true; //mark it as visited
                nodes[curr.row - 1][curr.column].cameFrom = curr;
            }

            //look down row + 1
            if (nodes[curr.row + 1][curr.column] != null && !nodes[curr.row + 1][curr.column].visited) {
                queue.add(nodes[curr.row + 1][curr.column]); //add to queue
                nodes[curr.row + 1][curr.column].visited = true; //mark it as visited
                nodes[curr.row + 1][curr.column].cameFrom = curr;
            }

            //look left column - 1
            if (nodes[curr.row][curr.column - 1] != null && !nodes[curr.row][curr.column - 1].visited) {
                queue.add(nodes[curr.row][curr.column - 1]); //add to queue
                nodes[curr.row][curr.column - 1].visited = true; //mark it as visited
                nodes[curr.row][curr.column - 1].cameFrom = curr;
            }

            //look right column + 1
            if (nodes[curr.row][curr.column + 1] != null && !nodes[curr.row][curr.column + 1].visited) {
                queue.add(nodes[curr.row][curr.column + 1]); //add to queue
                nodes[curr.row][curr.column + 1].visited = true; //mark it as visited
                nodes[curr.row][curr.column + 1].cameFrom = curr;
            }
        }
        //starting with the goal, following shortest path back
        Node currentNode = goal;

        while (currentNode.cameFrom != null) {
            if (currentNode.cameFrom.data == 'S')
                break;

            shortestPath.add(currentNode.cameFrom);
            currentNode = currentNode.cameFrom;
        }
        return shortestPath;
    }

    /**
     *
     * @param graph graph to be printed to output file
     * @param outPutFile name of output file
     *                   creates a text file if one does not already exist
     *                   creates text file with solved maze
     */

    private static void printOutputFile(Graph graph, String outPutFile) {
        try (PrintWriter output = new PrintWriter(new FileWriter(outPutFile))) {
            output.println(graph.height + " " + graph.width);
            for (int i = 0; i < graph.height; i++) {
                for (int j = 0; j < graph.width; j++) {
                    if (graph.nodes[i][j] == null) {
                        output.print("X");
                    } else {
                        output.print(graph.nodes[i][j].data);
                    }
                }
                output.print("\n");
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("Cannot write file");
            e.getCause();
        }
    }
}







