import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JBrainTetris extends JTetris {
    private JCheckBox brainMode;
    private JSlider adversary;
    private JLabel status;
    private Brain brain;
    private Brain.Move bestMove;
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        bestMove = null;
    }

    @Override
    public Piece pickNextPiece() {
        double random = Math.random() * 100;
        if (random >= adversary.getValue()) {
            status.setText("ok");
            return super.pickNextPiece();
        } else {
            status.setText("*ok*");
            return pickWorstPiece();
        }
    }

    private Piece pickWorstPiece() {
        double worstScore = 0;
        Piece result = null;
        for (Piece p : pieces) {
            board.undo();
            Brain.Move bestMoveForPiece = brain.bestMove(board, p, board.getHeight() - TOP_SPACE, null);
            if (bestMoveForPiece.score >= worstScore) {
                result = p;
                worstScore = bestMoveForPiece.score;
            }
        }
        return result;
    }

    @Override
    public void addNewPiece() {
        bestMove = null;
        super.addNewPiece();
        if (brainMode.isSelected()) {
            board.undo();
            bestMove = brain.bestMove(board, currentPiece, board.getHeight() - TOP_SPACE, bestMove);
        }
    }

    @Override
    public void tick(int verb) {
        if (verb == DOWN && brainMode.isSelected())
            brainAdjustTick();

        super.tick(verb);

    }

    private void brainAdjustTick() {
        if (bestMove != null) {
            if (!currentPiece.equals(bestMove.piece)) {
                currentPiece = currentPiece.fastRotation();
            } else if (currentX < bestMove.x) {
                currentX++;
            } else if (currentX > bestMove.x) {
                currentX--;
            }
        }
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active"); panel.add(brainMode);
        panel.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0); // min, max, current
        adversary.setPreferredSize(new Dimension(100,15)); panel.add(adversary);
        status = new JLabel("ok");
        panel.add(status);
        return panel;
    }


    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }
}
