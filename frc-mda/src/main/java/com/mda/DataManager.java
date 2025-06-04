package com.mda;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
}