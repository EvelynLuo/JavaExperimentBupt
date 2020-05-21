/*
 * GameTable.java:toShowGUI
 */
package project8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class GameTable extends Application {
	public BorderPane pane = new BorderPane();
	public GridPane grid = new GridPane();
	private static Lock lock = new ReentrantLock();
	private static Condition c1 = lock.newCondition();
	private static Condition c2 = lock.newCondition();
	private static Condition c3 = lock.newCondition();
	private int status = 1;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// top center bottom
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(0, 10, 0, 10));
		// Round in column 1-2, row 1
		Text round = new Text("  Round\t");
		round.setFont(Font.font("Arial", FontWeight.BLACK, 15));
		grid.add(round, 0, 1);
		// ThreadA in column 2-4, row 1
		Text ta = new Text("\t\tThread A");
		ta.setFont(Font.font("Arial", FontWeight.BLACK, 15));
		grid.add(ta, 1, 0, 3, 1);
		// ThreadB in column 5-7, row 1
		Text tb = new Text("\t\tThread B");
		tb.setFont(Font.font("Arial", FontWeight.BLACK, 15));
		grid.add(tb, 4, 0, 3, 1);
		// item in column 2, row 2
		Text st = new Text("Sleep\nTime");
		grid.add(st, 1, 1);
		Text rc = new Text("Random\ncharacter");
		grid.add(rc, 2, 1);
		Text po = new Text("Points\nobtained");
		grid.add(po, 3, 1);
		grid.add(new Text("Sleep\nTime"), 4, 1);
		grid.add(new Text("Random\ncharacter"), 5, 1);
		grid.add(new Text("Points\nobtained"), 6, 1);
		for (int i = 1; i < 6; i++) {
			grid.add(new Text("\t" + String.valueOf(i) + "\t"), 0, i + 1);
		}

		// thread threads concurrent
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(new ThreadA());
		executor.execute(new ThreadB());
		executor.execute(new ThreadC());
		executor.shutdown();
		// Console UI
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("|         |            Thread A          |             ThreadB           |");
		System.out.println("|  Round  |---------------------------------------------------------------");
		System.out.println("|  (1-5)  |  Sleep |  Random  |  Points  |  Sleep  |  Random  |  Points  |");
		System.out.println("|         |  Time  | character| obtained |  Time   | character| obtained |");
		System.out.println("--------------------------------------------------------------------------");
		// Create a scene and place the pane
		pane.setCenter(grid);
		Scene scene = new Scene(pane, 500, 260);
		primaryStage.setTitle("GameTable");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private Account account = new Account();

	public class ThreadA implements Runnable {
		@Override
		public synchronized void run() {

			for (int i = 0; i < 5; i++) {

				lock.lock();
				try {
					// 等待thread c
					if (status != 1) {
						c1.await();
					}
					// 执行线程a
					loopa(i);
					// invoke 2
					status = 2;
					c2.signal();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} finally {
					lock.unlock();
				}

			}
		}

		public void loopa(int N) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					for (int i = 1; i <= 1; i++) {

						Platform.runLater(() -> {
							// random char column 3
							grid.add(new Text("\t" + account.randomAchar((int) (Math.random() * 25) + 1, N)), 2, 2 + N);
							// sleep column 2 row 3-4-5-..
							grid.add(new Text(String.valueOf(account.getSleeptimeA())), 1, 2 + N);
						});

					}
				}
			});
		}
	}

	public class ThreadB implements Runnable {
		@Override
		public synchronized void run() {
			for (int i = 0; i < 5; i++) {
				lock.lock();
				try {
					// 等待 A
					if (status != 2)
						c2.await();
					// 执行b
					loopb(i);
					// invoke c
					status = 3;
					c3.signal();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}

		public void loopb(int N) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					for (int i = 1; i <= 1; i++) {
						// test
						Platform.runLater(() -> {
							grid.add(new Text("\t" + account.randomBchar((int) (Math.random() * 25) + 1, N)), 5, 2 + N);
							// sleep column 5 row 3-4-5-..
							grid.add(new Text(String.valueOf(account.getSleeptimeB())), 4, 2 + N);
						});
					}
				}
			});
		}
	}

	public class ThreadC extends Thread {
		@Override
		public synchronized void run() {
			for (int i = 0; i < 5; i++) {
				lock.lock();
				try {
					// 等待 B
					if (status != 3)
						c3.await();
					// 执行C
					loopc(i);
					// invoke c
					status = 1;
					c1.signal();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
		public void loopc(int N) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					for (int i = 1; i <= 1; i++) {
						Platform.runLater(() -> {
							// score a 4 row 3-4-5-..
							grid.add(new Text("\t" + String.valueOf(account.getPointsA())), 3, 2 + N);
							// score b column 7
							grid.add(new Text("\t" + String.valueOf(account.getPointsB())), 6, 2 + N);
						});

					}
				}
			});
		}
	}
}