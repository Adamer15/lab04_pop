module lab04.gui {
    requires lab04.client; // Wymaga modułu klienta

    requires javafx.controls; // Dodaj, jeśli używasz JavaFX
    requires javafx.fxml;

    opens lab04 to javafx.fxml;
    opens lab04.gui to javafx.fxml; // Otwiera GUI dla JavaFX
}
