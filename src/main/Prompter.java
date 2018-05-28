package main;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solicits the user for a choice
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Prompter implements Closeable {

    /**
     * The scanner to listen for user input
     */
    private final Scanner scanner;

    /**
     * Initializes the prompter
     */
    public Prompter() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Asks the user to choice an option
     * 
     * @param choices a list of user's choices
     * @return the user's choice, -1 if selection process is cancelled
     */
    public int getSelection(final Object[][] choices) {
        Integer selection = null;

        for (final Object[] prompt : choices) {
            System.out.printf((String) prompt[0], Arrays.copyOfRange(prompt, 1, prompt.length));
            System.out.println();
        }

        do {
            System.out.print("Choose an option: ");

            if (this.scanner.hasNextInt()) {
                selection = this.scanner.nextInt();

                if (selection == null || selection < 1 || selection > choices.length) {
                    System.err.println("Invalid selection.");
                    selection = null;
                }
            } else {
                if (this.scanner.hasNextLine()) {
                    System.err.println("Invalid selection.");
                    this.scanner.nextLine();
                } else {
                    System.out.println();
                    return -1;
                }
            }
        } while (selection == null);

        return selection;
    }

    /**
     * Close the prompt after use
     * 
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() {
        this.scanner.close();
    }
}
