/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 *
 * @author jorgecisneros
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHO_PANTALLA = 800;
    static int ALTO_PANTALLA = 600;
    
    int filasMarcianos = 5;
    int columnasMarcianos = 10;
    
    int contador = 0;
    
    BufferedImage buffer = null;
    
    //Bucle de animación del juego. En este caso, es un hilo de ejecución nuevo que se 
    //encarga de refrescar el contenido de la pantalla. 
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: código de la animación. 
            bucleDelJuego();
        }
    });
    
    Marciano miMarciano = new Marciano(ANCHO_PANTALLA);
    Nave miNave = new Nave();
    
            
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        setSize(ANCHO_PANTALLA, ALTO_PANTALLA);
        //Creamos una imagen del mismo alto y ancho que el jPanel1 y lo guarda en el buffer.
        buffer = (BufferedImage) jPanel1.createImage(ANCHO_PANTALLA, ALTO_PANTALLA); 
        buffer.createGraphics(); //Esta crea el contexto gráfico del buffer. 
        
        //Arrancamos el temporizador para que empiece el juego. 
        temporizador.start();
        
        // que se situe en el centro del ancho y abajo a 100px del suelo. 
        miNave.posX = ANCHO_PANTALLA/2 - miNave.imagen.getWidth(this)/2;
        miNave.posY = ALTO_PANTALLA - 100;
                
    }
    
    //Creamos el método que va a ir en el hilo secundario. 
    private void bucleDelJuego() {
        //Este método gobierna el redibujado de los objetos en el jPanel. 
        
        //1º borro todo lo que hay en el buffer. 
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHO_PANTALLA, ALTO_PANTALLA);
        
        contador++;
        //////////////////////////////////////////////////
        if (contador < 50) {
            g2.drawImage(miMarciano.imagen1, 10, 10, null);
        }
        else if (contador <100) {
            g2.drawImage(miMarciano.imagen2, 10, 10, null);
        }
        else {
            contador = 0;
        }
        //dibuja la nave
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);
        //////////////////////////////////////////////////
        
        //2º dibujo de golpe todo el buffer sobre el jPanel. 
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
