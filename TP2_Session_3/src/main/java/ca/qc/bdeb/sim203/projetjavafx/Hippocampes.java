package ca.qc.bdeb.sim203.projetjavafx;


import java.util.ArrayList;

public class Hippocampes extends Projectile {
    /**
     * CONSTRUCTEUR
     *
     * @param positionX Position en X de l'hippocampes
     * @param positionY Position en Y de l'hippocampes
     * @param charlotte Charlotte qui est sur l'écran
     * @param ennemis Les ennemis sur l'écran
     * @param tempsDuLancer Le moment où le projectile est lancé
     */
    public Hippocampes(double positionX, double positionY, Charlotte charlotte, ArrayList<Ennemis> ennemis, long tempsDuLancer) {
        super(positionX, positionY, charlotte, ennemis);
        this.tempsDuLancer = tempsDuLancer;
        positionInitial = positionY;
        largeur = 20;
        hauteur = 36;
        vitesseX = 500;
        imageChoisie = images[1];
        amplitude = 30 + random.nextInt(31);
        if (random.nextBoolean()){
            amplitude *= -1;
        }
        periode = 1 + random.nextDouble(2);
    }

    /**
     * Rafraîchir le mouvement
     *
     * @param diffTemps La différence de temps
     */
    @Override
    protected void update(double diffTemps) {
        positionX += diffTemps * vitesseX;
        collisionEnnemi();
    }
}
