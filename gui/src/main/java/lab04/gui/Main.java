package lab04.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lab04.client.NFZClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    private TableView<Map<String, String>> tableView;
    private TextField provinceField;
    private TextField cityField;
    private TextField benefitField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NFZ Queue Viewer");

        // Tworzenie pól tekstowych do wprowadzania danych
        provinceField = new TextField();
        provinceField.setPromptText("Kod województwa (np. 03)");

        cityField = new TextField();
        cityField.setPromptText("Miasto (np. Lublin)");

        benefitField = new TextField();
        benefitField.setPromptText("Świadczenie (np. PORADNIA STOMATOLOGICZNA)");

        // Przycisk do pobierania danych
        Button fetchButton = new Button("Pobierz dane");
        fetchButton.setOnAction(e -> fetchQueueData());

        // Status
        statusLabel = new Label();

        // Tabela wyników
        tableView = new TableView<>();
        setupTableView();

        // Layout
        GridPane inputPane = new GridPane();
        inputPane.setHgap(10);
        inputPane.setVgap(10);
        inputPane.add(new Label("Województwo:"), 0, 0);
        inputPane.add(provinceField, 1, 0);
        inputPane.add(new Label("Miasto:"), 0, 1);
        inputPane.add(cityField, 1, 1);
        inputPane.add(new Label("Świadczenie:"), 0, 2);
        inputPane.add(benefitField, 1, 2);
        inputPane.add(fetchButton, 1, 3);

        VBox root = new VBox(10, inputPane, tableView, statusLabel);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTableView() {
        TableColumn<Map<String, String>, String> providerCol = new TableColumn<>("Placówka");
        providerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("provider")));

        TableColumn<Map<String, String>, String> benefitCol = new TableColumn<>("Usługa");
        benefitCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("benefit")));

        TableColumn<Map<String, String>, String> addressCol = new TableColumn<>("Adres");
        addressCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("address")));

        TableColumn<Map<String, String>, String> phoneCol = new TableColumn<>("Telefon");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("phone")));

        TableColumn<Map<String, String>, Integer> queueCol = new TableColumn<>("Kolejka");
        queueCol.setCellValueFactory(data -> {
            String queueLength = data.getValue().get("queueLength");
            int queue = parseIntOrDefault(queueLength, 0);
            return new SimpleObjectProperty<>(queue);
        });

        TableColumn<Map<String, String>, Integer> waitingTimeCol = new TableColumn<>("Czas oczekiwania");
        waitingTimeCol.setCellValueFactory(data -> {
            String waitingTime = data.getValue().get("waitingTime");
            int time = parseIntOrDefault(waitingTime, 0);
            return new SimpleObjectProperty<>(time);
        });


        TableColumn<Map<String, String>, String> dateCol = new TableColumn<>("Data");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("date")));

        tableView.getColumns().addAll(providerCol, benefitCol, addressCol, phoneCol, queueCol, waitingTimeCol, dateCol);
    }

    private void fetchQueueData() {
        String province = provinceField.getText();
        String city = cityField.getText();
        String benefit = benefitField.getText();

        if (province.isEmpty() || city.isEmpty() || benefit.isEmpty()) {
            statusLabel.setText("Wypełnij wszystkie pola!");
            return;
        }

        // Czyszczenie poprzednich danych
        tableView.getItems().clear();
        statusLabel.setText("Pobieranie danych...");

        // Wykonywanie operacji w tle
        new Thread(() -> {
            try {
                NFZClient client = new NFZClient();
                List<Map<String, String>> facilities = client.getQueueEntriesByBenefit(province, city, benefit);

                // Aktualizacja interfejsu użytkownika w wątku JavaFX
                Platform.runLater(() -> {
                    tableView.setItems(FXCollections.observableArrayList(facilities));
                    statusLabel.setText("Pobrano " + facilities.size() + " wyników.");
                });
            } catch (IOException | InterruptedException e) {
                // Aktualizacja statusu błędu w wątku JavaFX
                Platform.runLater(() -> {
                    statusLabel.setText("Błąd podczas pobierania danych: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
