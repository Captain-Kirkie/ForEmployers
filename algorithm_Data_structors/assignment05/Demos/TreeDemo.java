package assignment05.Demos;

public class TreeDemo {

  public static void main(String[] args) {

    ExpressionTree t;

    // Expressions must have spaces seperating all all operands and operators

    // t = new ExpressionTree("3 + 5");
    // t = new ExpressionTree("1 + 2 * 3");
    // t = new ExpressionTree("2 * 5 + 4");
    // t = new ExpressionTree("2 * 5 + 4 / 3");
    t = new ExpressionTree("5 - 4 / 12 * 2 + 4");

    System.out.println(t.evaluate());

    t.writeDot("expressionTree.dot");

    // On a CADE machine, use the following command to convert expressionTree.dot to
    // a gif

    // First change directory to your project directory, where expressionTree.dot
    // resides, then run:
    // dot -Tgif expressionTree.dot -o expression.gif

    // Or in OSX, install the graphviz tool, then just double click on the .dot file

  }

}
