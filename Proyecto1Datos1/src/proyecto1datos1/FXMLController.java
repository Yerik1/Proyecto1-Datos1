/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package proyecto1datos1;

import java.awt.Robot;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase para controlador de la interfaz grafica del menu (principal)
 * @author Yerik
 */
public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button btDummy;
    @FXML
    private Button btAdvanced;
    
    @Override
    /**
     * Metodo que inicializa la ventana, por defecto al crear el controlador
     */
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    /**
     * Metodo asociado al boton dummy, se encarga de abrir la ventana de juego en modo
     * dummy
     */
    private void newDummy(ActionEvent event) throws Exception {
        Stage stage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDummy.fxml"));
        //Declara la escena a abrir
        Scene scene = new Scene(root);
        //Se ejecuta el abrir la escena
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    /**
     * Metodo asociado al boton advanced, se encarga de abrir la ventana de juego en modo
     * advanced
     */
    private void newAdvanced(ActionEvent event) throws Exception{
        Stage stage= new Stage();
        //se crea la ventana con el formato de juego advanced
        Parent root = FXMLLoader.load(getClass().getResource("FXMLAdvanced.fxml"));
        //Declara la escena a abrir
        Scene scene = new Scene(root);
        //Se ejecuta el abrir la escena
        
        stage.setScene(scene);
        stage.show();
    }
    
}
