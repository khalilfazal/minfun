package primitiveOperations.Numerical;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which calculates the difference of parameters.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Subtract extends NumericalOperation {

    /**
     * An indicator which is true if only the constructor has been called
     */
    private boolean first;

    /**
     * Set's up the primitive operation.
     */
    public Subtract() {
        super(null);
        this.first = true;
    }

    /**
     * Subtracts the difference by the parameter.
     * 
     * @see primitiveOperations.PrimitiveOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        if (this.output == null) {
            this.output = parameter.opposite();
        } else {
            if (this.first) {
                this.output = this.output.opposite();
                this.first = false;
            }

            this.output = this.output.minus(parameter);
        }
    }
}
