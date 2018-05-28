package main;

import java.util.Hashtable;
import java.util.Map;

import org.jscience.mathematics.number.Rational;

import parser.Node;
import parser.SimpleNode;
import primitiveOperations.PrimitiveOperation;
import primitiveOperations.Boolean.Equals;
import primitiveOperations.Boolean.GT;
import primitiveOperations.Boolean.GTEQ;
import primitiveOperations.Boolean.LT;
import primitiveOperations.Boolean.LTEQ;
import primitiveOperations.Numerical.Add;
import primitiveOperations.Numerical.Divide;
import primitiveOperations.Numerical.Multiply;
import primitiveOperations.Numerical.Subtract;
import exceptions.ReferenceException;
import exceptions.VariableNotFoundException;
import function.Function;
import function.FunctionTable;

/**
 * Evaluates scheme code from an input file.
 * 
 * @author Khalil Fazal
 * @student_number 100425046
 */
public class Interpreter {

    /**
     * Casts from a Node to a SimpleNode 
     * 
     * @param node a Node
     * @return a SimpleNode
     * @throws ClassCastException if node is not a SimpleNode
     */
    public static SimpleNode interpret(final Node node) {
        if (node instanceof SimpleNode) {
            return (SimpleNode) node;
        }

        throw new ClassCastException();
    }

    /**
     * A reference to the root node of the program
     */
    private final SimpleNode root;

    /**
     * The function table which will contain all of the functions in scope
     */
    private final FunctionTable ftable;

    /**
     * The variable table which will contain all of the variables in scope
     */
    private Map<String, Object> vtable;

    /**
     * Sets the root node and creates the tables
     * 
     * @param root the root node
     */
    public Interpreter(final SimpleNode root) {
        this.root = root;
        this.vtable = new Hashtable<String, Object>();
        this.ftable = new FunctionTable();
    }

    /**
     * Evaluates all children nodes of the root node
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    public void eval() throws ReferenceException {
        for (int i = 0; i < this.root.jjtGetNumChildren(); i++) {
            this.eval_sExpression(this.root.jjtGetChild(i));
        }
    }

    /**
     * Restarts eval_sExpression by converting the node type
     * 
     * @param sExpression a symbolic expression
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private void eval_sExpression(final Node sExpression) throws ReferenceException {
        this.eval_sExpression(interpret(sExpression));
    }

    /**
     * Evaluates a symbolic expression
     * 
     * @param sExpression a symbolic expression
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private void eval_sExpression(final SimpleNode sExpression) throws ReferenceException {
        final Object result;

        switch (sExpression.toString()) {
            case "LEFT_BRACKET":
                result = this.eval_expression(sExpression.jjtGetChild(0));
                break;
            case "VARIABLE":
                result = this.eval_expression(sExpression);
                break;
            case "BOOLEAN":
            case "INTEGER":
                result = sExpression.jjtGetValue();
                break;
            default:
                result = null;
                break;
        }

        if (result != null) {
            Object output;

            if (result instanceof Rational) {
                final Rational rational = (Rational) result;

                if (rational.getDivisor().equals(1)) {
                    output = rational.getDividend();
                } else {
                    output = rational;
                }
            } else if (result instanceof Boolean) {
                output = result.equals(true) ? "#t" : "#f";
            } else {
                output = result;
            }

            System.out.println(output);
        }
    }

    /**
     * Restarts eval_define by converting the node types
     * 
     * @param leftSide the left-side of the define operation
     * @param rightSide the right-side of the define operation
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private void eval_define(final Node leftSide, final Node rightSide) throws ReferenceException {
        this.eval_define(interpret(leftSide), interpret(rightSide));
    }

    /**
     * Evaluates a variable or a function assignment. The assignments are stored in the
     * variable table or the function table, respectively.
     * 
     * @param leftSide the left-side of the define operation
     * @param rightSide the right-side of the define operation
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private void eval_define(final SimpleNode leftSide, final SimpleNode rightSide) throws ReferenceException {
        switch (leftSide.toString()) {
            case "VARIABLE":
                this.vtable.put(leftSide.jjtGetValue().toString(), this.eval_expression(rightSide));
                break;
            case "LEFT_BRACKET":
                this.ftable.put(leftSide.jjtGetChild(0), rightSide);
                break;
            default:
                break;
        }
    }

    /**
     * Restarts eval_sExpression by converting the node type
     * 
     * @param expression an expression
     * @return the evaluation of {@link Interpreter#eval_expression(SimpleNode)}
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private Object eval_expression(final Node expression) throws ReferenceException {
        return this.eval_expression(interpret(expression));
    }

    /**
     * Evaluates an expression
     * 
     * @param expression an expression
     * @return what the expression evaluates to  
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private Object eval_expression(final SimpleNode expression) throws ReferenceException {
        final Object value = expression.jjtGetValue();

        switch (expression.toString()) {
            case "LEFT_BRACKET":
                return this.eval_expression(expression.jjtGetChild(0));
            case "BOOLEAN":
            case "INTEGER":
                return value;
            case "ELSE":
                return true;
            case "VARIABLE":
                final Object variableValue = this.vtable.get(value);

                if (variableValue == null) {
                    throw new VariableNotFoundException(value.toString());
                }

                return variableValue;
            case "FUNCTION":
                final Object[] children = new Object[expression.jjtGetNumChildren()];

                for (int i = 0; i < children.length; i++) {
                    final String childrenValue = this.eval_expression(expression.jjtGetChild(i)).toString();

                    try {
                        children[i] = Rational.valueOf(childrenValue);
                    } catch (final Exception e) {
                        // Exception thrown if value is not rational

                        children[i] = childrenValue;
                    }
                }

                return this.eval_operation(expression.jjtGetValue().toString(), children);
            case "CONDITION":
                return this.eval_condition(expression);
            case "DEFINE":
                this.eval_define(expression.jjtGetChild(0), expression.jjtGetChild(1));
                //$FALL-THROUGH$
            default:
                return null;
        }
    }

    /**
     * Evaluates a conditional block
     * 
     * A choice is executed if the condition is true or 1.
     * 
     * @param conditional a conditional block
     * @return the result of the conditional block
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private Object eval_condition(final SimpleNode conditional) throws ReferenceException {
        for (int i = 0; i < conditional.jjtGetNumChildren(); i++) {
            final Node child = conditional.jjtGetChild(i);
            final Object conditionObj = this.eval_expression(child.jjtGetChild(0));
            final String conditionStr = conditionObj.toString();
            final boolean condition;

            if (conditionObj instanceof Integer) {
                condition = Integer.parseInt(conditionStr) == 1 ? true : false;
            } else {
                condition = Boolean.parseBoolean(conditionStr);
            }

            if (condition) {
                return this.eval_expression(child.jjtGetChild(1));
            }
        }

        return null;
    }

    /**
     * Evaluates an operator, whether its a built-in operator or a function
     * 
     * @param operator the name of the operator
     * @param operands the operator's arguments
     * @return what the operation evaluates to
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private Object eval_operation(final String operator, final Object[] operands) throws ReferenceException {
        final PrimitiveOperation operation;

        switch (operator) {
            case "+":
                operation = new Add();
                break;
            case "-":
                operation = new Subtract();
                break;
            case "*":
                operation = new Multiply();
                break;
            case "/":
                operation = new Divide();
                break;
            case "=":
                operation = new Equals();
                break;
            case "<=":
                operation = new LTEQ();
                break;
            case "<":
                operation = new LT();
                break;
            case ">=":
                operation = new GTEQ();
                break;
            case ">":
                operation = new GT();
                break;
            default:
                return this.eval_function(this.ftable.get(operator), operands);
        }

        for (final Object operand : operands) {
            operation.apply((Rational) operand);
        }

        return operation.result();
    }

    /**
     * Evaluates a function. Arguments which the function operates on are assigned temporarily in the variable table.
     * After the function is finished being evaluated, the variable table's state is restored.
     * 
     * @param function a reference to an anonymous function retrieved from the function table
     * @param inputArguments arguments which will temporarily exist in the variable table as the function is evaluated
     * @return what the function evaluates to
     * @throws ReferenceException thrown when a function or a variable does not exist in scope
     */
    private Object eval_function(final Function function, final Object[] inputArguments) throws ReferenceException {
        final String[] arguments = function.getArguments();
        final Map<String, Object> backupVTable = new Hashtable<String, Object>(this.vtable);

        for (int i = 0; i < arguments.length; i++) {
            this.vtable.put(arguments[i], inputArguments[i]);
        }

        final Object output = this.eval_expression(function.getFunctionBody());

        // Restore the variable table
        this.vtable = backupVTable;

        return output;
    }

    /**
     * Displays how many variables or functions are in scope. Used for
     * debugging purposes. Deadcode otherwise.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder();

        output.append(String.format("%s%d%n", "Number of entries in the variable table: ", this.vtable.size()));
        output.append(String.format("%s%d", "Number of entries in the function table: ", this.ftable.size()));

        return output.toString();
    }
}
