package assignment07;

public class Node {
    boolean visited;
    Node cameFrom;
    char data;
    int row;
    int column;

    public Node(char data, int row, int column){
        visited = false;
        cameFrom = null;
        this.data = data;
        this.row = row;
        this.column = column;
    }
}
