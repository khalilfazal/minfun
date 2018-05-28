package function;

import java.util.Hashtable;

import parser.Node;
import parser.SimpleNode;
import exceptions.FunctionNotFoundException;

/**
 * Used for storing functions in scope
 * 
 * @author Khalil Fazal 
 * @student_number 100425046
 */
public class FunctionTable extends Hashtable<String, Function> {

    /**
     * A generated serial id
     */
    private static final long serialVersionUID = 4487584686110579759L;

    /**
     * Inserts a new function in the table
     * 
     * @param arguments the node which will contain the function's name and its arguments
     * @param body the node containing the function's body
     */
    public void put(final Node arguments, final Node body) {
        final String handle = ((SimpleNode) arguments).jjtGetValue().toString();

        this.put(handle, new Function(arguments, body));
    }

    /**
     * Retrieves a reference to a function by it's handle
     * 
     * @param handle the function's handle, i.e.: it's name
     * @return a reference to the function
     * @throws FunctionNotFoundException thrown when a function does not exit in scope
     */
    public Function get(final String handle) throws FunctionNotFoundException {
        final Function function = super.get(handle);

        if (function == null) {
            throw new FunctionNotFoundException(handle);
        }

        return function;
    }
}
