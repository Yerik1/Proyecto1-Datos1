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

    private boolean perdida=true;
    private int total=10;
    private int banderas= this.total;
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
    public int getBanderas(){
        return this.banderas;
    }
    
    public void setBanderas(int banderas){
        this.banderas=banderas;
    }
    public void fillTablero(){
        lbMines.setText(String.valueOf(this.total));
        int cont=0;
        while(cont<this.total){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    int rand= (int)(Math.random()*5+1);
                    if(rand>1){
                        if(tablero[i][j]==null){
                            tablero[i][j]=new Mines(false);
                        }
                    }else{
                        if(cont<this.total){
                            tablero[i][j]=new Mines(true);
                            cont++;
                        }
                        if(tablero[i][j]==null){
                            tablero[i][j]=new Mines(false);
                        }
                    }
                }
            }
        }
    }
    
    public boolean checkTerminar(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(!this.tablero[i][j].getActivado()){
                    if(!this.tablero[i][j].getEstado()){
                        return false;
                    }
                }
            }
        }
        return true;
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
                            checkCasilla(a,b,true);
                        }
                        if(event.getButton()==MouseButton.SECONDARY){
                            actualizarMinas(a,b,false,false);
                        }
                    }
                });
                gdTablero.add(btn, i, j);
            }
        }
    }

    public void actualizarMinas(int a, int b,boolean estado,boolean esp){
        if(estado){
            this.banderas++;
        }else{
            this.banderas--;
        }
        lbMines.setText(String.valueOf(this.banderas));
        for(final Node node : this.gdTablero.getChildren()){
            if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                    node.setDisable(true);
                    Button btn = new Button();
                    if(estado){
                        this.tablero[a][b].setBandera(false);
                        btn.setText("  ");
                        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton()==MouseButton.PRIMARY){
                                    btn.setVisible(false);
                                    checkCasilla(a,b,true);
                                }
                                if(event.getButton()==MouseButton.SECONDARY){
                                    btn.setVisible(false);
                                    actualizarMinas(a,b,false,false);
                                }
                            }
                        });
                    }else{
                        this.tablero[a][b].setBandera(true);
                        btn.setText("B");
                        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton()==MouseButton.SECONDARY){
                                    btn.setVisible(false);
                                    actualizarMinas(a,b,true,false);
                                }
                            }
                        });
                    }
                    if(!esp){
                        this.gdTablero.add(btn, a, b);
                    }
                    
                    
                    break;
                }
            }
        }
    }
        
    private void checkCasilla(int a, int b,boolean game) {
        Label lbl= new Label("");
        int cant=this.tablero[a][b].getMina();
        boolean act= !this.tablero[a][b].getActivado();
        if(cant==10&&act){
            for(final Node node : this.gdTablero.getChildren()){
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                        node.setDisable(true);
                        lbl.setText("M");
                        lbl.setFont(new Font("Arial",12));
                        lbl.setAlignment(Pos.CENTER);
                        this.gdTablero.add(lbl, a, b);
                        GridPane.setHalignment(lbl, HPos.CENTER);
                        lbResult.setText("Lo lamento has perdido");
                        this.tablero[a][b].setActivado();
                        this.perdida=false;
                        for(int i=0;i<8;i++){
                            for(int j=0;j<8;j++){
                                if(game){
                                    if(!this.tablero[i][j].getBandera()){
                                        for(final Node node2 : this.gdTablero.getChildren()){
                                            if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                                                if(node2 instanceof Button){
                                                    node2.setVisible(false);
                                                }
                                            }
                                        }
                                    }
                                    checkCasilla(i,j,false);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }else{
            if(act){
                try{
                    if(this.tablero[a-1][b-1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a-1][b].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a-1][b+1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a][b-1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a][b+1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a+1][b-1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a+1][b].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero[a+1][b+1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                for(final Node node : this.gdTablero.getChildren()){
                    if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                        if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                            node.setDisable(true);
                            this.tablero[a][b].setActivado();
                            if(cant==0){
                                lbl.setText("");
                            }else{
                                lbl.setText(String.valueOf(cant));
                            }
                            lbl.setAlignment(Pos.CENTER);
                            lbl.setFont(new Font("Arial",12));
                            this.gdTablero.add(lbl, a, b);
                            GridPane.setHalignment(lbl, HPos.CENTER);
                            if(cant==0){
                                for(int i=(a-1);i<=(a+1);i++){
                                    for(int j=(b-1);j<=(b+1);j++){
                                        try{
                                            checkCasilla(i,j,true);
                                        }catch(ArrayIndexOutOfBoundsException exception){                        
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if(checkTerminar()&&this.perdida){
        lbResult.setText("Felicidades has ganado");
            for(final Node node : this.gdTablero.getChildren()){
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    if(node instanceof Button){
                        node.setVisible(false);
                    }
                }
            }
        }
    }
}
