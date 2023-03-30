/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package proyecto1datos1;

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
import javafx.stage.Stage;
import static javafx.application.Application.launch;

/**
 *
 * @author kondy
 */
public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button btDummy;
    @FXML
    private Button btAdvanced;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void newDummy(ActionEvent event) throws Exception {
        Stage stage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDummy.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void newAdvanced(ActionEvent event) throws Exception{
        Stage stage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLAdvanced.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
}
