package lab04.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFZClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public NFZClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<String> getAllBenefits() throws IOException, InterruptedException {
        String url = "https://api.nfz.gov.pl/app-itl-api/benefits?format=json&name=abc&api-version=1.3";

        String jsonResponse = getRawJsonResponse(url);

        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        List<String> benefits = new ArrayList<>();

        rootNode.path("data").forEach(node -> {
            String benefit = node.path("attributes").path("name").asText();
            if (!benefit.isEmpty()) {
                benefits.add(benefit);
            }
        });

        return benefits;
    }

    public String getRawJsonResponse(String url) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(
                HttpRequest.newBuilder().uri(URI.create(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            System.err.println("Błąd HTTP: " + response.statusCode());
            System.err.println("Treść odpowiedzi: " + response.body());
            throw new IOException("Błąd HTTP: " + response.statusCode());
        }
    }

    public List<Map<String, String>> getQueueEntriesByBenefit(String provinceCode, String city, String benefit)
            throws IOException, InterruptedException {
        return getAllQueueEntries(provinceCode, city, benefit);
    }

    public List<Map<String, String>> getAllQueueEntries(String provinceCode, String city, String benefit)
            throws IOException, InterruptedException {
        List<Map<String, String>> allFacilities = new ArrayList<>();
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String encodedBenefit = URLEncoder.encode(benefit, StandardCharsets.UTF_8);

        String url = String.format(
                "https://api.nfz.gov.pl/app-itl-api/queues?page=1&limit=25&format=json&case=1&province=%s&locality=%s&benefit=%s&api-version=1.3",
                provinceCode, encodedCity, encodedBenefit
        );

        while (url != null) {
            String jsonResponse = getRawJsonResponse(url);

            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            rootNode.path("data").forEach(node -> {
                JsonNode attributes = node.path("attributes");
                Map<String, String> facilityData = new HashMap<>();
                facilityData.put("provider", attributes.path("provider").asText());
                facilityData.put("locality", attributes.path("locality").asText());
                facilityData.put("benefit", attributes.path("benefit").asText());
                facilityData.put("queueLength", attributes.path("statistics").path("provider-data").path("awaiting").asText());
                facilityData.put("waitingTime", attributes.path("statistics").path("provider-data").path("average-period").asText());
                facilityData.put("date", attributes.path("dates").path("date").asText());
                facilityData.put("address", attributes.path("address").asText());
                facilityData.put("phone", attributes.path("phone").asText());
                allFacilities.add(facilityData);
            });

            // Sprawdzenie linku do następnej strony
            JsonNode links = rootNode.path("links");
            url = links.has("next") && !links.path("next").isNull()
                    ? "https://api.nfz.gov.pl" + links.path("next").asText()
                    : null;

            // Logowanie
            System.out.println("Przetworzono stronę. Liczba wyników: " + allFacilities.size());
            Thread.sleep(2000);

        }

        return allFacilities;
    }
}
