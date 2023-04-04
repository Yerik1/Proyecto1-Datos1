/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package proyecto1datos1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

/**
 * Clase Main que inicializa la aplicacion abriendo el fxml principal
 * @author Yerik
 */
public class Proyecto1Datos1 extends Application {
    
    @Override
    /**
     * Metodo start que ejecuta el abrir el FXML del menu
     */
    public void start(Stage stage) throws Exception {
        //Declara el FXML
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        //Declara la escena a abrir
        Scene scene = new Scene(root);
        //Se ejecuta el abrir la escena
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo main para iniciar el programa
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
