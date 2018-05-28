package generator;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/**
 * Stores variables in the data directive.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Data extends Hashtable<String, Object> {

    /**
     * A generated serial number since Data is {@link Serializable}.
     */
    private static final long serialVersionUID = 2034410528632306397L;

    /**
     * A ASCII data type, ends with a null character
     */
    private static final String ASCIIZ = ".asciiz";

    /**
     * A colon
     */
    private static final String COLON = ":";

    /**
     * Quotation marks
     */
    private static final String QUOTE = "\"";

    /**
     * A word data type
     */
    private static final String WORD = ".word";

    /**
     * Shows a string representation of the data directive
     * 
     * @see java.util.Hashtable#toString()
     */
    @Override
    public synchronized String toString() {
        final StringBuilder output = new StringBuilder(".data" + Generator.NL);

        for (final Map.Entry<String, Object> entry : this.entrySet()) {
            final Object value = entry.getValue();

            output.append(Generator.TAB + entry.getKey() + COLON + Generator.SPACE);

            if (value instanceof Integer) {
                output.append(Data.WORD + Generator.SPACE + value.toString());
            } else if (value instanceof String) {
                output.append(Data.ASCIIZ + Generator.SPACE + QUOTE + value.toString() + QUOTE);
            }

            output.append(Generator.SPACE + Generator.NL);
        }

        return output.toString();
    }
}
