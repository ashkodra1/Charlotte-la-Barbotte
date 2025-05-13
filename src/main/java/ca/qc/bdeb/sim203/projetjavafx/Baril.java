package ca.qc.bdeb.sim203.projetjavafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Baril extends Mouvement {
    private boolean ouvert;
    public Baril(double positionX, double positionY) {
        super(positionX, positionY);
        largeur = 70;
        hauteur = 83;
        images = new Image[2];
        definirImages();
        imageChoisie = images[0];
    }
    @Override
    protected void definirImages() {
        images[0] = new Image(Images.BARIL.fichier);
        images[1] = new Image(Images.BARIL_OUVERT.fichier);
    }

    @Override
    protected void update(double tempsTotal) { //temps total pour difftemps, pas difftemps
        positionY = ((Main.HAUTEUR - hauteur)/2) * (Math.sin((2 * Math.PI * tempsTotal)/3)) + ((Main.HAUTEUR - hauteur)/2);
    }
    @Override
    protected void dessiner(GraphicsContext context) {
        if (modeDeboggage && !ouvert) {
            dessinerBordure(context);
        }
        context.drawImage(imageChoisie, camera.transformer(positionX), positionY, largeur, hauteur);
    }
    public void essaiDOuvrir(Charlotte charlotte) {
        if (collision(charlotte)) {
            imageChoisie = images[1];
            ouvert = true;
            charlotte.setNumeroProjectile(charlotte.getNumeroProjectile() + 1);
        }
    }
    public boolean isOuvert() {
        return ouvert;
    }
}