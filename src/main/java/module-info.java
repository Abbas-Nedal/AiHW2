module com.example.aihw2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.aihw2 to javafx.fxml;
    exports com.example.aihw2;
}