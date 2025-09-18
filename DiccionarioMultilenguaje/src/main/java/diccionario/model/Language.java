package diccionario.model;


public enum Language {
    SPANISH("Español"),
    ENGLISH("English"),
    FRENCH("Français"),
    GERMAN("Deutsch");
    
    private final String displayName;
    
    Language(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static Language fromString(String text) {
        for (Language language : Language.values()) {
            if (language.displayName.equalsIgnoreCase(text)) {
                return language;
            }
        }
        return SPANISH; // Default
    }
}