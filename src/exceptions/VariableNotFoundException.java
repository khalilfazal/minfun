package exceptions;

/**
 * An unchecked exception for cases where a variable can not be found in scope
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class VariableNotFoundException extends ReferenceException {

    /**
     * A generated serial id
     */
    private static final long serialVersionUID = -2306350400632653409L;

    /** 
     * @param variable the variable which couldn't be found
     */
    public VariableNotFoundException(final String variable) {
        super(String.format("Variable not found in scope: \"%s\"", variable), 4);
    }
}
