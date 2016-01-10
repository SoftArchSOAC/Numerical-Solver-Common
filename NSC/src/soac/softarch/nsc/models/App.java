package soac.softarch.nsc.models;

import soac.softarch.nsc.solver.DataLoader;

/**
 *
 * @author Vijay
 */
public class App implements Initiable {

    private final int id;
    private final String name;
    private final String store;
    private final int admin;

    private Admin admin_ref;

    public App(int id, String name, String store, int admin_id) {
        this.id = id;
        this.name = name;
        this.store = store;
        this.admin = admin_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStore() {
        return store;
    }

    public int getAdmin_id() {
        return admin;
    }

    public Admin getAdmin() {
        return admin_ref;
    }

    @Override
    public void init() {
        admin_ref = new DataLoader().getT(Admin.class, admin);
    }

}
