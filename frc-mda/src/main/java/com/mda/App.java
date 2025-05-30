package com.mda;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) throws Exception {
        String apiKey = "EirqUtI3fd05qcOdO5pdOEKEEGFc6IdAN4IYa5OU3vy83x5lDcSZKPZKfDrhtGdw"; // Replace with your actual TBA API key
        String eventKey = "2025mawne"; // 2023 Silicon Valley Regional
        String teamKey = "frc178";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.thebluealliance.com/api/v3/event/" + eventKey + "/rankings"))
            .header("X-TBA-Auth-Key", apiKey)
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode rankings = root.path("rankings");

        Integer fetchRank = null; // Variable to store the fetched rank

        boolean found = false;
        for (JsonNode team : rankings) {
            if (team.path("team_key").asText().equals(teamKey)) {
                fetchRank = team.path("rank").asInt();
                System.out.println("Team " + teamKey + " rank at " + eventKey + ": " + fetchRank);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Team " + teamKey + " not found via API. Trying with Selenium...");

            // Setup Selenium WebDriver (Make sure chromedriver is in your PATH)
            WebDriver driver = new ChromeDriver();
            driver.get("https://www.thebluealliance.com/event/" + eventKey);

            try {
                WebElement table = driver.findElement(By.className("rankings-table"));
                for (WebElement row : table.findElements(By.tagName("tr"))) {
                    if (row.getText().contains("178")) {
                        // Attempt to extract rank from the row text
                        String rowText = row.getText();
                        String[] parts = rowText.split("\\s+");
                        if (parts.length > 0) {
                            try {
                                fetchRank = Integer.parseInt(parts[0]);
                                System.out.println("Found via Selenium: " + rowText);
                                System.out.println("Extracted rank: " + fetchRank);
                            } catch (NumberFormatException nfe) {
                                System.out.println("Could not parse rank from row: " + rowText);
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not extract data via Selenium: " + e.getMessage());
            } finally {
                driver.quit();
            }
        }
    }
}
