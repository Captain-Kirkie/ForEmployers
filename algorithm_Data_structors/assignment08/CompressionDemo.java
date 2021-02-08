package assignment08;

import java.io.File;

//dot -Tpng expressionTree.dot -o expressionTree.png makes  png of tree
//dot -Tgif expressionTree.dot -o expressionTree.png
//dot -Tpng First_add.dot -o firstAdd.png


public class CompressionDemo {
  
  public static void compressFile(String infile, String outfile) {

    HuffmanTree t = new HuffmanTree();

    t.compressFile(new File(infile), new File(outfile));
    t.huffmanToDot("huffman_tree.dot");

  }
  
  public static void decompressFile(String infile, String outfile) {
    HuffmanTree t = new HuffmanTree();


    t.decompressFile(new File(infile), new File(outfile));
  }
  
  public static void main(String[] args) {
    compressFile("/Users/kirkhietpas/Desktop/GitHub/kirkHietpas/CS6012/CS6012-Fall2020/src/assignment08/original.txt", "compressed.txt");
    decompressFile("compressed.txt", "decompressed.txt");
  }
}