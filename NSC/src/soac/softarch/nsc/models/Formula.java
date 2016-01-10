package soac.softarch.nsc.models;

import soac.softarch.nsc.solver.DataLoader;

/**
 *
 * @author Vijay
 */
public class Formula implements Initiable {

    private final int id;
    private final String identifier;
    private final String string;
    private final int numerical_id;

    private Numerical numerical;

    public Formula(int id, String identifier, String string, int numerical_id) {
        this.id = id;
        this.identifier = identifier;
        this.string = string;
        this.numerical_id = numerical_id;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getString() {
        return string;
    }

    public int getNumerical_id() {
        return numerical_id;
    }

    public Numerical getNumerical() {
        return numerical;
    }

    @Override
    public void init() {
        numerical = new DataLoader().getT(Numerical.class, numerical_id);
    }

}
