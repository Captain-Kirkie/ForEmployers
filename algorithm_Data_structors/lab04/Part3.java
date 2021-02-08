package lab04;

public class Part3 {

  public static boolean checkPassword(String password) {
    String[] tokens = password.split(" "); //splits password
    int a = 0, b = 1;
    for (int i = 0; i < tokens.length; i++) { //length of password
      a += b; //add 0 + 1, 1+2, 3 + 4, 7 + 6, 14 + 8 ...exception thrown parseint(tokens[0]), a
      int currentToken = Integer.parseInt(tokens[i]); //get integer value of ASCII chars, turn int
      if (a != currentToken) { //if the ints arent equal return false
        return false;
      }
      b += 2; //add 2 to b
    }
    return b == 11;
  }

}