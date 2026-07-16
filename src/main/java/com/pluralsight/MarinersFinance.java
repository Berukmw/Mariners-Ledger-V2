package com.pluralsight;

import java.util.*;

public class MarinersFinance {

    // blue and reset for the color scheme
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {


        ConsoleWindow window = new ConsoleWindow();
        window.open();

        // welcome banner
        System.out.println(BLUE + "╔══════════════════════════════════════════╗");
        System.out.println("║                                          ║");
        System.out.println("║      SEATTLE MARINERS 2026 FINANCES      ║");
        System.out.println("║                                          ║");
        System.out.println("╚══════════════════════════════════════════╝" + RESET);
        System.out.println("\nLoading... " + BLUE + "Go M's!" + RESET + "\n");

        // load all transactions from the CSV once so we dont have to keep reading the file
        ArrayList<Transaction> transactions = FileManager.loadTransactions();

        // one scanner for the whole app, passing it down to every screen
        Scanner scanner = new Scanner(System.in);

        // kick off the home screen, everything runs from here
        HomeScreen homeScreen = new HomeScreen(scanner, transactions);
        homeScreen.display();

        // close it out when the user exits
        scanner.close();
    }
}