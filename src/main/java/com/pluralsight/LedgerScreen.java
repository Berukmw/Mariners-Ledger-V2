package com.pluralsight;

import java.util.*;

public class LedgerScreen {

    // color scheme - blue for headers/menus, green for revenue, red for expenses
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public LedgerScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        // keep the ledger screen running until user goes back home
        boolean running = true;

        while (running) {
            System.out.println("\n" + BLUE + "----- FINANCIAL LEDGER -----" + RESET);
            System.out.println(BLUE + "A)" + RESET + " All Entries");
            System.out.println(BLUE + "D)" + RESET + " Revenue");
            System.out.println(BLUE + "P)" + RESET + " Expenses");
            System.out.println(BLUE + "R)" + RESET + " Reports");
            System.out.println(BLUE + "H)" + RESET + " Home");
            System.out.print("\nEnter option: ");

            // toUpperCase so it works whether user types a or A
            String input = scanner.nextLine().toUpperCase();

            if (input.equals("A")) {
                displayAll();
            } else if (input.equals("D")) {
                displayRevenue();
            } else if (input.equals("P")) {
                displayExpenses();
            } else if (input.equals("R")) {
                // create reports screen and pass the same scanner and transactions list
                ReportsScreen reportsScreen = new ReportsScreen(scanner, transactions);
                reportsScreen.display();
            } else if (input.equals("H")) {
                running = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    // reusable header so we dont repeat this in every method
    public void printHeader() {
        System.out.println("-".repeat(105));
        System.out.printf(BLUE + "%-12s | %-8s | %-40s | %-20s | %14s" + RESET + "%n",
                "DATE", "TIME", "DESCRIPTION", "PAYEE", "AMOUNT");
        System.out.println("-".repeat(105));
    }

    // reusable total line - green if net positive, red if net negative
    public void printTotal(double total) {
        String color = total >= 0 ? GREEN : RED;
        System.out.printf(BLUE + "%-85s" + RESET + " | " + color + "%,14.2f" + RESET + "%n", "TOTAL", total);
        System.out.println("-".repeat(105));
    }

    public void displayAll() {
        System.out.println("\n" + BLUE + "--- ALL ENTRIES ---" + RESET);
        printHeader();

        // loop through every transaction and print it, green for revenue, red for expenses
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            String color = t.getAmount() >= 0 ? GREEN : RED;
            System.out.printf("%-12s | %-8s | %-40s | %-20s | " + color + "%,14.2f" + RESET + "%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            total += t.getAmount();
        }
        printTotal(total);
    }

    public void displayRevenue() {
        System.out.println("\n" + BLUE + "--- REVENUE ---" + RESET);
        printHeader();

        // loop through and only print transactions with a positive amount
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getAmount() > 0) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + GREEN + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                total += t.getAmount();
            }
        }
        printTotal(total);
    }

    public void displayExpenses() {
        System.out.println("\n" + BLUE + "--- EXPENSES ---" + RESET);
        printHeader();

        // loop through and only print transactions with a negative amount
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.getAmount() < 0) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + RED + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                total += t.getAmount();
            }
        }
        printTotal(total);
    }
}