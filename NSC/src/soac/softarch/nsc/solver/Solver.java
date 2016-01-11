package soac.softarch.nsc.solver;

import java.util.StringTokenizer;
import soac.softarch.nsc.models.Parameter;
import soac.softarch.nsc.models.Unit;
import soac.softarch.nsc.models.Formula;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author Vijay
 */
public class Solver {

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

    private static String[] retrieveParms(Variable... variables) {
        String[] retval = new String[variables.length];

        for (int i = 0; i < variables.length; i++) {
            retval[i] = variables[i].param.getSymbol();
        }

        return retval;
    }

    private static String getAsFilteredString(Formula formula, Variable... variables) {
        String filteredString = formula.getString();

        //replace every unit existing between { } with its standard multiplier
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

    public static class Variable {

        private Parameter param;
        private Unit unit;
        private double value;

        public Variable(Parameter param, Unit unit, double value) {
            this.param = param;
            this.unit = unit;
            this.value = value;
        }

        public Variable(Parameter param, Unit unit, Formula formula, Variable... variables) {
            this.param = param;
            this.unit = unit;
            this.value = solveFormula(formula, unit, variables);
        }

        public Parameter getParam() {
            return param;
        }

        public void setParam(Parameter param) {
            this.param = param;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

    }
}
