/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 *
 * @author kondy
 */
public class Nodo {
    private Nodo next;
    private int i;
    private int j;
    private boolean esMina;
    
    public Nodo(int i,int j){
        this.i=i;
        this.j=j;
        this.next= null;
        this.esMina=false;
    }
    public void setNext(Nodo nodo){
        this.next=nodo;
    }
    
    public void setI(int i){
        this.i=i;
    }
    
    public void setJ(int j){
        this.j=j;
    }
    
    public Nodo getNext(){
        return this.next;
    }
    
    public int getI(){
        return this.i;
    }
    
    public int getJ(){
        return this.j;
    }
    
    public void setEsMina(){
        this.esMina=true;
    }
    
    public boolean getEsMina(){
        return this.esMina;
    }
}
