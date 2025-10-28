# 🔧 The Guard - Technische Entwickler-Dokumentation

**Detaillierte Architektur und Implementierungs-Details**

---

## 📋 Inhaltsverzeichnis

1. [Architektur-Übersicht](#architektur-übersicht)
2. [Klassen-Dokumentation](#klassen-dokumentation)
3. [Game Loop Mechanik](#game-loop-mechanik)
4. [Design Patterns](#design-patterns)
5. [Datenstrukturen](#datenstrukturen)
6. [Erweiterungsmöglichkeiten](#erweiterungsmöglichkeiten)

---

## 🏗️ Architektur-Übersicht

### Layer-Architektur (Bottom-Up)

```
┌─────────────────────────────────────────┐
│         GuardGame (Main Loop)           │  ← Layer 4: Game Logic
├─────────────────────────────────────────┤
│   Parser | CommandWords | Command       │  ← Layer 3: Input System
├─────────────────────────────────────────┤
│     Player | Guard | Room                │  ← Layer 2: Game Entities
├─────────────────────────────────────────┤
│   Item | ItemType | GameState           │  ← Layer 1: Data Models
└─────────────────────────────────────────┘
```

### Dependency Graph

```
GuardGame
  ├─→ Parser
  │    └─→ CommandWords
  │    └─→ Command
  ├─→ Player
  │    └─→ Room
  │    └─→ Item
  ├─→ Guard
  │    └─→ Room
  ├─→ GameState (enum)
  └─→ Room
       └─→ Item
            └─→ ItemType (enum)
```

---

## 📚 Klassen-Dokumentation

### 1. GuardGame.java

**Verantwortlichkeiten:**
- Hauptspiel-Orchestrierung
- Raum-Erstellung und Verbindung
- Game Loop Management
- Win/Loss Condition Checking

**Wichtige Methoden:**

```java
public GuardGame()
// Initialisiert:
// - Parser für Input
// - 16 Räume mit Items/Verstecken
// - Player (Start: Empfangshalle)
// - Guard (Start: Überwachungsraum)
// - GameState = PLAYING

public void play()
// Hauptspiel-Schleife:
// 1. Player resetTurn() → 2 neue Aktionen
// 2. While (actionsRemaining > 0)
//    - Befehl einlesen
//    - Befehl verarbeiten
// 3. Guard bewegt sich
// 4. Game Over Check
// 5. Repeat bis gameState != PLAYING

private void createRooms()
// Erstellt alle 16 Räume
// Setzt Exits (north/south/east/west)
// Ruft setup-Methoden auf

private void setupXXX(Room room)
// Konfiguriert einzelne Räume:
// - addSearchableLocation()
// - addHidingSpot()
// - addItem()

private void processCommand(Command command)
// Switch-Statement für Befehle:
// - help, go, search, hide
// - use, inventory, next, quit

private void checkGameOver()
// Prüft: guard.isInSameRoom(player) && !player.isHidden()
// → GameState.PLAYER_CAUGHT
```

**Design-Entscheidungen:**

1. **Warum separate setup-Methoden?**
   - Bessere Lesbarkeit
   - Einfacher zu erweitern
   - Klare Separation of Concerns

2. **Warum HashMap für Räume?**
   ```java
   private Map<String, Room> rooms;
   ```
   - Schneller Zugriff per Name
   - Einfache Erweiterung
   - Alternative: Array (schlechter)

---

### 2. Player.java

**Verantwortlichkeiten:**
- Inventar-Management
- Aktions-Tracking (2 pro Runde)
- Versteck-Mechanik
- Movement mit Einschränkungen

**Wichtige Attribute:**

```java
private Room currentRoom;              // Aktuelle Position
private List<Item> inventory;          // Gesammelte Items
private boolean isHidden;              // Versteck-Status
private int actionsPerTurn = 2;        // Max Aktionen
private int actionsRemaining;          // Übrige Aktionen
private boolean hasMovedThisTurn;     // Movement-Lock
```

**Wichtige Methoden:**

```java
public boolean move(Room room)
// Checks:
// 1. hasMovedThisTurn? → return false
// 2. actionsRemaining <= 0? → return false
// 3. ✅ Bewege, setze isHidden = false
// 4. useAction(), hasMovedThisTurn = true

public boolean hide()
// Checks:
// 1. actionsRemaining <= 0? → return false
// 2. !currentRoom.canHideHere()? → return false
// 3. ✅ isHidden = true, useAction()

public void addToInventory(Item item)
// Fügt Item hinzu
// Ausgabe: "Du hast X aufgehoben!"

public boolean hasItemType(ItemType type)
// Durchsucht Inventar nach Typ
// Verwendet von: hasWeapon(), hasKey(), hasTeleporter()

public void resetTurn()
// Setzt zurück:
// - actionsRemaining = actionsPerTurn
// - hasMovedThisTurn = false
// Wird von GuardGame.play() pro Runde aufgerufen
```

**Design-Entscheidungen:**

1. **Warum hasMovedThisTurn?**
   ```java
   // Problem ohne Flag:
   move(room1); move(room2); // 2x bewegen in 1 Runde!
   
   // Lösung mit Flag:
   move(room1); // OK, flag = true
   move(room2); // BLOCKED! "Nur 1x bewegen pro Runde"
   ```

2. **Warum isHidden bei move() auf false?**
   - Realistisch: Bewegung macht Lärm
   - Balance: Verhindert "versteckt durchs Spiel laufen"
   - Gameplay: Spieler muss strategisch verstecken

---

### 3. Guard.java

**Verantwortlichkeiten:**
- Zufällige Raum-Bewegung
- Collision-Detection mit Player

**Wichtige Methoden:**

```java
public void moveRandomly()
// Algorithmus:
// 1. Hole alle Exits vom currentRoom
// 2. Filter: nur nicht-null Räume
// 3. Wähle zufälligen Exit
// 4. Bewege zu diesem Raum
// 5. Ausgabe: "🚨 Du hörst Schritte..."

public boolean isInSameRoom(Player player)
// return currentRoom == player.getCurrentRoom();
```

**Algorithmus-Details:**

```java
List<String> directions = currentRoom.getExits().keySet().stream()
    .filter(direction -> currentRoom.nextRoom(direction) != null)
    .collect(Collectors.toList());

if (!directions.isEmpty()) {
    String randomDirection = directions.get(random.nextInt(directions.size()));
    currentRoom = currentRoom.nextRoom(randomDirection);
}
```

**Warum Stream API?**
- Filtert null-Exits automatisch
- Funktional und lesbar
- Java 8+ Standard

---

### 4. Room.java

**Verantwortlichkeiten:**
- Raum-Beschreibung
- Exit-Management
- Item-Container
- Such- und Versteck-Orte

**Wichtige Attribute:**

```java
private String description;                    // "in der Küche"
private HashMap<String, Room> exits;          // north/south/east/west
private List<Item> items;                     // Items im Raum
private List<String> searchableLocations;     // "chest", "desk"
private List<String> hidingSpots;             // "hinter Theke"
private Random random;                        // Für getRandomHidingSpot()
```

**Wichtige Methoden:**

```java
public void setExits(Room north, Room east, Room south, Room west)
// Setzt alle 4 Himmelsrichtungen
// null = keine Tür in diese Richtung

public List<Item> search(String location)
// Algorithmus:
// 1. Ist location in searchableLocations? Nein → return []
// 2. Finde alle items mit isVisible() == false
// 3. Setze diese auf isVisible() = true
// 4. Return gefundene Items
// Ausgabe: "Du hast gefunden: ..."

public boolean canHideHere()
// return !hidingSpots.isEmpty();

public String getRandomHidingSpot()
// Wählt zufälliges Element aus hidingSpots
// Für Ausgabe: "Du versteckst dich HIER"
```

**Design-Entscheidungen:**

1. **Warum separate Lists für searchableLocations und items?**
   ```java
   // searchableLocations = WO kann gesucht werden
   // items = WAS liegt hier
   
   // Vorteil:
   search("chest");     // Sucht in Truhe
   search("desk");      // Sucht in Schreibtisch
   // → Verschiedene Orte, verschiedene Items
   ```

2. **Warum isVisible() Flag?**
   ```java
   // Phase 1: Item existiert, aber unsichtbar
   addItem(new Item("Schlüssel", ...));  // isVisible = false
   
   // Phase 2: Spieler sucht
   search("desk");  // Findet Item, setzt isVisible = true
   
   // Phase 3: Item ist jetzt sichtbar
   showVisibleItems();  // Zeigt Schlüssel an
   ```

---

### 5. Item.java

**Verantwortlichkeiten:**
- Item-Daten (Name, Beschreibung, Typ)
- Sichtbarkeits-Status
- Verwendbarkeits-Check

```java
public class Item {
    private String name;           // "Schlüssel"
    private String description;    // "Ein goldener Schlüssel"
    private ItemType type;         // WEAPON/KEY/TELEPORTER/HINT/TOOL
    private boolean isVisible;     // Anfangs false
}
```

**Warum isUsable()?**
```java
public boolean isUsable() {
    return type == ItemType.WEAPON || 
           type == ItemType.KEY || 
           type == ItemType.TELEPORTER;
}

// Verwendung in GuardGame:
if (item.isUsable()) {
    // Kann mit "use" benutzt werden
}
```

---

### 6. Enums

#### ItemType.java
```java
public enum ItemType {
    WEAPON,      // Messer, Pistole
    KEY,         // Schlüssel zum Entkommen
    TELEPORTER,  // Easter Egg
    HINT,        // Hinweis-Notizen
    TOOL         // Taschenlampe, etc.
}
```

**Warum Enum statt String?**
- Type-Safety: Compiler prüft
- Keine Tippfehler: `WEAPON` vs "weapn"
- IDE-Unterstützung: Auto-Complete

#### GameState.java
```java
public enum GameState {
    PLAYING,                    // Spiel läuft
    PLAYER_WON_WEAPON,         // Sieg: Wächter besiegt
    PLAYER_WON_KEY,            // Sieg: Entkommen
    PLAYER_WON_TELEPORTER,     // Sieg: Teleportiert
    PLAYER_CAUGHT,             // Niederlage
    QUIT                       // Abgebrochen
}
```

**Vorteile:**
- Klarer State-Machine
- Einfache Erweiterung (z.B. PAUSE)
- Switch-Statement freundlich

---

## 🔄 Game Loop Mechanik

### Detaillierter Ablauf

```java
public void play() {
    printWelcome();
    
    while (gameState == GameState.PLAYING) {
        // ============ PHASE 1: RESET ============
        player.resetTurn();      // 2 neue Aktionen
        printRoomInfo();         // Zeige wo du bist
        
        // ============ PHASE 2: PLAYER ACTIONS ============
        while (player.getActionsRemaining() > 0 && 
               gameState == GameState.PLAYING) {
            
            System.out.println("📊 Aktionen übrig: " + 
                             player.getActionsRemaining());
            
            Command command = parser.getCommand();  // Lese Input
            processCommand(command);               // Führe aus
        }
        
        // ============ PHASE 3: GUARD MOVEMENT ============
        if (gameState == GameState.PLAYING) {
            guard.moveRandomly();    // Wächter bewegt sich
            checkGameOver();         // Prüfe Kollision
        }
    }
    
    // ============ PHASE 4: GAME END ============
    printGameEnd();
}
```

### State-Transitions

```
[PLAYING]
    ↓
    ├─→ use key → [PLAYER_WON_KEY]
    ├─→ use weapon (versteckt + guard da) → [PLAYER_WON_WEAPON]
    ├─→ use teleporter → [PLAYER_WON_TELEPORTER]
    ├─→ guard catches player → [PLAYER_CAUGHT]
    └─→ quit → [QUIT]
    
Alle außer PLAYING → printGameEnd() → EXIT
```

---

## 🎨 Design Patterns

### 1. Command Pattern
```java
// Parser liest Input
Command command = parser.getCommand();

// GuardGame interpretiert Command
processCommand(command);

// Vorteil: Separation Input ↔ Logic
```

### 2. State Pattern (GameState)
```java
enum GameState { PLAYING, WON, LOST, ... }

// Zentrale State-Verwaltung
if (gameState == PLAYING) { ... }
```

### 3. Observer Pattern (implizit)
```java
// Guard bewegt sich → Spieler hört es
guard.moveRandomly();
System.out.println("🚨 Du hörst Schritte...");
```

### 4. Factory Pattern (Raum-Erstellung)
```java
private void setupKueche(Room room) {
    room.addSearchableLocation("cabinets");
    room.addHidingSpot("hinter Theke");
    room.addItem(new Item("Messer", ...));
}
```

---

## 📊 Datenstrukturen

### HashMap für Räume
```java
Map<String, Room> rooms = new HashMap<>();
rooms.put("kueche", kuecheRoom);

// Warum HashMap?
// ✅ O(1) Zugriff
// ✅ Lesbare Keys
// ❌ Kein Index-Zugriff (egal, nicht nötig)
```

### List für Items
```java
List<Item> inventory = new ArrayList<>();

// Warum ArrayList?
// ✅ Dynamische Größe
// ✅ Iteration einfach
// ✅ add/remove O(1) am Ende
```

### List für searchableLocations
```java
List<String> searchableLocations = new ArrayList<>();

// Alternative: Set?
// Nein - Duplikate sind OK, Reihenfolge egal
```

---

## 🚀 Erweiterungsmöglichkeiten

### 1. Locked Doors

**Implementierung:**
```java
// In Room.java
private Map<String, Boolean> lockedExits;  // direction → locked?
private Map<String, ItemType> keyRequired; // direction → welcher Schlüssel?

public boolean canGo(String direction, Player player) {
    if (!lockedExits.get(direction)) return true;
    
    ItemType requiredKey = keyRequired.get(direction);
    return player.hasItemType(requiredKey);
}
```

**Verwendung:**
```java
tresorRaum.lockExit("west", ItemType.KEY);

// Beim "go":
if (!currentRoom.canGo(direction, player)) {
    System.out.println("🔒 Die Tür ist verschlossen!");
}
```

---

### 2. Guard-Tracking System

**Implementierung:**
```java
// In Guard.java
public int getDistanceToPlayer(Player player) {
    // BFS-Algorithmus zur Distanz-Berechnung
    return calculateDistance(currentRoom, player.getCurrentRoom());
}

// In GuardGame.java
int distance = guard.getDistanceToPlayer(player);
if (distance == 1) {
    System.out.println("🚨 Die Schritte sind SEHR NAH!");
} else if (distance <= 3) {
    System.out.println("👂 Du hörst entfernte Schritte...");
}
```

---

### 3. Schwierigkeitsgrade

**Implementierung:**
```java
public enum Difficulty {
    EASY,    // Guard bewegt sich langsam
    NORMAL,  // Standard
    HARD     // Guard bewegt sich schnell + tracking
}

// In GuardGame:
private Difficulty difficulty;

public void play() {
    while (...) {
        // Player phase
        
        // Guard phase
        if (difficulty == HARD) {
            guard.moveTowardsPlayer(player);  // Intelligente Bewegung!
        } else {
            guard.moveRandomly();
        }
    }
}
```

---

### 4. Save/Load System

**Implementierung:**
```java
// Serialisierbare GameState-Klasse
public class SaveState implements Serializable {
    private Room playerRoom;
    private Room guardRoom;
    private List<Item> inventory;
    private GameState gameState;
}

// Speichern:
public void saveGame(String filename) {
    SaveState state = new SaveState(player, guard, ...);
    ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(filename)
    );
    oos.writeObject(state);
}

// Laden:
public void loadGame(String filename) {
    ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(filename)
    );
    SaveState state = (SaveState) ois.readObject();
    // Restore game state
}
```

---

### 5. Multi-Guard System

**Implementierung:**
```java
// In GuardGame:
private List<Guard> guards;

public void play() {
    while (...) {
        // Player phase
        
        // Guard phase
        for (Guard guard : guards) {
            guard.moveRandomly();
            
            if (guard.isInSameRoom(player) && !player.isHidden()) {
                gameState = GameState.PLAYER_CAUGHT;
                System.out.println("💀 Wächter #" + guard.getId() + 
                                 " hat dich erwischt!");
                break;
            }
        }
    }
}
```

---

## 🧪 Testing-Strategie

### Unit Tests (Beispiele)

```java
@Test
public void testPlayerInventory() {
    Player player = new Player(startRoom);
    Item key = new Item("Schlüssel", "...", ItemType.KEY);
    
    assertFalse(player.hasKey());
    player.addToInventory(key);
    assertTrue(player.hasKey());
}

@Test
public void testPlayerCanOnlyMoveOnce() {
    Player player = new Player(startRoom);
    
    assertTrue(player.move(room1));   // First move: OK
    assertFalse(player.move(room2));  // Second move: BLOCKED
    
    player.resetTurn();
    assertTrue(player.move(room2));   // New turn: OK again
}

@Test
public void testGuardRandomMovement() {
    Guard guard = new Guard(startRoom);
    Room initialRoom = guard.getCurrentRoom();
    
    guard.moveRandomly();
    
    assertNotEquals(initialRoom, guard.getCurrentRoom());
}
```

---

## 📈 Performance-Analyse

### Komplexität

| Operation | Komplexität | Begründung |
|-----------|-------------|------------|
| `player.move()` | O(1) | Direkte Zuweisung |
| `guard.moveRandomly()` | O(n) | n = Anzahl Exits |
| `room.search()` | O(m) | m = Anzahl Items |
| `player.hasItemType()` | O(k) | k = Inventar-Größe |
| Game Loop Iteration | O(1) | Konstante Operationen |

**Gesamt-Komplexität pro Runde:**
O(n + m + k) ≈ O(1) bei kleinen Werten

---

## 🔐 Code-Qualität

### Naming Conventions
```java
// Klassen: PascalCase
public class GuardGame { }

// Methoden: camelCase
public void moveRandomly() { }

// Konstanten: UPPER_SNAKE_CASE
public static final int MAX_ACTIONS = 2;

// Variablen: camelCase
private boolean isHidden;
```

### Error Handling
```java
// Defensive Programming
if (!command.hasSecondWord()) {
    System.out.println("Wohin möchtest du gehen?");
    return;  // Early return
}

// Null-Checks
if (nextRoom == null) {
    System.out.println("❌ Da ist keine Tür!");
}
```

---

## 📚 Verwendete Java-Features

| Feature | Version | Verwendung |
|---------|---------|------------|
| Enums | Java 5+ | ItemType, GameState |
| Generics | Java 5+ | List<Item>, Map<String, Room> |
| Stream API | Java 8+ | Filter, Collect |
| Lambda | Java 8+ | `.filter(item -> ...)` |
| Try-with-resources | Java 7+ | (Für Save/Load) |

---

**Erstellt von:** Fabian & Julian  
**Projekt:** The Guard - BBW Apprenticeship  
**Datum:** Oktober 2025  
**Version:** 1.0
