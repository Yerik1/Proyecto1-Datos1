/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto1datos1;

import java.awt.AWTException;
import java.io.IOException;
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
import javafx.stage.Stage;

/**
 * Clase controlador de la ventana de juego dummy
 *
 * @author Yerik
 */
public class FXMLDummyController implements Initializable{

    //tiempo transcurrido
    private int timer=0;
    //indica si la partida ha terminado
    private boolean perdida=false;
    //total de minas
    private int total=10;
    //contador de minas restantes
    private int minasRestantes= this.total;
    //turnos jugados
    private int turno= 0;
    //pila de pistas
    private Pila sugerencias = new Pila();
    //tablero de juego
    private Tablero tablero;
    //variables de interfaz
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
    //Hilo timer para ir contando el tiempo
    private Timer tiempo;
    private ArduinoController arduino;
    
    /**
     * Metodo que inicializa la ventana
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Se agrega la cantidad de minas del juego a una label
        this.lbMines.setText(String.valueOf(this.total));
        
        //se genera el tablero con la cantidad de minas establecidas
        this.tablero=new Tablero(this.total);
        
        //se inicializa el hilo de timer y se declara la funcionalidad del hilo
        this.tiempo=new Timer(1000, (java.awt.event.ActionEvent e) -> {
            
            //se incrementa en uno el tiempo cada segundo y se actualiza la label del tiempo
            this.timer++;
            Platform.runLater(()->this.lbTime.setText(String.valueOf(this.timer)));
        });
        
        //se inicia el timer
        this.tiempo.start();
        
        //se llena la gridpane de la interfaz con botones para jugar
        jugar();
        
        //se inicializa la comunicacion con el arduino y se le envia el tablero
        try {
            this.arduino= new ArduinoController();
            this.arduino.start();
            this.arduino.setTablero(this.tablero);
        } catch (AWTException ex) {
            Logger.getLogger(FXMLDummyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Funcion que llena el tablero con minas y actualiza el tablero de la interfaz
     */
    public void jugar(){
        
        //se recorre toda la matriz 8x8
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                
                //se crea un boton con texto vacio
                Button btn=new Button("  ");
                
                //se guardan las coordenadas del boton
                int a =i;
                int b =j;
                
                //se le asigna un id al boton con corde a su tipo
                btn.setId("normal");
                
                //se declara las funcionalidades del boton
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    
                    @Override
                    public void handle(MouseEvent event) {
                        
                        //cuando se es un click izquierdo
                        if(event.getButton()==MouseButton.PRIMARY){
                            try {
                                
                                //realiza la accion de revisar la casilla
                                checkCasilla(a,b,true,0);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLDummyController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        //cuando se es un click derecho
                        if(event.getButton()==MouseButton.SECONDARY){
                            try {
                                
                                //realiza la accion de poner o quitar bandera de la casilla
                                actualizarMinas(a,b,false);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLDummyController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                
                //se agrega el boton a la gridpane de la interfaz en las coordenadas correspondientes
                gdTablero.add(btn, i, j);
            }
        }
    }

    /**
     * Metodo que cambia el estado de un boton de tener bandera a no tener bandera
     * y viceversa
     * @param a, coordenada fila del boton
     * @param b, coordenada columna del boton
     * @param estado, booleano indicando si tiene bandera
     * @throws java.io.IOException
     */
    public void actualizarMinas(int a, int b,boolean estado) throws IOException{
        
        //si ya hay una bandera en el boton se suma uno a la cantidad de minas restantes
        if(estado){
            
            this.minasRestantes++;
            
            //se envia al arduino que es una casilla normal
            this.arduino.enviarSerial("N");
            
        }else{
            
            //en caso contrario se resta uno a la cantidad de minas faltantes
            this.minasRestantes--;
            
            //se envia al arduino que la casilla tiene bandera
            this.arduino.enviarSerial("B");
            
        }
        
        //se actualiza el texto de minas restantes
        lbMines.setText(String.valueOf(this.minasRestantes));
        
        //se recorre todos los elementos del gridpane
        for(final Node node : this.gdTablero.getChildren()){
            
            //verifica que los elementos se encuentren dentro de las casillas
            if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                
                //verifica que las coordenadas del elemento sean las mismas que las del boton
                if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                    
                    //oculta el elemento
                    node.setVisible(false);
                    
                    //se crea un boton para remplazar al elemento
                    Button btn = new Button();
                    
                    //en caso de que el boton tenga bandera
                    if(estado){
                        
                        //se elimina la bandera de tablero de juego
                        this.tablero.getTablero()[a][b].setBandera(false);
                        
                        //texto vacio del boton
                        btn.setText("  ");
                        
                        //se le asigna una id correspondiente al tipo de boton
                        btn.setId("normal");
                        
                        //se declaran las acciones del boton
                        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                
                                //en caso de click izquierdo
                                if(event.getButton()==MouseButton.PRIMARY){
                                    
                                    //se desabilita el boton y luego se revisa la casilla
                                    btn.setVisible(false);
                                    try {
                                        checkCasilla(a,b,true,0);
                                    } catch (IOException ex) {
                                        Logger.getLogger(FXMLDummyController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                                //en caso de click derecho
                                if(event.getButton()==MouseButton.SECONDARY){
                                    
                                    //se desabilita el boton y luego se cambia si hay bandera o no
                                    btn.setVisible(false);
                                    
                                    try {
                                        actualizarMinas(a,b,false);
                                    } catch (IOException ex) {
                                        Logger.getLogger(FXMLDummyController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        });
                    }else{
                        
                        //se agrega una bandera al tablero de juego en la casilla
                        this.tablero.getTablero()[a][b].setBandera(true);
                        
                        //se le asigna una id correspondiente al tipo de boton
                        btn.setId("bandera");
                        
                        //se agrega texto al boton
                        btn.setText("B");
                        
                        //se asignan las acciones del boton
                        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                
                                //en caso de ser click derechp
                                if(event.getButton()==MouseButton.SECONDARY){
                                    
                                    //se desabilita el boton y se cambia el estado de la bandera
                                    btn.setVisible(false);
                                    try {
                                        actualizarMinas(a,b,true);
                                    } catch (IOException ex) {
                                        Logger.getLogger(FXMLDummyController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        });
                    }
                    
                    //se agrega el boton al tablero de la interfaz
                    this.gdTablero.add(btn, a, b);
                    break;
                }
            }
        }
    }
    
    /**
     * Metodo que permite jugar una casilla y verificar si es mina o cuantas minas
     * tiene alrededor
     * @param a, fila del boton
     * @param b, columna del boton
     * @param game, indica si es un turno directo o no (caso cascada)
     * @param jugador, indica con un 0 si es el usuario o con un 1 si es la compu
     */
    private void checkCasilla(int a, int b,boolean game,int jugador) throws IOException {
        
        //verifica si es un turno manual del jugador usuario
        if(game&&jugador==0){
            
            //se aumenta los turnos
            this.turno++;
        }
        
        //cuando se llegue al turno 5 se agrega una pista a la pila
        if(this.turno==5&&game){
            agregarSugerencia();
            
            //se reinicia la cantidad de turnos
            this.turno=0;
        }
        
        //se crea una label para el texto que sustituye al boton
        Label lbl= new Label("");
        
        //se guarda si la casilla tene una mina o no
        int cant=this.tablero.getTablero()[a][b].getMina();
        
        //verifica que la casilla no este activada
        boolean act= !this.tablero.getTablero()[a][b].getActivado();
        
        //si se retorna una mina y la casilla no esta activada
        if(cant==10&&act){
            
            //recorre todos los elementos del tablero de la interfaz
            for(final Node node : this.gdTablero.getChildren()){
                
                //verifica que el elemento este asignado a una casilla
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    
                    //verifica que las coordenadas del elemento sean iguales a las del boton
                    if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                        
                        //comunica al arduino que el usuario presiono una mina
                        if(jugador==0&&game){
                            this.arduino.enviarSerial("M");
                        }
                        
                        //se detiene el timer debido a que se presiono una mina
                        this.tiempo.stop();
                        
                        //se desabilita el boton
                        node.setDisable(true);
                        
                        //se cambia el texto de label por una M de mina
                        if(!this.tablero.getTablero()[a][b].getBandera()&&jugador==0){
                            lbl.setText("M");
                        }else{
                            lbl.setText("O");
                        }
                        
                        //formato de texto
                        lbl.setFont(new Font("Arial",12));
                        lbl.setAlignment(Pos.CENTER);
                        
                        //Verifica que sea un turno manual
                        if(game){
                            
                            //en caso de ser el usuario
                            if(jugador==0){
                                
                                //se cambia a color azul para indicar que fue el usuario
                                lbl.setTextFill(Color.BLUE);
                                
                                //se indica que se perdio
                                lbResult.setText("Lo lamento has perdido");
                                
                            }else{
                                
                                //envia al arduino que el usuario gano
                                this.arduino.enviarSerial("W");
                                
                                //se cambia a color rojo para indicar que fue la compu
                                lbl.setTextFill(Color.RED);
                                
                                //se indica que se gano debido a que la compu perdio
                                lbResult.setText("Felicidades has ganado");
                            }
                        }
                        
                        //se detiene la comunicacion con el arduino
                        this.arduino.stopThread();
                        
                        //se agrega la label al tablero de la interfaz
                        this.gdTablero.add(lbl, a, b);
                        GridPane.setHalignment(lbl, HPos.CENTER);
                        
                        //se inidica que la casilla fue jugada
                        this.tablero.getTablero()[a][b].setActivado();
                        
                        //se indica que se perdio la partida
                        this.perdida=true;
                        
                        //se recorre todo el tablero de juego
                        for(int i=0;i<8;i++){
                            for(int j=0;j<8;j++){
                                
                                //en caso de ser un movimiento manual(para que no se embucle)
                                if(game){
                                    
                                    //se recorre todos los elementos del tablero de interfaz
                                    for(final Node node2 : this.gdTablero.getChildren()){
                                        
                                        //se asegura que el elemento este asignado a una casilla
                                        if(GridPane.getColumnIndex(node2)!=null&&GridPane.getRowIndex(node2)!=null){
                                            
                                            //se verifica que sea un boton
                                            if(node2 instanceof Button){
                                                
                                                //se deshabilita ek boton
                                                node2.setVisible(false);
                                            }
                                        }
                                    }
                                    
                                    //se realiza el movimiento para revelar el tablero
                                    checkCasilla(i,j,false,jugador);
                                }
                            }
                        }
                        //se rompe el ciclo una vez encontado el elemento de la casilla
                        break;
                    }
                }
            }
            
        //en caso de no ser mina
        }else{
            
            //se verifica que no este activada
            if(act){
                
                //se recorre cada una de las casillas de alrededor de la jugada
                //para encontrar el numero de minas que hay alrededor
                try{
                    if(this.tablero.getTablero()[a-1][b-1].getEstado()){
                        //en caso de ser 1 se le suma 
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
                
                //se envia al arduino que el usuario presiono una casilla segura
                if(jugador==0&&game){
                    this.arduino.enviarSerial("S");
                }
                
                //se recorre los elementos del tablero de la interfaz
                for(final Node node : this.gdTablero.getChildren()){
                    
                    //se verifica que el elemento este asignado a una casilla
                    if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                        
                        //se verifica que las coordenadas coincidan
                        if(GridPane.getColumnIndex(node)==a&&GridPane.getRowIndex(node)==b){
                            
                            //se desabilita el boton de esa casilla
                            node.setVisible(false);
                            
                            //se recorre nuevamente en caso de que se haya agregado otro boton al final
                            for(final Node node2 : this.gdTablero.getChildren()){
                                
                                //se verifica que el elemento este asignado a una casilla
                                if(GridPane.getColumnIndex(node2)!=null&&GridPane.getRowIndex(node2)!=null){
                                    
                                    //se verifica que las coordenadas coincidan
                                    if(GridPane.getColumnIndex(node2)==a&&GridPane.getRowIndex(node2)==b){
                                        
                                        //se verifica que sea un boton
                                        if(node2 instanceof Button){
                                            if(node2.getId().equalsIgnoreCase("bandera")){
                                                
                                                //en caso de ser un boton bandera se suma uno a la cantidad de minas restantes
                                                this.minasRestantes++;
                                            }
                                            
                                            // se actualiza el contador de minas restantes
                                            this.lbMines.setText(String.valueOf(this.minasRestantes));
                                            
                                            //se desabilita el boton
                                            node2.setVisible(false);
                                        }
                                        
                                    }
                                }
                            }
                            
                            //se desabilitan el primer boton
                            node.setVisible(false);
                            
                            //se cambia la casilla a activada
                            this.tablero.getTablero()[a][b].setActivado();
                            
                            //en caso de no tener minas alrededor
                            if(cant==0){
                                
                                //si es un turno manual se coloca un * para evidenciar el turno
                                if(game){
                                    lbl.setText("*");
                                }else{
                                    
                                    //en caso contrario no se agrega texto
                                    lbl.setText("");
                                }
                            }else{
                                
                                //en caso que si haya minas alrededor se agrega este valor
                                lbl.setText(String.valueOf(cant));
                            }
                            
                            //formato de texto
                            lbl.setAlignment(Pos.CENTER);
                            lbl.setFont(new Font("Arial",12));
                            
                            //distincion de color entre usuario y computadora
                            if(game){
                                if(jugador==0){
                                    lbl.setTextFill(Color.BLUE);
                                }else{
                                    lbl.setTextFill(Color.RED);
                                }
                            }
                            
                            //se agrega al tablero de la interfaz el texto
                            this.gdTablero.add(lbl, a, b);
                            GridPane.setHalignment(lbl, HPos.CENTER);
                            
                            //efecto cascada para cuando no hay minas alrededor
                            if(cant==0){
                                
                                //se revisan las casillas que rodean a la casilla
                                for(int i=(a-1);i<=(a+1);i++){
                                    for(int j=(b-1);j<=(b+1);j++){
                                        try{
                                            
                                            //se revisa las casillas de alrededor
                                            checkCasilla(i,j,false,0);
                                        }catch(ArrayIndexOutOfBoundsException exception){ 
                                            
                                            //en caso de que se salgadel tablero no se hace nada 
                                        }
                                    }
                                }
                            }
                            //se rompe el ciclo una vez encontado el elemento de la casilla
                            break;
                        }
                    }
                }
            }
        }
        
        //Condicion para ganar la partida
        if(this.tablero.checkTerminar()&&!this.perdida){
            
            //se envia al arduino que el usuario gano
            this.arduino.enviarSerial("W");
            
            //se detiene el timer
            this.tiempo.stop();
            
            //se detiene el arduino
            this.arduino.stopThread();
            
            //texto de partida ganada
            lbResult.setText("Felicidades has ganado");
            
            //se recorren todas las casillas
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    
                    //se verifica si la casilla es mina
                    if(this.tablero.getTablero()[i][j].getEstado()){
                        
                        //en caso de ser mina se escribe una O simulando la forma de la mina
                        lbl=new Label("O");
                        
                        //Formato de texto
                        lbl.setAlignment(Pos.CENTER);
                        lbl.setFont(new Font("Arial",12));
                        this.gdTablero.add(lbl, i, j);
                        GridPane.setHalignment(lbl, HPos.CENTER);
                        
                    }
                }
            }
            
            //se recorre todos los elementos del tablero de interfaz
            for(final Node node : this.gdTablero.getChildren()){
                
                //se verifica que el elemento este asignado a una casilla
                if(GridPane.getColumnIndex(node)!=null&&GridPane.getRowIndex(node)!=null){
                    
                    //se verifica que sea boton
                    if(node instanceof Button){
                        
                        //se desabilita el boton
                        node.setVisible(false);
                        
                    }
                }
            }
        }
        
        //en caso de que la partida este activa, haya sifo el turno del usuario
        //y sea un turno manual juega la compu
        if(!this.perdida&&jugador==0&&game){
            jugarCompu();
        }
    }

    @FXML
    /**
     * Metodo para boton que muestra una sugerencia en la interfaz
     */
    private void sugerencia(ActionEvent event) {
        
        //verifica si el tope de la pila esta vacio
        if(this.sugerencias.peek()==null){
            
            //en caso de estar vacio menciona el texto
            this.lbPista.setText("No hay pistas");
            
        //en caso que haya tope de la fila
        }else{
            
            //muestra la pista en la interfaz y la elimina de la pila
            this.lbPista.setText(this.sugerencias.peek());
            this.sugerencias.pop();
        }
    }
    
    /**
     * Metodo que permite agregar una sugerencia a la pila
     */
    public void agregarSugerencia(){
        
        //indicador si se ha agregado la pista
        boolean agregada=false;
        
        //Ciclo mientras no este agregada la pista
        while(!agregada){
            
            //coordenadas aleatorias
            int i= (int)(Math.random()*8);
            int j= (int)(Math.random()*8);
            
            //Verifica que la celda aleatoria no haya sido jugada y no sea una mina
            if(!this.tablero.getTablero()[i][j].getActivado()&&!this.tablero.getTablero()[i][j].getEstado()){
                
                //agrega la sugerencia a la pila sobre la celda aleatoria
                this.sugerencias.push("Pista: La celda "+String.valueOf(i+1)+" ,"+String.valueOf(j+1)+" es segura.");
                
                //se indica que la pista fue agregada
                agregada=true;
            }
        }
    }
    
    /**
     * Metodo para el movimiento de la compu
     * @throws IOException 
     */
    public void jugarCompu() throws IOException{
        
        //indicador de si ya se realizo el movimiento
        boolean jugado=false;
        
        //Ciclo mientras no se haya realizado el movimiento
        while(!jugado){
            
            //coordenadas aleatorias
            int i= (int)(Math.random()*8);
            int j= (int)(Math.random()*8);
            
            //verifica que la casilla aleatoria no haya sido jugada
            if(!this.tablero.getTablero()[i][j].getActivado()){
                
                //realiza la jugada en la casilla
                checkCasilla(i,j,true,1);
                
                //indica que el movimiento fue hecho
                jugado=true;
            }
        }
        
    }
    
}
