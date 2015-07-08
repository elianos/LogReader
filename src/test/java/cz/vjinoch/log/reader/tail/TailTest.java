package cz.vjinoch.log.reader.tail;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by usul on 3.3.2015.
 */
public class TailTest {

    @Test
    public void handleTest() throws IOException, InterruptedException {
        Tail testFileTail = new Tail("d:\\Workspace\\LogReader\\src\\test\\resources\\testFile.txt");
        testFileTail.mainLoop();

    }
}
