package exceptions;

import main.Exiter;

/**
 * Thrown when there are any problems with functions and variables not existing in scope
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public abstract class ReferenceException extends Exception {

    /**
     * A generated serial id
     */
    private static final long serialVersionUID = -5679846488675965829L;

    /**
     * What exit code to exit with when encountered
     */
    private final int exitCode;

    /**
     * @param reference the reference that can't be found in either the function or variable table
     * @param exitCode what exit code to exit with when encountered
     */
    public ReferenceException(final String reference, final int exitCode) {
        super(reference);
        this.exitCode = exitCode;
    }

    /**
     * Exit the program
     */
    public void exit() {
        Exiter.exit(this.exitCode, this.getMessage());
    }
}
