/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import javafx.stage.Stage;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * Clase que simula un controlador de comunicacion serial con un arduino
 * @author Yerik
 */
public class ArduinoController extends Thread {
    private SerialPort arduinoPort;
    private InputStream input;
    private OutputStream output;
    private boolean running = true;
    private int x;
    private int y;
    private int posX;
    private int posY;
    SerialPort [] AvailablePorts = SerialPort.getCommPorts();
    private Stage stage;
    private Robot arduino;
    private Tablero tablero;
    
    /**
     * Constructor del comunicador serial
     * @throws AWTException 
     */
    public ArduinoController() throws AWTException {
        
        //Clase robot permite que se utilice el teclad y el mouse de la computadora
        this.arduino=new Robot();
        
        //fila actual
        this.x=1;
        
        //columna actual
        this.y=1;
        
        //posicion inicial del mouse (varia con la computadora a utilizar)
        this.posX=600;
        this.posY=290;
        this.arduino.mouseMove(this.posX, this.posY);
        
        //muestra los puertos disponibles para el arduino y su descripcion
        System.out.println("\n\nAvailable Ports ");
            for (int i = 0; i<AvailablePorts.length ; i++){
		System.out.println(i + ". " + AvailablePorts[i].getSystemPortName() + " == " + AvailablePorts[i].getDescriptivePortName());
            }
            
        //puerto a utilizar (varia dependiendo donde se conecte el arduino)
        this.arduinoPort = SerialPort.getCommPort("COM6");
        
        //velocidad de comunicacion
        this.arduinoPort.setBaudRate(9600);
        
        // Abrir el puerto serial
        if (this.arduinoPort.openPort(1000)) {
            System.out.println("Puerto serial abierto correctamente");
        } else {
            
            //si no se logra abrir se cambia el running a false para que no inicie el hilo
            System.out.println("Error al abrir el puerto serial");
            this.running=false;
        }
        
        // Obtener los flujos de entrada y salida
        this.input = this.arduinoPort.getInputStream();
        this.output = this.arduinoPort.getOutputStream();
    }
    
    /**
     * Funcion que se ejecuta al empezar el hilo
     */
    @Override
    public void run() {
        
        //mientras este corriendo:
        while (this.running) {
            
            try {
                
                // Leer los datos del puerto serial
                if (this.input.available() > 0) {
                    byte[] buffer = new byte[this.input.available()];
                    this.input.read(buffer);
                    String data = new String(buffer);
                    
                    //se imprimen los datos del serial
                    System.out.println("Datos recibidos: " + data);
                    
                    //se realiza la accion correspondiente a los datos leidos
                    realizarAccion(data);
                }
                
                // Esperar un momento antes de volver a leer/Enviar datos
                Thread.sleep(100);
                
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Cerrar el puerto serial al terminar el hilo
        this.arduinoPort.closePort();
        System.out.println("Puerto serial cerrado");
    }
    
    /**
     * funcion que cambia el valor del booleano que permite al hilo funcionar
     * paradetener el hilo
     */
    public void stopThread() {
        this.running = false;
    }
    
    /**
     * funcion que realiza acciones dependiendo de la informacion recibida
     * @param data, informacion recibida
     * @throws IOException 
     */
    public void realizarAccion(String data) throws IOException{
        
        //Se mueve la posicion para arriba
        if(data.equals("U")){
            
            //se presiona la tecla de arriba
            this.arduino.keyPress(KeyEvent.VK_UP);
            this.arduino.keyRelease(KeyEvent.VK_UP);
            
            //la posicion es distinta de 1
            if(this.y!=1){
                
                //si se encuentra entre 2 y 8
                if(this.y>1&&this.y<=8){
                    
                    //se mueve hacia arriba
                    this.y--;
                    
                    //se mueve el mouse un boton para arriba
                    this.arduino.mouseMove(this.posX+25*(this.x-1), this.posY+25*(this.y-1));
                    
                    //se envia si la casilla actual tiene una bandera
                    if(this.tablero.getTablero()[this.x-1][this.y-1].getBandera()){
                        enviarSerial("B");
                    }else{
                        enviarSerial("N");
                    }
                }
            //en caso de ser 1, se mueve hacia el boton de pista  
            }else{
                
                //coordenadas boton pista
                this.arduino.mouseMove(700, 200);
                this.y--;
                
                //se avisa que no tiene bandera
                enviarSerial("N");
            }
        }
        
        //se mueve la posicion para abajo
        if(data.equals("D")){
            
            //se presiona la tecla de abajo
            this.arduino.keyPress(KeyEvent.VK_DOWN);
            this.arduino.keyRelease(KeyEvent.VK_DOWN);
            
            //mientras se este entre 0 y 7
            if(this.y>=0&&this.y<8){
                
                //si se encuentra en el boton de pista
                if(this.y==0){
                    
                    //contador de cuantas casillas se mueve
                    int cont=0;
                    
                    //se encuentra la diferencia entre la posicion horizontal
                    //y la posicion a la que se baja por defecto
                    int diferencia=this.x-5;
                    
                    //mientras el contador sea menor a la diferencia
                    while(cont<Math.abs(diferencia)){
                        
                        //si si la diferencia es negativa se mueve para la izq
                        if(diferencia<0){
                            this.arduino.keyPress(KeyEvent.VK_LEFT);
                            this.arduino.keyRelease(KeyEvent.VK_LEFT);
                        }else{
                            
                            //si la diferencia es positiva se mueve a la derecha
                            this.arduino.keyPress(KeyEvent.VK_RIGHT);
                            this.arduino.keyRelease(KeyEvent.VK_RIGHT);
                        }
                        cont++;
                    }
                }
                
                //se acomoda el mouse a la casilla de abajo
                this.y++;
                this.arduino.mouseMove(this.posX+25*(this.x-1), this.posY+25*(this.y-1));
                
                //se indica si la casilla tiene bandera o no
                if(this.tablero.getTablero()[this.x-1][this.y-1].getBandera()){
                        enviarSerial("B");
                    }else{
                        enviarSerial("N");
                    }
            }
        }
        
        //se mueve a la posicion de la izq
        if(data.equals("L")){
            
            //se presiona la tecla izq
            this.arduino.keyPress(KeyEvent.VK_LEFT);
            this.arduino.keyRelease(KeyEvent.VK_LEFT);
            
            //si no se esta en el boton de pista
            if(this.y!=0){
                
                //si se encuentra entre 2 y 8
                if(this.x>1&&this.x<=8){
                    
                    //se acomoda el mouse al botn de la izq
                    this.x--;
                    this.arduino.mouseMove(this.posX+25*(this.x-1), this.posY+25*(this.y-1));
                    
                    //se indica si esa casilla tiene bandera
                    if(this.tablero.getTablero()[this.x-1][this.y-1].getBandera()){
                        enviarSerial("B");
                    }else{
                        enviarSerial("N");
                    }
                }
            }
            
        }
        
        //se mueve a ala posicion de la derecha
        if(data.equals("R")){
            
            //se presiona la tecla de la derecha
            this.arduino.keyPress(KeyEvent.VK_RIGHT);
            this.arduino.keyRelease(KeyEvent.VK_RIGHT);
            
            //si no se esta en el boton de pista
            if(this.y!=0){
                
                //si se encuentra entre 1 y 7
                if(this.x>=1&&this.x<8){
                    
                    //se acomoda la posicion del mouse al boton de la derecha
                    this.x++;
                    this.arduino.mouseMove(this.posX+25*(this.x-1), this.posY+25*(this.y-1));
                    
                    //se envia si la casilla tiene bandera o no
                    if(this.tablero.getTablero()[this.x-1][this.y-1].getBandera()){
                        enviarSerial("B");
                    }else{
                        enviarSerial("N");
                    }
                }
            }
            
        }
        
        //se realiza un click izq
        if(data.equals("C")){
            this.arduino.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            this.arduino.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        
        //se realiza un click derecho
        if(data.equals("K")){
            this.arduino.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            this.arduino.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
    }
    
    /**
     * envia mensajes predeterminados al arduino
     * @param message, mensaje predeterminado
     * @throws IOException 
     */
    public void enviarSerial(String message) throws IOException{
        
        //mientras el mensaje no este vacio
        if(!message.isEmpty()){
            
            //se envia el string en bytes al arduino
            this.output.write(message.getBytes());
            
            //se evidencia la entrega de datos
            System.out.println("Datos enviados: "+message);
        }
    }
    
    /**
     * se actualiza la informacion del tablero de juego
     * @param tablero tablero de juego
     */
    public void setTablero(Tablero tablero){
        this.tablero=tablero;
    }
    
}

