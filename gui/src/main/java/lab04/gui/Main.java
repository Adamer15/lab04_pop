package lab04.gui;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lab04.client.NFZClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main extends Application {
    private ComboBox<String> provinceComboBox;
    private TextField cityField;
    private TextField benefitField;
    private TableView<Map<String, String>> tableView;
    private NFZClient nfzClient;

    @Override
    public void start(Stage primaryStage) {
        nfzClient = new NFZClient();

        provinceComboBox = new ComboBox<>();
        cityField = new TextField();
        benefitField = new TextField();
        Button searchButton = new Button("Szukaj");

        tableView = new TableView<>();
        configureTableView();

        cityField.setPromptText("Wpisz miejscowość");
        benefitField.setPromptText("Wpisz nazwę świadczenia");

        VBox layout = new VBox(10, provinceComboBox, cityField, benefitField, searchButton, tableView);
        layout.setStyle("-fx-padding: 10; -fx-spacing: 10;");

        searchButton.setOnAction(e -> fetchData());

        loadProvinceData();

        primaryStage.setScene(new Scene(layout, 800, 600));
        primaryStage.setTitle("NFZ GUI");
        primaryStage.show();
    }

    private void configureTableView() {
        TableColumn<Map<String, String>, String> providerCol = new TableColumn<>("Placówka");
        providerCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get("provider")));

        TableColumn<Map<String, String>, String> localityCol = new TableColumn<>("Miasto");
        localityCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get("locality")));

        TableColumn<Map<String, String>, String> benefitCol = new TableColumn<>("Usługa");
        benefitCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get("benefit")));

        TableColumn<Map<String, String>, String> queueCol = new TableColumn<>("Długość kolejki");
        queueCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get("queueLength")));

        TableColumn<Map<String, String>, String> waitingTimeCol = new TableColumn<>("Czas oczekiwania (dni)");
        waitingTimeCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get("waitingTime")));

        tableView.getColumns().addAll(providerCol, localityCol, benefitCol, queueCol, waitingTimeCol);
    }

    private void loadProvinceData() {
        provinceComboBox.setItems(FXCollections.observableArrayList(
                "01 - Dolnośląskie", "02 - Kujawsko-Pomorskie", "03 - Lubelskie", "04 - Lubuskie", "05 - Łódzkie",
                "06 - Małopolskie", "07 - Mazowieckie", "08 - Opolskie", "09 - Podkarpackie", "10 - Podlaskie",
                "11 - Pomorskie", "12 - Śląskie", "13 - Świętokrzyskie", "14 - Warmińsko-Mazurskie", "15 - Wielkopolskie",
                "16 - Zachodniopomorskie"
        ));
    }

    private void fetchData() {
        String province = provinceComboBox.getValue();
        String city = cityField.getText();
        String benefit = benefitField.getText();

        if (province == null || city.isEmpty() || benefit.isEmpty()) {
            showAlert("Błąd", "Proszę wybrać województwo oraz wpisać miejscowość i nazwę świadczenia.");
            return;
        }

        String provinceCode = province.split(" - ")[0];

        try {
            List<Map<String, String>> entries = nfzClient.getQueueEntriesByBenefit(provinceCode, city, benefit);
            tableView.setItems(FXCollections.observableArrayList(entries));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem podczas pobierania danych.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
