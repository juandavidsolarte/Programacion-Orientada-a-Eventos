module com.example.escriturarapida {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens com.example.escriturarapida to javafx.fxml;
    opens com.example.escriturarapida.Controllers to javafx.fxml;

    exports com.example.escriturarapida;
}