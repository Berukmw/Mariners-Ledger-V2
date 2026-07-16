package com.pluralsight;

import java.time.*;
import java.util.*;

public class ReportsScreen {

    // color scheme - blue for headers/menus, green for revenue, red for expenses
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
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

    public void monthToDate() {
        // grab todays date to compare against transactions
        LocalDate now = LocalDate.now();
        System.out.println("\n" + BLUE + "--- MONTH TO DATE: " + now.getMonth() + " " + now.getYear() + " ---" + RESET);
        System.out.println("(amounts shown as monthly cost)");
        printHeader();

        // loop through every transaction and only show ones from this month
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                // divide by 12 to show monthly cost instead of full annual amount
                double monthlyAmount = t.getAmount() / 12;
                String color = monthlyAmount >= 0 ? GREEN : RED;
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + color + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), monthlyAmount);
                total += monthlyAmount;
            }
        }
        printTotal(total);
    }

    public void previousMonth() {
        // go back one month from today
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        System.out.println("\n" + BLUE + "--- PREVIOUS MONTH: " + lastMonth.getMonth() + " " + lastMonth.getYear() + " ---" + RESET);
        System.out.println("(amounts shown as monthly cost)");
        printHeader();

        // loop through and only show transactions from last month
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == lastMonth.getMonth() && date.getYear() == lastMonth.getYear()) {
                // divide by 12 to show monthly cost instead of full annual amount
                double monthlyAmount = t.getAmount() / 12;
                String color = monthlyAmount >= 0 ? GREEN : RED;
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + color + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), monthlyAmount);
                total += monthlyAmount;
            }
        }
        printTotal(total);
    }

    public void yearToDate() {
        // grab the current year to filter against
        int currentYear = LocalDate.now().getYear();
        System.out.println("\n" + BLUE + "--- YEAR TO DATE: " + currentYear + " ---" + RESET);
        printHeader();

        // loop through and show everything from this year
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == currentYear) {
                String color = t.getAmount() >= 0 ? GREEN : RED;
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + color + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                total += t.getAmount();
            }
        }
        printTotal(total);
    }

    public void previousYear() {
        // subtract 1 from current year to get last year
        int lastYear = LocalDate.now().getYear() - 1;
        System.out.println("\n" + BLUE + "--- PREVIOUS YEAR: " + lastYear + " ---" + RESET);
        printHeader();

        // loop through and only show transactions from last year
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == lastYear) {
                String color = t.getAmount() >= 0 ? GREEN : RED;
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + color + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                total += t.getAmount();
            }
        }
        printTotal(total);
    }

    public void searchByPayee() {
        System.out.print("Enter payee name: ");
        // lowercase so the search works regardless of how user types it
        String vendor = scanner.nextLine().toLowerCase();
        System.out.println("\n" + BLUE + "--- PAYEE SEARCH: " + vendor + " ---" + RESET);
        printHeader();

        // loop through and check if the payee name contains what the user typed
        double total = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getVendor().toLowerCase().contains(vendor)) {
                String color = t.getAmount() >= 0 ? GREEN : RED;
                System.out.printf("%-12s | %-8s | %-40s | %-20s | " + color + "%,14.2f" + RESET + "%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                total += t.getAmount();
            }
        }
        printTotal(total);
    }
}