package primitiveOperations;

import org.jscience.mathematics.number.Rational;

/**
 * A generic primitive operation. Could either be operations returning
 * a numerical value or a boolean value.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public abstract class PrimitiveOperation {

    /**
     * Performs a primitive operation by using a parameter
     * 
     * @param parameter a parameter for the primitive operation
     */
    public abstract void apply(Rational parameter);

    /**
     * The resulting state of the primitive operations
     * 
     * @return the result, could be either boolean or Rational
     */
    public abstract Object result();

    /**
     * Show the current result. Used for debugging.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.result().toString();
    }
}
