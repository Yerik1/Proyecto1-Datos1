/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 * Clase que representa un nodo de una lista enlazada
 * @author Yerik
 */
public class Nodo {
    //nodo siguiente
    private Nodo next;
    //coordenada de fila
    private int i;
    //coordenada de columna
    private int j;
    //estado si es completamente seguro para un humano saber si hay mina en la
    //casilla representada por el nodo
    private boolean esMina;
    
    /**
     * Constructor de Objeto nodo
     * @param i, coordenada de fila del nodo
     * @param j, coordenada de columna del nodo
     */
    public Nodo(int i,int j){
        this.i=i;
        this.j=j;
        //no tiene nodo siguiente ni se sabe si es mina
        this.next= null;
        this.esMina=false;
    }
    
    /**
     * Metodo que permite cambiar el valor de nodo siguiente
     * @param nodo, nodo que va a ser el siguiente al nodo actual
     */
    public void setNext(Nodo nodo){
        this.next=nodo;
    }
    
    /**
     * Metodo que permite cambiar la coordenada i del nodo
     * @param i, coordenada de fila por la cual se va a cambiar
     */
    public void setI(int i){
        this.i=i;
    }
    
    /**
     * Metodo que permite cambiar la coordenada i del nodo
     * @param j, coordenada de fila por la cual se va a cambiar
     */
    public void setJ(int j){
        this.j=j;
    }
    
    /**
     * Metodo que retorna el nodo siguiente
     * @return nodo siguiente
     */
    public Nodo getNext(){
        return this.next;
    }
    
    /**
     * Metodo que retorna la coordenada i del nodo
     * @return coordenada i del nodo
     */
    public int getI(){
        return this.i;
    }
    
    /**
     * Metodo que retorna la coordenada j del nodo
     * @return coordenada j del nodo
     */
    public int getJ(){
        return this.j;
    }
    
    /**
     * Metodo que modifica el valor si es humanamente posible saber si es mina
     */
    public void setEsMina(){
        this.esMina=true;
    }
    
    /**
     * Metodo que retorna si es humana mente posible saber si es mina
     * @return si es humana mente posible saber si es mina
     */
    public boolean getEsMina(){
        return this.esMina;
    }
}
