package com.pluralsight;

import java.time.*;
import java.util.*;

public class ReportsScreen {

    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public ReportsScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

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

    public void monthToDate() {
        LocalDate now = LocalDate.now();
        System.out.println("\n" + BLUE + "--- MONTH TO DATE ---" + RESET);

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }

    public void previousMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        System.out.println("\n" + BLUE + "--- PREVIOUS MONTH ---" + RESET);

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == lastMonth.getMonth() && date.getYear() == lastMonth.getYear()) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }

    public void yearToDate() {
        int currentYear = LocalDate.now().getYear();
        System.out.println("\n" + BLUE + "--- YEAR TO DATE ---" + RESET);

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == currentYear) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }

    public void previousYear() {
        int lastYear = LocalDate.now().getYear() - 1;
        System.out.println("\n" + BLUE + "--- PREVIOUS YEAR ---" + RESET);

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == lastYear) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }

    public void searchByPayee() {
        System.out.print("Enter payee name: ");
        String vendor = scanner.nextLine().toLowerCase();
        System.out.println("\n" + BLUE + "--- PAYEE SEARCH: " + vendor + " ---" + RESET);

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getVendor().toLowerCase().contains(vendor)) {
                System.out.printf("%-12s | %-8s | %-40s | %-20s | %,14.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }
}