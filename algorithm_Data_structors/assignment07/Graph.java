package assignment07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph {
    private int V; //number of vertices
    Node[][] nodes; //two D matrix that represents a graph
    String[] dimensions;

    int height;
    int width;

    int startPositionRow;
    int startPositionColumn;


    Node start = null; //keep track of start
    Node goal = null; //keep track of goal

    public Graph(File file) {
        try (Scanner input = new Scanner(file)) {
            //get dimensions for graph and create 2D array
            dimensions = input.nextLine().split(" "); //get the dimensions

            if (dimensions.length < 2) {
                System.err.println("Cannot find dimensions");
                System.exit(1);
            }


            height = Integer.parseInt(dimensions[0]); //change from string to int
            width = Integer.parseInt(dimensions[1]); //change from string to int
            nodes = new Node[height][width]; //create a new 2D array of correct size


            char[] line;
            for (int i = 0; i < height; i++) {
                line = input.nextLine().toCharArray(); //get the next line create array of chars
                for (int j = 0; j < width; j++) {
                    if (line[j] == 'X') {
                        nodes[i][j] = null;
                    } else {
                        nodes[i][j] = new Node(line[j], i, j); //that line gets added to that row
                        if (nodes[i][j].data == 'S') { //keep track of where the start is
                            start = nodes[i][j];
                            startPositionRow = i;
                            startPositionColumn = j;
                        }
                        else if (nodes[i][j].data == 'G') //keep track of where the goal is
                            goal = nodes[i][j];
                    }
                }
            }

            if (start == null || goal == null) {
                System.err.println("Cannot find Start or Goal");
                System.exit(1);
            }

            System.out.println();
            printGraph();
            System.out.println();

        } catch (FileNotFoundException e) {
            System.err.println("No such file exists");
            e.printStackTrace();
            System.exit(1);
        }
    }

    //    2DMatrix[Row][Column]
    public void printGraph() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (nodes[i][j] == null) {
                    System.out.print("X ");
                } else {
                    System.out.print(nodes[i][j].data + " ");
                }
            }
            System.out.println(" ");
        }
    }
}
