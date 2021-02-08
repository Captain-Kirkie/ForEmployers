package assignment05;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a "dictionary" of strings using a binary search tree and offers
 * methods for spell-checking documents.
 *
 * @author
 */
public class SpellChecker {

    private BinarySearchTree<String> dictionary;

    /**
     * Default constructor--creates empty dictionary.
     */
    public SpellChecker() {
        dictionary = new BinarySearchTree<String>();
    }

    /**
     * Constructor--creates dictionary from a list of words.
     *
     * @param words - the List of Strings used to build the dictionary
     */
    public SpellChecker(List<String> words) {
        this();
        buildDictionary(words);
    }
  /**
   * getter method for binary tree, to test
   *
   */

  public BinarySearchTree<String> getDictionary() {
    return dictionary;
  }

  /**
     * Constructor--creates dictionary from a file.
     * <p>
     * //   * @param dictionary_file
     * - the File that contains Strings used to build the dictionary
     */
    public SpellChecker(File dictionaryFile) {
        this();
        buildDictionary(readFromFile(dictionaryFile));
    }

    /**
     * Add a word to the dictionary.
     *
     * @param word - the String to be added to the dictionary
     */
    public void addToDictionary(String word) {
        this.dictionary.add(word);
    }

    /**
     * Remove a word from the dictionary.
     *
     * @param word - the String to be removed from the dictionary
     */
    public void removeFromDictionary(String word) {
        this.dictionary.remove(word);
    }

    /**
     * Spell-checks a document against the dictionary.
     * <p>
     * //   * @param document_file
     * - the File that contains Strings to be looked up in the dictionary
     *
     * @return a List of misspelled words
     */
    public List<String> spellCheck(File documentFile) {

        List<String> wordsToCheck = readFromFile(documentFile);
        List<String> misSpelledWords = new ArrayList<String>();

        for (String s : wordsToCheck) {
            if (!this.dictionary.contains(s)) {
                misSpelledWords.add(s);
            }
        }
        return misSpelledWords;
    }

    /**
     * Fills in the dictionary with the input list of words.
     *
     * @param words - the List of Strings to be added to the dictionary
     */

    private void buildDictionary(List<String> words) {
        this.dictionary.addAll(words);
    }

    /**
     * Returns a list of the words contained in the specified file. (Note that
     * symbols, digits, and spaces are ignored.)
     *
     * @param file - the File to be read
     * @return a List of the Strings in the input file
     */
    private List<String> readFromFile(File file) {
        ArrayList<String> words = new ArrayList<String>();

        try (Scanner fileInput = new Scanner(file)) {
            /*
             * Java's Scanner class is a simple lexer for Strings and primitive types (see
             * the Java API, if you are unfamiliar).
             */

            /*
             * The scanner can be directed how to delimit (or divide) the input. By default,
             * it uses whitespace as the delimiter. The following statement specifies
             * anything other than alphabetic characters as a delimiter (so that punctuation
             * and such will be ignored). The string argument is a regular expression that
             * specifies "anything but an alphabetic character". You need not understand any
             * of this for the assignment.
             */
            fileInput.useDelimiter("\\s*[^a-zA-Z]\\s*");

            while (fileInput.hasNext()) {
                String s = fileInput.next();
                if (!s.equals("")) {
                    words.add(s.toLowerCase());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File " + file + " cannot be found.");
        }

        //System.out.println("Document is " + words);

        return words;
    }
}
