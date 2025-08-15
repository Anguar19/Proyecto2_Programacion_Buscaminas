/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;


/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */

/**
 * Contiene la lógica principal del juego Buscaminas.
 * Maneja la creación del tablero, colocación de minas, apertura de casillas
 * y verificación de victoria/derrota.
 */

public class TableroJuego {
    PosicionCasilla[][] Casillas; // Matriz que representa el tablero
    int CantiFilas;  // Número de filas del tablero
    int CantiColumnas; // Número de columnas del tablero
    int CantiMinas;  // Número total de minas
    
    int numCasillasAbiertas; // Contador de casillas abiertas
    boolean generacionMinas; // Indica si las minas han sido colocadas
    
    // Eventos para manejar el flujo del juego
    private Consumer<List<PosicionCasilla>> eventoPartidaPerdida;
    private Consumer<List<PosicionCasilla>> eventoPartidaGanada;
    private Consumer<PosicionCasilla> eventoCasillaAbierta;
    
    
    /**
     * Constructor que inicializa el tablero con las dimensiones especificadas
     * @param CantiFilas Número de filas
     * @param CantiColumnas Número de columnas
     * @param CantiMinas Número de minas
     */
    
    public TableroJuego(int CantiFilas, int CantiColumnas, int CantiMinas) {
        this.CantiFilas = CantiFilas;
        this.CantiColumnas = CantiColumnas;
        this.CantiMinas = CantiMinas;
        this.InicioCasillas();
        this.generacionMinas = false;
    }
    
    /**
     * Inicializa la matriz de casillas
     */

    public void InicioCasillas() {
        Casillas = new PosicionCasilla[this.CantiFilas][this.CantiColumnas];
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                Casillas[a][b] = new PosicionCasilla(a, b);
            }
        }
    }
    
     /**
     * Coloca minas aleatoriamente en el tablero, evitando la casilla inicial
     * @param filaIgnorar Fila de la casilla inicial (no contendrá mina)
     * @param columnaIgnorar Columna de la casilla inicial (no contendrá mina)
     */
    
    private void CrearMinas(int filaIgnorar, int columnaIgnorar) {
        int minasCreadas = 0;
        while (minasCreadas != CantiMinas) {
            // Genera posiciones aleatorias hasta encontrar una válida
            int ubiTemFila;
            int ubiTemColumna;
            // Verifica que no sea la casilla inicial y que no tenga mina
            do {
                ubiTemFila = (int) (Math.random() * Casillas.length);
                ubiTemColumna = (int) (Math.random() * Casillas[0].length);
            } while ((ubiTemFila == filaIgnorar && ubiTemColumna == columnaIgnorar)
                    || Casillas[ubiTemFila][ubiTemColumna].isMina());
            
            Casillas[ubiTemFila][ubiTemColumna].setMina(true);
            minasCreadas++;
        }
        actualizarNumeroMinasAlrededor();
        this.generacionMinas = true;
        //this.impresionTablero();//
    }
    
     /**
     * Calcula el número de minas adyacentes para cada casilla
     */
    
    private void actualizarNumeroMinasAlrededor() {
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                if (Casillas[a][b].isMina()) {
                    List<PosicionCasilla> casillasAlrededor = obtenerCasillasAlrededor(a, b);
                    casillasAlrededor.forEach((c) -> c.aumentarMinasAlrededor());
                }
            }
        }
    }
    
    /**
     * Obtiene las casillas adyacentes a una posición dada
     * @param posFila Fila de la casilla central
     * @param posColumna Columna de la casilla central
     * @return Lista de casillas adyacentes válidas
     */
    
    private List<PosicionCasilla> obtenerCasillasAlrededor(int posFila, int posColumna) {
        List<PosicionCasilla> listaCasillas = new LinkedList<>();
        // Verifica las 8 direcciones posibles
        for (int a = 0; a < 8; a++) {
            int tmpPosFila = posFila;
            int tmpPosColumna = posColumna;
            // Ajusta coordenadas según la dirección
            switch (a) {
                case 0: tmpPosFila--; break; //Arriba
                case 1: tmpPosFila--; tmpPosColumna++; break; //Arriba Derecha
                case 2: tmpPosColumna++; break; //Derecha
                case 3: tmpPosColumna++; tmpPosFila++; break; //Derecha Abajo
                case 4: tmpPosFila++; break; //Abajo
                case 5: tmpPosFila++; tmpPosColumna--; break; //Abajo Izquierda
                case 6: tmpPosColumna--; break; //Izquierda
                case 7: tmpPosFila--; tmpPosColumna--; break; //Izquierda Arriba
            }
            // Verifica que las coordenadas estén dentro del tablero
            if (tmpPosFila >= 0 && tmpPosFila < this.Casillas.length
                    && tmpPosColumna >= 0 && tmpPosColumna < this.Casillas[0].length) {
                listaCasillas.add(this.Casillas[tmpPosFila][tmpPosColumna]);
            }
        }
        return listaCasillas;
    }
    
    List<PosicionCasilla> obtenerCasillasConMinas() {
        List<PosicionCasilla> casillasConMinas = new LinkedList<>();
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                if (Casillas[a][b].isMina()) {
                    casillasConMinas.add(Casillas[a][b]);
                }
            }
        }
        return casillasConMinas;
    }
    
    /**
     * Maneja la selección de una casilla por el jugador
     * @param posFila Fila de la casilla seleccionada
     * @param posColumna Columna de la casilla seleccionada
     */
    
    public void seleccionarCasilla(int posFila, int posColumna) {
        if (!this.generacionMinas) {
            this.CrearMinas(posFila, posColumna);
        }
         // Notifica que la casilla fue abierta
        eventoCasillaAbierta.accept(this.Casillas[posFila][posColumna]);
        
        if (this.Casillas[posFila][posColumna].isMina()) {
           // Caso mina - partida perdida 
            eventoPartidaPerdida.accept(obtenerCasillasConMinas());
        } else if (this.Casillas[posFila][posColumna].getMinasLados() == 0) {
             // Caso casilla vacía - abre recursivamente casillas adyacentes
            marcarCasillaAbierta(posFila, posColumna);
            List<PosicionCasilla> casillasAlrededor = obtenerCasillasAlrededor(posFila, posColumna);
            for (PosicionCasilla casilla : casillasAlrededor) {
                if (!casilla.iscasillaabierta()) {
                    seleccionarCasilla(casilla.getUbicacionFila(), casilla.getUbicacionColumna());
                }
            }
        } else {
            // Caso casilla con número - solo abre esta
            marcarCasillaAbierta(posFila, posColumna);
        }
        // Verifica si el jugador ganó
        if (partidaGanada()) {
            eventoPartidaGanada.accept(obtenerCasillasConMinas());
        }
    }
    
    void marcarCasillaAbierta(int posFila, int posColumna) {
        if (!this.Casillas[posFila][posColumna].iscasillaabierta()) {
            numCasillasAbiertas++;
            this.Casillas[posFila][posColumna].setcasillaabierta(true);
        }
    }
    
    /**
     * Verifica si el jugador ha ganado (todas las casillas sin mina abiertas)
     * @return true si el jugador ganó, false en caso contrario
     */
    boolean partidaGanada() {
        return numCasillasAbiertas >= (CantiFilas * CantiColumnas) - CantiMinas;
    }
    
    private void impresionTablero() {
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                System.out.print(Casillas[a][b].isMina() ? "*" : "0");
            }
            System.out.println();
        }
    }
    
    private void imprimirPistas() {
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                System.out.print(Casillas[a][b].getMinasLados());
            }
            System.out.println();
        }
    }
    
    // Setters para los eventos
    public void setEventoPartidaPerdida(Consumer<List<PosicionCasilla>> eventoPartidaPerdida) {
        this.eventoPartidaPerdida = eventoPartidaPerdida;
    }
    
    public void setEventoCasillaAbierta(Consumer<PosicionCasilla> eventoCasillaAbierta) {
        this.eventoCasillaAbierta = eventoCasillaAbierta;
    }
    
    public void setEventoPartidaGanada(Consumer<List<PosicionCasilla>> eventoPartidaGanada) {
        this.eventoPartidaGanada = eventoPartidaGanada;
    }
    
    
    
}

