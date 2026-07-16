package com.pluralsight;

import java.time.*;
import java.util.*;

public class HomeScreen {

    // blue and reset for the color scheme
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public HomeScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        // keep the home screen running until user exits
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println(BLUE + "┌───────────────────────────────────┐" + RESET);
            System.out.println(BLUE + "│" + RESET + "            HOME SCREEN            " + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────┤" + RESET);
            System.out.println(BLUE + "│  D)" + RESET + "  Add Revenue                  " + BLUE + "│" + RESET);
            System.out.println(BLUE + "│  P)" + RESET + "  Add Expense                  " + BLUE + "│" + RESET);
            System.out.println(BLUE + "│  L)" + RESET + "  Financial Ledger             " + BLUE + "│" + RESET);
            System.out.println(BLUE + "│  X)" + RESET + "  Exit                         " + BLUE + "│" + RESET);
            System.out.println(BLUE + "└───────────────────────────────────┘" + RESET);
            System.out.print("\nEnter option: ");

            // toUpperCase so it works whether user types d or D
            String input = scanner.nextLine().toUpperCase();

            if (input.equals("D")) {
                addRevenue();
            } else if (input.equals("P")) {
                addExpense();
            } else if (input.equals("L")) {
                // create ledger screen and pass the same scanner and transactions list
                LedgerScreen ledgerScreen = new LedgerScreen(scanner, transactions);
                ledgerScreen.display();
            } else if (input.equals("X")) {
                System.out.println("See ya! " + BLUE + "Go M's!!" + RESET);
                running = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    // keeps asking for a value until the user enters something other than blank
    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field can't be blank. Please enter a value.");
        }
    }

    // keeps asking for Amount until the user enters a valid number
    // instead of crashing the whole app on bad input
    private double readAmount() {
        while (true) {
            System.out.print("Amount: ");
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number. Please enter an amount like 250 or 99.99.");
            }
        }
    }

    public void addRevenue() {
        System.out.println("\n" + BLUE + "----- ADD REVENUE -----" + RESET);

        String description = readRequiredText("Description: ");

        String vendor = readRequiredText("Payee: ");

        double amount = readAmount();

        // make sure revenue is always positive
        if (amount < 0) amount = Math.abs(amount);

        // use todays date and time for the transaction
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);

        // create the transaction, add to the in memory list, and save to CSV
        Transaction t = new Transaction(date, time, description, vendor, amount);
        transactions.add(t);
        FileManager.saveTransaction(t);
        System.out.println("Revenue of $" + String.format("%.2f", amount) + " saved successfully.");
    }

    public void addExpense() {
        System.out.println("\n" + BLUE + "----- ADD EXPENSE -----" + RESET);

        String description = readRequiredText("Description: ");

        String vendor = readRequiredText("Payee: ");

        double amount = readAmount();

        // make sure expenses are always stored as negative
        if (amount > 0) amount = -amount;

        // use todays date and time for the transaction
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);

        // create the transaction, add to the in memory list, and save to CSV
        Transaction t = new Transaction(date, time, description, vendor, amount);
        transactions.add(t);
        FileManager.saveTransaction(t);
        System.out.println("Expense of $" + String.format("%.2f", Math.abs(amount)) + " saved successfully.");
    }
}