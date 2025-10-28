# The Guard 🎮
*Ein textbasiertes Adventure-Spiel*  
**Von Fabian und Julian**  
**Oktober 2025**

---

## 📖 Inhaltsverzeichnis

1. [Spielidee](#spielidee)
2. [Installation & Start](#installation--start)
3. [Spielmechanik](#spielmechanik)
4. [Spielbefehle](#spielbefehle)
5. [Karte & Räume](#karte--räume)
6. [Lösungswege](#lösungswege)
7. [Technische Dokumentation](#technische-dokumentation)
8. [Entwicklung & Architektur](#entwicklung--architektur)

---

## 🎯 Spielidee

**The Guard** ist ein textbasiertes Adventure-Spiel, bei dem du aus einem bewachten Gebäude entkommen musst. Ein KI-gesteuerter Wächter patrouilliert zufällig durch die Räume, während du versuchst einen Ausweg zu finden.

### Drei Wege zum Sieg:
- 🔑 **Schlüssel finden** und durch den Hauptausgang entkommen
- ⚔️ **Waffe finden** und den Wächter überwältigen
- ✨ **Teleporter entdecken** und magisch fliehen (Easter Egg)

### Game Over Bedingung:
- 💀 Der Wächter erwischt dich, wenn ihr im selben Raum seid und du **nicht versteckt** bist!

---

## 🚀 Installation & Start

### Voraussetzungen
- Java 8 oder höher installiert
- Terminal/PowerShell

### Kompilieren
```bash
cd the_guard
javac -d target/classes src/main/java/ch/bbw/zork/*.java
```

### Spielen
```bash
cd target/classes
java ch.bbw.zork.GuardGame
```

**Alternative (mit Maven):**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="ch.bbw.zork.GuardGame"
```

---

## 🎮 Spielmechanik

### Aktionssystem
- Du hast **2 Aktionen pro Runde**
- Jede Aktion (go, search, hide) verbraucht eine Aktion
- Du kannst nur **1x pro Runde** den Raum wechseln
- Nach deinen 2 Aktionen bewegt sich der Wächter automatisch

### Versteck-Mechanik
- Nur in Räumen mit Verstecken möglich
- Versteckte Spieler werden vom Wächter nicht gesehen
- Bewegung beendet das Versteck automatisch
- Mit `next` kannst du versteckt eine Runde überspringen

### Such-System
- Items sind anfangs unsichtbar/versteckt
- Durchsuche Orte mit `search <ort>`
- Gefundene Items werden automatisch aufgehoben
- Jeder Raum hat verschiedene durchsuchbare Orte

---

## 🎯 Spielbefehle

| Befehl | Beschreibung | Beispiel |
|--------|--------------|----------|
| `help` | Zeigt die Hilfe | `help` |
| `go <richtung>` | Bewege dich | `go north` |
| `search <ort>` | Durchsuche einen Ort | `search desk` |
| `hide` | Verstecke dich | `hide` |
| `inventory` | Zeige Inventar | `inventory` |
| `use <item>` | Benutze Gegenstand | `use key` |
| `next` | Nächste Runde (versteckt) | `next` |
| `quit` | Spiel beenden | `quit` |

### Richtungen
- `north` (Norden)
- `south` (Süden)
- `east` (Osten)
- `west` (Westen)

---

## 🗺️ Karte & Räume

```
                    [Dachboden]
                         │
                         │
    [Bibliothek] ── [Flur OG] ── [Büro Chef] ── [Tresor Raum]
         │               │              │
         │               │              │
    [Keller] ─── [Flur EG] ── [Empfangshalle] [Sicherheitsraum]
         │          │              │              │
         │          │              │              │
[Heizungskeller] [Küche] ─── [Cafeteria] ── [Überwachungsraum]
         │          │              │
         │          │              │
    [Versteck] [Lagerraum] ── [Aussenbereich]
```

### Wichtige Räume

| Raum | Suchbare Orte | Verstecke | Items |
|------|---------------|-----------|-------|
| **Empfangshalle** | desk, drawer | hinter Rezeption | Hinweis-Notiz |
| **Büro Chef** | desk, safe | unter Schreibtisch | 🔑 **Schlüssel** |
| **Küche** | cabinets, fridge | hinter Theke | 🔪 Messer (Waffe) |
| **Versteck** | chest | im Raum selbst | ✨ **Teleporter** |
| **Bibliothek** | shelves, books | hinter Regalen | - |
| **Keller** | shelves | hinter Regalen | Taschenlampe |
| **Tresor Raum** | vault | - | 🔫 Betäubungspistole |
| **Dachboden** | boxes, chest | hinter Möbeln | - |
| **Lagerraum** | boxes, crates | hinter Kisten | - |

---

## 🏆 Lösungswege

### 🎯 Route 1: Schlüssel-Flucht (EINFACH - 5 Minuten)

**Ziel:** Finde den Schlüssel im Büro des Chefs und entkommen

**Schritte:**
1. **Start in Empfangshalle**
   ```
   search desk          → Findet Hinweis: "Schlüssel im Büro"
   search drawer
   ```

2. **Zum Büro des Chefs**
   ```
   go north             → Flur OG
   go east              → Büro Chef
   ```

3. **Schlüssel finden**
   ```
   search desk          → 🔑 SCHLÜSSEL gefunden!
   inventory            → Prüfen ob vorhanden
   ```

4. **Zum Ausgang**
   ```
   go west              → Flur OG
   go south             → Empfangshalle
   go east              → Cafeteria
   go south             → Aussenbereich
   ```

5. **Entkommen!**
   ```
   use schlüssel        → 🎉 GEWONNEN!
   ```

**Tipp:** Wenn der Wächter kommt: `hide` → `next` → weitermachen

---

### ⚔️ Route 2: Wächter besiegen (MITTEL - 10 Minuten)

**Ziel:** Finde eine Waffe und eliminiere den Wächter

**Schritte:**
1. **Zur Küche navigieren**
   ```
   go west              → Flur EG
   go south             → Keller
   go south             → Heizungskeller
   go east              → Küche
   ```

2. **Waffe besorgen**
   ```
   search cabinets      → 🔪 Messer gefunden!
   search fridge        → Weitere Items
   ```

3. **Verstecken und warten**
   ```
   hide                 → Versteckt hinter der Theke
   next                 → Warte auf Wächter...
   next                 → Noch warten...
   ```

4. **Angriff!** (wenn Wächter im Raum)
   ```
   use messer           → ⚔️ Wächter besiegt!
   ```

**Wichtig:** 
- Du MUSST versteckt sein zum Angreifen
- Der Wächter MUSS im selben Raum sein
- Sonst: "Du musst versteckt sein!" oder "Wächter ist nicht hier!"

---

### ✨ Route 3: Teleporter Easter Egg (SCHWER - 15 Minuten)

**Ziel:** Finde das geheime Versteck und benutze den Teleporter

**Schritte:**
1. **Zum Heizungskeller**
   ```
   go west              → Flur EG
   go south             → Keller
   go south             → Heizungskeller
   ```

2. **Geheimes Versteck finden**
   ```
   go west              → 🎉 Versteck entdeckt!
   ```

3. **Teleporter finden**
   ```
   search chest         → ✨ TELEPORTER gefunden!
   inventory            → Prüfen
   ```

4. **Teleportieren**
   ```
   use teleporter       → 🌀 WEGGEBEAMT!
   ```

**Fun Fact:** Dies ist der geheime Weg - schwer zu finden aber cool! 😎

---

## 💡 Überlebens-Tipps

### Wenn der Wächter kommt:
```
hide                    → SOFORT verstecken!
next                    → Warte bis er weitergeht
```

### Sichere Erkundungs-Strategie:
1. Raum betreten
2. Sofort `hide` (falls möglich)
3. Alle Orte `search`
4. Weitergehen oder warten

### Effizienz:
- Nutze beide Aktionen pro Runde voll aus
- Plane deine Route im Voraus
- Merke dir wo Verstecke sind

---

## 🔧 Technische Dokumentation

### Projekt-Struktur
```
the_guard/
├── src/main/java/ch/bbw/zork/
│   ├── GuardGame.java          → Hauptspiel-Logik
│   ├── Player.java             → Spieler-Klasse
│   ├── Guard.java              → KI-Wächter
│   ├── Room.java               → Raum-System
│   ├── Item.java               → Item-Objekte
│   ├── ItemType.java           → Item-Kategorien
│   ├── GameState.java          → Spielzustände
│   ├── Command.java            → Befehl-Verarbeitung
│   ├── CommandWords.java       → Gültige Befehle
│   ├── Parser.java             → Input-Parser
│   ├── Game.java               → Original Zork
│   └── Zork2.java              → Original Main
├── target/classes/             → Kompilierte .class Dateien
├── pom.xml                     → Maven Konfiguration
└── README.md                   → Diese Datei
```

### Technologie-Stack
- **Sprache:** Java 21 (kompatibel mit Java 8+)
- **Build-Tool:** Maven 3.x
- **Design Pattern:** MVC (Model-View-Controller)
- **Basis:** Zork Text Adventure Framework

---

## 🏗️ Entwicklung & Architektur

### Klassen-Übersicht

#### `GuardGame.java` - Hauptspiel
- Orchestriert das gesamte Spiel
- Erstellt 16 Räume mit Items und Verstecken
- Implementiert Game Loop (Spieler-Phase → Guard-Phase)
- Prüft Gewinn-/Verlust-Bedingungen

#### `Player.java` - Spieler-Logik
- Inventar-Verwaltung (List<Item>)
- Aktionssystem (2 pro Runde)
- Versteck-Mechanik (`isHidden`)
- Movement-Beschränkung (1x pro Runde)

#### `Guard.java` - KI-Gegner
- Zufällige Bewegung zwischen Räumen
- Collision-Detection mit Spieler
- Unabhängige Bewegungslogik

#### `Room.java` - Raum-System
- Durchsuchbare Orte (List<String>)
- Versteck-Plätze (List<String>)
- Item-Container (List<Item>)
- Exit-Management (HashMap<String, Room>)

#### `Item.java` - Gegenstände
- Name, Beschreibung, Typ
- Sichtbarkeits-Flag (versteckt/sichtbar)
- Verwendbarkeits-Check

#### Enums
- `ItemType`: WEAPON, KEY, TELEPORTER, HINT, TOOL
- `GameState`: PLAYING, PLAYER_WON_WEAPON, PLAYER_WON_KEY, PLAYER_WON_TELEPORTER, PLAYER_CAUGHT, QUIT

### Game Loop Architektur

```java
while (gameState == PLAYING) {
    // PHASE 1: Spieler-Aktionen
    player.resetTurn();  // 2 neue Aktionen
    
    while (player.getActionsRemaining() > 0) {
        command = parser.getCommand();
        processCommand(command);  // Führe Aktion aus
    }
    
    // PHASE 2: Guard-Bewegung
    guard.moveRandomly();
    
    // PHASE 3: Game Over Check
    checkGameOver();
}
```

### Design-Entscheidungen

**Warum 2 Aktionen?**
- Balance zwischen Exploration und Spannung
- Spieler muss strategisch planen
- Verhindert zu schnelles Durchspielen

**Warum nur 1x bewegen?**
- Verhindert "Raum-Hopping" zur Flucht
- Erhöht Schwierigkeit
- Realistischer

**Warum Zufalls-Guard?**
- Unvorhersehbarkeit = Spannung
- Jedes Spiel ist anders
- Nicht zu schwer (kein perfektes Tracking)

### Erweiterungsmöglichkeiten

🔮 **Zukünftige Features:**
- [ ] Locked Doors (Türen mit Schlüsseln)
- [ ] Sound-Effekte (ASCII-Art)
- [ ] Schwierigkeitsgrade
- [ ] Guard-Tracking (Spieler hört Schritte)
- [ ] Mehr Items (Dietrich, Rauchbomben)
- [ ] Save/Load System
- [ ] Mehrere Wächter
- [ ] Timer/Zeitlimit

---

## 👥 Credits

**Entwickler:** Fabian & Julian  
**Projekt:** Apprenticeship - BBW  
**Basis:** Zork Framework (Michael Kolling)  
**Version:** 1.0  
**Datum:** Oktober 2025

---

## 📝 Lizenz

Bildungsprojekt - BBW Apprenticeship

---

## 🎮 Viel Erfolg!

**Pro-Tipp:** Starte mit der Schlüssel-Route - sie ist am einfachsten!

**Noch Fragen?** Check die Spieler-Anleitung: `WALKTHROUGH.md`
