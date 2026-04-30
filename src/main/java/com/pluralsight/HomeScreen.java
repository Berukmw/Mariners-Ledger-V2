package com.pluralsight;

import java.util.*;

public class HomeScreen {

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public HomeScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        String input = "";

        while (!input.equals("X")) {
            System.out.println("\n----- HOME SCREEN -----");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("\nEnter option: ");

            input = scanner.nextLine().toUpperCase();   // makes characters uppercase

            if (input.equals("D")) {
                System.out.println("Add Deposit coming soon...");
            } else if (input.equals("P")) {
                System.out.println("Make Payment coming soon...");
            } else if (input.equals("L")) {
                System.out.println("Ledger coming soon...");
            } else if (input.equals("X")) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}
