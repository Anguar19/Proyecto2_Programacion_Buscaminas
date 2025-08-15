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
public class TableroJuego {
    PosicionCasilla[][] Casillas;
    int CantiFilas;
    int CantiColumnas;
    int CantiMinas;
    
    int numCasillasAbiertas;
    boolean generacionMinas;
    
    private Consumer<List<PosicionCasilla>> eventoPartidaPerdida;
    private Consumer<List<PosicionCasilla>> eventoPartidaGanada;
    private Consumer<PosicionCasilla> eventoCasillaAbierta;
    
    public TableroJuego(int CantiFilas, int CantiColumnas, int CantiMinas) {
        this.CantiFilas = CantiFilas;
        this.CantiColumnas = CantiColumnas;
        this.CantiMinas = CantiMinas;
        this.InicioCasillas();
        this.generacionMinas = false;
    }
    
    public void InicioCasillas() {
        Casillas = new PosicionCasilla[this.CantiFilas][this.CantiColumnas];
        for (int a = 0; a < Casillas.length; a++) {
            for (int b = 0; b < Casillas[a].length; b++) {
                Casillas[a][b] = new PosicionCasilla(a, b);
            }
        }
    }
    
    private void CrearMinas(int filaIgnorar, int columnaIgnorar) {
        int minasCreadas = 0;
        while (minasCreadas != CantiMinas) {
            int ubiTemFila;
            int ubiTemColumna;
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
        this.impresionTablero();
    }
    
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
    
    private List<PosicionCasilla> obtenerCasillasAlrededor(int posFila, int posColumna) {
        List<PosicionCasilla> listaCasillas = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            int tmpPosFila = posFila;
            int tmpPosColumna = posColumna;
            switch (i) {
                case 0: tmpPosFila--; break; //Arriba
                case 1: tmpPosFila--; tmpPosColumna++; break; //Arriba Derecha
                case 2: tmpPosColumna++; break; //Derecha
                case 3: tmpPosColumna++; tmpPosFila++; break; //Derecha Abajo
                case 4: tmpPosFila++; break; //Abajo
                case 5: tmpPosFila++; tmpPosColumna--; break; //Abajo Izquierda
                case 6: tmpPosColumna--; break; //Izquierda
                case 7: tmpPosFila--; tmpPosColumna--; break; //Izquierda Arriba
            }
            
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
    
    public void seleccionarCasilla(int posFila, int posColumna) {
        if (!this.generacionMinas) {
            this.CrearMinas(posFila, posColumna);
        }
        
        eventoCasillaAbierta.accept(this.Casillas[posFila][posColumna]);
        
        if (this.Casillas[posFila][posColumna].isMina()) {
            eventoPartidaPerdida.accept(obtenerCasillasConMinas());
        } else if (this.Casillas[posFila][posColumna].getMinasLados() == 0) {
            marcarCasillaAbierta(posFila, posColumna);
            List<PosicionCasilla> casillasAlrededor = obtenerCasillasAlrededor(posFila, posColumna);
            for (PosicionCasilla casilla : casillasAlrededor) {
                if (!casilla.iscasillaabierta()) {
                    seleccionarCasilla(casilla.getUbicacionFila(), casilla.getUbicacionColumna());
                }
            }
        } else {
            marcarCasillaAbierta(posFila, posColumna);
        }
        
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
    
    public static void main(String[] args) {
        TableroJuego tablero = new TableroJuego(5, 5, 5);
        tablero.impresionTablero();
        System.out.println("---");
        tablero.imprimirPistas();
    }
    
}

