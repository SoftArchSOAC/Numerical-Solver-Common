package soac.softarch.nsc.models;

import soac.softarch.nsc.solver.DataLoader;

/**
 *
 * @author Vijay
 */
public class Chapter implements Initiable {

    private final int id;
    private final String name;
    private final int app_id;

    private App app;

    public Chapter(int id, String name, int app_id) {
        this.id = id;
        this.name = name;
        this.app_id = app_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getApp_id() {
        return app_id;
    }

    public App getApp() {
        return app;
    }

    @Override
    public void init() {
        app = DataLoader.getInstance().getT(App.class, app_id);
    }

}
