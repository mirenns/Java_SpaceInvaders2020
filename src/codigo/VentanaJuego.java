/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
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
    
    //Buffer para guardar las imágenes de todos los marcianos. 
    BufferedImage plantilla = null;
    Image[] imagenes = new Image[30];
    
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
    //Creamos un arrayList para los disparos. Nos permite tener varios disparos en la pantalla. Se tienen que llamar igual que la clase. 
    ArrayList<Disparo> listaDisparos = new ArrayList(); 
    ArrayList<Explosion> listaExplosiones = new ArrayList(); //Creamos la explosión cuando se produzca la colisión entre marciano y disparo. 
            
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        //Cargamos la plantilla con las imágenes
        try {
            plantilla = ImageIO.read(getClass().getResource("/imagenes/invaders2.png"));
        }
        catch (IOException ex){
        }
        //Esto sirve para cargar las subimagenes de la plantilla en el array de bufferedimages
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) { 
                imagenes[i*4 + j] = plantilla.getSubimage(j*64, i*64, 64, 64).getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            }
        }
        //Cargamos las imágenes de dos naves diferentes. 
        imagenes[20] = plantilla.getSubimage(0, 320, 66, 32);
        imagenes[21] = plantilla.getSubimage(66, 320, 64, 32);
        
        //Cargamos las imágenes de las explosiones. 
        imagenes[22] = plantilla.getSubimage(255, 320, 32, 32); //Explosión parteB
        imagenes[23] = plantilla.getSubimage(255, 289, 32, 32); //Explosión parteA
        
        setSize(ANCHO_PANTALLA, ALTO_PANTALLA);
        //Creamos una imagen del mismo alto y ancho que el jPanel1 y lo guarda en el buffer.
        buffer = (BufferedImage) jPanel1.createImage(ANCHO_PANTALLA, ALTO_PANTALLA); 
        buffer.createGraphics(); //Esta crea el contexto gráfico del buffer. 
        
        //Arrancamos el temporizador para que empiece el juego. 
        temporizador.start();
        
        //Cargamos la imagen 20, que es la de la nave. 
        miNave.imagen = imagenes[21];
        //Que se situe en el centro del ancho y abajo a 100px del suelo. 
        miNave.posX = ANCHO_PANTALLA/2 - miNave.imagen.getWidth(this)/2;
        miNave.posY = ALTO_PANTALLA - 100;
        
        //Creamos el ARRAY que nos crea la estructura de los marcianos. 
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j] = new Marciano(ANCHO_PANTALLA);
                listaMarcianos[i][j].imagen1 = imagenes[2*i];
                listaMarcianos[i][j].imagen2 = imagenes[2*i+1];
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
    
    private void pintaDisparos (Graphics2D g2) {
        //Pinta todos los disparos
        Disparo disparoAux;
        for (int i=0; i<listaDisparos.size(); i++) {
            disparoAux = listaDisparos.get(i);
            disparoAux.mueve();
            if (disparoAux.posY < 0) { //Cuando salga por encima de la pantalla, que desaparezca. 
                listaDisparos.remove(i);
            }
            else {
                g2.drawImage(disparoAux.imagen, disparoAux.posX, disparoAux.posY, null);
            }
        }
    }
    
    private void pintaExplosiones (Graphics2D g2) {
        //Pinta todas las explosiones
        Explosion explosionAux;
        for (int i=0; i<listaExplosiones.size(); i++) {
            explosionAux = listaExplosiones.get(i);
            //tenemos que hacer que la explosión desaparezca después de un determinado tiempo. Para eso usamos el tiempoDeVida declarado en la clase Explosión
            explosionAux.tiempoDeVida --; //Empezaba en 50, y va a ir restando. 
            if (explosionAux.tiempoDeVida > 25) {
                g2.drawImage(explosionAux.imagen1, explosionAux.posX, explosionAux.posY, null); //que pinte imagenes[23]
            }
            else {
                g2.drawImage(explosionAux.imagen2, explosionAux.posX, explosionAux.posY, null); //que pinte imagenes[22]
            }
            if (explosionAux.tiempoDeVida <= 0) {
                listaExplosiones.remove(i); //que cuando tiempoDeVida llegue a 0, desaparezca. 
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
        pintaDisparos(g2); //que pinte los disparos en la pantalla
        miNave.mueve(); //si tiene a true el boton de la derecha, sumará, si es el de la izquierda, restará.
        chequeaColision(); //que chequee si hay colisión entre marciano y disparo. 
        pintaExplosiones(g2); //que pinte las explosiones en la pantalla
        
        //////////////////////////////////////////////////
        
        //2º dibujo de golpe todo el buffer sobre el jPanel. 
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);
        
    }
    
    //creamos un método que chequee si un disparo y un marciano colisionan.
    public void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        for (int k = 0; k < listaDisparos.size(); k++) {

            //Calculo el rectángulo que contiene al disparo
            rectanguloDisparo.setFrame(listaDisparos.get(k).posX, listaDisparos.get(k).posY, listaDisparos.get(k).imagen.getWidth(null), listaDisparos.get(k).imagen.getHeight(null));

            for (int i = 0; i < filasMarcianos; i++) {
                for (int j = 0; j < columnasMarcianos; j++) {
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, listaMarcianos[i][j].imagen1.getWidth(null), listaMarcianos[i][j].imagen1.getHeight(null));
                    if (rectanguloDisparo.intersects(rectanguloMarciano)) {
                        //si entra aquí es porque han chocado un marciano y el disparo.
                        
                        //Aquí pintamos las explosiones, en la posición donde se produce la colisión, para cuando el disparo choque con el marciano. 
                        Explosion e = new Explosion();
                        e.posX = listaMarcianos[i][j].posX;
                        e.posY = listaMarcianos[i][j].posY;
                        e.imagen1 = imagenes[23]; //que se pinte la parteA de la explosión.
                        e.imagen2 = imagenes[22]; //que se pinte la parteB de la explosión. 
                        listaExplosiones.add(e); //lo añadimos al arrayList de explosiones. 
                        e.sonidoExplosion.start();//hacemos que suene la explosión. 
                        
                        listaMarcianos[i][j].posY = 2000; //esto hace que el marciano desaparezca. 
                        listaDisparos.remove(k); //esto hace que el disparo desaparezca. 
                    }
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
            
            case KeyEvent.VK_SPACE : Disparo d = new Disparo();
                                     d.sonidoDisparo.start();
                                     d.posicionaDisparo(miNave);
                                     //Agregamos el disparo a la lista de disparos. 
                                     listaDisparos.add(d);
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
