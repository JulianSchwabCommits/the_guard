package ch.bbw.zork;
/**
 * Author:  Michael Kolling, Version: 1.1, Date: August 2000
 * refactoring: Rinaldo Lanza, September 2020
 * Extended for Guard Game: Fabian & Julian, 2025
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Room {
	
	private String description;
	private HashMap<String, Room> exits;
	private List<Item> items;                    // Items im Raum
	private List<String> searchableLocations;     // z.B. "chest", "cabinet", "desk"
	private List<String> hidingSpots;             // z.B. "hinter dem Schrank", "unter dem Tisch"
	private Random random;

	public Room(String description) {
		this.description = description;
		this.exits = new HashMap<>();
		this.items = new ArrayList<>();
		this.searchableLocations = new ArrayList<>();
		this.hidingSpots = new ArrayList<>();
		this.random = new Random();
	}

	public void setExits(Room north, Room east, Room south, Room west) {
		exits.put("north", north);
		exits.put("east", east);
		exits.put("south", south);
		exits.put("west", west);
	}

	public String shortDescription() {
		return description;
	}

	public String longDescription() {
		StringBuilder stringBuilder = new StringBuilder("You are in " + description + ".\n");
		stringBuilder.append(exitString());
		return stringBuilder.toString();
	}

	private String exitString() {
		return "Exits:" + String.join(" ", exits.keySet());
	}

	public Room nextRoom(String direction) {
		return exits.get(direction);
	}

	// ===== NEW METHODS FOR GUARD GAME =====

	/**
	 * Fügt ein Item zum Raum hinzu
	 */
	public void addItem(Item item) {
		items.add(item);
	}

	/**
	 * Entfernt ein Item aus dem Raum
	 */
	public void removeItem(Item item) {
		items.remove(item);
	}

	/**
	 * Fügt einen durchsuchbaren Ort hinzu (z.B. "chest", "cabinet")
	 */
	public void addSearchableLocation(String location) {
		searchableLocations.add(location);
	}

	/**
	 * Fügt ein Versteck hinzu (z.B. "hinter dem Schrank")
	 */
	public void addHidingSpot(String spot) {
		hidingSpots.add(spot);
	}

	/**
	 * Prüft ob man sich in diesem Raum verstecken kann
	 */
	public boolean canHideHere() {
		return !hidingSpots.isEmpty();
	}

	/**
	 * Gibt ein zufälliges Versteck zurück
	 */
	public String getRandomHidingSpot() {
		if (hidingSpots.isEmpty()) {
			return "irgendwo";
		}
		return hidingSpots.get(random.nextInt(hidingSpots.size()));
	}

	/**
	 * Durchsucht einen bestimmten Ort im Raum
	 * Gibt gefundene Items zurück
	 */
	public List<Item> search(String location) {
		List<Item> foundItems = new ArrayList<>();
		
		if (!searchableLocations.contains(location)) {
			System.out.println("Du kannst hier keinen '" + location + "' durchsuchen.");
			return foundItems;
		}

		// Finde versteckte Items an diesem Ort
		for (Item item : items) {
			if (!item.isVisible()) {
				item.setVisible(true);
				foundItems.add(item);
			}
		}

		if (foundItems.isEmpty()) {
			System.out.println("Du findest nichts Interessantes im " + location + ".");
		} else {
			System.out.println("Du hast gefunden:");
			for (Item item : foundItems) {
				System.out.println("- " + item.getName());
			}
		}

		return foundItems;
	}

	/**
	 * Zeigt alle sichtbaren Items im Raum
	 */
	public void showVisibleItems() {
		List<Item> visibleItems = items.stream()
			.filter(Item::isVisible)
			.collect(java.util.stream.Collectors.toList());
		
		if (!visibleItems.isEmpty()) {
			System.out.println("\nDu siehst hier:");
			for (Item item : visibleItems) {
				System.out.println("- " + item.getName());
			}
		}
	}

	/**
	 * Zeigt verfügbare Suchaktionen
	 */
	public void showSearchableLocations() {
		if (!searchableLocations.isEmpty()) {
			System.out.println("\nDurchsuchbare Orte:");
			for (String location : searchableLocations) {
				System.out.println("- " + location);
			}
		}
	}

	// Getter
	public List<Item> getItems() {
		return items;
	}

	public List<String> getSearchableLocations() {
		return searchableLocations;
	}

	public List<String> getHidingSpots() {
		return hidingSpots;
	}

	public HashMap<String, Room> getExits() {
		return exits;
	}
}




