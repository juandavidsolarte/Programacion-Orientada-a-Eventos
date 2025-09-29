package com.example.escriturarapida.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.awt.*;

/**
 * Controlador principal del juego de escritura rápida.
 * <p>
 * Gestiona niveles, vidas, temporizador, validación de palabras,
 * pausa y finalización del juego.
 *
 * @author Juan Solarte
 * @version 1.0
 */

public class GameControllers {

    //----- declaraciones de atributos de la clase ------
    @FXML
    private TextField entradaTexto;
    @FXML
    private Button startButton;
    @FXML
    public Label nivelLabel;
    @FXML
    public Label palabraLabel;
    @FXML
    public Label estadoLabel;
    @FXML
    public Label livesLabel;
    @FXML
    public Label labelSegundos;
    @FXML
    private Button pauseButton;

    // --- NUEVOS ELEMENTOS PARA EL PANEL DE RESULTADOS ---
    @FXML
    private VBox panelResultados;
    @FXML
    private Label resumenNivel;
    @FXML
    private Label resumenVidas;
    @FXML
    private Label resumenTiempo;
    @FXML
    private Button btnReiniciar;

    // ---- METODO PARA MOSTRAR EN BLANCO ----------------

    /**
     * Inicializa la interfaz en estado "esperando".
     * Limpia labels, deshabilita campos y botones hasta que se inicie el juego.
     */

    @FXML
    public void initialize() {


        estadoLabel.setText("");
        tiempoLabel.setText("");

        nivelLabel.setText("Level: 0");
        livesLabel.setText("Vidas: 2");

        // DESHABILITAR
        entradaTexto.setDisable(true);
        pauseButton.setDisable(true);
    }

    // -------- METODO PARA MOSTRAR EN BLANCO ----------------

    // ------------------------------- VARIABLES ------------
    private boolean enPausa = false;
    //---------- NIVEL -----------
    private int levelNumber = 1;

    //----SE AGREGA LOGICA PARA TENER INTENTENTOS O VIDAS ---
    private int vidas = 2;

    // ----- RESUMEN DE LA PARTIDA ---
    private int nivelesCompletados;
    private int vidasFinales;
    private int tiempoRestanteFinal;

    //--------- VAR. TIEMPO ---------
    @FXML
    private Label tiempoLabel;
    private Timeline timeline;

    private int tiempoBase = 20;// Se ve la necesidad de usar una varible base para modificar cada aumenta level
    private int segundosRestantes = 20; // contador empieza en 20 como s eespecifica


    //--------- VAR. TIEMPO ---------

    /**
     * Reduce el contador de tiempo y gestiona el fin de la ronda.
     * <p>
     * Muestra el valor actual antes de restar (para visualización completa).
     * Si el tiempo llega a 0, resta una vida y muestra nueva palabra.
     *
     * @param segundosARestar cantidad de segundos a restar (normalmente 1)
     */
    // ---------- metodo que se ejecuta cada segundo -------------
    private void restarTiempo(int segundosARestar) {

        tiempoLabel.setStyle("-fx-text-fill: black; -fx-alignment: center;");
        tiempoLabel.setText("" + segundosRestantes);

        segundosRestantes = segundosRestantes - segundosARestar;

        // si llega a cero, parar el timer y si resta una vida.
        if (segundosRestantes <= 0) {
            timeline.stop();
            vidas = vidas - 1;

            if (vidas <= 0) {
                finalizarPartida();
                return;
            }


            //SI SE ACABA EL TIEMPO SALE MENSAJE
            estadoLabel.setText("Tiempo agotado! -1 vida");
            estadoLabel.setStyle("-fx-text-fill: red;");

            mostrarNuevaPalabra();
            timeline.play();

        }
    }

    //----------- DICCIONARIO Y PALABRA -----------------
    private String[] diccionario = {
            "HOLA", "CASA", "PERRO", "GATO", "COMPUTADORA",
            "TECLADO", "PROGRAMACION", "JAVA", "VENTANA", "BOTON",
            "TELEFONO", "ESCUELA", "ESTUDIANTE", "PROFESOR", "LIBRO",
            "LAPIZ", "MESA", "SILLA", "CARRO", "BICICLETA","MONITOR",
            "MOUSE", "INTERNET", "ALGORITMO", "CLASE",
            "OBJETO", "METODO", "VARIABLE", "CICLO", "FUNCION",
            "ARREGLO", "CADENA", "BOOLEANO", "ENTERO", "FLOTANTE",
            "PANTALLA", "ALTAVOZ", "MICROFONO", "IMPRESORA", "ROUTER",
            "SMARTPHONE", "TABLET", "CONSOLA", "VIDEOJUEGO", "TECLADO",
            "AURICULARES", "BATERIA", "CARGADOR", "MEMORIA", "DISCO",
            "PROCESADOR", "GRAFICOS", "VENTILADOR", "FUENTE", "PUERTO",
            "CABLE", "ADAPTADOR", "DRIVER", "SISTEMA", "OPERATIVO",
            "ARCHIVO", "CARPETA", "DESCARGA", "SUBIR", "NUBE",
            "CONTRASEÑA", "USUARIO", "PERFIL", "AJUSTES", "NOTIFICACION"
    };
    private int palabraActual = 0; // indice de la palabra que se esta mostrando
    private String palabraMostrada = ""; // la palabra que esta en el label

    // ------------- metodo para mostrar una nueva palabra ------------
    private void mostrarNuevaPalabra() {
        palabraMostrada = diccionario[palabraActual];
        palabraLabel.setText(palabraMostrada);
        palabraLabel.setStyle("-fx-text-fill: black; -fx-alignment: center;");
        entradaTexto.setText("");
        palabraActual = (palabraActual + 1) % diccionario.length;

        // Reiniciar el temporizador con el tiempo base actual
        segundosRestantes = tiempoBase;
        tiempoLabel.setText("" + segundosRestantes);
    }
    // ------------- metodo para mostrar una nueva palabra ------------

    /**
     * Valida si la palabra escrita coincide con la mostrada.
     * <p>
     * - Si es correcta: sube de nivel y muestra la siguiente palabra.
     * - Si es incorrecta: resta una vida.
     * <p>
     * Finaliza la partida si se agotan las vidas.
     */


    //--------- Metodo para verificar que si lo que escribio esta correcto -----
    public void verificarPalabra() {
        String textoEscrito = entradaTexto.getText();

        //----- verificar si lo que escribio es exactamente igual a la palabra---
        if (textoEscrito.equals(palabraMostrada)) {
            estadoLabel.setText("CORRECTO!");
            //Actualizar el nivel
            levelNumber++;
            //Aqui ya modifica el tiempo base para la siguiente palabra
            verificarMultiplos(levelNumber);

            nivelLabel.setText("Level: "+levelNumber);
            estadoLabel.setStyle("-fx-text-fill: #32cf8b;"); // Verde personalizado

            mostrarNuevaPalabra();

        //---- Si es erroneo disminuye una vida ----
        } else {
            vidas--;
            livesLabel.setText("Vidas: " + vidas);
            if (vidas <= 0) {
                finalizarPartida();
                return;
            }


            estadoLabel.setText("INCORRECTO!-1 vida");
            estadoLabel.setStyle("-fx-text-fill: red;");
            entradaTexto.setText("");

        }
    }
    //--------- metodo para verificar que si lo que escribio esta correcto-----

    /**
     * Reduce el tiempo base cada 5 niveles (mínimo 2 segundos).
     *
     * @param currentLevel nivel actual para verificar múltiplo de 5
     */


    //-------------VERFICAR MULTIPLOS DE 5--------
    public void verificarMultiplos(int currentLevel) {
        // Se modifica para que solo en los niveles pares el tiempo de juego debe disminuir en 2 seg hasta quedar en 2 seg.
        if (currentLevel % 2 == 0) {
            tiempoBase = Math.max(tiempoBase - 2, 2); // reducir en 2, mínimo 2
            //restarTiempo(2);
            System.out.println("¡Nivel múltiplo de 5! Nuevo tiempo base: " + tiempoBase);

        }
        //Le agrego validacion de si no

    }
    //-------------VERFICAR MULTIPLOS DE 5--------


    //-------------------METODO PARA FINALIZAR LA PARTIDA--------

    /**
     * Detiene el juego y guarda los datos finales para el resumen.
     * <p>
     * Deshabilita la escritura, habilita el botón de reinicio
     * y muestra mensaje de fin de partida.
     */

    private void finalizarPartida() {
        if (timeline != null) {
            timeline.stop();
        }

        // Guardar datos
        this.nivelesCompletados = levelNumber - 1;
        this.vidasFinales = vidas;
        this.tiempoRestanteFinal = segundosRestantes;

        // Mostrar resumen en consola (opcional)
        System.out.println("=== RESUMEN FINAL ===");
        System.out.println("Niveles completados: " + nivelesCompletados);
        System.out.println("Vidas restantes: " + vidasFinales);
        System.out.println("Tiempo restante: " + tiempoRestanteFinal);
        System.out.println("=====================");

        // OCULTAR ELEMENTOS DEL JUEGO
        palabraLabel.setVisible(false);
        estadoLabel.setVisible(false);
        tiempoLabel.setVisible(false);
        labelSegundos.setVisible(false);
        entradaTexto.setVisible(false);
        startButton.setVisible(false);
        pauseButton.setVisible(false);

        //  MOSTRAR PANEL DE RESULTADOS
        resumenNivel.setText("Niveles completados: " + nivelesCompletados);
        resumenVidas.setText("Vidas restantes: " + vidasFinales);
        resumenTiempo.setText("Tiempo restante: " + tiempoRestanteFinal + "s");
        panelResultados.setVisible(true);
    }


    //-------------------METODO PARA FINALIZAR LA PARTIDA--------

    /**
     * Boton Inicio, Inicia o reinicia el juego.
     * <p>
     * Reinicia nivel, vidas y temporizador. Habilita la escritura
     * y muestra la primera palabra del diccionario.
     */


    @FXML
    public void onActionPlayButton(ActionEvent event) {
        System.out.println("Play pressed");


        // Se coloca en el mismo boton para reiniciar.
        levelNumber = 1;
        vidas = 2;

        // Iniciar label por defecto
        nivelLabel.setText("Level: " + levelNumber);
        livesLabel.setText("Vidas: " + vidas);
        tiempoLabel.setText("" + segundosRestantes);
        estadoLabel.setText("¡Juego iniciado!");
        labelSegundos.setVisible(true);
        pauseButton.setDisable(false);

        // Iniciar label por defecto
        entradaTexto.setDisable(false);
        entradaTexto.setText("");
        entradaTexto.requestFocus();

        //-----------------CONTADOR--------------------
        // Reiniciar contador
        segundosRestantes = tiempoBase;
        tiempoLabel.setText("" + segundosRestantes);

        // Si ya había un timeline, lo detenemos
        if (timeline != null) {
            timeline.stop();
        }

        // hago que el timer baje cada segundo
        timeline = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.seconds(1), e -> restarTiempo(1));
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE); // que se repita infinito
        timeline.play(); // empezar el timer
        //---------------CONTADOR---------------------


        //----------- MOSTRAR LA PRIMERA PALABRA AL OPRIMIR
        palabraActual = 0;
        mostrarNuevaPalabra();


        // FOCO PARA QUE NO SE BLOQUE EL TEXTFIELD
        entradaTexto.requestFocus();
    }




    @FXML
    private void onPauseButton(ActionEvent event) {

        if (enPausa == false) {
            timeline.pause();
            entradaTexto.setDisable(true);
            estadoLabel.setText("Juego en pausa");
            estadoLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
            pauseButton.setText("Reanudar");
            enPausa = true;
        } else {
            timeline.play();
            entradaTexto.setDisable(false);
            entradaTexto.requestFocus();
            estadoLabel.setText("¡Juego reanudado!");
            estadoLabel.setStyle("-fx-text-fill: black;");
            pauseButton.setText("Pausa");
            enPausa = false;
        }
    }



    @FXML
    void onKeyPressedTextField(KeyEvent event) {
        String information = entradaTexto.getText();
        System.out.println(event.getCode());

        // si presiono ENTER, verificar la palabra
        if (event.getCode().toString().equals("ENTER")) {
            verificarPalabra();
        }
    }


    @FXML
    private void onReiniciarJuego() {

        panelResultados.setVisible(false);


        palabraLabel.setVisible(true);
        estadoLabel.setVisible(true);
        tiempoLabel.setVisible(true);
        labelSegundos.setVisible(true);
        entradaTexto.setVisible(true);
        startButton.setVisible(true);
        pauseButton.setVisible(true);


        onActionPlayButton(null);
    }
}