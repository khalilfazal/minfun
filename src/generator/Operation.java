package generator;

/**
 * An assembly operation
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum Operation {

    /**
     * Add
     */
    ADD("Add value in register %s"),

    /**
     * Add immediate value (with overflow)
     */
    ADDI("Add immediate value: %s"),

    /**
     * Branch if equal
     */
    BEQ("Branch to %s if %s is equal to %s"),

    /**
     * Branch if not equal to zero
     */
    BNEZ("Branch to %s if %s is not equal to zero"),

    /**
     * Jump
     */
    J("Jump to label: %s"),

    /**
     * Jump to label and link
     */
    JAL("Jump to label: %s"),

    /**
     * Jump to register
     */
    JR("Jump to address in register %s"),

    /**
     * Load address
     */
    LA("Load string: %s"),

    /**
     * Load immediate 
     */
    LI("Load immediate value: %s"),

    /**
     * Load Word 
     */
    LW("Load word from %s"),

    /**
     * Move values stored between registers
     */
    MOVE("Move value from %s to %s"),

    /**
     * Subtract (with overflow)
     */
    SUB("Subtract %s from the stack pointer's address"),

    /**
     * Store word
     */
    SW("Store value to %s"),

    /**
     * System call
     */
    SYSCALL("System call");

    /**
     * Format of the comment for this operation
     */
    private String format;

    /**
     * @param format the operation's comment format
     */
    private Operation(final String format) {
        this.format = format;
    }

    /**
     * @return the format for the operation's format
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
