package function;

import parser.Node;
import parser.SimpleNode;

/**
 * Used for references to anonymous functions which will be contained in a function table.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Function {

    /**
     * A list of the function's arguments
     */
    private final String[] arguments;

    /**
     * A reference to the function's entry point
     */
    private final Node body;

    /**
     * Creates an anonymous function
     * 
     * @param arguments A list of the function's arguments
     * @param body A reference to the function's entry point
     */
    public Function(final Node arguments, final Node body) {
        final int argumentLength = arguments.jjtGetNumChildren();

        this.arguments = new String[argumentLength];

        for (int i = 0; i < argumentLength; i++) {
            this.arguments[i] = ((SimpleNode) arguments.jjtGetChild(i)).jjtGetValue().toString();
        }

        this.body = body;
    }

    /**
     * @return A list of arguments
     */
    public String[] getArguments() {
        return this.arguments.clone();
    }

    /**
     * @return Gets the function's body
     */
    public Node getFunctionBody() {
        return this.body;
    }

    /**
     * Shows a string representation of the function. Uses MatLab's scheme for displaying anonymous functions.
     * Used for debugging purposes, otherwise this is deadcode.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder("@(");
    
        for (final String argument : this.arguments) {
            output.append(argument + " ");
        }
    
        output.deleteCharAt(output.length() - 1);
        output.append(")");
        return output.toString();
    }
}
