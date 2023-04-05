/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto1datos1;

import java.net.URL;
import java.util.ResourceBundle;
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
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author kondy
 */
public class FXMLAdvancedController implements Initializable {
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
    private Lista general= new Lista();
    private Lista segura= new Lista();
    private Lista incertidumbre= new Lista();
    private Lista registro=new Lista();
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
    public int getBanderas(){
        return this.banderas;
    }
    
    public void setBanderas(int banderas){
        this.banderas=banderas;
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
                                    btn.setVisible(false);
                                    checkCasilla(a,b,true,0);
                                }
                                if(event.getButton()==MouseButton.SECONDARY){
                                    btn.setVisible(false);
                                    actualizarMinas(a,b,false,false);
                                }
                            }
                        });
                    }else{
                        this.tablero.getTablero()[a][b].setBandera(true);
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
                                    if(!this.tablero.getTablero()[a][b].getBandera()){
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
                cant=checkCantidad(a,b);
                for(final Node node : this.gdTablero.getChildren()){
                    if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                        if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                            node.setDisable(true);
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
        if(tablero.checkTerminar()&&this.perdida){
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
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(!this.tablero.getTablero()[i][j].getActivado()){
                    Nodo nuevo=new Nodo(i,j);
                    Nodo copia=new Nodo(i,j);
                    if(checkMina(nuevo)){
                        nuevo.setEsMina();
                        copia.setEsMina();
                        System.out.println("Casilla "+String.valueOf(i+1)+", "+String.valueOf(j+1)+" es mina");
                    }
                    this.general.agregar(nuevo);
                    this.registro.agregar(copia);
                    System.out.println("Agregada Casilla: "+String.valueOf(i+1)+", "+String.valueOf(j+1)+" a lista general.");
                }
            }
        }
        while(this.general.getHead()!=null){
            Nodo casilla=this.general.buscarAleatorio();
            this.general.eliminar(casilla);
            casilla.setNext(null);
            System.out.println("Eliminada Casilla: "+String.valueOf(casilla.getI()+1)+", "+String.valueOf(casilla.getJ()+1)+" de lista general.");
            if(checkAlrededor(casilla.getI(),casilla.getJ())<getAlrededor(casilla.getI(),casilla.getJ())){
                if(!casilla.getEsMina()){
                    if(checkSegura(casilla)){
                        this.segura.agregar(casilla);
                        System.out.println("Agregada Casilla: "+String.valueOf(casilla.getI()+1)+", "+String.valueOf(casilla.getJ()+1)+" a lista segura.");
                    }else{
                        this.incertidumbre.agregar(casilla);
                        System.out.println("Agregada Casilla: "+String.valueOf(casilla.getI()+1)+", "+String.valueOf(casilla.getJ()+1)+" a lista incertidumbre.");
                    }
                }else{
                    this.incertidumbre.agregar(casilla);
                    System.out.println("Agregada Casilla: "+String.valueOf(casilla.getI()+1)+", "+String.valueOf(casilla.getJ()+1)+" a lista incertidumbre.");
                }
                
            }else{
                this.incertidumbre.agregar(casilla);
                System.out.println("Agregada Casilla: "+String.valueOf(casilla.getI()+1)+", "+String.valueOf(casilla.getJ()+1)+" a lista incertidumbre.");
            }
        }
        if(this.segura.getHead()!=null){
            Nodo jugada=this.segura.buscarAleatorio();
            checkCasilla(jugada.getI(),jugada.getJ(),true,1);
            System.out.println("Casilla "+String.valueOf(jugada.getI()+1)+", "+String.valueOf(jugada.getJ()+1)+" jugada segura");
        }else{
            Nodo jugada=this.incertidumbre.buscarAleatorio();
            checkCasilla(jugada.getI(),jugada.getJ(),true,1);
            System.out.println("Casilla "+String.valueOf(jugada.getI()+1)+", "+String.valueOf(jugada.getJ()+1)+" jugada incertidumbre");
        }
        this.general.eliminarLista();
        System.out.println("Lista general vacia");
        this.segura.eliminarLista();
        System.out.println("Lista segura vacia");
        this.incertidumbre.eliminarLista();
        System.out.println("Lista general vacia");
        this.registro.eliminarLista();
    }
    
    public boolean checkMina(Nodo casilla){
        boolean seguro=false;
        int i=casilla.getI();
        int j=casilla.getJ();
        try{
            if(this.tablero.getTablero()[i-1][j-1].getActivado()){
                if(checkAlrededor(i-1,j-1)==checkCantidad(i-1,j-1)){
                    seguro=true;
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i-1][j].getActivado()){
                if(checkAlrededor(i-1,j)==checkCantidad(i-1,j)){
                    seguro=true;
                    
                }       
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i-1][j+1].getActivado()){
                if(checkAlrededor(i-1,j+1)==checkCantidad(i-1,j+1)){
                    seguro=true;
                    
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i][j-1].getActivado()){
                if(checkAlrededor(i,j-1)==checkCantidad(i,j-1)){
                    seguro=true;
                    
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i][j+1].getActivado()){
                if(checkAlrededor(i,j+1)==checkCantidad(i,j+1)){
                    seguro=true;
                    
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j-1].getActivado()){
                if(checkAlrededor(i+1,j-1)==checkCantidad(i+1,j-1)){
                    seguro=true;
                    
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j].getActivado()){
                if(checkAlrededor(i+1,j)==checkCantidad(i+1,j)){
                    seguro=true;
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j+1].getActivado()){
                if(checkAlrededor(i+1,j+1)==checkCantidad(i+1,j+1)){
                    seguro=true;
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        return seguro;
    }
    
    public int checkCantidad(int i,int j){
        int cant=0;
        try{
            if(this.tablero.getTablero()[i-1][j-1].getEstado()){
               cant++;
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i-1][j].getEstado()){
               cant++;      
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i-1][j+1].getEstado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i][j-1].getEstado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i][j+1].getEstado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j-1].getEstado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j].getEstado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j+1].getEstado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        return cant;
    }
    
    public int checkAlrededor(int i, int j){
        int cant=0;
        try{
            if(!this.tablero.getTablero()[i-1][j-1].getActivado()){
               cant++; 
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i-1][j].getActivado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i-1][j+1].getActivado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i][j-1].getActivado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i][j+1].getActivado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i+1][j-1].getActivado()){
               cant++;  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i+1][j].getActivado()){
               cant++;       
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(!this.tablero.getTablero()[i+1][j+1].getActivado()){
               cant++;
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        return cant;
    }
    
    public int getAlrededor(int i, int j){
        int cant=0;
        try{
            this.tablero.getTablero()[i-1][j-1].getMina();
            cant++;
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i-1][j].getMina();
            cant++;  
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i-1][j+1].getMina();
            cant++;  
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i][j-1].getMina();
            cant++;  
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i][j+1].getMina();
            cant++;  
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i+1][j-1].getMina();
            cant++;  
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i+1][j].getMina();
            cant++;       
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            this.tablero.getTablero()[i+1][j+1].getMina();
            cant++;
            
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        return cant;
    }
    
    public boolean checkSegura(Nodo nodo){
        int i=nodo.getI();
        int j=nodo.getJ();
        boolean resultado=false;
        try{
            if(this.tablero.getTablero()[i-1][j-1].getActivado()){
                System.out.println("1");
                System.out.println(String.valueOf(checkCantidad(i-1,j-1)));
                System.out.println(String.valueOf(checkHumano(i-1,j-1)));
                if(checkCantidad(i-1,j-1)==checkHumano(i-1,j-1)) {
                    resultado=true;
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i-1][j].getActivado()){
                System.out.println("2");
                System.out.println(String.valueOf(checkCantidad(i-1,j)));
                System.out.println(String.valueOf(checkHumano(i-1,j)));
                if(checkCantidad(i-1,j)==checkHumano(i-1,j)) {
                    resultado=true;
                }  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i-1][j+1].getActivado()){
                System.out.println("4");
                System.out.println(String.valueOf(checkCantidad(i-1,j+1)));
                System.out.println(String.valueOf(checkHumano(i-1,j+1)));
                if(checkCantidad(i-1,j+1)==checkHumano(i-1,j+1)) {
                    resultado=true;
                }  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i][j-1].getActivado()){
                System.out.println("4");
                System.out.println(String.valueOf(checkCantidad(i,j-1)));
                System.out.println(String.valueOf(checkHumano(i,j-1)));
                if(checkCantidad(i,j-1)==checkHumano(i,j-1)) {
                    resultado=true;
                }  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i][j+1].getActivado()){
                System.out.println("5");
                System.out.println(String.valueOf(checkCantidad(i,j+1)));
                System.out.println(String.valueOf(checkHumano(i,j+1)));
                if(checkCantidad(i,j+1)==checkHumano(i,j+1)) {
                    resultado=true;
                } 
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j-1].getActivado()){
                System.out.println("6");
                System.out.println(String.valueOf(checkCantidad(i+1,j-1)));
                System.out.println(String.valueOf(checkHumano(i+1,j-1)));
                if(checkCantidad(i+1,j-1)==checkHumano(i+1,j-1)) {
                    resultado=true;
                }  
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j].getActivado()){
                System.out.println("7");
                System.out.println(String.valueOf(checkCantidad(i+1,j)));
                System.out.println(String.valueOf(checkHumano(i+1,j)));
                if(checkCantidad(i+1,j)==checkHumano(i+1,j)) {
                    resultado=true;
                }       
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        try{
            if(this.tablero.getTablero()[i+1][j+1].getActivado()){
                System.out.println("8");
                System.out.println(String.valueOf(checkCantidad(i+1,j+1)));
                System.out.println(String.valueOf(checkHumano(i+1,j+1)));
                if(checkCantidad(i+1,j+1)==checkHumano(i+1,j+1)) {
                    resultado=true;
                }
            }
        }catch(ArrayIndexOutOfBoundsException exception){
        }
        return resultado;
    }
    
    public int checkHumano(int i, int j){
        int cant=0;
        Nodo buscar;
        buscar=this.registro.buscar(i-1, j-1);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        buscar=this.registro.buscar(i-1, j);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        buscar=this.registro.buscar(i-1, j+1);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        
        buscar=this.registro.buscar(i, j-1);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        
        buscar=this.registro.buscar(i, j+1);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        buscar=this.registro.buscar(i+1, j-1);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        buscar=this.registro.buscar(i+1, j);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }
        buscar=this.registro.buscar(i+1, j+1);
        if(buscar!=null&&buscar.getEsMina()){
            cant++;
        }

        return cant;
        
        
    }
}
