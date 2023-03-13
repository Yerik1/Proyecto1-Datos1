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
    
    public Mines(boolean estado){
        this.estado=estado;
    }
    
    public boolean getEstado(){
        return this.estado;
    }
    
    public int getMina(){
        if(estado){
            return 10;
        }else{
            return 0;
        }
    }
}
