package com.guard;

import java.util.Scanner;

/**
 * Main game logic for The Guard room game
 */
public class Game {
    private Scanner scanner;
    private boolean running;
    
    public Game() {
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        System.out.println("Welcome to The Guard!");
        System.out.println("You are in a mysterious room...");
        System.out.println("Type 'help' for commands or 'quit' to exit.");
        System.out.println();
        
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "help":
                    showHelp();
                    break;
                case "look":
                    look();
                    break;
                case "quit":
                case "exit":
                    quit();
                    break;
                default:
                    System.out.println("Unknown command: " + input);
                    System.out.println("Type 'help' for available commands.");
                    break;
            }
        }
        
        scanner.close();
    }
    
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  help  - Show this help message");
        System.out.println("  look  - Look around the room");
        System.out.println("  quit  - Exit the game");
    }
    
    private void look() {
        System.out.println("You are in a dimly lit room.");
        System.out.println("There is a guard standing by the door.");
        System.out.println("The guard watches you carefully...");
    }
    
    private void quit() {
        System.out.println("Thanks for playing The Guard!");
        running = false;
    }
}