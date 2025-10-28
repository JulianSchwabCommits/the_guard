package ch.bbw.zork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * "The Guard" - Ein textbasiertes Adventure-Spiel
 * 
 * Der Spieler muss aus einem Gebäude entkommen, während ein Wächter patrouilliert.
 * 
 * @author Fabian & Julian
 * @version 1.0, Oktober 2025
 */
public class GuardGame {

    private Parser parser;
    private Player player;
    private Guard guard;
    private GameState gameState;
    private Map<String, Room> rooms;

    public GuardGame() {
        parser = new Parser(System.in);
        rooms = new HashMap<>();
        createRooms();
        player = new Player(rooms.get("empfangshalle"));
        guard = new Guard(rooms.get("ueberwachungsraum"));  // Guard startet woanders
        gameState = GameState.PLAYING;
    }

    /**
     * Erstellt alle Räume gemäß der Karte
     */
    private void createRooms() {
        // Räume erstellen
        Room empfangshalle = new Room("in der Empfangshalle - eine grosse Eingangshalle mit Rezeption");
        Room flurEG = new Room("im Flur des Erdgeschosses");
        Room flurOG = new Room("im Flur des Obergeschosses");
        Room bibliothek = new Room("in der Bibliothek mit hohen Bücherregalen");
        Room bueroChef = new Room("im Büro des Chefs - ein luxuriös eingerichteter Raum");
        Room tresorRaum = new Room("im Tresor-Raum - stark gesichert");
        Room sicherheitsraum = new Room("im Sicherheitsraum");
        Room ueberwachungsraum = new Room("im Überwachungsraum - voller Monitore");
        Room cafeteria = new Room("in der Cafeteria");
        Room kueche = new Room("in der Küche");
        Room lagerraum = new Room("im Lagerraum voller Kisten");
        Room keller = new Room("im dunklen Keller");
        Room heizungskeller = new Room("im Heizungskeller mit vielen Rohren");
        Room versteck = new Room("in einem geheimen Versteck");
        Room dachboden = new Room("auf dem staubigen Dachboden");
        Room aussenbereich = new Room("im Aussenbereich - du bist frei!");

        // Exits setzen (basierend auf der Karte)
        empfangshalle.setExits(flurOG, cafeteria, null, flurEG);
        flurEG.setExits(null, empfangshalle, null, keller);
        flurOG.setExits(dachboden, bueroChef, empfangshalle, bibliothek);
        bibliothek.setExits(null, flurOG, keller, null);
        bueroChef.setExits(null, null, null, flurOG);  // Tresor separat (braucht Schlüssel)
        keller.setExits(bibliothek, flurEG, heizungskeller, null);
        heizungskeller.setExits(keller, kueche, null, versteck);
        kueche.setExits(null, cafeteria, lagerraum, heizungskeller);
        cafeteria.setExits(null, ueberwachungsraum, aussenbereich, empfangshalle);
        lagerraum.setExits(kueche, aussenbereich, null, versteck);
        ueberwachungsraum.setExits(null, null, null, cafeteria);  // Sicherheitsraum separat
        versteck.setExits(null, lagerraum, null, heizungskeller);
        dachboden.setExits(null, null, flurOG, null);
        aussenbereich.setExits(cafeteria, null, null, lagerraum);
        
        // Sicherheitsraum und Tresor - nur mit Schlüssel erreichbar
        sicherheitsraum.setExits(null, null, null, null);  // Wird später verbunden
        tresorRaum.setExits(null, null, null, null);      // Wird später verbunden

        // Durchsuchbare Orte und Verstecke hinzufügen
        setupEmpfangshalle(empfangshalle);
        setupBibliothek(bibliothek);
        setupBueroChef(bueroChef);
        setupTresorRaum(tresorRaum);
        setupKueche(kueche);
        setupLagerraum(lagerraum);
        setupKeller(keller);
        setupVersteck(versteck);
        setupDachboden(dachboden);

        // Räume zur Map hinzufügen
        rooms.put("empfangshalle", empfangshalle);
        rooms.put("flurEG", flurEG);
        rooms.put("flurOG", flurOG);
        rooms.put("bibliothek", bibliothek);
        rooms.put("bueroChef", bueroChef);
        rooms.put("tresorRaum", tresorRaum);
        rooms.put("sicherheitsraum", sicherheitsraum);
        rooms.put("ueberwachungsraum", ueberwachungsraum);
        rooms.put("cafeteria", cafeteria);
        rooms.put("kueche", kueche);
        rooms.put("lagerraum", lagerraum);
        rooms.put("keller", keller);
        rooms.put("heizungskeller", heizungskeller);
        rooms.put("versteck", versteck);
        rooms.put("dachboden", dachboden);
        rooms.put("aussenbereich", aussenbereich);
    }

    // Setup-Methoden für einzelne Räume
    private void setupEmpfangshalle(Room room) {
        room.addSearchableLocation("desk");
        room.addSearchableLocation("drawer");
        room.addHidingSpot("hinter der Rezeption");
        room.addItem(new Item("Notiz", "Ein Hinweis: 'Der Schlüssel ist im Büro des Chefs'", ItemType.HINT));
    }

    private void setupBibliothek(Room room) {
        room.addSearchableLocation("shelves");
        room.addSearchableLocation("books");
        room.addHidingSpot("hinter den Bücherregalen");
        room.addHidingSpot("unter dem Tisch");
    }

    private void setupBueroChef(Room room) {
        room.addSearchableLocation("safe");
        room.addSearchableLocation("desk");
        room.addHidingSpot("unter dem Schreibtisch");
        room.addItem(new Item("Schlüssel", "Ein goldener Schlüssel - öffnet den Ausgang!", ItemType.KEY));
    }

    private void setupTresorRaum(Room room) {
        room.addSearchableLocation("vault");
        room.addItem(new Item("Waffe", "Eine Betäubungspistole", ItemType.WEAPON));
    }

    private void setupKueche(Room room) {
        room.addSearchableLocation("cabinets");
        room.addSearchableLocation("fridge");
        room.addHidingSpot("hinter der Theke");
        room.addItem(new Item("Messer", "Ein scharfes Küchenmesser", ItemType.WEAPON));
    }

    private void setupLagerraum(Room room) {
        room.addSearchableLocation("boxes");
        room.addSearchableLocation("crates");
        room.addHidingSpot("hinter den Kisten");
    }

    private void setupKeller(Room room) {
        room.addSearchableLocation("shelves");
        room.addHidingSpot("hinter den Regalen");
        room.addItem(new Item("Taschenlampe", "Eine funktionierende Taschenlampe", ItemType.TOOL));
    }

    private void setupVersteck(Room room) {
        room.addSearchableLocation("chest");
        room.addHidingSpot("im geheimen Raum selbst");
        room.addItem(new Item("Teleporter", "Ein mysteriöses Portal-Gerät!", ItemType.TELEPORTER));
    }

    private void setupDachboden(Room room) {
        room.addSearchableLocation("boxes");
        room.addSearchableLocation("chest");
        room.addHidingSpot("hinter alten Möbeln");
    }

    /**
     * Hauptspiel-Schleife
     */
    public void play() {
        printWelcome();

        while (gameState == GameState.PLAYING) {
            player.resetTurn();
            printRoomInfo();
            
            // Spieler hat 2 Aktionen
            while (player.getActionsRemaining() > 0 && gameState == GameState.PLAYING) {
                System.out.println("\n📊 Aktionen übrig: " + player.getActionsRemaining());
                Command command = parser.getCommand();
                processCommand(command);
            }

            // Nach dem Zug: Wächter bewegt sich
            if (gameState == GameState.PLAYING) {
                guard.moveRandomly();
                checkGameOver();
            }
        }

        printGameEnd();
    }

    /**
     * Willkommensnachricht
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       🏢 THE GUARD 🏢              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Du bist in einem Gebäude gefangen!");
        System.out.println("Ein Wächter patrouilliert durch die Räume.");
        System.out.println();
        System.out.println("🎯 ZIELE:");
        System.out.println("  🔑 Finde einen Schlüssel um zu entkommen");
        System.out.println("  ⚔️  Finde eine Waffe um den Wächter zu besiegen");
        System.out.println("  ✨ Finde den geheimen Teleporter");
        System.out.println();
        System.out.println("Tippe 'help' für Hilfe.");
        System.out.println();
    }

    /**
     * Zeigt Raum-Informationen
     */
    private void printRoomInfo() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("📍 " + player.getCurrentRoom().longDescription());
        player.getCurrentRoom().showVisibleItems();
        player.getCurrentRoom().showSearchableLocations();
        System.out.println("═".repeat(50));
    }

    /**
     * Verarbeitet Befehle
     */
    private void processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("❌ Unbekannter Befehl...");
            return;
        }

        String commandWord = command.getCommandWord();

        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "search":
                searchLocation(command);
                break;
            case "hide":
                hidePlayer();
                break;
            case "use":
                useItem(command);
                break;
            case "inventory":
                player.showInventory();
                break;
            case "next":
                nextTurn();
                break;
            case "quit":
                gameState = GameState.QUIT;
                break;
            default:
                System.out.println("❌ Dieser Befehl ist noch nicht implementiert.");
        }
    }

    private void printHelp() {
        System.out.println("\n📖 HILFE");
        System.out.println("Verfügbare Befehle:");
        System.out.println("  go <richtung>      - Bewege dich (north/south/east/west)");
        System.out.println("  search <ort>       - Durchsuche einen Ort");
        System.out.println("  hide               - Verstecke dich");
        System.out.println("  use <item>         - Benutze ein Item");
        System.out.println("  inventory          - Zeige Inventar");
        System.out.println("  next               - Nächste Runde (wenn versteckt)");
        System.out.println("  help               - Zeige diese Hilfe");
        System.out.println("  quit               - Spiel beenden");
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Wohin möchtest du gehen?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = player.getCurrentRoom().nextRoom(direction);

        if (nextRoom == null) {
            System.out.println("❌ Da ist keine Tür!");
        } else {
            player.move(nextRoom);
            System.out.println("✅ Du bewegst dich " + direction + ".");
        }
    }

    private void searchLocation(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Was möchtest du durchsuchen?");
            player.getCurrentRoom().showSearchableLocations();
            return;
        }

        if (player.getActionsRemaining() <= 0) {
            System.out.println("❌ Du hast keine Aktionen mehr!");
            return;
        }

        String location = command.getSecondWord();
        List<Item> foundItems = player.getCurrentRoom().search(location);
        
        // Items automatisch aufheben
        for (Item item : foundItems) {
            player.addToInventory(item);
            player.getCurrentRoom().removeItem(item);
        }

        // Aktion verbrauchen
        if (!foundItems.isEmpty() || player.getCurrentRoom().getSearchableLocations().contains(location)) {
            player.getActionsRemaining();  // Wird in Player.search() behandelt - TODO: Fix this
        }
    }

    private void hidePlayer() {
        player.hide();
    }

    private void useItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Welches Item möchtest du benutzen?");
            player.showInventory();
            return;
        }

        String itemName = command.getSecondWord();
        
        // Prüfe Win-Bedingungen
        if (itemName.equalsIgnoreCase("schlüssel") || itemName.equalsIgnoreCase("key")) {
            if (player.hasKey()) {
                gameState = GameState.PLAYER_WON_KEY;
            } else {
                System.out.println("❌ Du hast keinen Schlüssel!");
            }
        } else if (itemName.equalsIgnoreCase("waffe") || itemName.equalsIgnoreCase("weapon") || itemName.equalsIgnoreCase("messer")) {
            if (player.hasWeapon() && player.isHidden() && guard.isInSameRoom(player)) {
                gameState = GameState.PLAYER_WON_WEAPON;
            } else if (!player.hasWeapon()) {
                System.out.println("❌ Du hast keine Waffe!");
            } else if (!guard.isInSameRoom(player)) {
                System.out.println("❌ Der Wächter ist nicht hier!");
            } else {
                System.out.println("❌ Du musst versteckt sein um anzugreifen!");
            }
        } else if (itemName.equalsIgnoreCase("teleporter")) {
            if (player.hasTeleporter()) {
                gameState = GameState.PLAYER_WON_TELEPORTER;
            } else {
                System.out.println("❌ Du hast keinen Teleporter!");
            }
        }
    }

    private void nextTurn() {
        if (!player.isHidden()) {
            System.out.println("❌ Du musst versteckt sein um zu warten!");
            return;
        }
        
        System.out.println("⏭️  Du wartest versteckt...");
        player.resetTurn();  // Beendet den Zug vorzeitig
    }

    /**
     * Prüft Game Over Bedingung
     */
    private void checkGameOver() {
        if (guard.isInSameRoom(player) && !player.isHidden()) {
            gameState = GameState.PLAYER_CAUGHT;
        }
    }

    /**
     * Zeigt Ende-Nachricht
     */
    private void printGameEnd() {
        System.out.println("\n" + "═".repeat(50));
        
        switch (gameState) {
            case PLAYER_WON_KEY:
                System.out.println("🎉 GEWONNEN! Du hast den Schlüssel benutzt und bist entkommen!");
                break;
            case PLAYER_WON_WEAPON:
                System.out.println("🎉 GEWONNEN! Du hast den Wächter überwältigt!");
                break;
            case PLAYER_WON_TELEPORTER:
                System.out.println("🎉 GEWONNEN! Der Teleporter hat dich in Sicherheit gebracht!");
                break;
            case PLAYER_CAUGHT:
                System.out.println("💀 GAME OVER! Der Wächter hat dich erwischt!");
                break;
            case QUIT:
                System.out.println("👋 Danke fürs Spielen!");
                break;
            default:
                System.out.println("Spiel beendet.");
        }
        
        System.out.println("═".repeat(50));
    }

    /**
     * Main-Methode zum Starten des Spiels
     */
    public static void main(String[] args) {
        GuardGame game = new GuardGame();
        game.play();
    }
}
