package soac.softarch.nsc.models;

/**
 *
 * @author Vijay
 */
public class Unit {

    private final int id;
    private final String name;
    private final String symbol;
    private final String standard_multiplier;
    private final int chapter_id;

    public Unit(int id, String name, String symbol, String standard_multiplier, int chapter_id) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.standard_multiplier = standard_multiplier;
        this.chapter_id = chapter_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStandard_multiplier() {
        return standard_multiplier;
    }

    public int getChapter_id() {
        return chapter_id;
    }
}
