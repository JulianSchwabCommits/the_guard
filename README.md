# the_guard




## Map
´´´
                [Dachboden]
                    |                    (nur mit schlüssel)
[Bibliothek] -- [Flur OG] -- [Büro Chef] -- [Tresor Raum]
     |               |                             | (nur mit schlüssel)
[Keller] --    [Flur EG] -- [Empfangshalle]  [Sicherheitsraum]
     |               |              |              |
[Heizungskeller]  [Küche] -- [Cafeteria] -- [Überwachungsraum]
     |              |              |
[Versteck] -- [Lagerraum] -- [Aussenbereich]
´´´

Empfangshalle (Startpunkt) - "die grosse Eingangshalle mit Rezeption"
Flur EG - "der Hauptflur im Erdgeschoss"
Flur OG - "der Flur im Obergeschoss"
Bibliothek - "eine grosse Bibliothek mit vielen Versteckmöglichkeiten"
Büro Chef - "das Büro des Chefs mit wertvollen Gegenständen"
Tresor Raum - "ein schwer gesicherter Raum mit dem Haupttresor"
Sicherheitsraum - "der Raum mit den Sicherheitssystemen"
Überwachungsraum - "Raum mit Überwachungsmonitoren"
Cafeteria - "die Mitarbeitercafeteria"
Küche - "die Küche mit verschiedenen Utensilien"
Lagerraum - "ein Lagerraum voller Kisten und Schränke"
Keller - "der dunkle Kellerbereich"
Heizungskeller - "der Heizungskeller mit vielen Rohren"
Versteck - "ein geheimes Versteck im Keller"
Dachboden - "der staubige Dachboden"
Aussenbereich - "der Bereich ausserhalb des Gebäudes"


# Java Classes:
classDiagram
    class GuardGame {
        -Parser parser
        -Room currentRoom
        -Player player
        -Guard guard
        -GameState gameState
        -Map~String, Room~ rooms
        +GuardGame()
        +play()
        +processCommand(Command)
        +checkWinCondition()
        +isGameOver()
    }

    class Player {
        -Room currentRoom
        -List~Item~ inventory
        -boolean isHidden
        -int actionsPerTurn
        -int actionsRemaining
        +Player(Room startRoom)
        +move(Room room)
        +hide()
        +search(String location)
        +useItem(Item item)
        +addToInventory(Item item)
        +hasWeapon()
        +hasKey()
        +hasTeleporter()
    }

    class Guard {
        -Room currentRoom
        -Random random
        +Guard(Room startRoom)
        +moveRandomly(List~Room~ rooms)
        +getCurrentRoom()
    }

    class Room {
        -String description
        -Map~String, Room~ exits
        -List~Item~ items
        -List~String~ searchableLocations
        -List~String~ hidingSpots
        +Room(String description)
        +setExits(Map exits)
        +addItem(Item item)
        +removeItem(Item item)
        +getHidingSpots()
        +search(String location)
        +canHideHere()
    }

    class Item {
        -String name
        -String description
        -ItemType type
        -boolean isVisible
        +Item(String name, String description, ItemType type)
        +use()
        +isUsable()
    }

    class ItemType {
        WEAPON
        KEY
        TELEPORTER
        HINT
        TOOL
    }

    class Command {
        -String commandWord
        -String secondWord
        +Command(String commandWord, String secondWord)
        +getCommandWord()
        +getSecondWord()
        +isUnknown()
        +hasSecondWord()
    }

    class CommandWords {
        -List~String~ validCommands
        +CommandWords()
        +isCommand(String word)
        +showAll()
        +getValidCommands()
    }

    class Parser {
        -CommandWords commandWords
        -InputStream inputStream
        +Parser(InputStream inputStream)
        +getCommand()
        +showCommands()
    }

    class GameState {
        PLAYING
        PLAYER_WON_WEAPON
        PLAYER_WON_KEY
        PLAYER_WON_TELEPORTER
        PLAYER_CAUGHT
        QUIT
    }

    GuardGame --> Player
    GuardGame --> Guard
    GuardGame --> Room
    GuardGame --> Parser
    GuardGame --> GameState
    Player --> Room
    Player --> Item
    Guard --> Room
    Room --> Item
    Item --> ItemType
    Parser --> Command
    Parser --> CommandWords

## New commands:
private List<String> validCommands = Arrays.asList(
    "go", "quit", "help", 
    "search", "hide", "inventory", "use", 
    "look", "next", "attack"
);