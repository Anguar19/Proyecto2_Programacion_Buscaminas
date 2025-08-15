/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class TableroJuego {
    PosicionCasilla[][] Casillas;
    
    public int CantiFilas;
    public int CantiColumnas;
    public int CantiMinas;
    


    public TableroJuego(int cantidadFilas, int cantidadColumnas, int cantidadMinas) {
        this.CantiFilas = cantidadFilas;
        this.CantiColumnas = cantidadColumnas;
        this.CantiMinas = cantidadMinas;
    }
    
    public void iniciarCasilla(){
        Casillas = new PosicionCasilla[this.CantiFilas][this.CantiColumnas];
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                Casillas[a][b] = new PosicionCasilla(a, b);
            }
        }
    }
}
