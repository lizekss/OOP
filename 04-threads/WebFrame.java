import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class WebFrame extends JFrame {
    private static final int TABLE_WIDTH = 600;
    private static final int TABLE_HEIGHT = 300;
    private static final String FILE_PATH = "links.txt";

    private DefaultTableModel model;
    private JTable table;
    private JPanel panel;
    private JButton single, multi, stop;
    private JTextField field;
    private JLabel running, comp, elapsed;
    private JProgressBar bar;

    private Semaphore workersLimit;
    private CountDownLatch workersDone;
    private AtomicInteger runningThreads = new AtomicInteger();
    private AtomicInteger completedThreads = new AtomicInteger();

    private Launcher launcher = new Launcher();
    private WebWorker[] workers = new WebWorker[0];

    public WebFrame() {
        super("WebLoader");
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        model = new DefaultTableModel(new String[] { "url", "status"}, 0);
        loadLinksFromFile();
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scrollpane);
        add(panel);
        ActionListener l = new Listener();
        single = new JButton("Single Thread Fetch");
        single.addActionListener(l);
        multi = new JButton("Concurrent Fetch");
        multi.addActionListener(l);
        field = new JTextField(6);
        field.setText("4");
        field.setMaximumSize(new Dimension(field.getPreferredSize()));
        panel.add(single);
        panel.add(multi);
        panel.add(field);
        running = new JLabel("Running:0");
        comp = new JLabel("Completed:0");
        elapsed = new JLabel("Elapsed:0ms");
        panel.add(running);
        panel.add(comp);
        panel.add(elapsed);
        bar = new JProgressBar();
        bar.setMaximum(model.getRowCount());
        panel.add(bar);
        stop = new JButton("Stop");
        stop.setEnabled(false);
        stop.addActionListener(l);
        panel.add(stop);
        add(panel);
    }

    private void loadLinksFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            while (true) {
                String s = br.readLine();
                if (s == null)
                    break;
                model.addRow(new String[]{s});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void workerStartedUpdateGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                running.setText("Running: " + runningThreads.incrementAndGet());
            } });
    }

    public void workerFinishedUpdateGUI(int row, String status) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model.setValueAt(status, row, 1);
                workersLimit.release();
                running.setText("Running: " + runningThreads.decrementAndGet());
                comp.setText("Completed: " + completedThreads.incrementAndGet());
                bar.setValue(completedThreads.get() + 1);
                workersDone.countDown();
            } });
    }



    class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().contains("Fetch")) {
                enterRunningState();
                if (e.getSource().equals(multi)) {
                    workersLimit = new Semaphore(Integer.parseInt(field.getText()));
                } else if (e.getSource().equals(single)) {
                    workersLimit = new Semaphore(1);
                }
                launcher = new Launcher();
                launcher.start();
            } else if (e.getSource().equals(stop)) {
                if (launcher.isAlive()) {
                    launcher.interrupt();
                }
                exitRunningState();
            }
        }
    }

    private void enterRunningState() {
        stop.setEnabled(true);
        single.setEnabled(false);
        multi.setEnabled(false);
        for (int i = 0; i < model.getRowCount(); i++)
            model.setValueAt("", i, 1);
        bar.setValue(0);
        completedThreads = new AtomicInteger();
        runningThreads = new AtomicInteger();
        bar.setMaximum(model.getRowCount());
        comp.setText("Completed: 0");
        elapsed.setText("Elapsed: 0ms");
    }

    private void exitRunningState() {
        stop.setEnabled(false);
        single.setEnabled(true);
        multi.setEnabled(true);
    }

    class Launcher extends Thread {
        public void run() {

            int numWorkers = model.getRowCount();
            workers = new WebWorker[numWorkers];
            workersDone = new CountDownLatch(numWorkers);
            long startTime = System.currentTimeMillis();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    running.setText("Running: " + runningThreads.incrementAndGet());
                } });

            for (int i = 0; i < numWorkers; i++) {
                String link = (String) model.getValueAt(i, 0);
                try {
                    workersLimit.acquire();
                    workers[i] = new WebWorker(i, link, WebFrame.this);
                    workers[i].start();
                } catch (InterruptedException e) {
                    launcherFinishedUpdateGUI();
                    return;
                }
            }

            try {
                workersDone.await();
            } catch (InterruptedException e) {
                launcherFinishedUpdateGUI();
                return;
                //for (WebWorker w : workers)
                //   if (w.isAlive()) w.interrupt();
            }
            long endTime = System.currentTimeMillis();
            elapsed.setText("Elapsed: " + (endTime - startTime) + "ms");
            launcherFinishedUpdateGUI();
        }
    }

    private void launcherFinishedUpdateGUI() {
        for (WebWorker w : workers)
            if (w != null && w.isAlive()) w.interrupt();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                running.setText("Running: " + runningThreads.decrementAndGet());
                exitRunningState();
            } });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WebFrame frame = new WebFrame();
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } });
    }
}
