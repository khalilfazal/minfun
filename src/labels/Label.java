package labels;

import generator.Generator;
import generator.Operation;
import generator.Register;
import generator.SystemCall;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

/**
 * A label contains statements of assembly code
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Label {

    /**
     * Separates operation keywords from the operands
     */
    public static final String SEPARATOR = "," + Generator.SPACE;

    /**
     * A hash character for comments
     */
    private static final String HASH = "#";

    /**
     * Generate a comment for a statement
     * 
     * @param operation the operation
     * @param operands the arguments for the operation
     * @return the generated comment
     */
    private static String generateComment(final Operation operation, final Object[] operands) {
        final String format = operation.getFormat();

        switch (operation) {
            case ADD:
            case ADDI:
            case SUB:
                return String.format(format, operands[2]);
            case BEQ:
                return String.format(format, operands[2], operands[0], operands[1]);
            case LA:
            case LI:
                return String.format(format, operands[1]);
            case J:
            case JAL:
            case JR:
                return String.format(format, operands[0]);
            case LW:
            case SW:
                String wordLocation;

                if (operands[1].equals("($sp)")) {
                    wordLocation = "stack";
                } else {
                    wordLocation = operands[1].toString();
                }

                return String.format(format, wordLocation);
            case BNEZ:
            case MOVE:
                return String.format(format, operands[1], operands[0]);
            case SYSCALL:
                return format;
            default:

                // Unreachable
                return null;
        }
    }

    /**
     * the name of the label
     */
    private final String label;

    /**
     * The label's id
     */
    private final Integer id;

    /**
     * The list of operations contained in the label
     */
    private final List<Pair<String, String>> operations;

    /**
     * Constructs a named label with a null id
     * 
     * @param name the label's name
     */
    public Label(final String name) {
        this(name, null);
    }

    /**
     * Constructs a named label
     * 
     * @param name the label's name
     * @param id the label's id
     */
    public Label(final String name, final Integer id) {
        this.label = name;
        this.id = id;
        this.operations = new ArrayList<Pair<String, String>>();
    }

    /**
     * @return the max length of a statement line without comments
     */
    private int getMaxLength() {
        int output = 0;

        for (final Pair<String, String> line : this.operations) {
            final int lineLength = line.getValue0().length() + Generator.TABLENGTH;

            if (lineLength > output) {
                output = lineLength;
            }
        }

        return output;
    }

    /**
     * Exits the program through a syscall
     */
    public void gen_exit() {
        this.addStatement(Operation.LI, Register.V0, SystemCall.EXIT);
        this.addStatement(Operation.SYSCALL);
    }

    /**
     * Implements the pop operation
     * 
     * http://www.cs.uiuc.edu/class/fa06/cs232/lectures/L4.ppt
     * 
     * @param r where to move the popped value to
     */
    public void gen_pop(final Register r) {
        this.addStatement(Operation.LW, r, Register.SP.getValue());
        this.addStatement(Operation.ADDI, Register.SP, Register.SP, 4);
    }

    /**
     * Implements the push operation
     * 
     * http://www.cs.uiuc.edu/class/fa06/cs232/lectures/L4.ppt
     * 
     * @param r where to get the value to push from
     */
    public void gen_push(final Register r) {
        this.addStatement(Operation.SUB, Register.SP, Register.SP, 4);
        this.addStatement(Operation.SW, r, Register.SP.getValue());
    }

    /**
     * Replace the parent's label in the stack with the contents of a register.
     * Free the registers after use.
     * 
     * @param parent stores the address of the parent label
     * @param result stores a value to store in the stack
     */
    public void swapAndJump(final Register parent, final Register result) {
        this.gen_pop(parent);
        this.gen_push(result);
        this.addStatement(Operation.JR, parent);
        parent.free();
        result.free();
    }

    /** 
     * Adds a statement under a label with a comment
     * 
     * @param operation the left side of a statement
     * @param operands the right side of a statement
     */
    public void addStatement(final Operation operation, final Object... operands) {
        final StringBuilder statement = new StringBuilder(operation.toString());

        if (operands.length > 0) {
            statement.append(Generator.SPACE + operands[0].toString());

            for (int i = 1; i < operands.length; i++) {
                statement.append(SEPARATOR + operands[i].toString());
            }
        }

        this.operations.add(Pair.with(statement.toString(), generateComment(operation, operands)));
    }

    /**
     * Get the label
     * 
     * @return the label
     */
    public String getName() {
        return this.label + (this.id != null ? this.id.toString() : "");
    }

    /**
     * Used only when sorting labels
     * 
     * @return this label's id
     */
    public Integer getID() {
        if (this.id == null) {
            return Integer.MIN_VALUE;
        }

        return this.id;
    }

    /**
     * Jump from the callee(this) to the called label
     * 
     * @param called the called label
     */
    public void call(final Label called) {
        this.addStatement(Operation.JAL, called.getName());
    }

    /**
     * Save the address of the parent label to the stack
     */
    public void saveParent() {
        this.gen_push(Register.RA);
    }

    /**
     * Represents the entirety of the label as a string as valid assembly code
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder(this.getName() + ":" + Generator.NL);
        final int maxLength = this.getMaxLength();

        for (final Pair<String, String> line : this.operations) {
            final String comment = line.getValue1();
            final int beforeLength = output.length();

            output.append(Generator.TAB + line.getValue0());

            if (comment != null) {
                final int length = output.length() - beforeLength;

                output.append(Generator.createLine(Generator.SPACE, maxLength - length));
                output.append(Generator.TAB + HASH + Generator.SPACE + comment);
            }

            output.append(Generator.NL);
        }

        return output.toString();
    }
}