/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 * Clase que asemeja una pila de sugerencias
 * @author Yerik
 */
public class Pila {
    //Maximo de sugerencias de la pila
    private int maximo=13;
    //Array con las sugerencias acomuladas
    private String[] sugerencias;
    //Numero de ultima sugerencia en la pila
    private int top;
    
    /**
     * Constructor de Objeto Pila
     */
    public Pila(){
        //genera el array con las sugerencias
        this.sugerencias=new String[maximo];
        //indica que la pila esta vacia
        this.top=0;
    }
    
    /**
     * Metodo que permite agregar una sugerencia a la pila
     * @param pista, pista a agregar a la pila
     */
    public void push(String pista){
        //Comprueba si la pila esta llena y si no agrega la sugerencia
        if(this.top==this.maximo){
            System.out.print("Pila llena");
        }else{
            //se agrega la sugerencia
            this.sugerencias[this.top]=pista;
            //se actualiza el lugar de la ultima sugerencia
            this.top++;
        }
    }
    
    /**
     * Metodo que retorna la ultima pista agregada
     * @return ultima pista agregada
     */
    public String peek(){
        //si la pila esta vacia retorna null si no retorna la pista
        if(this.top==0){
            return null;
        }else{
            return this.sugerencias[this.top-1];
        }
        
    }
    
    /**
     * Metodo que elimina la ultima pista agregada a la pila
     */
    public void pop(){
        //se elimina la ultima pista
        this.sugerencias[this.top-1]=null;
        //se actualiza el lugar de la ultima sugerencia
        this.top--;
    }
}
