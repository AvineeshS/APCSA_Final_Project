package com.mda;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {
    public static void main(String[] args) throws Exception {
        String apiKey = "EirqUtI3fd05qcOdO5pdOEKEEGFc6IdAN4IYa5OU3vy83x5lDcSZKPZKfDrhtGdw"; // Replace with your actual TBA API key
        String eventKey = "2023casj"; // 2023 Silicon Valley Regional
        String teamKey = "frc254";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.thebluealliance.com/api/v3/event/" + eventKey + "/rankings"))
            .header("X-TBA-Auth-Key", apiKey)
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode rankings = root.path("rankings");

        boolean found = false;
        for (JsonNode team : rankings) {
            if (team.path("team_key").asText().equals(teamKey)) {
                int rank = team.path("rank").asInt();
                System.out.println("Team " + teamKey + " rank at " + eventKey + ": " + rank);
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
                    if (row.getText().contains("254")) {
                        System.out.println("Found via Selenium: " + row.getText());
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
