package soac.softarch.nsc.solver;

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
        String formulaString = formula.getString();

        String[] paramNames = retrieveParmNames(variables);

//old code for {P,U} mechanism
//        for (int i = 0; formulaString.indexOf('}') > 0;) {
//            String id_str = formulaString.substring(
//                    formulaString.indexOf('{') + 1,
//                    formulaString.indexOf('}')
//            );
//            i = formulaString.indexOf('}', i) + 1;
//
//            int comma_pos = id_str.indexOf(',');
//            int parm_id = Integer.parseInt(id_str.substring(0, comma_pos));
//            int unit_id = Integer.parseInt(id_str.substring(comma_pos));
//
//            Chapter c = formula.getNumerical().getTopic().getChapter();
//            List<Parameter> parameters = dl.getListOfT(Parameter.class, c);
//
//            for (Parameter parameter : parameters) {
//                if (parameter.getId() == id) {
//                    formulaString = formulaString.replace("{" + id + "}", parameter.getSymbol());
//                }
//            }
//        }
//
        Expression exp = new ExpressionBuilder(formulaString).
                variables(paramNames).
                build();

        for (Variable variable : variables) {
            exp.setVariable(variable.param.getSymbol(), variable.value);
        }

        return exp.evaluate();
    }

    private static String[] retrieveParmNames(Variable... variables) {
        String[] retval = new String[variables.length];

        for (int i = 0; i < variables.length; i++) {
            retval[i] = variables[i].param.getSymbol();
        }

        return retval;
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
