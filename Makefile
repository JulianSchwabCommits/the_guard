# Makefile for The Guard game

# Default target
.PHONY: all
all: compile

# Check Java installation
.PHONY: check-java
check-java:
	@echo "Checking Java installation..."
	@java -version

# Compile the Java source files
.PHONY: compile
compile:
	@echo "Compiling Java source files..."
	@mkdir -p out
	@javac -d out src/main/java/com/guard/*.java
	@echo "Compilation complete."

# Run the game
.PHONY: run
run: compile
	@echo "Starting The Guard..."
	@java -cp out com.guard.TheGuard

# Clean compiled files
.PHONY: clean
clean:
	@echo "Cleaning compiled files..."
	@rm -rf out

# Test compilation and basic functionality
.PHONY: test
test: compile
	@echo "Testing basic functionality..."
	@echo "quit" | timeout 5s java -cp out com.guard.TheGuard > /dev/null && echo "✅ Test passed" || echo "❌ Test failed"

# Help target
.PHONY: help
help:
	@echo "Available targets:"
	@echo "  compile    - Compile Java source files"
	@echo "  run        - Compile and run the game"
	@echo "  clean      - Remove compiled files"
	@echo "  test       - Test basic functionality"
	@echo "  check-java - Check Java installation"
	@echo "  help       - Show this help message"