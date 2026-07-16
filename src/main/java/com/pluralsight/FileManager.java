package com.pluralsight;

import java.io.*;
import java.time.*;
import java.util.*;

public class FileManager {

    // matches the actual file name on disk (capital T) so this works on
    // case-sensitive file systems like Mac/Linux, not just Windows/IntelliJ
    private static final String FILE_NAME = "Transactions.csv";

    public static ArrayList<Transaction> loadTransactions() {
        // this is where we store all the transactions once we read them in
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line = reader.readLine(); // skip the header row

            // keep reading lines until we hit the end of the file
            while ((line = reader.readLine()) != null) {
                // split each line by the pipe delimiter to get the individual fields
                String[] parts = line.split("\\|");

                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // create a transaction object and add it to the list
                Transaction t = new Transaction(date, time, description, vendor, amount);
                transactions.add(t);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // send the full list back to main
        return transactions;
    }

    public static void saveTransaction(Transaction t) {
        try {
            // true means append mode so we don't overwrite existing transactions
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(t.getDate() + "|" + t.getTime() + "|" + t.getDescription() + "|" + t.getVendor() + "|" + t.getAmount());
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
}