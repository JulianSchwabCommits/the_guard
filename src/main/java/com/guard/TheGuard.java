package com.guard;

import java.util.Scanner;

/**
 * The Guard - A simple room game
 * Entry point for the application
 */
public class TheGuard {
    
    public static void main(String[] args) {
        System.out.println("=== THE GUARD ===");
        System.out.println("Room Game Starting...");
        System.out.println();
        
        Game game = new Game();
        game.start();
    }
}