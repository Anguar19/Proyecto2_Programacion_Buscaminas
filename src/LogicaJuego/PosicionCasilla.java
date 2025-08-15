/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

/**
 *
 * @author Frank
 */

/**
 * Representa una casilla individual en el tablero de Buscaminas.
 * Almacena información sobre su posición, si contiene mina, número de minas adyacentes
 * y si ha sido abierta por el jugador.
 */

public class PosicionCasilla {
    private int ubicacionFila; // Posición vertical en el tablero
    private boolean Mina; // Indica si la casilla contiene una mina
    private int ubicacionColumna; // Posición horizontal en el tablero
    private int minasLados;  // Número de minas en casillas adyacentes
    private boolean casillaabierta; // Indica si la casilla ha sido revelada
     
 /**
     * Constructor que inicializa una casilla con sus coordenadas
     * @param ubicacionFila Fila donde se encuentra la casilla
     * @param ubicacionColumna Columna donde se encuentra la casilla
     */
    
    public PosicionCasilla(int ubicacionFila, int ubicacionColumna) {
        this.ubicacionFila = ubicacionFila;
        this.ubicacionColumna = ubicacionColumna;
    }
    
     
 // Métodos getters y setters para acceder a los atributos
    
    /**
     * Incrementa el contador de minas adyacentes
     */
    
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
        public int getMinasLados() {
        return minasLados;
    }

    public void setMinasLados(int minasLados) {
        this.minasLados = minasLados;
    }        
      public void aumentarMinasAlrededor(){ //para recorrer todas las casiilas incrementable
          this.minasLados++;
      }
      public boolean iscasillaabierta() {
        return casillaabierta;
    }

    public void setcasillaabierta(boolean casillaabierta) {
        this.casillaabierta = casillaabierta;
    }
}
