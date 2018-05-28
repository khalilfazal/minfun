package labels;

import java.util.Comparator;

/**
 * Sorts Labels by their id
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum ByID implements Comparator<Label> {

    /**
     * the singleton instance
     */
    INSTANCE;

    /**
     * Sorts similar labels by their id numbers
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Label label1, final Label label2) {
        return label1.getID().compareTo(label2.getID());
    }
}
