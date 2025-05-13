package ca.qc.bdeb.sim203.projetjavafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public abstract class Mouvement {
    protected Image[] images;
    protected Image imageChoisie;
    protected Random random = new Random();
    protected Camera camera;
    protected double acceleration;
    protected double vitesseX;
    protected double vitesseY;
    protected double positionX;
    protected double positionY;
    protected double largeur;
    protected double hauteur;
    protected boolean modeDeboggage;

    /**
     * CONSTRUCTEUR
     *
     * @param positionX position en X de l'objet en mouvement
     * @param positionY position en Y de l'objet en mouvement
     */
    public Mouvement(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        modeDeboggage = false;
    }
    protected abstract void definirImages();
    protected abstract void update(double diffTemps);

    protected abstract void dessiner(GraphicsContext context);

    /**
     * Vérification de collision entre deux objets
     *
     * @param autre L'objet avec lequel on est possiblement en collision
     * @return Boolean disans s'il y a une collision
     */
    protected boolean collision(Mouvement autre) {
        boolean x1 = autre.positionX <= (this.positionX + this.largeur);
        boolean x2 = (autre.positionX + autre.largeur) >= this.positionX;
        boolean y1 = autre.positionY <= (this.positionY + this.hauteur);
        boolean y2 = (autre.positionY + autre.hauteur) >= this.positionY;
        return x1 && x2 && y1 && y2;
    }

    /**
     * Dessiner la bordure des object pour le mode déboggage
     *
     * @param context Canvas
     */
    protected void dessinerBordure(GraphicsContext context) {
        context.setFill(Color.YELLOW);
        context.fillRect(camera.transformer(positionX) - 2, positionY - 2, largeur + 4, 2); //ligne horizontale haut
        context.fillRect(camera.transformer(positionX) - 2, positionY + hauteur, largeur + 4, 2); //ligne horizontale bas
        context.fillRect(camera.transformer(positionX) - 2, positionY - 2, 2, hauteur + 4); //ligne verticale gauche
        context.fillRect(camera.transformer(positionX) + largeur, positionY - 2, 2, hauteur + 4); //ligne verticale droite
    }

    /**
     * Remettre à la position initiale
     */
    protected void resetPositions() {
        positionX = 0;
        positionY = Main.HAUTEUR / 2;
    }
    public void setImageChoisie(Image imageChoisie) {
        this.imageChoisie = imageChoisie;
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public double getPositionX() {
        return positionX;
    }
    public void setModeDeboggage(boolean modeDeboggage) {
        this.modeDeboggage = modeDeboggage;
    }
}
