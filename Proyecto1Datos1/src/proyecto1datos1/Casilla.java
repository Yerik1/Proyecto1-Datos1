/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 * Clase que representa una casilla de un tablero
 * @author Yerik
 */
public class Casilla {
    //booleano que indica si la casilla es mina
    private boolean mina;
    //booleano que indica si la casilla fue presionada
    private boolean activada;
    //booleano que indica si la casilla tiene una bandera
    private boolean bandera;
    
    /**
     * Constructor de objeto casilla 
     * @param estado, indica si la casilla tiene una mina
     */
    public Casilla(boolean estado){
        //se cambia el estado de la mina por el indicado en el parametro
        this.mina=estado;
        //se inician en falso debido a que son casillas nuevas
        this.activada=false;
        this.bandera=false;
    }
    
    /**
     * Metdod que retorna el estado de la mina
     * @return estado de la mina
     */
    public boolean getEstado(){
        return this.mina;
    }
    
    /**
     * Metodo que retorna si la casiila fue 
     * @return si la casilla esta activada
     */
    public boolean getActivado(){
        return this.activada;
    }
    
    /**
     * Metodo que cambia si hay mina en la casilla
     */
    public void setActivado(){
        this.activada=true;
    }
    
    /**
     * Metodo que retorna si hay bandera en la casilla
     * @return sila casilla tiene bandera
     */
    public boolean getBandera(){
        return this.bandera;
    }
    
    /**
     * Metodo que coloca o quita una bandera de la casilla
     * @param bandera, booleano que indica si poner o quitar bandera
     */
    public void setBandera(boolean bandera){
        this.bandera=bandera;
    }
    
    /**
     * Metodo que retorna si la casilla tiene una mina
     * @return 0 si no hay mina o 10 si hay mina
     */
    public int getMina(){
        //comprueba si hay mina
        if(this.mina){
            return 10;
        }else{
            return 0;
        }
    }
}
