/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 *
 * @author kondy
 */
public class Mines {
    private boolean estado;
    private boolean activada;
    private boolean bandera;
    
    public Mines(boolean estado){
        this.estado=estado;
        this.activada=false;
        this.bandera=false;
    }
    
    public boolean getEstado(){
        return this.estado;
    }
    
    public boolean getActivado(){
        return this.activada;
    }
    
    public void setActivado(){
        this.activada=true;
    }
    
    public boolean getBandera(){
        return this.bandera;
    }
    
    public void setBandera(boolean bandera){
        this.bandera=bandera;
    }
    
    public int getMina(){
        if(estado){
            return 10;
        }else{
            return 0;
        }
    }
}
