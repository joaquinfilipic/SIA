package ohh1.model;

public enum CellColor {
    BLANK("Blank"),
    RED("Red"),
    BLUE("Blue");

    private String name;

    private CellColor(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
