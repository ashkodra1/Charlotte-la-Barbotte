package ca.qc.bdeb.sim203.projetjavafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public abstract class Projectile extends Mouvement {
    protected Charlotte charlotte;
    protected ArrayList<Ennemis> ennemis;
    protected double amplitude;
    protected double positionInitial;
    protected double periode;
    protected long tempsDuLancer;


    /**
     * @param positionX Position en X du projectile
     * @param positionY Position en Y du projectile
     * @param charlotte Charlotte qui est sur l'écran
     * @param ennemis Ennemis sur l'écran
     */
    public Projectile(double positionX, double positionY, Charlotte charlotte, ArrayList<Ennemis> ennemis) {
        super(positionX, positionY);
        this.charlotte = charlotte;
        this.ennemis = ennemis;
        images = new Image[3];
        definirImages();
        modeDeboggage = charlotte.modeDeboggage;
    }

    /**
     * Pour définir l'image de chaque projectile
     */
    protected void definirImages() {
        images[0] = new Image(Images.ETOILE.fichier);
        images[1] = new Image(Images.HIPPOCAMPE.fichier);
        images[2] = new Image(Images.SARDINES.fichier);
    }

    /**
     * Pour dessiner le jeu
     *
     * @param context Le canvas
     */
    protected void dessiner(GraphicsContext context) {
        if (modeDeboggage) {
            dessinerBordure(context);
        }
        context.drawImage(imageChoisie, camera.transformer(positionX), positionY, largeur, hauteur);
    }

    /**
     * Vérification de collision entre projectile et l'ennemi
     */
    protected void collisionEnnemi() {
        if (!ennemis.isEmpty()) {
            for (int i = 0; i < ennemis.size(); i++) {
                if (collision(ennemis.get(i))) {
                    ennemis.remove(ennemis.get(i));
                    i--;
                }
            }
        }
    }

    /**
     * Changer le paramètre en Y du projectile
     *
     * @param positionY position en Y du projectile
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }
}