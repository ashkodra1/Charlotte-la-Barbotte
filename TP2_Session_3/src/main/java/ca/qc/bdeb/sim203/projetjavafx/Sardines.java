package ca.qc.bdeb.sim203.projetjavafx;

import java.util.ArrayList;

public class Sardines extends Projectile{


    /**
     * CONSTRUCTION
     *
     * @param positionX position en X des sardines
     * @param positionY position en Y des sardines
     * @param charlotte Charlotte qui est sur l'écran
     * @param ennemis Les ennemis sur l'écran
     */
    public Sardines(double positionX, double positionY, Charlotte charlotte, ArrayList<Ennemis> ennemis) {
        super(positionX, positionY, charlotte, ennemis);
        vitesseX = 300;
        vitesseY = 0;
        largeur = 35;
        hauteur = 29;
        imageChoisie = images[2];
    }

    /** Pour bien afficher le mouvement
     * @param diffTemps la différence de temps
     */
    @Override
    protected void update(double diffTemps) {
        double forceElectrique;
        double forceElectriqueX = 0;
        double forceElectriqueY = 0;
        if (!ennemis.isEmpty()) {
            for (Ennemis ennemi: ennemis) {
                if (ennemi.positionX > charlotte.positionX) {
                    double distanceX = (ennemi.positionX - (positionX + largeur)) * (ennemi.positionX - (positionX + largeur));
                    double distanceY = (ennemi.positionY - positionY) * (ennemi.positionY - positionY);
                    double distance = Math.sqrt(distanceX + distanceY);
                    if (distance < 0.01) {
                        distance = 0.01;
                    }
                    double deltaX = positionX - ennemi.positionX;
                    double deltaY = positionY - ennemi.positionY;
                    double proportionX = deltaX / distance;
                    double proportionY = deltaY / distance;
                    forceElectrique = (1000*-100*200)/(distance * distance);
                    forceElectriqueX += forceElectrique * proportionX;
                    forceElectriqueY += forceElectrique * proportionY;
                }
            }
            vitesseX += diffTemps * forceElectriqueX;
            vitesseY += diffTemps * forceElectriqueY;
        }
        if (vitesseX > 500) {
            vitesseX = 500;
        } else if (vitesseX < 300) {
            vitesseX = 300;
        }
        if (vitesseY > 500) {
            vitesseY = 500;
        } else if (vitesseY < -500) {
            vitesseY = -500;
        }
        double positionYTemp = positionY + (diffTemps * vitesseY);
        if ((positionYTemp <= 0 && vitesseY < 0) || (vitesseY > 0 && positionYTemp + hauteur >= Main.HAUTEUR)) {
            vitesseY *= -1;
        }
        positionX += diffTemps * vitesseX;
        positionY += diffTemps * vitesseY;
        collisionEnnemi();
    }
}
