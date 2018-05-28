package primitiveOperations.Numerical;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which calculates the product of parameters.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Multiply extends NumericalOperation {

    /**
     * Set's up the primitive operation.
     */
    public Multiply() {
        super(1);
    }

    /**
     * Multiplies the parameter by the sum.
     * 
     * @see primitiveOperations.PrimitiveOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational input) {
        this.output = this.output.times(input);
    }
}
