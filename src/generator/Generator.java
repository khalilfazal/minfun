package generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import labels.Label;
import labels.LabelTable;
import main.Interpreter;
import parser.Node;
import parser.SimpleNode;

/**
 * Generates assembly code from a Mfun source file
 * 
 * The following table shows the instructions that you have to implement
 *
 *                                  | Minifun 
 *  ________________________________|_________
 *  Sequence of Instructions        |    x    
 *  Integer Values                  |    x    
 *  Boolean Values                  |        
 *  Variables                       |    x    
 *  Assignment Operator             |    x    
 *  Addition Operator               |    x       
 *  Subtraction Operator            |            
 *  Multiplication Operator         |            
 *  Division Operator               |            
 *  Equality Operator               |            
 *  Greater Than Operator           |            
 *  Less Than Operator              |             
 *  Greater Than Or Equal Operator  |            
 *  Less Than or Equal Operator     |            
 *  Conditional Statements          |    x           
 *  Loop Statements                 |                
 *  Function Definitions            |            
 *  Function Calls                  |            
 *  Return Statements               |            
 *  Print Statement                 |            
 *  Lists                           |            
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Generator {

    /**
     * A newline
     */
    public static final String NL = "\n";

    /**
     * A space
     */
    public static final char SPACE = ' ';

    /**
     * Number of spaces in a tab
     */
    public static final int TABLENGTH = 4;

    /**
     * A tab
     */
    public static final String TAB = createLine(SPACE, TABLENGTH);

    /**
    * A static method for creating a series of characters of arbitrary length
    * 
    * @param unit A character which the line will be made up of
    * @param length the length of the series
    * @return A series of characters
    */
    public static String createLine(final char unit, final int length) {
        final char[] line = new char[length];
        Arrays.fill(line, unit);
        return new String(line);
    }

    /**
     * Checks to see if the node is a define statement
     * 
     * @param node the node to check
     * @return true if the node is a define statement
     */
    private static boolean isDefine(final Node node) {
        if (node.jjtGetNumChildren() == 0) {
            return false;
        }

        return node.jjtGetChild(0).toString().equals("DEFINE");
    }

    /**
     * The root node of the Mfun source
     */
    private final SimpleNode root;

    /**
     * The assembly file to be written to
     */
    private final File output;

    /**
     * Contains the data directive
     */
    private final Data data;

    /**
     * A list of defined labels
     */
    private final LabelTable labelTable;

    /**
     * Initializes the generator
     *   
     * @param root the root node of the Mfun source
     * @param output the assembly file to be written to
     */
    public Generator(final SimpleNode root, final File output) {
        this.root = root;
        this.output = output;
        this.data = new Data();
        this.labelTable = new LabelTable();
    }

    /**
     * Generates assembly code
     * 
     * @throws IOException if there is a problem writing to the assembly file
     */
    public void gen() throws IOException {
        try (FileWriter writer = new FileWriter(this.output)) {

            // Write the Assembly file's header
            writer.write(".globl" + Generator.SPACE + "main" + Generator.NL + Generator.NL);

            // Generate the main label
            Label current = this.labelTable.put();

            for (int i = 0; i < this.root.jjtGetNumChildren(); i++) {
                final Node child = Interpreter.interpret(this.root.jjtGetChild(i));

                if (isDefine(child)) {
                    this.gen_define(current, child.jjtGetChild(0).jjtGetChild(0), child.jjtGetChild(0).jjtGetChild(1));
                } else {
                    current = this.gen_print(current, this.gen_expression(current, child));
                }
            }

            // Add the exit statement to main
            current.gen_exit();

            // Write out all of the labels
            writer.write(this.labelTable.toString());

            // Write out the data directive
            writer.write(this.data.toString());
        }
    }

    /**
     * Prints the result of an expression stored in a register
     * If the register contains a value of '-1', the print statements are skipped.
     * 
     * @param label where to print the result
     * @param r the register storing the output
     * @return the label to continue from
     */
    private Label gen_print(final Label label, final Register r) {
        final Label next = this.labelTable.put();
        final Register compare = Register.getFree();

        label.addStatement(Operation.LI, compare, -1);

        // Eventually jump to the next label if the register has a value of -1
        label.addStatement(Operation.BEQ, compare, r, next.getName());
        compare.free();

        label.addStatement(Operation.LI, Register.V0, SystemCall.PRINT_INT);
        label.addStatement(Operation.MOVE, Register.A0, r.toString());
        r.free();

        label.addStatement(Operation.SYSCALL);
        label.addStatement(Operation.LI, Register.V0, SystemCall.PRINT_STRING);

        final String nl = "newline";
        label.addStatement(Operation.LA, Register.A0, nl);
        this.data.put(nl, "\\n");

        label.addStatement(Operation.SYSCALL);
        label.addStatement(Operation.J, next.getName());
        return next;
    }

    /**
     * Restarts gen_define by converting the node types
     * 
     * @param label the label pointing to an assignment 
     * @param leftSide the left-side of the define operation
     * @param rightSide the right-side of the define operation
     */
    private void gen_define(final Label label, final Node leftSide, final Node rightSide) {
        this.gen_define(label, Interpreter.interpret(leftSide), Interpreter.interpret(rightSide));
    }

    /**
     * Generates a variable or a function assignment. The assignments are stored in the
     * {@link Data} directive
     * 
     * @param label the label pointing to an assignment
     * @param leftSide the left-side of the define operation
     * @param rightSide the right-side of the define operation
     */
    private void gen_define(final Label label, final SimpleNode leftSide, final SimpleNode rightSide) {
        switch (leftSide.toString()) {
            case "VARIABLE":
                final String variable = leftSide.jjtGetValue().toString();
                final Register result = this.gen_expression(label, rightSide);

                label.addStatement(Operation.SW, result.toString(), variable);
                this.data.put(variable, 0);
                result.free();
                break;
            default:
                break;
        }
    }

    /**
     * Restarts gen_define by converting the node types
     * 
     * @param label the location where the assembly code is written
     * @param node the node pointing to the root of the expression
     * @return the register that stores the output of the expression
     */
    private Register gen_expression(final Label label, final Node node) {
        return this.gen_expression(label, Interpreter.interpret(node));
    }

    /**
     * Generates assembly code to evaluate an expression
     * 
     * @param label the location where the assembly code is written
     * @param node the node pointing to the root of the expression
     * @return the register that stores the output of the expression
     */
    private Register gen_expression(final Label label, final SimpleNode node) {
        final Register output = Register.getFree();
        final String immediate = node.jjtGetValue().toString();

        switch (node.toString()) {
            case "INTEGER":
                label.addStatement(Operation.LI, output, immediate);
                break;
            case "VARIABLE":
                label.addStatement(Operation.LW, output, immediate);
                break;
            case "ELSE":
                label.addStatement(Operation.LI, output, 1);
                break;
            default:
                final SimpleNode child = Interpreter.interpret(node.jjtGetChild(0));

                switch (child.jjtGetValue().toString()) {
                    case "+":
                        return this.gen_add(label, child, output);
                    case "cond":
                        output.free();
                        this.gen_cond(label, child);
                        output.unfree();
                        label.gen_pop(output);
                        //$FALL-THROUGH$
                    default:
                        break;
                }
                break;
        }

        return output;
    }

    /**
     * Generates assembly code to add integers
     * 
     * @param label the label to insert the assembly statements
     * @param root where the integers that need to be added are
     * @param r the initial register to store the sum
     * @return the final register containing the sum
     */
    private Register gen_add(final Label label, final SimpleNode root, Register r) {
        label.addStatement(Operation.MOVE, r, Register.ZERO);

        if (root.jjtGetNumChildren() > 0) {
            for (int i = 0; i < root.jjtGetNumChildren(); i++) {
                final Object operand = Interpreter.interpret(root.jjtGetChild(i)).jjtGetValue();

                if (operand instanceof Integer) {
                    label.addStatement(Operation.ADDI, r, r, operand.toString());
                } else {

                    // Save the accumulated sum to the stack
                    label.gen_push(r);
                    r.free();

                    // Store the result of the expression in a new register
                    final Register temp = this.gen_expression(label, Interpreter.interpret(root.jjtGetChild(i)));

                    // Add the result of the expression to the accumulated sum
                    r = Register.getFree();
                    label.gen_pop(r);
                    label.addStatement(Operation.ADD, r, r, temp);
                    temp.free();
                }
            }
        }

        return r;
    }

    /**
     * Branches to a new label if the condition doesn't evaluate to a zero.
     * 
     * @param label the location where the assembly code is written
     * @param node the node pointing to the root of the condition
     */
    private void gen_cond(final Label label, final SimpleNode node) {
        final Label cond = this.labelTable.addLabel("cond");
        label.call(cond);
        cond.saveParent();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            final Node choice = node.jjtGetChild(i);
            final Register condition = this.gen_expression(cond, choice.jjtGetChild(0));
            final Label branch = this.labelTable.addLabel("branch");

            cond.addStatement(Operation.BNEZ, condition, branch.getName());
            condition.free();

            final Register result = this.gen_expression(branch, choice.jjtGetChild(1));
            branch.swapAndJump(Register.getFree(), result);
        }

        // If none of the conditions are true, store -1 in the register
        // If a value of -1 is detected in the register, the print instructions
        // won't happen.
        final Register result = Register.getFree();
        cond.addStatement(Operation.LI, result, -1);
        cond.swapAndJump(Register.getFree(), result);
    }
}
