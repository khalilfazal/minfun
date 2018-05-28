package labels;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

/**
 * ByLabel is a singleton class.
 * 
 * Sort labels alphabetically.
 * The main label appears before all other labels.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum ByName implements Comparator<String> {

    /**
     * the singleton instance
     */
    INSTANCE;

    /**
     * Sort alphabetically unless one of them is main,
     * then main gets sorted first.
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final String left, final String right) {
        return ComparisonChain.start()
                .compareTrueFirst(left.equals("main"), right.equals("main"))
                .compare(left, right)
                .result();
    }
}
