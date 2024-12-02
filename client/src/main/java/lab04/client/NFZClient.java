package lab04.client;

import lab04.client.model.NFZResponse;
import lab04.client.model.QueueEntry;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class NFZClient {
    private static final String API_URL = "https://api.nfz.gov.pl/app-itl-api/queues";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public NFZClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Pobiera dane z API NFZ na podstawie podanych parametrów.
     *
     * @param page             Strona wyników.
     * @param limit            Limit wyników na stronę.
     * @param province         Kod województwa (np. "01" dla Mazowieckiego).
     * @param benefitForChildren Czy świadczenie jest przeznaczone dla dzieci.
     * @return Lista wpisów w kolejce (QueueEntry).
     * @throws IOException          W przypadku problemów z przetwarzaniem JSON.
     * @throws InterruptedException W przypadku przerwania żądania HTTP.
     */
    public List<QueueEntry> getQueueEntries(int page, int limit, String province, boolean benefitForChildren)
            throws IOException, InterruptedException {
        // Budowanie URL z parametrami
        String url = String.format("%s?page=%d&limit=%d&format=json&province=%s&benefitForChildren=%b&api-version=1.3",
                API_URL, page, limit, province, benefitForChildren);

        // Tworzenie żądania HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Wysyłanie żądania i odbieranie odpowiedzi
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Logowanie surowego JSON do konsoli
        System.out.println("JSON Response:");
        System.out.println(response.body());

        // Parsowanie odpowiedzi
        if (response.statusCode() == 200) {
            NFZResponse nfzResponse = objectMapper.readValue(response.body(), NFZResponse.class);
            return nfzResponse.getData();
        } else {
            throw new IOException("Błąd HTTP: " + response.statusCode());
        }
    }
    public String getRawJsonResponse(int page, int limit, String province, boolean benefitForChildren)
            throws IOException, InterruptedException {
        // Budowanie URL z parametrami
        String url = String.format("%s?page=%d&limit=%d&format=json&province=%s&benefitForChildren=%b&api-version=1.3",
                API_URL, page, limit, province, benefitForChildren);

        // Tworzenie żądania HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Wysyłanie żądania i odbieranie odpowiedzi
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("Błąd HTTP: " + response.statusCode());
        }
    }


}
