package ca.qc.bdeb.sim203.projetjavafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Ennemis extends Mouvement{
    /**
     * CONSTRUCTEUR
     *
     * @param positionX Position en X de l'ennemis
     * @param positionY Position en Y de l'ennemis
     * @param numeroNiveau Le numéro du niveau
     * @param modeDeboggage Boolean déterminant si on est en mode déboggage
     */
    public Ennemis(double positionX, double positionY, int numeroNiveau, boolean modeDeboggage) {
        super(positionX, positionY);
        this.modeDeboggage = modeDeboggage;
        hauteur = 50 + (random.nextDouble(71));
        largeur = (hauteur * 120)/104;
        images = new Image[5];
        definirImages();
        imageChoisie = images[random.nextInt(5)];
        vitesseX = -1 * ((100 * Math.pow(numeroNiveau, 0.33)) + 200);
        vitesseY = random.nextDouble(100);//pour inclure 100
        if (random.nextBoolean()) {
            vitesseY *= -1;
        }
        acceleration = 500;
    }

    /**
     * Définir les images d'ennemis
     */
    @Override
    protected void definirImages() {
        images[0] = new Image(Images.POISSON1.fichier);
        images[1] = new Image(Images.POISSON2.fichier);
        images[2] = new Image(Images.POISSON3.fichier);
        images[3] = new Image(Images.POISSON4.fichier);
        images[4] = new Image(Images.POISSON5.fichier);
    }

    /**
     * Rafraîchir le mouvement
     *
     * @param diffTemps différence de temps
     */
    @Override
    protected void update(double diffTemps) {
        vitesseX -= diffTemps * acceleration;
        positionX += diffTemps * vitesseX;
        positionY += diffTemps * vitesseY;
    }

    /**
     * Dessiner les ennemis
     *
     * @param context Canvas
     */
    protected void dessiner(GraphicsContext context) {
        if (imageChoisie != null) {
            if (modeDeboggage) {
                super.dessinerBordure(context);
            }
            context.drawImage(imageChoisie, camera.transformer(positionX), positionY, largeur, hauteur);
        }
    }
}
