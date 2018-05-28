package labels;

import generator.Generator;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * A collection of assembly labels 
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class LabelTable {

    /**
     * The default name of a new label
     */
    private static final String prefix = "main";

    /**
     * Stores the labels in a multimap
     */
    private final Multimap<String, Label> labels;

    /**
     * The labels are stored by name then by id.
     * The main label appears first.
     */
    public LabelTable() {
        this.labels = TreeMultimap.create(ByName.INSTANCE, ByID.INSTANCE);
    }

    /**
     * Add a new label to the table
     * 
     * @param name a requested name for the label
     * @return the newly created label
     */
    public Label addLabel(final String name) {
        final Label newLabel;
        final int count = this.labels.get(name).size();

        if (count == 0) {
            newLabel = new Label(name);
        } else {
            newLabel = new Label(name, count - 1);
        }

        this.labels.put(name, newLabel);
        return newLabel;
    }

    /**
     * @return an anonymous label
     */
    public Label put() {
        return this.addLabel(prefix);
    }

    /**
     * Represents all of the labels as strings
     * 
     * @see java.util.Hashtable#toString()
     */
    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder();

        for (final Label label : this.labels.values()) {
            output.append(label.toString());
            output.append(Generator.NL);
        }

        return output.toString();
    }
}
