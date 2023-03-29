/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 *
 * @author kondy
 */
public class Pila {
    private int maximo=13;
    private String[] sugerencias;
    private int top;
    
    public Pila(){
        this.sugerencias=new String[maximo];
        this.top=0;
    }
    
    public void push(String pista){
        if(this.top==this.maximo){
            System.out.print("Pila llena");
        }else{
            this.sugerencias[this.top]=pista;
            this.top++;
        }
    }
    
    public String peek(){
        if(this.top==0){
            return null;
        }else{
            return this.sugerencias[this.top-1];
        }
        
    }
    
    public void pop(){
        this.sugerencias[this.top-1]=null;
        this.top--;
    }
}
