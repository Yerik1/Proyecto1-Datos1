/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto1datos1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author kondy
 */
public class FXMLDummyController implements Initializable{

    
    private Mines[][] tablero=new Mines[8][8] ;
    @FXML
    private GridPane gdTablero;
    @FXML
    private Label lbMines;
    @FXML
    private Label lbTime;
    @FXML
    private Label lbResult;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fillTablero();
        jugar();
    }    
    
    public void fillTablero(){
        int tot=25;
        lbMines.setText(String.valueOf(tot));
        int cont=0;
        while(cont<tot){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    int rand= (int)(Math.random()*5+1);
                    if(rand>1){
                        if(tablero[i][j]==null){
                            tablero[i][j]=new Mines(false);
                        }
                    }else{
                        if(cont<tot){
                            tablero[i][j]=new Mines(true);
                            cont++;
                        }
                    }
                }
            }
        }
    }
    
    public void jugar(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                Button btn=new Button("  ");
                int a =i;
                int b =j;
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton()==MouseButton.PRIMARY){
                            Label lbl=checkCasilla(a,b);
                            lbl.setFont(new Font("Arial",12));
                            gdTablero.add(lbl, a, b);
                            GridPane.setHalignment(lbl, HPos.CENTER);
                        }
                    }
                });
                gdTablero.add(btn, i, j);
            }
        }
    }

    private Label checkCasilla(int a, int b) {
        Label lbl= new Label("");
        int cant=tablero[a][b].getMina();
        if(cant==10){
            for(final Node node : this.gdTablero.getChildren()){
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                        node.setDisable(true);
                    }
                }
            }
            lbl= new Label("M");
            lbl.setAlignment(Pos.CENTER);
        }else{
            try{
                if(tablero[a-1][b-1].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a-1][b].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a-1][b+1].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a][b-1].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a][b+1].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a+1][b-1].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a+1][b].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            try{
                if(tablero[a+1][b+1].getEstado()){
                    cant++;
                }
            }catch(ArrayIndexOutOfBoundsException exception){
            }
            for(final Node node : this.gdTablero.getChildren()){
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                        node.setDisable(true);
                    }
                }
            }
            lbl= new Label(String.valueOf(cant));
            lbl.setAlignment(Pos.CENTER);
        }
        return lbl;
    }
    
    
}
