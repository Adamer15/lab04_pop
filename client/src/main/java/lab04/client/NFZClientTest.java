package lab04.client;

import lab04.client.model.QueueEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class NFZClientTest {
    public static void main(String[] args) {
        NFZClient client = new NFZClient();

        try {
            // Pobieranie danych o kolejkach
            List<QueueEntry> entries = client.getQueueEntries(1, 25, "03", false);

            // Wyświetlanie wyników
            for (QueueEntry entry : entries) {
                System.out.println(entry);
            }

            // Pobranie surowego JSON-a i zapisanie go do pliku
            String jsonResponse = client.getRawJsonResponse(1, 5, "01", true);
            saveJsonToFile(jsonResponse, "response.json");

            System.out.println("JSON zapisany do pliku response.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveJsonToFile(String json, String filename) throws IOException {
        Path filePath = Path.of(filename);
        Files.writeString(filePath, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
