package com.example.escriturarapida;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Clase principal que inicia el juego de escritura rápida.
 * <p>
 * Carga la interfaz gráfica y muestra la ventana principal.
 *
 * @author Juan Solarte
 * @version 1.0
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("front.fxml"));


        BorderPane root = fxmlLoader.load(); // Usar BorderPane directamente
        Scene scene = new Scene(root);

        stage.setTitle("Escritura Rapida");
        stage.setScene(scene);

        // Auto-ajustar el tamaño de la ventana al contenido
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
