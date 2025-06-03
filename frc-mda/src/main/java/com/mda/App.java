package com.mda;

public class App {
    public static void main(String[] args) {
        String apiKey = "EirqUtI3fd05qcOdO5pdOEKEEGFc6IdAN4IYa5OU3vy83x5lDcSZKPZKfDrhtGdw";
        String eventKey = "2025mawne";
        String teamKey = "frc178";

        DataManager dataManager = new DataManager();

        Integer rank = dataManager.fetchRankFromApi(apiKey, eventKey, teamKey);
        if (rank != null) {
            System.out.println("Team " + teamKey + " rank: " + rank);
        } else {
            System.out.println("API failed, trying Selenium...");
            rank = dataManager.fetchRankWithSelenium(eventKey, teamKey);
            if (rank != null) {
                System.out.println("Team " + teamKey + " rank (Selenium): " + rank);
            } else {
                System.out.println("Could not find rank for team " + teamKey);
            }
        }
    }
}
