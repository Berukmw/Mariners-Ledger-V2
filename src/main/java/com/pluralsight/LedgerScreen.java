package com.pluralsight;

import java.util.*;


public class LedgerScreen {

    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public LedgerScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        boolean running = true;

        while (running) {
            System.out.println("\n" + BLUE + "----- FINANCIAL LEDGER -----" + RESET);
            System.out.println(BLUE + "A)" + RESET + " All Entries");
            System.out.println(BLUE + "D)" + RESET + " Revenue");
            System.out.println(BLUE + "P)" + RESET + " Expenses");
            System.out.println(BLUE + "R)" + RESET + " Reports");
            System.out.println(BLUE + "H)" + RESET + " Home");
            System.out.print("\nEnter option: ");

            String input = scanner.nextLine().toUpperCase();

            if (input.equals("A")) {
                displayAll();
            } else if (input.equals("D")) {
                displayRevenue();
            } else if (input.equals("P")) {
                displayExpenses();
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
        System.out.println("\n" + BLUE + "--- ALL ENTRIES ---" + RESET);
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());        }
    }

    public void displayRevenue() {
        System.out.println("\n" + BLUE + "--- REVENUE ---" + RESET);
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }

    public void displayExpenses() {
        System.out.println("\n" + BLUE + "--- EXPENSES ---" + RESET);
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }
}