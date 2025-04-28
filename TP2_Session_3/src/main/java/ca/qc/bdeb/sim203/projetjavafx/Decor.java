package ca.qc.bdeb.sim203.projetjavafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Decor extends Mouvement {
    /**
     * CONSTRUCTEUR
     *
     * @param dernierePositionX Dernière position du décors
     * @param positionY Position en Y du décors
     */
    public Decor(double dernierePositionX, double positionY) {
        super(dernierePositionX, positionY); //positionY donnée ici est celle où le bas de l'image doit être
        largeur = 80;
        hauteur = 119;
        images = new Image[6];
        definirImages();
        imageChoisie = images[random.nextInt(6)];
        this.positionY -= hauteur; //positionY = toujours -10;
    }

    /**
     * Définir les images de décors
     */
    protected void definirImages() {
        images[0] = new Image(Images.DECOR1.fichier);
        images[1] = new Image(Images.DECOR2.fichier);
        images[2] = new Image(Images.DECOR3.fichier);
        images[3] = new Image(Images.DECOR4.fichier);
        images[4] = new Image(Images.DECOR5.fichier);
        images[5] = new Image(Images.DECOR6.fichier);
    }
    @Override
    protected void update(double diffTemps) {} //inutile, ne bouge pas

    /**
     * Dessiner le décors
     *
     * @param context Canvas
     */
    protected void dessiner(GraphicsContext context) {
        context.drawImage(imageChoisie, camera.transformer(positionX), positionY, largeur, hauteur);
    }
}
