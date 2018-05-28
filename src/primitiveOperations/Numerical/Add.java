package primitiveOperations.Numerical;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which calculates the summation of parameters.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Add extends NumericalOperation {

    /**
     * Set's up the primitive operation.
     */
    public Add() {
        super(0);
    }

    /**
     * Adds the parameter to the sum.
     * 
     * @see primitiveOperations.PrimitiveOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        this.output = this.output.plus(parameter);
    }
}
