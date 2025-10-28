package ch.bbw.zork;

/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 * Extended for Guard Game: Fabian & Julian, 2025
 */

import java.util.Arrays;
import java.util.List;

public class CommandWords {

	// Erweiterte Befehle für Guard Game
	private List<String> validCommands = Arrays.asList(
		"go",       // Raum wechseln
		"quit",     // Spiel beenden
		"help",     // Hilfe anzeigen
		"search",   // Ort durchsuchen
		"hide",     // Verstecken
		"use",      // Item benutzen
		"next",     // Nächste Runde (wenn versteckt)
		"inventory" // Inventar anzeigen
	);

	public boolean isCommand(String commandWord) {
		return validCommands.stream()
				.filter( c -> c.equals(commandWord) )
				.count()>0;
	}

	public String showAll() {
		return String.join(" ", validCommands);
	}

}





