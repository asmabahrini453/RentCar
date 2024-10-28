package com.stage.rentcar.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("localisation")
public class NominatimController {

    private final NominatimService nominatimService;

    public NominatimController(NominatimService nominatimService) {
        this.nominatimService = nominatimService;
    }

    // Endpoint to get display name only (existing)
    @GetMapping("/localisation/display-name")
    public String getDisplayName(@RequestParam String query) {
        return nominatimService.getDisplayName(query);
    }

    // New endpoint to get location details (display name, lat, lon)
    @GetMapping("/localisation/details")
    public Location getLocation(@RequestParam String query) {
        return nominatimService.getLocation(query);
    }
}
