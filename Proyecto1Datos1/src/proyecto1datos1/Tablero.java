/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 *
 * @author Yerik
 */
public class Tablero {
    int tamaño=8;
    private Casilla[][] tablero;
    
    public Tablero(int total){
        tablero=new Casilla[this.tamaño][this.tamaño];
        fillTablero(total);
    }
    
    public void fillTablero(int total){
        int cont=0;
        while(cont<total){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    int rand= (int)(Math.random()*5+1);
                    if(rand>1){
                        if(this.tablero[i][j]==null){
                            this.tablero[i][j]=new Casilla(false);
                        }
                    }else{
                        if(cont<total){
                            this.tablero[i][j]=new Casilla(true);
                            cont++;
                        }
                        if(this.tablero[i][j]==null){
                            this.tablero[i][j]=new Casilla(false);
                        }
                    }
                }
            }
        }
    }
    
    public boolean checkTerminar(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(!this.tablero[i][j].getActivado()){
                    if(!this.tablero[i][j].getEstado()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public Casilla[][] getTablero(){
        return this.tablero;
    }
    
    public void setTablero(Casilla[][] tablero){
        this.tablero=tablero;
    }
}
