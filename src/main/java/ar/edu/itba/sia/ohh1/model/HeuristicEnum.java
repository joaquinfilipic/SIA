package ar.edu.itba.sia.ohh1.model;

public enum HeuristicEnum {

    FIRST("First", 1),
    SECOND("Second", 2);

    private String name;
    private int value;

    HeuristicEnum(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
