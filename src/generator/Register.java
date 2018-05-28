package generator;

/**
 * A register to store output values from expressions
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum Register {

    /**
     * Register for arguments 0
     */
    A0(false),

    /**
     * Return Address
     */
    RA(false),

    /**
     * The stack pointer
     */
    SP(false),

    /**
     * Temporary register 0
     */
    T0(true),

    /**
     * Temporary register 1 
     */
    T1(true),

    /**
     * Temporary register 2
     */
    T2(true),

    /**
     * Result register 0
     */
    V0(false),

    /**
     * Constantly hold a value of zero
     */
    ZERO(false);

    /**
     * Appears before a register's label in assembly code
     */
    private static final String handle = "$";

    /**
     * Left bracket
     */
    private static final String LEFT_PARANTHESIS = "(";

    /**
     * Right bracket
     */
    private static final String RIGHT_PARANTHESIS = ")";

    /**
     * Get the next free register
     * 
     * @return the next free register
     */
    public static Register getFree() {
        for (final Register r : Register.values()) {
            if (r.temporary && r.free) {
                r.free = false;
                return r;
            }
        }

        return null;
    }

    /**
     * Whether the register can be used to hold temporary values
     */
    private final boolean temporary;

    /**
     * True if the register can be overwritten
     */
    private boolean free;

    /**
     * @param temporary true if the register is temporary
     */
    private Register(final boolean temporary) {
        this.temporary = temporary;
        this.free = true;
    }

    /**
     * Sets the register as free
     */
    public void free() {
        this.free = true;
    }

    /**
     * Blocks a register
     */
    public void unfree() {
        this.free = false;
    }

    /**    
     * Use the register by freeing it and returning it's string
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return handle + this.name().toLowerCase();
    }

    /**
     * @return the value stored in the register
     */
    public String getValue() {
        return LEFT_PARANTHESIS + this.toString() + RIGHT_PARANTHESIS;
    }
}
