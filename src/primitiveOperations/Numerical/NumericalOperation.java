package primitiveOperations.Numerical;

import org.jscience.mathematics.number.Rational;

import primitiveOperations.PrimitiveOperation;

/**
 * A generic primitive operation which returns a numerical value.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public abstract class NumericalOperation extends PrimitiveOperation {

    /**
     * The primitive operation's result
     */
    protected Rational output;

    /**
     * @param identity the value which parameters will be compared against
     */
    public NumericalOperation(final Integer identity) {
        if (identity != null) {
            this.output = Rational.valueOf(identity, 1);
        }
    }

    /**
     * @see primitiveOperations.PrimitiveOperation#result()
     */
    @Override
    public Rational result() {
        return this.output;
    }
}
