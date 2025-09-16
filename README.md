# The Guard

A simple text-based room game for Apprenticeship.

## Prerequisites

- Java 8 or higher must be installed
- The application automatically checks for Java installation before running

## How to Run

### On Linux/macOS:
```bash
./run.sh
```

### On Windows:
```batch
run.bat
```

### Manual compilation and execution:
```bash
# Compile
javac -d out src/main/java/com/guard/*.java

# Run
java -cp out com.guard.TheGuard
```

## Game Commands

- `help` - Show available commands
- `look` - Look around the room
- `quit` or `exit` - Exit the game

## Project Structure

```
the_guard/
├── src/main/java/com/guard/
│   ├── TheGuard.java      # Main entry point
│   └── Game.java          # Game logic
├── run.sh                 # Linux/macOS runner script
├── run.bat                # Windows runner script
└── README.md              # This file
```

The application includes automatic Java version checking and will provide installation instructions if Java is not found.
