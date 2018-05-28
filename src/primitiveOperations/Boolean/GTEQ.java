package primitiveOperations.Boolean;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which checks to see if parameters are ordered from least to greatest.
 * This models the general case of checking if one parameter
 * is greater than or equal to another.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class GTEQ extends BooleanOperation {

    /**
     * Set's up the primitive operation.
     */
    public GTEQ() {
        super();
    }

    /**
     * Replace the value which new parameters will be compared with if there is an monotonically increasing change in value.
     * 
     * @see primitiveOperations.Boolean.BooleanOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        super.apply(parameter);

        if (this.comparator.isGreaterThan(parameter) || this.comparator.equals(parameter)) {
            this.comparator = parameter;
            this.output = true;
        } else {
            this.output = false;
        }
    }

}
