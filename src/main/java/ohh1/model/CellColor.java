package ohh1.model;

public enum CellColor {
	
    BLANK("Blank", 0),
    RED("Red", 1),
    BLUE("Blue", 2);

    private String name;
    private int value;

    private CellColor(final String name, final int value) {
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
