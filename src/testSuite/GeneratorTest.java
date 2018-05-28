package testSuite;

import generator.Generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import main.Mfun;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests How Mfun generates assembly code
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
@RunWith(Parameterized.class)
public class GeneratorTest extends MfunTest {

    /**
     * Generates tests to be run, one test per available program file to test
     * with.
     * 
     * @return a collection of test cases
     */
    @Parameters(name = "Test file {index}: {0}")
    public static Iterable<File[]> testFiles() {
        return testFiles("generator/");
    }

    /**
     * The generated output assembly file
     */
    private File output;

    /**
     * Capture output from the generator
     */
    private final ByteArrayOutputStream generated;

    /**
     * Constructs a test for the generator.
     * 
     * @param file a scheme program file
     */
    public GeneratorTest(final File file) {
        super(file);
        this.generated = new ByteArrayOutputStream();
    }

    /**
     * Test the way assembly code is generated. 
     * This test case fails if Exceptions were thrown.
     */
    @Test
    public void generate() {
        // Interpret the code to get expected output
        System.setOut(new PrintStream(this.interpreted));
        super.interpret();
        System.setOut(new PrintStream(this.generated));

        this.output = Mfun.getOutputFile(this.file);
        final Generator generator = new Generator(this.root, this.output);

        try {
            generator.gen();

            // Execute the assembly code
            final String command = "spim -file " + this.output.getPath();
            final Process proc = Runtime.getRuntime().exec(command);
            processExecOutput(proc, 5);
        } catch (final IOException e) {
            this.collector.addError(e);
        }

        Assert.assertEquals(this.interpreted.toString(), this.generated.toString());
    }
}
