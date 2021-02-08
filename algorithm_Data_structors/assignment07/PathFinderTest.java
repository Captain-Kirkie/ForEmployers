package assignment07;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    public static int findPathLength(String inputFile) throws FileNotFoundException {
        int dotCount = 0;
        File file = new File(inputFile);
        char[] chars;
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            chars = scanner.nextLine().toCharArray();
            for(char c : chars){
                if(c == '.');
                dotCount++;
            }
        }
        return dotCount;
    }

    @Test
    void countDots() throws FileNotFoundException {
        System.out.println("Kirk's Solution " + findPathLength("randomPathKirk.txt"));
        System.out.println("Varun's solution " + findPathLength("src/assignment07/mazes/randomMazeSol.txt"));
        assertEquals(findPathLength("randomPathKirk.txt"),findPathLength("src/assignment07/mazes/randomMazeSol.txt"));
        System.out.println("Kirk's Solution " +  findPathLength("demoMazeKirk.txt"));
        System.out.println("Varun's Solution " +  findPathLength("src/assignment07/mazes/demoMazeSol.txt"));
        assertEquals(findPathLength("demoMazeKirk.txt"), findPathLength("src/assignment07/mazes/demoMazeSol.txt"));
    }

    @Test
    void turnTest() throws IOException {
        PathFinder.solveMaze("src/assignment07/mazes/turn.txt", "turn.txt");
    }

    @Test
    void cannotFindFile() throws IOException {
       // PathFinder.solveMaze("src/assignment07/mazes/trn.txt", "break.txt");
    }


}