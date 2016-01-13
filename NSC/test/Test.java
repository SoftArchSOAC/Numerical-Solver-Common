
import java.util.List;
import java.util.Scanner;
import soac.softarch.nsc.models.Admin;
import soac.softarch.nsc.models.App;
import soac.softarch.nsc.models.Chapter;
import soac.softarch.nsc.models.Formula;
import soac.softarch.nsc.models.Numerical;
import soac.softarch.nsc.models.Parameter;
import soac.softarch.nsc.models.Topic;
import soac.softarch.nsc.models.Unit;
import soac.softarch.nsc.solver.DataLoader;
import soac.softarch.nsc.solver.Solver;

/**
 *
 * @author Vijay
 */
public class Test {

    public static void main(String[] args) {
        DataLoader dl = DataLoader.getInstance();
        dl.updateContent();

        Scanner s = new Scanner(System.in);
        double v1 = s.nextDouble();

        //"johndoe@gmail.com"
        Admin ad1 = dl.getT(Admin.class, 1);
        List<App> a1 = dl.getListOfT(App.class, ad1);
        List<Chapter> c1 = dl.getListOfT(Chapter.class, a1.get(0));
        List<Topic> t1 = dl.getListOfT(Topic.class, c1.get(0));
        List<Numerical> n1 = dl.getListOfT(Numerical.class, t1.get(0));
        List<Formula> f1 = dl.getListOfT(Formula.class, n1.get(0));

        List<Parameter> p1 = dl.getListOfT(Parameter.class, c1.get(0));
        List<Unit> u1 = dl.getListOfT(Unit.class, c1.get(0));

        Solver.Variable var1 = new Solver.Variable(p1.get(0), u1.get(0), v1);
        Solver.Variable var2 = new Solver.Variable(p1.get(1), u1.get(1), 1);

        double ans = Solver.solveFormula(f1.get(0), u1.get(0), new Solver.Variable[]{var1, var2});
        System.out.println("Answer = " + ans);
    }
}
