import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	 class CheckButtonListener implements ActionListener {
		 @Override
		 public void actionPerformed(ActionEvent e) {
			 if (e.getSource().equals(check)) {
				 buildAndSolveSudoku();
			 }
		 }
	 }

	 class InputChangeListener implements DocumentListener {
		 @Override
		 public void insertUpdate(DocumentEvent e) {
			 if (autoCheck.isSelected())
				 buildAndSolveSudoku();

		 }

		 @Override
		 public void removeUpdate(DocumentEvent e) {
			 if (autoCheck.isSelected())
				 buildAndSolveSudoku();
		 }

		 @Override
		 public void changedUpdate(DocumentEvent e) {

		 }
	 }
	 private JButton check;
	private JCheckBox autoCheck;
	private JTextArea left;
	private JTextArea right;

	public SudokuFrame() {
		super("Sudoku Solver");
		// Could do this:
		// setLocationByPlatform(true);
		setLayout(new BorderLayout(4, 4));
		addComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	 private void addComponents() {
		JPanel bottom = new JPanel();
		check = new JButton("Check");
		check.addActionListener(new CheckButtonListener());
		bottom.add(check);
		autoCheck = new JCheckBox("Auto check", true);
		bottom.add(autoCheck);
		add(bottom, BorderLayout.SOUTH);

		left = new JTextArea(15, 20);
		left.getDocument().addDocumentListener(new InputChangeListener());

		left.setLineWrap(true);
		left.setBorder(new TitledBorder("Puzzle"));
		add(left, BorderLayout.CENTER);

		right = new JTextArea(15, 20);
		right.setBorder(new TitledBorder("Solution"));
		add(right, BorderLayout.EAST);
	 }

	 private void buildAndSolveSudoku() {
		 String text = left.getText();
		 right.setText("");
		 try {
			 Sudoku s = new Sudoku(text);
			 int solutions = s.solve();
			 String soln = s.getSolutionText();
			 long elapsed = s.getElapsed();
			 right.append(soln + "\n");
			 right.append("solutions: " + solutions + "\n");
			 right.append("elapsed: " + elapsed + "\n");
		 } catch (Exception e) {
			 right.setText("Parsing problem");
		 }

	 }

	 public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
