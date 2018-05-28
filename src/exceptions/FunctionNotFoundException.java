package exceptions;

/**
 * An unchecked exception for cases where a function can not be found in scope
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class FunctionNotFoundException extends ReferenceException {

    /**
     * A generated serial id
     */
    private static final long serialVersionUID = 5636234713140358670L;

    /**  
     * @param function the function which couldn't be found
     */
    public FunctionNotFoundException(final String function) {
        super(String.format("Function not found in scope: \"%s\"", function), 5);
    }
}
