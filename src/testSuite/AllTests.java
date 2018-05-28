package testSuite;

import main.Mfun;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all tests for {@link Mfun}
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
@RunWith(Suite.class)
@SuiteClasses({
        InterpreterTest.class,
        GeneratorTest.class,
})
public class AllTests {

}
