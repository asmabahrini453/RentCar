package com.stage.rentcar.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class NominatimService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String nominatimUrl = "https://nominatim.openstreetmap.org/search";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Existing method to get display name only
    public String getDisplayName(String query) {
        String url = String.format("%s?q=%s&format=json&addressdetails=1", nominatimUrl, query);
        String response = restTemplate.getForObject(url, String.class);
        return extractDisplayName(response);
    }

    private String extractDisplayName(String jsonResponse) {
        try {
            List<Map<String, Object>> results = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
            if (!results.isEmpty()) {
                Map<String, Object> firstResult = results.get(0);
                return (String) firstResult.get("display_name");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // New method to get location with coordinates
    public Location getLocation(String query) {
        String url = String.format("%s?q=%s&format=json&addressdetails=1", nominatimUrl, query);
        String response = restTemplate.getForObject(url, String.class);
        return extractLocation(response);
    }

    private Location extractLocation(String jsonResponse) {
        try {
            List<Map<String, Object>> results = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
            if (!results.isEmpty()) {
                Map<String, Object> firstResult = results.get(0);
                String displayName = (String) firstResult.get("display_name");
                double lat = Double.parseDouble((String) firstResult.get("lat"));
                double lon = Double.parseDouble((String) firstResult.get("lon"));

                return new Location(displayName, lat, lon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
