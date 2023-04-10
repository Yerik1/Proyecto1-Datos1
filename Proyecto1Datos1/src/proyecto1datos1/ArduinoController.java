/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import javafx.stage.Stage;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class ArduinoController extends Thread {
    private SerialPort arduinoPort;
    private InputStream input;
    private OutputStream output;
    private boolean running = true;
    private int ventana;
    private int x;
    private int y;
    private int posX;
    private int posY;
    SerialPort [] AvailablePorts = SerialPort.getCommPorts();
    private Stage stage;
    private Robot arduino;
    
    public ArduinoController(Stage stage) throws AWTException {
        // Seleccionar el puerto serial y la velocidad de transmisión
        this.stage=stage;
        this.arduino=new Robot();
        this.ventana=1;
        this.x=1;
        this.y=1;
        this.posX=600;
        this.posY=290;
        this.arduino.mouseMove(this.posX, this.posY);
        System.out.println("\n\nAvailable Ports ");
            for (int i = 0; i<AvailablePorts.length ; i++){
		System.out.println(i + " - " + AvailablePorts[i].getSystemPortName() + " -> " + AvailablePorts[i].getDescriptivePortName());
            }
        this.arduinoPort = SerialPort.getCommPort("COM6");
        this.arduinoPort.setBaudRate(9600);
        
        // Abrir el puerto serial
        if (this.arduinoPort.openPort(1000)) {
            System.out.println("Puerto serial abierto correctamente");
        } else {
            System.out.println("Error al abrir el puerto serial");
            this.running=false;
        }
        
        // Obtener los flujos de entrada y salida
        this.input = this.arduinoPort.getInputStream();
        this.output = this.arduinoPort.getOutputStream();
    }
    
    @Override
    public void run() {
        while (this.running) {
            try {
                if(!this.stage.isShowing()){
                    stopThread();
                }
                // Leer los datos del puerto serial
                if (this.input.available() > 0) {
                    byte[] buffer = new byte[this.input.available()];
                    this.input.read(buffer);
                    String data = new String(buffer);
                    System.out.println("Datos recibidos: " + data);
                    realizarAccion(data);
                }
                
                // Enviar datos al puerto serial
                
                // Esperar un momento antes de volver a leer/Enviar datos
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Cerrar el puerto serial
        this.arduinoPort.closePort();
        System.out.println("Puerto serial cerrado");
    }
    
    public void stopThread() {
        this.running = false;
    }
    
    public void realizarAccion(String data){
        if(data.equals("U")){
            this.arduino.keyPress(KeyEvent.VK_UP);
            this.arduino.keyRelease(KeyEvent.VK_UP);
            if(this.y!=1){
                if(this.y>1&&this.y<=8){
                    this.y--;
                    this.arduino.mouseMove(this.posX, this.posY+25*(this.y-1));
                }

            }else{
                this.arduino.mouseMove(700, 200);
                this.y--;
            }
        }
        if(data.equals("D")){
            this.arduino.keyPress(KeyEvent.VK_DOWN);
            this.arduino.keyRelease(KeyEvent.VK_DOWN);
            if(this.y>=0&&this.y<8){
                if(this.y==0){
                    int cont=0;
                    int diferencia=this.x-5;
                    while(cont<Math.abs(diferencia)){
                        if(diferencia<0){
                            this.arduino.keyPress(KeyEvent.VK_LEFT);
                            this.arduino.keyRelease(KeyEvent.VK_LEFT);
                        }else{
                            this.arduino.keyPress(KeyEvent.VK_RIGHT);
                            this.arduino.keyRelease(KeyEvent.VK_RIGHT);
                        }
                        cont++;
                    }
                }
                this.y++;
                this.arduino.mouseMove(this.posX, this.posY+25*(this.y-1));
            }
        }
        if(data.equals("L")){
            this.arduino.keyPress(KeyEvent.VK_LEFT);
            this.arduino.keyRelease(KeyEvent.VK_LEFT);
        }
        if(data.equals("R")){
            this.arduino.keyPress(KeyEvent.VK_RIGHT);
            this.arduino.keyRelease(KeyEvent.VK_RIGHT);
        }
        if(data.equals("C")){
            this.arduino.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            this.arduino.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        if(data.equals("K")){
            this.arduino.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            this.arduino.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
    }
    
}

