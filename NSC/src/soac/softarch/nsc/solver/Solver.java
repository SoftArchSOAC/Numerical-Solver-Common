package soac.softarch.nsc.solver;

import java.util.StringTokenizer;
import soac.softarch.nsc.models.Parameter;
import soac.softarch.nsc.models.Unit;
import soac.softarch.nsc.models.Formula;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * This helper class is used to solve formulas.
 *
 * @see Formula
 * @see Variable
 * @author Vijay
 */
public class Solver {

    /**
     * This helper method solves a given {@link Formula} based on provided
     * {@link Variable}s, and returns the result in form of given {@link Unit}.
     *
     * @param formula The formula to solve
     * @param resultUnit Resultant answer in unit?
     * @param variables Variables of this formula
     * @return After calculating the provided {@code formula} based on given
     * {@code variables}, result will be returned in form of {@code resultUnit}.
     */
    public static double solveFormula(Formula formula, Unit resultUnit, Variable... variables) {
        String formulaString = getAsFilteredString(formula, variables);
        Expression exp = new ExpressionBuilder(formulaString).
                variables(retrieveParms(variables)).
                build();

        for (Variable variable : variables) {
            exp.setVariable(variable.param.getSymbol(), variable.value);
        }

        return exp.evaluate();
    }

    //This method accepts an array of 'Variable' objects, and returns an array of
    //String, whose values are the symbols of the parameters of those variables.
    private static String[] retrieveParms(Variable... variables) {
        String[] retval = new String[variables.length];

        for (int i = 0; i < variables.length; i++) {
            retval[i] = variables[i].param.getSymbol();
        }

        return retval;
    }

    //This method converts this formula into it's processable formula string.
    private static String getAsFilteredString(Formula formula, Variable... variables) {
        String filteredString = formula.getString();

        //replace every unit existing between ' ' with its standard multiplier
        StringBuilder tempFinal = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(filteredString, "'");

        outer:
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            String token = tokenizer.nextToken();
            String[] paramUnitPair = token.split(" ");

            if (i % 2 == 0) {//process only tokens enclosed in ' '
                tempFinal.append(token);
                continue;
            }

            if (paramUnitPair.length > 1) {//token is a 'P U' pair
                for (Variable variable : variables) {
                    if (variable.unit.getSymbol().equals(paramUnitPair[1])) {
                        token = token.replace(paramUnitPair[1], variable.unit.getStandard_multiplier());
                        tempFinal.append(token);
                        continue outer;
                    }
                }
            } else {
                tempFinal.append(token);
            }
        }
        filteredString = tempFinal.toString().replace("'", "");

        //replace ' ' with { }
        boolean open = true;
        for (int i = 0; i < filteredString.length(); i++) {
            if (filteredString.charAt(i) == '\'') {
                filteredString = filteredString.replaceFirst("\'", open ? "{" : "}");
                open = !open;
            }
        }

        //return final filtered string
        return filteredString;
    }

    /**
     * A {@code Variable} is an entity in a {@link Formula} that is provided by
     * the user. It has a {@link Parameter}, {@link Unit}, and a value.
     */
    public static class Variable {

        private Parameter param;
        private Unit unit;
        private double value;

        /**
         * Initializes this <b>Variable</b> with given {@code Parameter},
         * {@code Unit} and value.
         *
         * @param param The Parameter of this Variable
         * @param unit The Unit of this Variable.
         * @param value Value of this Variable.
         */
        public Variable(Parameter param, Unit unit, double value) {
            this.param = param;
            this.unit = unit;
            this.value = value;
        }

        /**
         * Initializes this <b>Variable</b> with given {@code Parameter},
         * {@code Unit}. The value of this <b>Variable</b> is derived based on
         * provided {@code Formula} and its list of {@code Variable}s.<br>
         * This way of creating a <b>Variable</b> is useful when its value is a
         * derived one.
         *
         * @param param The Parameter of this Variable
         * @param unit The Unit of this Variable.
         * @param formula The Formula from which value will be derived.
         * @param variables The Variables of that formula.
         */
        public Variable(Parameter param, Unit unit, Formula formula, Variable... variables) {
            this.param = param;
            this.unit = unit;
            this.value = solveFormula(formula, unit, variables);
        }

        /**
         * Returns the {@link Parameter} of this <b>Variable</b>.
         *
         * @return the parameter of this Variable.
         */
        public Parameter getParam() {
            return param;
        }

        /**
         * Sets the {@link Parameter} of this <b>Variable</b>.
         *
         * @param param The parameter to set.
         */
        public void setParam(Parameter param) {
            this.param = param;
        }

        /**
         * Returns the {@link Unit} of this <b>Variable</b>.
         *
         * @return the unit of this Variable.
         */
        public Unit getUnit() {
            return unit;
        }

        /**
         * Sets the {@link Unit} of this <b>Variable</b>.
         *
         * @param unit The unit to set.
         */
        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        /**
         * Returns the value of this <b>Variable</b>.
         *
         * @return the value of this Variable.
         */
        public double getValue() {
            return value;
        }

        /**
         * Sets the value of this <b>Variable</b>.
         *
         * @param value The value to set.
         */
        public void setValue(double value) {
            this.value = value;
        }

    }
}
