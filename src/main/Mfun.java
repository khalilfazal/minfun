package main;

import exceptions.ReferenceException;
import generator.Generator;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.ParseException;
import parser.Parser;
import parser.SimpleNode;

/**
 * Starts the Minifun compiler
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public enum Mfun {
    ;
    /** 
     * This program's main function
     * 
     * Exit codes:
     * <ul>
     * <li>-2: Lexicographical error</li>
     * <li>-1: Syntactical error</li>
     * <li> 0: Successful interpretation or generation</li>
     * <li> 1: No input file given</li>
     * <li> 2: Issues looking for the file</li>
     * <li> 3: Choice selection for the action to be performed on the Mfun source file was interrupted</li>
     * <li> 4: Variable not found in the current scope</li>
     * <li> 5: Function not found in the current scope</li>
     * <li> 6: Division by zero</li>
     * <li> 7: Error writing to assembly file</li>
     * </ul>
     * 
     * @param args args[0] is the scheme input file
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            Exiter.exit(1, "The Mfun compiler takes a filename as a parameter.");
        }

        final File file = new File(args[0]);

        try {
            final SimpleNode root = Parser.parse(file);

            // A list of actions that can be performed on the Mfun source file
            final String[][] choices = new String[][] {
                    { "1. Dump the root node." },
                    { "2. Print the abstract syntax tree." },
                    { "3. Run the interpreter." },
                    { "4. Generate assembly code." }
            };

            try (final Prompter prompt = new Prompter()) {
                switch (prompt.getSelection(choices)) {
                    case 1:
                        root.dump(Generator.TAB);
                        break;
                    case 2:
                        Parser.print_AST(root, Generator.TAB);
                        break;
                    case 3:
                        try {
                            new Interpreter(root).eval();
                        } catch (final ReferenceException e) {
                            // exit 4 or 5
                            e.exit();
                        } catch (final ArithmeticException e) {
                            Exiter.exit(6, "Division by zero");
                        }

                        Exiter.exit(0, "The interpreter ran successfully");
                        break;
                    case 4:
                        final File output = getOutputFile(file);
                        final Generator generator = new Generator(root, output);

                        try {
                            generator.gen();
                        } catch (final IOException e) {
                            Exiter.exit(7, "Error writing to assembly file.");
                        }

                        Exiter.exit(0, String.format("Successfully wrote assembly file: %s", output.getPath()));
                        break;
                    default:
                        prompt.close();
                        Exiter.exit(3, "Selection process interrupted.");
                }
            }
        } catch (final ParseException e) {
            System.err.println(e.toString());
            Exiter.exit(-1, String.format("The file \"%s\" is not syntactically valid.", file));
        }
    }

    /**
     * Uses a regular expression to extract a filename's basename from the file extension
     * 
     * @param file the output file's name is extracted from the input file's name
     * @return the output file
     */
    public static File getOutputFile(final File file) {
        final String outputDir = "./output/";
        final String regex = "^(.*/)?([^/]*?)\\.[^/\\.]+$";
        final Matcher groups = Pattern.compile(regex).matcher(file.getPath());

        groups.find();

        return new File(outputDir + groups.group(2) + ".asm");
    }
}
