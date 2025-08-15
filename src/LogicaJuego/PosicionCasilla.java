/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

/**
 *
 * @author Frank
 */
public class PosicionCasilla {
    private int ubicacionFila; // LA POSICION DE LA CASILLA EN LA FILA
    private boolean Mina; // SI LA CASILLA TIENE UNA MINA 
    private int ubicacionColumna; // LA POSICION DE LA CASILLA EN LA COLUMNA
    private int minasLados; 

    public int getMinasLados() {
        return minasLados;
    }

    public void setMinasLados(int minasLados) {
        this.minasLados = minasLados;
    }
     
//CONSTRUCTOR DE UBICAIONfILA Y UBICACIONCOLUMNA, CON ESTO SE OBTINE LA COORDENADADA DE LA CASILLA
    public PosicionCasilla(int ubicacionFila, int ubicacionColumna) {
        this.ubicacionFila = ubicacionFila;
        this.ubicacionColumna = ubicacionColumna;
    }
    
     
//GETTERS Y SETTERS
    public int getUbicacionFila() {
        return ubicacionFila;
    }

    public void setUbicacionFila(int ubicacionFila) {
        this.ubicacionFila = ubicacionFila;
    }

    public boolean isMina() {
        return Mina;
    }

    public void setMina(boolean Mina) {
        this.Mina = Mina;
    }

    public int getUbicacionColumna() {
        return ubicacionColumna;
    }

    public void setUbicacionColumna(int ubicacionColumna) {
        this.ubicacionColumna = ubicacionColumna;
    }
            
      public void aumentarMinasAlrededor(){ // metodo para recorrer todas las casiilas incrementable
          this.minasLados++;
      }
}
