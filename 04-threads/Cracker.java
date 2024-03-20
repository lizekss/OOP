// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import sun.plugin2.message.Message;

import java.security.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();

	class Worker extends Thread {
		private int start, end;
		public Worker(int a, int b) {
			start = a;
			end = b;
		}
		public void run() {
			search();
			latch.countDown();
		}

		private void search() {
			for (int i = start; i < end; i++) {
				recSearch("" + CHARS[i]);
			}
		}

		private void recSearch(String cur) {
			if (cur.length() > maxLen)
				return;

			if (Arrays.equals(hashOf(cur), target))
				matches.add(cur);

			for (char c : CHARS) {
				recSearch(cur + c);
			}
		}

	}

	private byte[] target;
	private HashSet<String> matches;
	private CountDownLatch latch;
	private int maxLen;
	private int numWorkers;

	public Cracker(String targ, int len, int num) {
		target = hexToArray(targ);
		matches = new HashSet<>();
		latch = new CountDownLatch(num);
		numWorkers = num;
		maxLen = len;
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) System.out.println("Args: target [length] [workers]");

		String targ = args[0];

		if (args.length > 1) {
			int len = Integer.parseInt(args[1]);
			int num = 1;
			if (args.length > 2) {
				num = Integer.parseInt(args[2]);
			}
			// a! 34800e15707fae815d7c90d49de44aca97e2d759
			// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
			Cracker c = new Cracker(targ, len, num);
			c.crackPassword();
		} else {
			System.out.println(hexToString(Cracker.hashOf(targ)));
		}
	}

	public void crackPassword() {
		startWorkers();

		try {
			latch.await();
		} catch (InterruptedException e) {e.printStackTrace();}

		for (String match : matches)
			System.out.println(match);

		System.out.println("all done");
	}

	private void startWorkers() {
		int range = CHARS.length / numWorkers;
		int idx = 0;
		for (int i = 0; i < numWorkers; i++) {
			int endIdx = idx + range;
			if (i == numWorkers - 1 && endIdx < CHARS.length)
				endIdx = CHARS.length;
			Worker w = new Worker(idx, endIdx);
			w.start();
			idx = endIdx;
		}
	}

	public static byte[] hashOf(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(s.getBytes());
			return md.digest(); }
		catch (NoSuchAlgorithmException e) { e.printStackTrace();}
		return null;
	}
}
