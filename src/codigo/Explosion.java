/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author mirenordonezdearce
 */
public class Explosion {
    //declaramos las dos imágenes de la explosión
    Image imagen1 = null;
    Image imagen2 = null;
    
    //declaramos la posición de la imagen. 
    public int posX = 0;
    public int posY = 0;
    
    //declaramos el tiempo que irá reduciéndose cuando se produzca la explosión, para que aparezcan las imágenes de la misma. 
    public int tiempoDeVida = 50;
    
    //Declaramos el sonido de la explosión. Para inicializarlo vamos a necesitar un try catch
    Clip sonidoExplosion;
    
    public Explosion() {
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/explosion.wav")));
        } 
        catch (LineUnavailableException ex) {
            
        } 
        catch (UnsupportedAudioFileException ex) {
            
        } 
        catch (IOException ex) {
            
        }
    }
}

