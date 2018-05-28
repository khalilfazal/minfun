package primitiveOperations.Boolean;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which checks for equality between two numbers
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Equals extends BooleanOperation {

    /**
     * Set's up the primitive operation.
     */
    public Equals() {
        super();
    }

    /**
     * @see primitiveOperations.Boolean.BooleanOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        super.apply(parameter);
        this.output = parameter.equals(this.comparator);
    }
}
