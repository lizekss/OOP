// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private static final int UPDATE_INTERVAL = 10000;
	private static final int DEFAULT_COUNT = 1000000;
	private static final int WORKER_SLEEP = 100;
	private static final int FIELD_WIDTH = 7;

	class WorkerThread extends Thread {
		public void run() {
			int end = Integer.parseInt(field.getText());
			for (int i = 0; i <= end; i++) {
				if (isInterrupted())
					break;

				if (i % UPDATE_INTERVAL == 0) {
					try {
						sleep(WORKER_SLEEP);
					} catch (InterruptedException e) {
						break;
					}
					String toDisplay = "" + i;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							label.setText(toDisplay);
						} });
				}
			}
		}
	}

	class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(start)) {
				if (worker.isAlive()) {
					worker.interrupt();
				}
				worker = new WorkerThread();
				worker.start();
			} else if (e.getSource().equals(stop)) {
				if (worker.isAlive())
					worker.interrupt();
			}
		}
	}

	private JTextField field;
	private JLabel label;
	private JButton start, stop;
	private WorkerThread worker;

	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Listener l = new Listener();
		field = new JTextField(FIELD_WIDTH);
		field.setText("" + DEFAULT_COUNT);
		add(field);
		label = new JLabel("0");
		add(label);
		start = new JButton("Start");
		start.addActionListener(l);
		add(start);
		stop = new JButton("Stop");
		stop.addActionListener(l);
		add(stop);

		worker = new WorkerThread();
	}


	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			} });
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		for (int i = 0; i < 4; i++) {
			frame.add(new JCount());
			frame.add(Box.createRigidArea(new Dimension(0,40)));
		}
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

