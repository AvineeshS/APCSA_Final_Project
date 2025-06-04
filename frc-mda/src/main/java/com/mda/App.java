package com.mda;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String apiKey = "EirqUtI3fd05qcOdO5pdOEKEEGFc6IdAN4IYa5OU3vy83x5lDcSZKPZKfDrhtGdw";
        String eventKey = "2025mawne";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the team number (numbers only, e.g. 195): ");
        String teamNumber = scanner.nextLine().trim();

        // Validate input is a number
        if (!teamNumber.matches("\\d+")) {
            System.out.println("Invalid input. Please enter numbers only.");
            scanner.close();
            return;
        }

        String teamKey = "frc" + teamNumber;
        DataManager dataManager = new DataManager();

        Integer OPPrank = dataManager.fetchRankFromApi(apiKey, eventKey, teamKey);
        Integer MYrank = dataManager.fetchRankFromApi(apiKey, eventKey, "frc178");

        if (OPPrank != null && MYrank != null) {
            if (OPPrank < MYrank) {
                System.out.println("Team " + teamKey + " is ranked higher than Team frc178, good luck you might be cooked");
            } else if (OPPrank > MYrank) {
                System.out.println("Team " + teamKey + " is ranked lower than Team frc178, good luck make sure your robot works!");
            } else {
                System.out.println("So I'm not sure just do whatever");
            }
        } else if (OPPrank == null) {
            System.out.println("Could not find rank for team " + teamKey + ". The team may not be at this event or does not exist.");
        } else {
            System.out.println("Could not find rank for team. The team may not be at this event or does not exist.");
        }
        scanner.close();
    }
}