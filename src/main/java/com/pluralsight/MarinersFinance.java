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
        System.out.println(BLUE + "─────────────────────────────────────\n" +
                "                  N                  \n" +
                "                  |                  \n" +
                "          W ------+------ E          \n" +
                "                  |                  \n" +
                "                  S                  \n" +
                "\n" +
                "   S E A T T L E   M A R I N E R S   \n" +
                "             L E D G E R             \n" +
                "─────────────────────────────────────" + RESET);
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