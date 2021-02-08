package lab00;

public class CoinFlipExperiment {
	
	public static void main(String[] args) {
		int[] counts = new int[201];
		
		for(int i = 0; i < 100000; i++) {
			int amount = CoinFlipExperiment.coinFlipExperiment(100);
			counts[amount + 100] = counts[amount + 100] + 1;//this line adds amount to corresponding array position
			System.out.println("Win/Loss amount: " + amount);
		}
		  System.out.println ("Likelihood of win/loss amount after 100 flips:");
		  System.out.print ("Amount");
		  System.out.print ("\t"); // A tab character
		  System.out.print ("Probability");
		  System.out.println ();

		  // Loop through amounts
		  double attempts = 100000;
		  for (int i = 0; i <= 200; i++)
		  {
		    System.out.print (i - 100);
		    System.out.print ("\t");
		    System.out.print (counts[i] / attempts);
		    System.out.println ();
		  }
	}
	
	static public int coinFlipExperiment(int numFlips) {
		int winnings = 0;
		for(int i = 0; i < numFlips; i++) {
			double flip = Math.random();
			if(flip < 0.505) {
				winnings++;
			}else {
				winnings--;
			}
		}
		return winnings;
	}
}
