package generator;

/**
 * Used to request a system service through a {@link Operation#SYSCALL}
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum SystemCall {

    /**
     * Prints an integer
     */
    PRINT_INT(1),

    /**
     * Prints a string
     */
    PRINT_STRING(4),

    /**
     * Exits the program
     */
    EXIT(10);

    /**
     * The system's call code
     */
    private final Integer code;

    /**
     * Sets the system service's code
     * 
     * @param code the service's code
     */
    private SystemCall(final int code) {
        this.code = code;
    }

    /**
     * @return the service's code 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.code.toString();
    }
}
