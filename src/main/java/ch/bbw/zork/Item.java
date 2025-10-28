package ch.bbw.zork;

/**
 * Repräsentiert einen Gegenstand im Spiel
 */
public class Item {
    private String name;
    private String description;
    private ItemType type;
    private boolean isVisible;  // Manche Items sind versteckt

    public Item(String name, String description, ItemType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.isVisible = false;  // Items sind anfangs versteckt
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * Prüft ob das Item benutzbar ist (Waffe, Schlüssel, Teleporter)
     */
    public boolean isUsable() {
        return type == ItemType.WEAPON || 
               type == ItemType.KEY || 
               type == ItemType.TELEPORTER;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}
