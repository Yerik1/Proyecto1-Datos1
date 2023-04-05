/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto1datos1;

import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author kondy
 */
public class FXMLDummyController implements Initializable{

    private int timer=0;
    private boolean perdida=true;
    private int total=10;
    private int banderas= this.total;
    private int turno= 0;
    private Pila sugerencias = new Pila();
    private Tablero tablero;
    @FXML
    private GridPane gdTablero;
    @FXML
    private Label lbMines;
    @FXML
    private Label lbTime;
    @FXML
    private Label lbResult;
    @FXML
    private Button btPista;
    @FXML
    private Label lbPista;
    private Timer tiempo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbMines.setText(String.valueOf(this.total));
        tablero=new Tablero(this.total);
        this.tiempo=new Timer(1000, (java.awt.event.ActionEvent e) -> {
            this.timer++;
            Platform.runLater(()->this.lbTime.setText(String.valueOf(this.timer)));
        });
        this.tiempo.start();
        jugar();
    }    
    
    
    public void jugar(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                Button btn=new Button("  ");
                int a =i;
                int b =j;
                btn.setId("normal");
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton()==MouseButton.PRIMARY){
                            checkCasilla(a,b,true,0);
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
                        this.tablero.getTablero()[a][b].setBandera(false);
                        btn.setText("  ");
                        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton()==MouseButton.PRIMARY){
                                    btn.setId("normal");
                                    btn.setVisible(false);
                                    checkCasilla(a,b,true,0);
                                }
                                if(event.getButton()==MouseButton.SECONDARY){
                                    btn.setId("bandera");
                                    btn.setVisible(false);
                                    actualizarMinas(a,b,false,false);
                                }
                            }
                        });
                    }else{
                        this.tablero.getTablero()[a][b].setBandera(true);
                        btn.setId("bandera");
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
        
    private void checkCasilla(int a, int b,boolean game,int jugador) {
        if(game&&jugador==0){
            this.turno++;
        }
        if(this.turno==5&&game){
            agregarSugerencia();
            this.turno=0;
        }
        Label lbl= new Label("");
        int cant=this.tablero.getTablero()[a][b].getMina();
        boolean act= !this.tablero.getTablero()[a][b].getActivado();
        if(cant==10&&act){
            for(final Node node : this.gdTablero.getChildren()){
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                        this.tiempo.stop();
                        node.setDisable(true);
                        lbl.setText("M");
                        lbl.setFont(new Font("Arial",12));
                        lbl.setAlignment(Pos.CENTER);
                        if(game){
                            if(jugador==0){
                                lbl.setTextFill(Color.BLUE);
                                lbResult.setText("Lo lamento has perdido");
                            }else{
                                lbl.setTextFill(Color.RED);
                                lbResult.setText("Felicidades has ganado");
                            }
                        }
                        this.gdTablero.add(lbl, a, b);
                        GridPane.setHalignment(lbl, HPos.CENTER);
                        this.tablero.getTablero()[a][b].setActivado();
                        this.perdida=false;
                        for(int i=0;i<8;i++){
                            for(int j=0;j<8;j++){
                                if(game){
                                    if(!this.tablero.getTablero()[i][j].getBandera()){
                                        for(final Node node2 : this.gdTablero.getChildren()){
                                            if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                                                if(node2 instanceof Button){
                                                    node2.setVisible(false);
                                                }
                                            }
                                        }
                                    }
                                    checkCasilla(i,j,false,jugador);
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
                    if(this.tablero.getTablero()[a-1][b-1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a-1][b].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a-1][b+1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a][b-1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a][b+1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a+1][b-1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a+1][b].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                try{
                    if(this.tablero.getTablero()[a+1][b+1].getEstado()){
                        cant++;
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                }
                for(final Node node : this.gdTablero.getChildren()){
                    if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                        if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                            node.setVisible(false);
                            for(final Node node2 : this.gdTablero.getChildren()){
                                if(GridPane.getColumnIndex(node2)!=null&&GridPane.getRowIndex(node2)!=null){
                                    if(GridPane.getColumnIndex(node2)==a&&GridPane.getRowIndex(node2)==b){
                                        if(node2 instanceof Button){
                                            if(node2.getId().equalsIgnoreCase("bandera")){
                                                this.banderas++;
                                            }
                                            this.lbMines.setText(String.valueOf(this.banderas));
                                            node2.setVisible(false);
                                        }
                                        
                                    }
                                }
                            }
                            node.setVisible(false);
                            this.tablero.getTablero()[a][b].setActivado();
                            if(cant==0){
                                if(game){
                                    lbl.setText("*");
                                }else{
                                    lbl.setText("");
                                }
                            }else{
                                lbl.setText(String.valueOf(cant));
                            }
                            lbl.setAlignment(Pos.CENTER);
                            lbl.setFont(new Font("Arial",12));
                            if(game){
                                if(jugador==0){
                                    lbl.setTextFill(Color.BLUE);
                                }else{
                                    lbl.setTextFill(Color.RED);
                                }
                            }
                            this.gdTablero.add(lbl, a, b);
                            GridPane.setHalignment(lbl, HPos.CENTER);
                            if(cant==0){
                                for(int i=(a-1);i<=(a+1);i++){
                                    for(int j=(b-1);j<=(b+1);j++){
                                        try{
                                            checkCasilla(i,j,false,0);
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
        if(this.tablero.checkTerminar()&&this.perdida){
            this.tiempo.stop();
            lbResult.setText("Felicidades has ganado");
            for(final Node node : this.gdTablero.getChildren()){
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    if(node instanceof Button){
                        node.setVisible(false);
                    }
                }
            }
        }
        if(this.perdida&&jugador==0&&game){
            jugarCompu();
        }
    }

    @FXML
    private void sugerencia(ActionEvent event) {
        if(this.sugerencias.peek()==null){
            this.lbPista.setText("No hay pistas");
        }else{
            this.lbPista.setText(this.sugerencias.peek());
            this.sugerencias.pop();
        }
    }
    
    public void agregarSugerencia(){
        boolean agregada=false;
        while(!agregada){
            int i= (int)(Math.random()*8);
            int j= (int)(Math.random()*8);
            if(!this.tablero.getTablero()[i][j].getActivado()&&!this.tablero.getTablero()[i][j].getEstado()){
                this.sugerencias.push("Pista: La celda "+String.valueOf(i+1)+" ,"+String.valueOf(j+1)+" es segura.");
                agregada=true;
            }
        }
    }
    
    public void jugarCompu(){
        boolean jugado=false;
        while(!jugado){
            int i= (int)(Math.random()*8);
            int j= (int)(Math.random()*8);
            if(!this.tablero.getTablero()[i][j].getActivado()){
                checkCasilla(i,j,true,1);
                for(final Node node : this.gdTablero.getChildren()){
                    if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                        if(GridPane.getColumnIndex(node)==i&&GridPane.getRowIndex(node)==j){
                            if(node instanceof Button){
                                node.setVisible(false);
                            }
                            for(final Node node2 : this.gdTablero.getChildren()){
                                if(GridPane.getColumnIndex(node2)!=null&&GridPane.getRowIndex(node2)!=null){
                                    if(GridPane.getColumnIndex(node2)==i&&GridPane.getRowIndex(node2)==j){
                                        if(node2 instanceof Button){
                                            if(node2.getId().equalsIgnoreCase("bandera")){
                                                this.banderas++;
                                                this.lbMines.setText(String.valueOf(this.banderas));
                                            }
                                            node2.setVisible(false);
                                        }
                                        
                                    }
                                }
                            }
                        }
                    }
                }
                jugado=true;
            }
        }
        
    }
    
}
