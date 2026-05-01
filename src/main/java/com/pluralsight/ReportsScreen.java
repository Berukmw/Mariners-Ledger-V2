package com.pluralsight;

import java.time.*;
import java.util.*;

public class ReportsScreen {

    private Scanner scanner;
    private ArrayList<Transaction> transactions;

    public ReportsScreen(Scanner scanner, ArrayList<Transaction> transactions) {
        this.scanner = scanner;
        this.transactions = transactions;
    }

    public void display() {

        boolean running = true;

        while (running) {
            System.out.println("\n----- REPORTS SCREEN -----");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
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
                searchByVendor();
            } else if (input.equals("0")) {
                running = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    public void monthToDate() {
        LocalDate now = LocalDate.now();
        System.out.println("\n--- MONTH TO DATE ---");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }

    public void previousMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        System.out.println("\n--- PREVIOUS MONTH ---");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            LocalDate date = t.getDate();

            if (date.getMonth() == lastMonth.getMonth() && date.getYear() == lastMonth.getYear()) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }

    public void yearToDate() {
        int currentYear = LocalDate.now().getYear();
        System.out.println("\n--- YEAR TO DATE ---");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == currentYear) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }

    public void previousYear() {
        int lastYear = LocalDate.now().getYear() - 1;
        System.out.println("\n--- PREVIOUS YEAR ---");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == lastYear) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }

    public void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine().toLowerCase();
        System.out.println("\n--- VENDOR SEARCH: " + vendor + " ---");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getVendor().toLowerCase().contains(vendor)) {
                System.out.println(t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getAmount());
            }
        }
    }
}