package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert den Spieler mit Inventar und Verstecken-Mechanik
 */
public class Player {
    private Room currentRoom;
    private List<Item> inventory;
    private boolean isHidden;
    private int actionsPerTurn;
    private int actionsRemaining;
    private boolean hasMovedThisTurn;

    public Player(Room startRoom) {
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.isHidden = false;
        this.actionsPerTurn = 2;
        this.actionsRemaining = 2;
        this.hasMovedThisTurn = false;
    }

    /**
     * Bewegt den Spieler in einen neuen Raum
     */
    public boolean move(Room room) {
        if (hasMovedThisTurn) {
            System.out.println("Du kannst dich nur einmal pro Runde bewegen!");
            return false;
        }
        
        if (actionsRemaining <= 0) {
            System.out.println("Du hast keine Aktionen mehr übrig!");
            return false;
        }

        currentRoom = room;
        isHidden = false;  // Wenn man sich bewegt, ist man nicht mehr versteckt
        useAction();
        hasMovedThisTurn = true;
        return true;
    }

    /**
     * Spieler versteckt sich im aktuellen Raum
     */
    public boolean hide() {
        if (actionsRemaining <= 0) {
            System.out.println("Du hast keine Aktionen mehr übrig!");
            return false;
        }

        if (!currentRoom.canHideHere()) {
            System.out.println("Es gibt hier keine Verstecke!");
            return false;
        }

        isHidden = true;
        useAction();
        System.out.println("Du versteckst dich " + currentRoom.getRandomHidingSpot() + ".");
        return true;
    }

    /**
     * Fügt ein Item zum Inventar hinzu
     */
    public void addToInventory(Item item) {
        inventory.add(item);
        System.out.println("Du hast " + item.getName() + " aufgehoben!");
    }

    /**
     * Prüft ob Spieler ein bestimmtes Item-Type hat
     */
    public boolean hasItemType(ItemType type) {
        for (Item item : inventory) {
            if (item.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWeapon() {
        return hasItemType(ItemType.WEAPON);
    }

    public boolean hasKey() {
        return hasItemType(ItemType.KEY);
    }

    public boolean hasTeleporter() {
        return hasItemType(ItemType.TELEPORTER);
    }

    /**
     * Verbraucht eine Aktion
     */
    private void useAction() {
        actionsRemaining--;
    }

    /**
     * Setzt die Aktionen für einen neuen Zug zurück
     */
    public void resetTurn() {
        actionsRemaining = actionsPerTurn;
        hasMovedThisTurn = false;
    }

    /**
     * Zeigt das Inventar an
     */
    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Dein Inventar ist leer.");
        } else {
            System.out.println("\n=== INVENTAR ===");
            for (Item item : inventory) {
                System.out.println("- " + item.toString());
            }
        }
    }

    // Getter
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public int getActionsRemaining() {
        return actionsRemaining;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
