/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1datos1;

/**
 * Clase que asemeja un tablero de juego
 * @author Yerik
 */
public class Tablero {
    //tamaño tablero
    int tamaño=8;
    
    //array de objetos casilla
    private Casilla[][] tablero;
    
    /**
     * constructor de la clase tablero 
     * @param total total de minas a colocar
     */
    public Tablero(int total){
        //se inicializa el array
        tablero=new Casilla[this.tamaño][this.tamaño];
        
        //se llena el tablero
        fillTablero(total);
    }
    
    /**
     * metodo que llena el tablero de juego con la cantidad de minas establecidas
     * @param total total de minas
     */
    public void fillTablero(int total){
        //contador de minas colocadas
        int cont=0;
        
        //mientras el contador sea menor al total
        while(cont<total){
            
            //se recorre toda la matriz
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    
                    //probabilidad de colocar una mina
                    int rand= (int)(Math.random()*5+1);
                    
                    //si el numero es 1 se coloca una mina
                    if(rand>1){
                        if(this.tablero[i][j]==null){
                            this.tablero[i][j]=new Casilla(false);
                        }
                    }else{
                        if(cont<total){
                            this.tablero[i][j]=new Casilla(true);
                            cont++;
                        }
                        
                        //en caso que se llegue al total antes de terminar de 
                        //recorrer el tablero y se cumpla la probabilidad,
                        //se colocan casillas vacias
                        if(this.tablero[i][j]==null){
                            this.tablero[i][j]=new Casilla(false);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Revisa si todas las casillas del tablero que no son minas fueron jugadas
     * @return true si se recorrieron todsa, false en caso que no
     */
    public boolean checkTerminar(){
        
        //se recorre todo el tablero
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                //se obtiene si la casilla fue jugada y si es distinta de mina
                if(!this.tablero[i][j].getActivado()){
                    if(!this.tablero[i][j].getEstado()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * retrona el tablero de juego
     * @return tablero de juego
     */
    public Casilla[][] getTablero(){
        return this.tablero;
    }
    
    /**
     * Actualiza el tablero de juego
     * @param tablero tblero con el cual se actualiza
     */
    public void setTablero(Casilla[][] tablero){
        this.tablero=tablero;
    }
}
