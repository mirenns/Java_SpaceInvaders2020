
package codigo;

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author mirenordonezdearce
 */
public class Marciano {
    
    public Image imagen1 = null;
    public Image imagen2 = null;
    
    public int posX = 0;
    public int posY = 0;
    
    //nos va a servir para que el marciano cuando se mueva por la pantalla sepa cuánto mide de ancho
    //para moverse sólo por el ancho y no más allá. 
    private int anchoPantalla; 
    
    public int vida = 50;
    
    public Marciano(int _anchoPantalla) { 
        anchoPantalla = _anchoPantalla;
        try {
            imagen1 = ImageIO.read(getClass().getResource("/imagenes/marcianito1.png"));
            imagen2 = ImageIO.read(getClass().getResource("/imagenes/marcianito2.png"));
        }
        catch (Exception e){
        
        }
    }
    
    public void mueve(boolean direccion) { //habrá que mejorarlo, porque cuando llegue a la pared no debe seguir moviéndose. 
        if (direccion) { //si direccion vale true, se mueve a la derecha. 
            posX++;
        }
        else { //si vale false, a la izquiera. 
            posX--;
        }
    }
}
