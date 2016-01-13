package soac.softarch.nsc.models;

import soac.softarch.nsc.solver.DataLoader;

/**
 *
 * @author Vijay
 */
public class Topic implements Initiable {

    private final int id;
    private final String name;
    private final int chapter_id;

    private Chapter chapter;

    public Topic(int id, String name, int chapter_id) {
        this.id = id;
        this.name = name;
        this.chapter_id = chapter_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public void init() {
        this.chapter = DataLoader.getInstance().getT(Chapter.class, chapter_id);
    }

}
