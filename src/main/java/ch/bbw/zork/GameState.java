package ch.bbw.zork;

/**
 * Verschiedene Zustände des Spiels
 */
public enum GameState {
    PLAYING,                    // Spiel läuft
    PLAYER_WON_WEAPON,         // Gewonnen: Wächter mit Waffe besiegt
    PLAYER_WON_KEY,            // Gewonnen: Mit Schlüssel entkommen
    PLAYER_WON_TELEPORTER,     // Gewonnen: Mit Teleporter geflohen
    PLAYER_CAUGHT,             // Verloren: Vom Wächter erwischt
    QUIT                       // Spieler hat aufgegeben
}
