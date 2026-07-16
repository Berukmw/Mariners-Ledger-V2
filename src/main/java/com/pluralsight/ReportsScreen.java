package com.pluralsight;

import java.time.*;
import java.util.*;

public class ReportsScreen {

    // blue and reset for the color scheme
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public ReportsScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        // keep the reports screen running until user goes back
        boolean running = true;

        while (running) {
            System.out.println("\n" + BLUE + "----- REPORTS SCREEN -----" + RESET);
            System.out.println(BLUE + "1)" + RESET + " Month To Date");
            System.out.println(BLUE + "2)" + RESET + " Previous Month");
            System.out.println(BLUE + "3)" + RESET + " Year To Date");
            System.out.println(BLUE + "4)" + RESET + " Previous Year");
            System.out.println(BLUE + "5)" + RESET + " Search by Payee");
            System.out.println(BLUE + "0)" + RESET + " Back");
            System.out.print("\nEnter option: ");

            String input = scanner.nextLine();

            if (input.equals("1")) {
                monthToDate();
            } else if (input.equals("2")) {
                previousMonth();
            } else if (input.equals("3")) {
                yearToDate();
            } else if (input.equals("4")) {
                previousYear();
            } else if (input.equals("5")) {
                searchByPayee();
            } else if (input.equals("0")) {
                running = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    // reusable header so we dont repeat this in every method
    public void printHeader() {
        System.out.println("-".repeat(110));
        System.out.printf(BLUE + "%-12s | %-8s | %-40s | %-24s | %14s" + RESET + "%n",
                "DATE", "TIME", "DESCRIPTION", "PAYEE", "AMOUNT");
        System.out.println("-".repeat(110));
    }

    public void monthToDate() {
        // grab todays date to compare against transactions
        LocalDate now = LocalDate.now();
        System.out.println("\n" + BLUE + "--- MONTH TO DATE: " + now.getMonth() + " " + now.getYear() + " ---" + RESET);
        System.out.println("(amounts shown as monthly cost)");
        printHeader();

        // loop through every transaction and only show ones from this month
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                // divide by 12 to show monthly cost instead of full annual amount
                System.out.printf("%-12s | %-8s | %-40s | %-24s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount() / 12);
            }
        }
        System.out.println("-".repeat(110));
    }

    public void previousMonth() {
        // go back one month from today
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        System.out.println("\n" + BLUE + "--- PREVIOUS MONTH: " + lastMonth.getMonth() + " " + lastMonth.getYear() + " ---" + RESET);
        System.out.println("(amounts shown as monthly cost)");
        printHeader();

        // loop through and only show transactions from last month
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == lastMonth.getMonth() && date.getYear() == lastMonth.getYear()) {
                // divide by 12 to show monthly cost instead of full annual amount
                System.out.printf("%-12s | %-8s | %-40s | %-24s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount() / 12);
            }
        }
        System.out.println("-".repeat(110));
    }

    public void yearToDate() {
        // grab the current year to filter against
        int currentYear = LocalDate.now().getYear();
        System.out.println("\n" + BLUE + "--- YEAR TO DATE: " + currentYear + " ---" + RESET);
        printHeader();

        // loop through and show everything from this year
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == currentYear) {
                System.out.printf("%-12s | %-8s | %-40s | %-24s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
        System.out.println("-".repeat(110));
    }

    public void previousYear() {
        // subtract 1 from current year to get last year
        int lastYear = LocalDate.now().getYear() - 1;
        System.out.println("\n" + BLUE + "--- PREVIOUS YEAR: " + lastYear + " ---" + RESET);
        printHeader();

        // loop through and only show transactions from last year
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == lastYear) {
                System.out.printf("%-12s | %-8s | %-40s | %-24s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
        System.out.println("-".repeat(110));
    }

    public void searchByPayee() {
        System.out.print("Enter payee name: ");
        // lowercase so the search works regardless of how user types it
        String vendor = scanner.nextLine().toLowerCase();
        System.out.println("\n" + BLUE + "--- PAYEE SEARCH: " + vendor + " ---" + RESET);
        printHeader();

        // loop through and check if the payee name contains what the user typed
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getVendor().toLowerCase().contains(vendor)) {
                System.out.printf("%-12s | %-8s | %-40s | %-24s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
        System.out.println("-".repeat(110));
    }
}