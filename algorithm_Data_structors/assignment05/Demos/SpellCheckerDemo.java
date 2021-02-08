package assignment05.Demos;

import assignment05.SpellChecker;

import java.io.File;
import java.util.List;

/**
 * A small demonstration of the SpellChecker class.
 * 
 * @author
 */
public class SpellCheckerDemo {

  public static void main(String[] args) {

    SpellChecker mySC = new SpellChecker(new File("src/assignment05/dictionary.txt"));

    run_spell_check(mySC, "src/assignment05/hello_world.txt");
    run_spell_check(mySC, "src/assignment05/good_luck.txt");
  }

  public static void run_spell_check(SpellChecker sc, String documentFilename) {

    File doc = new File(documentFilename);
    List<String> misspelledWords = sc.spellCheck(doc);
    if (misspelledWords.size() == 0) {
      System.out.println("There are no misspelled words in file " + doc + ".");
    } else {
      System.out.println("The misspelled words in file " + doc + " are:");
      for (String w : misspelledWords) {
        System.out.println("\t" + w);
      }
    }
  }
}
