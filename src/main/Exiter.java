package main;

import java.io.PrintStream;

/**
 * A static class which handles the compiler's termination
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum Exiter {
    ;
    /** 
     * Handles the compiler's termination.
     * Eventually terminates the program
     * 
     * @param code if zero, print to stdio. else print to stderr.
     * @param message the message to show on the output stream
     */
    public static void exit(final int code, final String message) {
        try (final PrintStream stream = code == 0 ? System.out : System.err) {
            stream.println(String.format("%d: %s", code, message));
            System.exit(code);
        }
    }
}
