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
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 *
 * @author jorgecisneros
 */
public class VentanaJuego extends javax.swing.JFrame {

    //declaramos las constantes de la pantalla, que pueden ser utilizados por las clases hijas. 
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
    //Vamos a declarar la estructura de los marcianos que aparecerán en pantalla. Con un ARRAY
    //Vamos a declarar una lista de dos dimensiones, por eso hay dos corchetes. 
    Marciano [][]listaMarcianos = new Marciano [filasMarcianos] [columnasMarcianos];
    boolean direccionMarciano = true; //si es true se mueve a la derecha. 
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
    
            
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
        
        //Que se situe en el centro del ancho y abajo a 100px del suelo. 
        miNave.posX = ANCHO_PANTALLA/2 - miNave.imagen.getWidth(this)/2;
        miNave.posY = ALTO_PANTALLA - 100;
        
        //Creamos el ARRAY que nos crea la estructura de los marcianos. 
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j] = new Marciano(ANCHO_PANTALLA);
                listaMarcianos[i][j].posX = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null)); //15px será la distancia entre marcianos
                listaMarcianos[i][j].posY = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));
            }
        }
        
        //Damos una posicion al disparo inicial para que no choque con ningún marciano.
        miDisparo.posY = -2000;
                
    }
    
    //Creamos el método que va a añadir la estructura de los marcianos en la pantalla
    private void pintaMarcianos(Graphics2D _g2) {
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j].mueve(direccionMarciano);
                //HAY QUE ARREGLAR, QUE SEA CUANDO UNO SOLO TOQUE, SINO NO SE MUEVEN A LA VEZ.
                //para que cuando llegue a la pared, cambie de dirección:
                if (listaMarcianos[i][j].posX >= ANCHO_PANTALLA - listaMarcianos[i][j].imagen1.getWidth(null) || listaMarcianos[i][j].posX <= 0) {
                    direccionMarciano = !direccionMarciano;
                    //Hago que todos los marcianos bajen una fila.
                    for (int k = 0; k < filasMarcianos; k++) {
                        for (int m = 0; m < columnasMarcianos; m++) {
                            listaMarcianos[k][m].posY += listaMarcianos[k][m].imagen1.getHeight(null);
                        }
                    }
                }
                //para que pinte los marcianos moviéndose:
                if (contador < 50) {
                    _g2.drawImage(listaMarcianos[i][j].imagen1, listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, null);
                }
                else if (contador < 100) {
                    _g2.drawImage(listaMarcianos[i][j].imagen2, listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, null);
                }
                else {
                    contador = 0;
                }
            }
        }
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
        pintaMarcianos(g2);
        //dibuja la nave
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);
        g2.drawImage(miDisparo.imagen, miDisparo.posX, miDisparo.posY, null);
        miNave.mueve(); //si tiene a true el boton de la derecha, sumará, si es el de la izquierda, restará.
        miDisparo.mueve();
        chequeaColision();
        
        //////////////////////////////////////////////////
        
        //2º dibujo de golpe todo el buffer sobre el jPanel. 
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);
        
    }
    
    //creamos un método que chequee si un disparo y un marciano colisionan.
    public void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        //Calculo el rectángulo que contiene al disparo
        rectanguloDisparo.setFrame(miDisparo.posX, miDisparo.posY, miDisparo.imagen.getWidth(null), miDisparo.imagen.getHeight(null));
        
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                rectanguloMarciano.setFrame(listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, listaMarcianos[i][j].imagen1.getWidth(null), listaMarcianos[i][j].imagen1.getHeight(null));
                if (rectanguloDisparo.intersects(rectanguloMarciano)) {
                    //si entra aquí es porque han chocado un marciano y el disparo.
                    listaMarcianos[i][j].posY = 2000;
                    miDisparo.posY = -2000;
                }
            }
        }

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
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

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

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT: miNave.setPulsadoIzquierda(true); break;
                
            case KeyEvent.VK_RIGHT: miNave.setPulsadoDerecha(true); break;
            
            case KeyEvent.VK_SPACE : miDisparo.posicionaDisparo(miNave);
                                     break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT : miNave.setPulsadoIzquierda(false); break;
                
            case KeyEvent.VK_RIGHT : miNave.setPulsadoDerecha(false); break;
        }
    }//GEN-LAST:event_formKeyReleased

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
