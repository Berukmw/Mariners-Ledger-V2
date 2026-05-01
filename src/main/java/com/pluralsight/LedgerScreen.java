package com.pluralsight;

import java.util.*;

public class LedgerScreen {

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public LedgerScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        boolean running = true;

        while (running) {
            System.out.println("\n----- LEDGER SCREEN -----");
            System.out.println("A) All Entries");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("\nEnter option: ");

            String input = scanner.nextLine().toUpperCase();

            if (input.equals("A")) {
                displayAll();
            } else if (input.equals("D")) {
                displayDeposits();
            } else if (input.equals("P")) {
                displayPayments();
            } else if (input.equals("R")) {
                ReportsScreen reportsScreen = new ReportsScreen(scanner, transactions);
                reportsScreen.display();
            } else if (input.equals("H")) {
                running = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    public void displayAll() {
        System.out.println("\n--- ALL ENTRIES ---");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
        }
    }

    public void displayDeposits() {
        System.out.println("\n--- DEPOSITS ---");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }

    public void displayPayments() {
        System.out.println("\n--- PAYMENTS ---");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }
}