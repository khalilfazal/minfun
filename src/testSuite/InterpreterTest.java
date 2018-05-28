package testSuite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests how Mfun interprets scheme code
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
@RunWith(Parameterized.class)
public class InterpreterTest extends MfunTest {

    /**
     * Generates tests to be run, one test per available program file to test
     * with.
     * 
     * @return a collection of test cases
     */
    @Parameters(name = "Test file {index}: {0}")
    public static Iterable<File[]> testFiles() {
        return testFiles("interpreter/");
    }

    /**
     * Stores the expected output, taken from executing csi
     */
    private final ByteArrayOutputStream expected;

    /**
     * @param file the Mfun source file
     */
    public InterpreterTest(final File file) {
        super(file);
        this.expected = new ByteArrayOutputStream();
    }

    /**
     * Test the way the Mfun code is interpreted
     * This test case fails if Exceptions were thrown.
     */
    @Override
    @Test
    public void interpret() {
        // Interpret the code to get expected output
        System.setOut(new PrintStream(this.expected));

        try {
            final String command = "csi -p \"$(cat " + this.file.getPath() + ")\" | grep -P '\\-?[0-9]+|#t|#f'";
            final Process proc = new ProcessBuilder("bash", "-c", command).start();
            processExecOutput(proc, 0);
        } catch (final IOException e) {
            this.collector.addError(e);
        }

        System.setOut(new PrintStream(this.interpreted));
        super.interpret();

        Assert.assertEquals(this.expected.toString(), this.interpreted.toString());
    }
}
