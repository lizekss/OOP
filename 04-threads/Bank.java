// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	public static final int INITIAL_BALANCE = 1000;

	class Worker extends Thread {
		public void run() {
			try {
				while(true) {
					Transaction next = queue.take();
					if (next.equals(nullTrans)) {
						break;
					}
					processTrans(next);
				}
			} catch (InterruptedException e) { e.printStackTrace();
			}
			latch.countDown();
		}

		private void processTrans(Transaction t) {
			if (t.from < t.to) {
				synchronized(accounts[t.from]) {
					synchronized(accounts[t.to]) {
						accounts[t.from].withdraw(t.amount);
						accounts[t.to].deposit(t.amount);
					}
				}
			} else {
				synchronized(accounts[t.to]) {
					synchronized(accounts[t.from]) {
						accounts[t.from].withdraw(t.amount);
						accounts[t.to].deposit(t.amount);
					}
				}
			}
		}
	}

	private ArrayBlockingQueue<Transaction> queue;
	private Account[] accounts;
	private final Transaction nullTrans;
	private CountDownLatch latch;

	public Bank() {
		queue = new ArrayBlockingQueue<Transaction>(10);
		accounts = new Account[ACCOUNTS];
		for (int i = 0; i < ACCOUNTS; i++) {
			accounts[i] = new Account(this, i, INITIAL_BALANCE);
		}
		nullTrans = new Transaction(-1,0,0);
	}
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				Transaction t = new Transaction(from, to, amount);
				queue.put(t);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) {
		latch = new CountDownLatch(numWorkers);
		for (int i = 0; i < numWorkers; i++) {
			Worker w = new Worker();
			w.start();
		}
		readFile(file);
		try {
			for (int i = 0; i < numWorkers; i++) {
				queue.put(nullTrans);
			}
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printAccountBalances();
	}

	private void printAccountBalances() {
		for (int i = 0; i < accounts.length; i++) {
			System.out.println("acct:" + i + " bal:" + accounts[i].getBalance() + " trans:" + accounts[i].getTransactions());
		}
	}


	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		Bank bank = new Bank();
		bank.processFile(file, numWorkers);
	}
}

