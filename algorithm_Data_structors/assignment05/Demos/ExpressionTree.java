package assignment05.Demos;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Stack;

public class ExpressionTree {

  BinaryNode root;

  // A binary node specifically for expression trees
  private class BinaryNode {
    // Data part of the node
    char operator; // interior nodes will have an operator (+, -, *, /)
    double operand;// leaf nodes will have an operand (a number)

    // Subtrees
    BinaryNode left;
    BinaryNode right;

    boolean isLeaf() {
      return (left == null) && (right == null);
    }

    @Override
    public String toString() {
      if (isLeaf())
        return "" + operand;
      return "" + operator;
    }
  }

  // Perform a post-order depth first traversal of the tree to evaluate the
  // expression it represents.
  private double PostOrderEvaluate(BinaryNode N) throws UnsupportedOperationException {
    // If the node is a leaf (operand), return the value
    if (N.isLeaf())
      return N.operand;

    // Otherwise,
    // 1. evaluate the left expression tree
    double leftVal = PostOrderEvaluate(N.left);
    // 2. evaluate the right expression tree
    double rightVal = PostOrderEvaluate(N.right);

    // 3. perform N's operator on two sub-expressions
    switch (N.operator) {
    case '+':
      return leftVal + rightVal;
    case '-':
      return leftVal - rightVal;
    case '*':
      return leftVal * rightVal;
    case '/':
      return leftVal / rightVal;
    }

    // If we get passed the switch statement, some invalid character was used
    throw new UnsupportedOperationException();
  }

  // Driver calls recursive method starting at root
  public double evaluate() throws UnsupportedOperationException {
    return PostOrderEvaluate(root);
  }

  // Builds an expression tree given an infix expression string
  public void buildFromInfixExpression(String infix) {

    /*
     * First, we will convert the expression to postfix form (we could bypass this
     * and build the tree directly) See
     * http://penguin.ewu.edu/cscd300/Topic/Stack/WikiExtract.html as an example of
     * the infix -> postfix conversion algorithm
     * 
     * Next, we will build the tree from the postfix string See
     * http://en.wikipedia.org/wiki/Binary_expression_tree for the algorithm
     * 
     */

    // Get easier to manage postfix form
    String postfix = infixToPostfix(infix);

    // Separate individual pieces of the expression
    String[] tokens = postfix.split(" ");

    // We need a stack for the tree construction algorithm
    Stack<BinaryNode> s = new Stack<BinaryNode>();

    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].equals("+") || tokens[i].equals("-") || tokens[i].equals("*") || tokens[i].equals("/")) {
        // operator found, create subtree
        BinaryNode right = s.pop(); // right operand
        BinaryNode left = s.pop(); // left operand
        BinaryNode newNode = new BinaryNode();
        newNode.left = left;
        newNode.right = right;
        newNode.operator = tokens[i].charAt(0);
        s.push(newNode);
      } else // operand found, push new node
      {
        BinaryNode newNode = new BinaryNode();
        newNode.operand = Double.parseDouble(tokens[i]);
        s.push(newNode);
      }
    }

    // The final node on the stack is the root node of the full expression tree
    root = s.pop();
  }

  // Converts an infix expression string to postfix form
  private String infixToPostfix(String infix) {
    String postfix = "";
    Stack<String> s = new Stack<String>();
    String[] tokens = infix.split(" ");
    OperatorComparator cmp = new OperatorComparator();
    for (int i = 0; i < tokens.length; i++) {

      if (tokens[i].equals("+") || tokens[i].equals("-") || tokens[i].equals("*") || tokens[i].equals("/")) {// operator
                                                                                                             // found,
                                                                                                             // check
                                                                                                             // stack
        if (s.isEmpty())
          s.push(tokens[i]);
        else {
          while (true) {
            if (s.isEmpty())
              break;

            if (cmp.compare(s.peek(), tokens[i]) >= 0)
              postfix += s.pop() + " ";
            else
              break;
          }
          s.push(tokens[i]);
        }
      } else // operand found, just add it to the postfix version
        postfix += tokens[i] + " ";

    }
    // Handle any remaining operators
    while (!s.isEmpty()) {
      postfix += s.pop() + " ";
    }
    return postfix;
  }

  // Constructor
  // (hidden down here to avoid clutter during class demo)
  public ExpressionTree(String infix) {
    buildFromInfixExpression(infix);
  }

  // Driver method
  // Generates a .dot file representing this tree.
  // Use each node's hashCode to uniquely identify it
  public void writeDot(String filename) {
    try {
      PrintWriter output = new PrintWriter(new FileWriter(filename));
      output.println("graph g {");
      if (root != null)
        output.println(root.hashCode() + "[label=\"" + root + "\"]");
      writeDotRecursive(root, output);
      output.println("}");
      output.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Recursively traverse the tree, outputting each node to the .dot file
  private void writeDotRecursive(BinaryNode n, PrintWriter output) throws Exception {
    if (n == null)
      return;
    if (n.left != null) {
      output.println(n.left.hashCode() + "[label=\"" + n.left + "\"]");
      output.println(n.hashCode() + " -- " + n.left.hashCode());
    }
    if (n.right != null) {
      output.println(n.right.hashCode() + "[label=\"" + n.right + "\"]");
      output.println(n.hashCode() + " -- " + n.right.hashCode());
    }

    writeDotRecursive(n.left, output);
    writeDotRecursive(n.right, output);
  }

  // Comparator for comparing precedence of operators (* is > +)
  private class OperatorComparator implements Comparator<String> {
    public int compare(String arg0, String arg1) {
      return precedence(arg0.charAt(0)) - precedence(arg1.charAt(0));

    }

    private int precedence(char op) {
      switch (op) {
      case '+':
      case '-':
        return 0;
      case '*':
      case '/':
        return 1;
      }

      // Just make the compiler happy. Hopefully this will never happen.
      return -1;
    }

  }
}
