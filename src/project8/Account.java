//Account.java
package project8;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JOptionPane;

import javafx.application.Platform;


public class Account {
	private int balance =0;
	private int scoreA = 0;
	private int scoreB = 0;
	private int sleeptimeA =1;
	private int sleeptimeB =1;
	private int pointsA = 0;
	private int pointsB = 0;
	private static Lock lock = new ReentrantLock();
	public int getBalance() {
		return balance;
	}
	public  String randomAchar(int amount,int i) {
		lock.lock();
		setBalance(0);
		balance = amount;
		try {
			sleeptimeA =amount*amount%1000;
			Thread.sleep(sleeptimeA);
			System.out.print("| Round"+(i+1)
					+ "  |   "
					+ String.format("%3d", sleeptimeA));
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		//amount是随机生成的char，getBalance是两者之差
		//invoke randomBchar
		lock.unlock();
		System.out.print("  |     "
				+ (char) ('a' + amount)
				+ "    |    ");
		return String.valueOf((char) ('a' + amount));
	}
	public  String randomBchar(int amount,int i) {
		lock.lock();
		try {
			sleeptimeB = amount*amount%1000;
			Thread.sleep(sleeptimeB);
			balance = balance - amount;
			//System.out.println((char) ('a' + amount)+"\t");
			 pointsA =scoreA;
			 pointsB =scoreB;
			if(balance>0) {
				scoreA += 2;
			}
			else if(balance==0) {
				scoreA += 1;
				scoreB += 1;
			}
			else {
				scoreB += 2;
			}
			getAScore();
			getScoreB();
			pointsA =scoreA-pointsA;
			pointsB =scoreB-pointsB;
			System.out.print(
					 String.format("%2d", pointsA)
					+ "    |   "
					+ String.format("%3d", sleeptimeB));
			System.out.print("   |     "
					+ (char) ('a' + amount)
					+ "    |    ");
			System.out.print(
					 String.format("%2d", pointsB)
					+ "    |");
			System.out.println();
			//cond.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}	
		lock.unlock();
		if(i==4) {
			System.out.println("--------------------------------------------------------------------------");
		    if(getAScore()>getScoreB()) {
		    	Platform.runLater(() -> { 
		    		JOptionPane.showMessageDialog(null, "A is the Winner", "Final Result", JOptionPane.INFORMATION_MESSAGE);
				});
			
		    	System.out.println("A is the Winner");}
			else if(getAScore()<getScoreB()) {
				Platform.runLater(() -> { 
		    		JOptionPane.showMessageDialog(null, "B is the Winner", "Final Result", JOptionPane.INFORMATION_MESSAGE);
				});
				
				System.out.println("B is the Winner");}
			else {
				Platform.runLater(() -> { 
		    		JOptionPane.showMessageDialog(null, "A is equal to B", "Final Result", JOptionPane.INFORMATION_MESSAGE);
				});
			
				System.out.println("A is equal to B");
			}
	 }
		return String.valueOf((char) ('a' + amount));
	}
	public int getAScore() {	
		return scoreA;
	}
	public int getScoreB() {
		return scoreB;
	}
	public int getSleeptimeA() {
		return sleeptimeA;
	}
	public int getSleeptimeB() {
		return sleeptimeB;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getPointsA() {
		return pointsA;
	}
	public int getPointsB() {
		return pointsB;
	}
}
