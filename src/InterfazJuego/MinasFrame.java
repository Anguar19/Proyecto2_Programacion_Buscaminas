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
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

public class MinasFrame extends javax.swing.JFrame {

    int numFilas=10;
    int numColumnas=10;
    int numMinas=10;
    
    JButton[][] botonesTablero;
    
    TableroJuego tableroBuscaminas;
    
    /**
     * Creates new form MinasFrame
     */
    public MinasFrame() {
        initComponents();
        juegoNuevo();
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
        descargarControles();
        cargarControles();
        crearTableroBuscaminas();
        repaint();
    }
    
    private void crearTableroBuscaminas(){
        tableroBuscaminas = new TableroJuego(numFilas, numColumnas, numMinas);
        
        // EVENTO PARTIDA PERDIDA - CON EMOJI DE MINA
        tableroBuscaminas.setEventoPartidaPerdida(new Consumer<List<PosicionCasilla>>() {
            @Override
            public void accept(List<PosicionCasilla> t) {
                for(PosicionCasilla casillaConMina: t){
                    JButton boton = botonesTablero[casillaConMina.getUbicacionFila()][casillaConMina.getUbicacionColumna()];
                    boton.setText("💣"); // Emoji de bomba
                    boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16)); // Fuente que soporte emojis
                    boton.setBackground(Color.RED); // Fondo rojo para resaltar
                    boton.setForeground(Color.WHITE); // Texto blanco para contraste
                }
            }
        });
        
        // EVENTO PARTIDA GANADA - CON EMOJI DE CELEBRACIÓN
        tableroBuscaminas.setEventoPartidaGanada(new Consumer<List<PosicionCasilla>>() {
            @Override
            public void accept(List<PosicionCasilla> t) {
                for(PosicionCasilla casillaConMina: t){
                    JButton boton = botonesTablero[casillaConMina.getUbicacionFila()][casillaConMina.getUbicacionColumna()];
                    boton.setText("🎉"); // Emoji de celebración
                    boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    boton.setBackground(Color.GREEN); // Fondo verde
                    boton.setForeground(Color.WHITE); // Texto blanco
                }
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
    
    private void cargarControles(){
        
        int posXReferencia=25;
        int posYReferencia=25;
        int anchoControl=30;
        int altoControl=30;
        
        botonesTablero = new JButton[numFilas][numColumnas];
        for (int i = 0; i < botonesTablero.length; i++) {
            for (int j = 0; j < botonesTablero[i].length; j++) {
                botonesTablero[i][j]=new JButton();
                botonesTablero[i][j].setName(i+","+j);
                botonesTablero[i][j].setBorder(null);
                if (i==0 && j==0){
                    botonesTablero[i][j].setBounds(posXReferencia, 
                            posYReferencia, anchoControl, altoControl);
                    
                }else if (i==0 && j!=0){
                    botonesTablero[i][j].setBounds(
                            botonesTablero[i][j-1].getX()+botonesTablero[i][j-1].getWidth(), 
                            posYReferencia, anchoControl, altoControl);
                }else{
                    botonesTablero[i][j].setBounds(
                            botonesTablero[i-1][j].getX(), 
                            botonesTablero[i-1][j].getY()+botonesTablero[i-1][j].getHeight(), 
                            anchoControl, altoControl);                    
                }
                botonesTablero[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnClick(e);
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
            .addGap(0, 277, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuTamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTamanoActionPerformed
        int num=Integer.parseInt(JOptionPane.showInputDialog("Digite tamaño de la matriz, n*n"));
        this.numFilas=num;
        this.numColumnas=num;
        juegoNuevo();
    }//GEN-LAST:event_menuTamanoActionPerformed

    private void menuNumeroMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNumeroMinasActionPerformed
       int num=Integer.parseInt(JOptionPane.showInputDialog("Digite número de Minas"));
        this.numMinas=num;
        juegoNuevo();

    }//GEN-LAST:event_menuNumeroMinasActionPerformed

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
