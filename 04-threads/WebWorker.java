import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class WebWorker extends Thread {
    private int row;
    private String urlString;

    private WebFrame frame;
    private Date finishedTime;
    private long elapsed;
    private long bytes;
    private boolean interruptedWhileDownloading = false;

    public WebWorker(int row, String url, WebFrame frame) {
        urlString = url;
        this.row = row;
        this.frame = frame;
    }

    public void run() {
        frame.workerStartedUpdateGUI();
        long startTime = System.currentTimeMillis();
        boolean successful = download();
        long endTime = System.currentTimeMillis();
        elapsed = endTime - startTime;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        if (interruptedWhileDownloading) {
            frame.workerFinishedUpdateGUI(row, "interrupted");
            return;
        }

        finishedTime = new Date();
        String status = "err";
        if (successful) {
            status = formatter.format(finishedTime) + " " + elapsed + "ms " + bytes + " bytes";
        }

        if (isInterrupted()) {
            frame.workerFinishedUpdateGUI(row, "interrupted");
            return;
        }
        frame.workerFinishedUpdateGUI(row, status);
    }
/*
  This is the core web/download i/o code... */
    // TODO handle interruption
 	private boolean download() {
        InputStream input = null;
        StringBuilder contents = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            if (isInterrupted())  {
                interruptedWhileDownloading = true;
                return false;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                if (isInterrupted())  {
                    interruptedWhileDownloading = true;
                    return false;
                }
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            bytes = contents.length();
            return true;

        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) { return false;
        } catch (InterruptedException exception) {
            interruptedWhileDownloading = true;
            return false;
        } catch (IOException ignored) { return false;
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }

    }
}
