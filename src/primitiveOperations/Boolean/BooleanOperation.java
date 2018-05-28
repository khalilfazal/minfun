package primitiveOperations.Boolean;

import org.jscience.mathematics.number.Rational;

import primitiveOperations.PrimitiveOperation;

/**
 * A generic primitive operation which returns booleans.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public abstract class BooleanOperation extends PrimitiveOperation {

    /**
     * Parameters will be compared with this while determining an appropriate result
     */
    protected Rational comparator;

    /**
     * The primitive operation's result
     */
    protected boolean output;

    /**
     * 
     */
    public BooleanOperation() {
        this.output = true;
    }

    /**
     * @see primitiveOperations.PrimitiveOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        if (this.comparator == null) {
            this.comparator = parameter;
        }
    }

    /**
     * @see primitiveOperations.PrimitiveOperation#result()
     */
    @Override
    public Boolean result() {
        return this.output;
    }
}
