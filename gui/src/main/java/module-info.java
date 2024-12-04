module lab04.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires lab04.client; // Dodanie zależności do modułu klienta

    opens lab04.gui to javafx.fxml;
    exports lab04.gui;
}
