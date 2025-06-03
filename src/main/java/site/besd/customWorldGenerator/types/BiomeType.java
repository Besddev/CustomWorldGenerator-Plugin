package site.besd.customWorldGenerator.types;

public enum BiomeType {
    PLAINS("Plains World"),
    DESERT("Clean Desert World"),
    RED_SAND("Red Sand World"),
    MUSHROOM("Mushroom World");
    
    private final String displayName;

    BiomeType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }

}