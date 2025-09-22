// This Java code defines an enum called `Language` inside the `diccionario.model` package. The enum
// represents different languages with their corresponding display names. Each language has a display
// name associated with it, such as "Español" for SPANISH, "English" for ENGLISH, "Français" for
// FRENCH, and "Deutsch" for GERMAN.
package diccionario.model;


public enum Language {
    SPANISH("Español"),
    ENGLISH("English"),
    FRENCH("Français"),
    GERMAN("Deutsch");
    
    private final String displayName;
    /*  */
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