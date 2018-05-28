package testSuite;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Interpreter;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;

import parser.ParseException;
import parser.Parser;
import parser.SimpleNode;
import exceptions.ReferenceException;

/**
 *  Tests Mfun code
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public abstract class MfunTest {

    /**
     * The location of the test directory
     */
    private static String testDir = "./testCases/";

    /**
     * Generates tests to be run, one test per available program file to test
     * with.
     * 
     * @param caseDir the directory containing test files pertaining to the test case
     * @return a collection of test cases
     */
    protected static Iterable<File[]> testFiles(final String caseDir) {
        final List<File[]> output = new ArrayList<File[]>();
        final File[] files = new File(testDir + caseDir).listFiles();
        Arrays.sort(files);

        for (final File file : files) {
            output.add(new File[] { file });
        }

        return output;
    }

    /**
     * Processes the output stream of a shell command
     * 
     * @param proc a shell process
     * @param skip number of lines to skip from the output
     * @throws IOException thrown if output line's can't be read
     */
    protected static void processExecOutput(final Process proc, final int skip) throws IOException {
        BufferedReader stdout = null;

        try {
            stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s = null;

            for (int i = 0; i < skip; i++) {
                stdout.readLine();
            }

            while ((s = stdout.readLine()) != null) {
                System.out.println(s);
            }
        } finally {
            if (stdout != null) {
                stdout.close();
            }
        }
    }

    /**
     * The file containing scheme code
     */
    protected final File file;

    /**
     * The root node
     */
    protected final SimpleNode root;

    /**
     * Capture output from the interpreter
     */
    protected final ByteArrayOutputStream interpreted;

    /**
     * Collects all errors so the test suite still runs after the first error.
     * All of the test results will be shown together at the end
     */
    @Rule
    public final ErrorCollector collector;

    /**
     * Constructs a test.
     * 
     * @param file The test input file
     */
    public MfunTest(final File file) {
        this.file = file;
        this.collector = new ErrorCollector();
        this.root = this.getRoot();
        this.interpreted = new ByteArrayOutputStream();
    }

    /**
     * Get the root node, needed for the tests.
     * @return the root of the Mfun code
     */
    public SimpleNode getRoot() {
        SimpleNode root = null;

        try {
            root = Parser.parse(this.file);
        } catch (final ParseException e) {
            this.collector.addError(e);
        }

        return root;
    }

    /**
     * Interpret Mfun code 
     */
    public void interpret() {
        try {
            new Interpreter(this.root).eval();
        } catch (final ArithmeticException | ReferenceException e) {
            this.collector.addError(e);
        }
    }
}
