package ca.qc.bdeb.sim203.projetjavafx;

import java.util.ArrayList;

public class Etoiles extends Projectile {
    /**
     * CONSTRUCTEUR
     *
     * @param positionX Position en X de l'étoile
     * @param positionY Position en Y de l'étoile
     * @param charlotte Charlotte qui est sur l'écran
     * @param ennemis Ennemis qui son sur l'écran
     */
    public Etoiles(double positionX, double positionY, Charlotte charlotte, ArrayList<Ennemis> ennemis) {
        super(positionX, positionY, charlotte, ennemis);
        largeur = 36;
        hauteur = 35;
        vitesseX = 800;
        imageChoisie = images[0];
    }

    /**
     * Rafraîchir le mouvement
     *
     * @param diffTemps la différence de temps
     */
    protected void update(double diffTemps) {
        positionX += diffTemps * vitesseX;
        collisionEnnemi();
    }
}
