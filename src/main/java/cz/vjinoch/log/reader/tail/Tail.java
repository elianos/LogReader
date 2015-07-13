package cz.vjinoch.log.reader.tail;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * TODO: comment
 *
 * @author usul
 */
public class Tail {

    private final File logFile;

    public Tail(String logFileUrl) {
        this.logFile = new File(logFileUrl);
    }

    public void mainLoop() throws IOException, InterruptedException {
        RandomAccessFile fileHandler = new RandomAccessFile(logFile, "r");
        long filePointer = 0;
        long fileLength = 0;
        int i = 0;

        while (i < 10) {
            fileLength = fileHandler.length();
            if (filePointer < fileLength) {
                String line = fileHandler.readLine();
                if (line.length() != 0) {
                    writeLine(line);
                }
                filePointer = fileHandler.getFilePointer();
                while (filePointer < fileLength) {
                    writeLine(fileHandler.readLine());
                    filePointer = fileHandler.getFilePointer();
                }
            }
            synchronized (this) {
                this.wait(1000);
            }
        }


    }

    private void writeLine(String line) {
        System.out.println(line);
    }

}
