package com.mda;

public class App {
    public static void main(String[] args) {
        String apiKey = "EirqUtI3fd05qcOdO5pdOEKEEGFc6IdAN4IYa5OU3vy83x5lDcSZKPZKfDrhtGdw";
        String eventKey = "2025mawne";
        String teamKey = "frc195";

        DataManager dataManager = new DataManager();

        Integer OPPrank = dataManager.fetchRankFromApi(apiKey, eventKey, teamKey);
        Integer MYrank = dataManager.fetchRankFromApi(apiKey, eventKey, "frc178");
        if (OPPrank != null) {
            System.out.println("Team " + teamKey + " rank: " + OPPrank);
        } else {
            System.out.println("API failed, trying Selenium...");
            OPPrank = dataManager.fetchRankWithSelenium(eventKey, teamKey);
            if (OPPrank != null) {
                System.out.println("Team " + teamKey + " rank (Selenium): " + OPPrank);
            } else {
                System.out.println("Could not find rank for team " + teamKey);
            }
        }
    }
}
