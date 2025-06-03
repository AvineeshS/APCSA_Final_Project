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

public class DataManager {
    public Integer fetchRankFromApi(String apiKey, String eventKey, String teamKey) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.thebluealliance.com/api/v3/event/" + eventKey + "/rankings"))
                .header("X-TBA-Auth-Key", apiKey)
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode rankings = root.path("rankings");

            for (JsonNode team : rankings) {
                if (team.path("team_key").asText().equals(teamKey)) {
                    return team.path("rank").asInt();
                }
            }
        } catch (Exception e) {
            System.out.println("API error: " + e.getMessage());
        }
        return null;
    }

    public Integer fetchRankWithSelenium(String eventKey, String teamKey) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("https://www.thebluealliance.com/event/" + eventKey);

            WebElement table = driver.findElement(By.className("rankings-table"));
            for (WebElement row : table.findElements(By.tagName("tr"))) {
                if (row.getText().contains(teamKey.replace("frc", ""))) {
                    String rowText = row.getText();
                    String[] parts = rowText.split("\\s+");
                    if (parts.length > 0) {
                        try {
                            int fetchRank = Integer.parseInt(parts[0]);
                            System.out.println("Found via Selenium: " + rowText);
                            System.out.println("Extracted rank: " + fetchRank);
                            return fetchRank;
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
        return null;
    }
}