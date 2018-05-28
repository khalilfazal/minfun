package primitiveOperations.Numerical;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which calculates the division of parameters.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Divide extends NumericalOperation {

    /**
     * An indicator which is true if only the constructor has been called
     */
    private boolean first;

    /**
     * Sets up the primitive operation.
     * The operation's result is 1/identity if only the constructor is called.
     */
    public Divide() {
        super(null);
        this.first = true;
    }

    /**
     * Divides the quotient by the parameter. If this is the first time this method is called,
     * the result is inverted.
     * 
     * @see primitiveOperations.PrimitiveOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        if (this.output == null) {
            this.output = parameter.equals(Rational.ZERO) ? Rational.ZERO : parameter.inverse();
        } else {
            if (this.first && !this.output.equals(Rational.ZERO)) {
                this.output = this.output.inverse();
                this.first = false;
            }

            this.output = this.output.divide(parameter);
        }
    }
}
