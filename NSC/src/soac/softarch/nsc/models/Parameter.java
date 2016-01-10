package soac.softarch.nsc.models;

/**
 *
 * @author Vijay
 */
public class Parameter {

    private final int id;
    private final String name;
    private final String symbol;
    private final String value;
    private final String default_value;
    private final int chapter_id;

    public Parameter(int id, String name, String symbol, String value, String default_value, int chapter_id) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.value = value;
        this.default_value = default_value;
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

    public String getValue() {
        return value;
    }

    public String getDefault_value() {
        return default_value;
    }

    public int getChapter_id() {
        return chapter_id;
    }
}
