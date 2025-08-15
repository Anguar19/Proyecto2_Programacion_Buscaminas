/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package InterfazJuego;

/**
 *
 * @author Frank
 */
import LogicaJuego.PosicionCasilla;
import LogicaJuego.TableroJuego;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

public class MinasFrame extends javax.swing.JFrame {

    int numFilas=10;
    int numColumnas=10;
    int numMinas=10;
    
    JButton[][] botonesTablero;
    
    TableroJuego tableroBuscaminas;
    
      // Estadísticas del juego
    private int juegosJugados = 0;
    private int juegosGanados = 0;
    private int juegosPerdidos = 0;
    
     // Estado del juego
    private boolean juegoActivo = true;
    private int minasRestantes;
    
    // Label para mostrar minas restantes
    private JLabel lblMinasRestantes;
    
    /**
     * Creates new form MinasFrame
     */
    public MinasFrame() {
        initComponents();
        configurarVentana();
        juegoNuevo();;
        
         // Configurar acción para Nuevo Juego
    menuNuevoJuego.addActionListener(e -> juegoNuevo());
    }
    
     private void configurarVentana() {
        setTitle("Buscaminas Proyecto 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Agregar label para minas restantes en la parte superior
        lblMinasRestantes = new JLabel("Minas: " + numMinas);
        lblMinasRestantes.setFont(new Font("Arial", Font.BOLD, 14));
        lblMinasRestantes.setBounds(10, 5, 150, 20);
        getContentPane().add(lblMinasRestantes);
    }
     
    private void mostrarEstadisticas() {
        String mensaje = String.format(
            "=== ESTADÍSTICAS ===\n" +
            "Juegos Jugados: %d\n" +
            "Juegos Ganados: %d\n" +
            "Juegos Perdidos: %d\n" +
            "Porcentaje de Victoria: %.1f%%",
            juegosJugados,
            juegosGanados,
            juegosPerdidos,
            juegosJugados > 0 ? (juegosGanados * 100.0 / juegosJugados) : 0
        );
        JOptionPane.showMessageDialog(this, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }  
     
    void descargarControles(){
        if (botonesTablero!=null){
            for (int i = 0; i < botonesTablero.length; i++) {
                for (int j = 0; j < botonesTablero[i].length; j++) {
                    if (botonesTablero[i][j]!=null){
                        getContentPane().remove(botonesTablero[i][j]);
                    }
                }
            }
        }
    }
    
    private void juegoNuevo(){
        juegoActivo = true;
        minasRestantes = numMinas;
        actualizarContadorMinas();
        descargarControles();
        cargarControles();
        crearTableroBuscaminas();
        repaint();
    }
    
     /**
     * Actualiza el contador de minas restantes
     */
    private void actualizarContadorMinas() {
        if (lblMinasRestantes != null) {
            lblMinasRestantes.setText("Minas: " + minasRestantes);
        }
    }
    
    private void crearTableroBuscaminas(){
        tableroBuscaminas = new TableroJuego(numFilas, numColumnas, numMinas);
        
        // EVENTO PARTIDA PERDIDA - CON EMOJI DE MINA
        tableroBuscaminas.setEventoPartidaPerdida(new Consumer<List<PosicionCasilla>>() {
            @Override
            public void accept(List<PosicionCasilla> t) {
                juegoActivo = false;
                juegosPerdidos++;
                for(PosicionCasilla casillaConMina: t){
                    JButton boton = botonesTablero[casillaConMina.getUbicacionFila()][casillaConMina.getUbicacionColumna()];
                    boton.setText("💣"); // Emoji de bomba
                    boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16)); // Fuente que soporte emojis
                    boton.setBackground(Color.RED); // Fondo rojo para resaltar
                    boton.setForeground(Color.WHITE); // Texto blanco para contraste
                }
                preguntarNuevoJuego();
            }
        });
        
        // EVENTO PARTIDA GANADA - CON EMOJI DE CELEBRACIÓN
        tableroBuscaminas.setEventoPartidaGanada(new Consumer<List<PosicionCasilla>>() {
            @Override
            public void accept(List<PosicionCasilla> t) {
                 juegoActivo = false;
                 juegosGanados++;
                for(PosicionCasilla casillaConMina: t){
                    JButton boton = botonesTablero[casillaConMina.getUbicacionFila()][casillaConMina.getUbicacionColumna()];
                    boton.setText("🎉"); // Emoji de celebración
                    boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    boton.setBackground(Color.GREEN); // Fondo verde
                    boton.setForeground(Color.WHITE); // Texto blanco
                }
                preguntarNuevoJuego();
            }
        });
        
        // EVENTO CASILLA ABIERTA
        tableroBuscaminas.setEventoCasillaAbierta(new Consumer<PosicionCasilla>() {
            @Override
            public void accept(PosicionCasilla t) {
                JButton boton = botonesTablero[t.getUbicacionFila()][t.getUbicacionColumna()];
                boton.setEnabled(false);
                boton.setText(t.getMinasLados()==0?"":t.getMinasLados()+"");
                
                // OPCIONAL: Agregar colores a los números según la cantidad de minas alrededor
                if (t.getMinasLados() > 0) {
                    Color[] colores = {
                        Color.BLUE,    // 1
                        Color.GREEN,   // 2
                        Color.RED,     // 3
                        Color.MAGENTA, // 4
                        Color.CYAN,    // 5
                        Color.ORANGE,  // 6
                        Color.PINK,    // 7
                        Color.BLACK    // 8
                    };
                    boton.setForeground(colores[Math.min(t.getMinasLados()-1, 7)]);
                    boton.setFont(new Font("Arial", Font.BOLD, 12));
                }
            }
        });
    }
    
    private void preguntarNuevoJuego() {
        mostrarEstadisticas();
        
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas jugar de nuevo?",
            "Nuevo Juego",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            juegoNuevo();
        } else {
            System.exit(0);
        }
    }
    
     private void cargarControles() {
        int posXReferencia = 25;
        int posYReferencia = 35; // Ajustado para dejar espacio al contador
        int anchoControl = 30;
        int altoControl = 30;
        
        botonesTablero = new JButton[numFilas][numColumnas];
        
        for (int i = 0; i < botonesTablero.length; i++) {
            for (int j = 0; j < botonesTablero[i].length; j++) {
                botonesTablero[i][j] = new JButton();
                botonesTablero[i][j].setName(i + "," + j);
                botonesTablero[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                botonesTablero[i][j].setBackground(Color.LIGHT_GRAY);
                botonesTablero[i][j].setFocusPainted(false);
                
                // Calcular posición del botón
                if (i == 0 && j == 0) {
                    botonesTablero[i][j].setBounds(posXReferencia, posYReferencia, anchoControl, altoControl);
                } else if (i == 0 && j != 0) {
                    botonesTablero[i][j].setBounds(
                        botonesTablero[i][j-1].getX() + botonesTablero[i][j-1].getWidth(),
                        posYReferencia, anchoControl, altoControl);
                } else {
                    botonesTablero[i][j].setBounds(
                        botonesTablero[i-1][j].getX(),
                        botonesTablero[i-1][j].getY() + botonesTablero[i-1][j].getHeight(),
                        anchoControl, altoControl);
                }
                
                // Agregar listeners para click izquierdo y derecho
                final int fila = i;
                final int columna = j;
                
                botonesTablero[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!juegoActivo) return;
                        
                        JButton boton = (JButton) e.getSource();
                        
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            // Click izquierdo - destapar casilla
                            if (!boton.getText().equals("🚩")) {
                                tableroBuscaminas.seleccionarCasilla(fila, columna);
                            }
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            // Click derecho - marcar/desmarcar
                            if (boton.isEnabled()) {
                                if (boton.getText().equals("🚩")) {
                                    // Desmarcar
                                    boton.setText("");
                                    boton.setBackground(Color.LIGHT_GRAY);
                                    minasRestantes++;
                                } else if (minasRestantes > 0) {
                                    // Marcar
                                    boton.setText("🚩");
                                    boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                                    boton.setBackground(Color.YELLOW);
                                    minasRestantes--;
                                }
                                actualizarContadorMinas();
                            }
                        }
                    }
                });
                
                getContentPane().add(botonesTablero[i][j]);
            }
        }
        this.setSize(botonesTablero[numFilas-1][numColumnas-1].getX()+
                botonesTablero[numFilas-1][numColumnas-1].getWidth()+30,
                botonesTablero[numFilas-1][numColumnas-1].getY()+
                botonesTablero[numFilas-1][numColumnas-1].getHeight()+70
                );
    }
    
    private void btnClick(ActionEvent e) {
        JButton btn=(JButton)e.getSource();
        String[] coordenada=btn.getName().split(",");
        int posFila=Integer.parseInt(coordenada[0]);
        int posColumna=Integer.parseInt(coordenada[1]);
        //JOptionPane.showMessageDialog(rootPane, posFila+","+posColumna);
        tableroBuscaminas.seleccionarCasilla(posFila, posColumna);
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuNuevoJuego = new javax.swing.JMenuItem();
        menuTamano = new javax.swing.JMenuItem();
        menuNumeroMinas = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("Juego");

        menuNuevoJuego.setText("Nuevo");
        jMenu1.add(menuNuevoJuego);

        menuTamano.setText("Tamaño");
        menuTamano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTamanoActionPerformed(evt);
            }
        });
        jMenu1.add(menuTamano);

        menuNumeroMinas.setText("Numero MInas");
        menuNumeroMinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNumeroMinasActionPerformed(evt);
            }
        });
        jMenu1.add(menuNumeroMinas);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuTamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTamanoActionPerformed
         String input = JOptionPane.showInputDialog(
        this,
        "Digite tamaño de la matriz (n×n):\n(Debe ser mayor a 2)",
        "Configurar Tamaño",
        JOptionPane.QUESTION_MESSAGE
    );
    
    if (input != null) {
        try {
            int num = Integer.parseInt(input);
            if (num > 2) {
                this.numFilas = num;
                this.numColumnas = num;
                juegoNuevo();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "El tamaño debe ser mayor a 2",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor ingrese un número válido",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    }//GEN-LAST:event_menuTamanoActionPerformed

    private void menuNumeroMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNumeroMinasActionPerformed
       String input = JOptionPane.showInputDialog(
        this,
        "Digite número de Minas:\n(Máximo: " + (numFilas * numColumnas - 1) + ")",
        "Configurar Minas",
        JOptionPane.QUESTION_MESSAGE
    );
    
    if (input != null) {
        try {
            int num = Integer.parseInt(input);
            int maxMinas = numFilas * numColumnas - 1;
            
            if (num > 0 && num <= maxMinas) {
                this.numMinas = num;
                juegoNuevo();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "El número de minas debe estar entre 1 y " + maxMinas,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor ingrese un número válido",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    }//GEN-LAST:event_menuNumeroMinasActionPerformed

     private void menuTamanoActionPerformed() {
        String input = JOptionPane.showInputDialog(
            this,
            "Digite tamaño de la matriz (n×n):\n(Debe ser mayor a 2)",
            "Configurar Tamaño",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (input != null) {
            try {
                int num = Integer.parseInt(input);
                if (num > 2) {
                    this.numFilas = num;
                    this.numColumnas = num;
                    this.numMinas = 2 * num; // 2*L según requerimiento
                    juegoNuevo();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "El tamaño debe ser mayor a 2",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Por favor ingrese un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Acción del menú Número de Minas
     */
    private void menuNumeroMinasActionPerformed() {
        String input = JOptionPane.showInputDialog(
            this,
            "Digite número de Minas:\n(Máximo: " + (numFilas * numColumnas - 1) + ")",
            "Configurar Minas",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (input != null) {
            try {
                int num = Integer.parseInt(input);
                int maxMinas = numFilas * numColumnas - 1;
                
                if (num > 0 && num <= maxMinas) {
                    this.numMinas = num;
                    juegoNuevo();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "El número de minas debe estar entre 1 y " + maxMinas,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Por favor ingrese un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Acción del menú Salir
     */
    private void menuSalirActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarEstadisticas();
        System.exit(0);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MinasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MinasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MinasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MinasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MinasFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem menuNuevoJuego;
    private javax.swing.JMenuItem menuNumeroMinas;
    private javax.swing.JMenuItem menuTamano;
    // End of variables declaration//GEN-END:variables
}
