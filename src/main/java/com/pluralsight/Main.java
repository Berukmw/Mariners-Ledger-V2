package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        Transaction t = new Transaction(
                LocalDate.now(),
                LocalTime.now(),
                "Test Purchase",
                "Amazon",
                -49.99
        );

        System.out.println(t.getDate());
        System.out.println(t.getVendor());
        System.out.println(t.getAmount());
    }
}