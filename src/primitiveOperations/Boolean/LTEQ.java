package primitiveOperations.Boolean;

import org.jscience.mathematics.number.Rational;

/**
 * A primitive operation which checks to see if parameters are ordered from greatest to least.
 * This models the general case of checking if one parameter
 * is less than or equal to another.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class LTEQ extends BooleanOperation {

    /**
     * Set's up the primitive operation.
     */
    public LTEQ() {
        super();
    }

    /**
     * Replace the value which new parameters will be compared with if there is an monotonically decreasing change in value.
     * 
     * @see primitiveOperations.Boolean.BooleanOperation#apply(org.jscience.mathematics.number.Rational)
     */
    @Override
    public void apply(final Rational parameter) {
        super.apply(parameter);

        if (this.comparator.isLessThan(parameter) || this.comparator.equals(parameter)) {
            this.comparator = parameter;
            this.output = true;
        } else {
            this.output = false;
        }
    }

}
