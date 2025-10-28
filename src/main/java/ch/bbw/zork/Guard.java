package ch.bbw.zork;

import java.util.List;
import java.util.Random;

/**
 * Der Wächter - KI-Gegner der sich zufällig durch die Räume bewegt
 */
public class Guard {
    private Room currentRoom;
    private Random random;

    public Guard(Room startRoom) {
        this.currentRoom = startRoom;
        this.random = new Random();
    }

    /**
     * Bewegt den Wächter zufällig in einen angrenzenden Raum
     */
    public void moveRandomly() {
        List<String> directions = currentRoom.getExits().keySet().stream()
            .filter(direction -> currentRoom.nextRoom(direction) != null)
            .collect(java.util.stream.Collectors.toList());

        if (!directions.isEmpty()) {
            String randomDirection = directions.get(random.nextInt(directions.size()));
            Room nextRoom = currentRoom.nextRoom(randomDirection);
            
            if (nextRoom != null) {
                currentRoom = nextRoom;
                System.out.println("\n🚨 Du hörst Schritte... Der Wächter bewegt sich!");
            }
        }
    }

    /**
     * Gibt die aktuelle Position des Wächters zurück
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Prüft ob der Wächter im selben Raum wie der Spieler ist
     */
    public boolean isInSameRoom(Player player) {
        return currentRoom == player.getCurrentRoom();
    }
}
