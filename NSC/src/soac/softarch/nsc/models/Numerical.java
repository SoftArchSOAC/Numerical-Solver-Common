package soac.softarch.nsc.models;

import soac.softarch.nsc.solver.DataLoader;

/**
 *
 * @author Vijay
 */
public class Numerical implements Initiable {

    private final int id;
    private final String identifier;
    private final String statement;
    private final String solution;
    private final int topic_id;

    private Topic topic;

    public Numerical(int id, String identifier, String statement, String solution, int topic_id) {
        this.id = id;
        this.identifier = identifier;
        this.statement = statement;
        this.solution = solution;
        this.topic_id = topic_id;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getStatement() {
        return statement;
    }

    public String getSolution() {
        return solution;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public Topic getTopic() {
        return topic;
    }

    @Override
    public void init() {
        this.topic = DataLoader.getInstance().getT(Topic.class, topic_id);
    }

}
