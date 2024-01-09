package com.example.backend.controller;

//import com.example.backend.config.AuthenticationListener;
import com.example.backend.entity.*;
import com.example.backend.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class DefaultController {

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private AirportService airportService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "You are logged in.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/flights")
    public ResponseEntity<List<Segment>> getAllSegments() {
        List<Segment> segments = segmentService.getAllSegments();
        return ResponseEntity.ok().body(segments);
    }

//    @GetMapping("/api/markets")
//    public ResponseEntity<List<UniqueMarket>> getUniqueMarkets() {
//        List<UniqueMarket> uniqueMarkets = segmentService.findUniqueMarkets();
//        return ResponseEntity.ok(uniqueMarkets);
//    }

//    @GetMapping("/api/markets")
//    public ResponseEntity<String> getUniqueMarkets() {
//        List<UniqueMarket> uniqueMarkets = segmentService.findUniqueMarkets();
//
//        // Convert uniqueMarkets to GeoJSON format
//        String geoJson = convertToGeoJson(uniqueMarkets);
//
//        // Set the appropriate headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.valueOf("application/geo+json"));
//
//        return new ResponseEntity<>(geoJson, headers, HttpStatus.OK);
//    }

    @GetMapping("/api/markets")
    public ResponseEntity<String> getUniqueMarkets() {
        List<UniqueMarket> uniqueMarkets = segmentService.findUniqueMarkets();

        List<Map<String, Object>> features = new ArrayList<>();
        for (UniqueMarket market : uniqueMarkets) {
            Map<String, Object> feature = Map.of(
                    "type", "Feature",
                    "geometry", Map.of(
                            "type", "LineString",
                            "coordinates", new double[][] {
                                    {market.getOrigLongitude(), market.getOrigLatitude()},
                                    {market.getDestLongitude(), market.getDestLatitude()}
                            }
                    ),
                    "properties", Map.of(
                            "orig", market.getOrig(),
                            "dest", market.getDest()
                    )
            );
            features.add(feature);
        }

        Map<String, Object> geoJson = Map.of(
                "type", "FeatureCollection",
                "features", features
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String geoJsonString = objectMapper.writeValueAsString(geoJson);
            return ResponseEntity.ok(geoJsonString);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating GeoJSON");
        }
    }
    @GetMapping("/api/airports")
    public ResponseEntity<List<Airport>> getUniqueAirports() {
        List<Airport> airports = airportService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/api/countries")
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> countries = countryService.getCountries();
        return ResponseEntity.ok(countries);
    }


    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> countries = userService.getUsers();
        return ResponseEntity.ok(countries);
    }


    @GetMapping("/api/roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.ok(roles);
    }


    private String convertToGeoJson(List<UniqueMarket> uniqueMarkets) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode featureCollection = mapper.createObjectNode();
        featureCollection.put("type", "FeatureCollection");
        ArrayNode features = featureCollection.putArray("features");

        for (UniqueMarket market : uniqueMarkets) {
            // Add a feature for the origin
            addMarketFeature(features, market, market.getOrig(), "Origin");
            // Add a feature for the destination
            addMarketFeature(features, market, market.getDest(), "Destination");
        }

        try {
            return mapper.writeValueAsString(featureCollection);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private void addMarketFeature(ArrayNode features, UniqueMarket market, String location, String type) {
        ObjectNode feature = features.addObject();
        feature.put("type", "Feature");

        ObjectNode properties = feature.putObject("properties");
        properties.put("market", market.toString());
        properties.put("locationType", type);

        // Placeholder methods to get latitude and longitude for a given location
        double longitude = getLongitudeForLocation(location);
        double latitude = getLatitudeForLocation(location);

        ObjectNode geometry = feature.putObject("geometry");
        geometry.put("type", "Point");
        ArrayNode coordinates = geometry.putArray("coordinates");
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    // Placeholder methods - replace with your actual implementation
    private double getLongitudeForLocation(String location) {
        // Logic to retrieve longitude for the given location
        return 0.0; // Replace with actual logic
    }

    private double getLatitudeForLocation(String location) {
        // Logic to retrieve latitude for the given location
        return 0.0; // Replace with actual logic
    }


}