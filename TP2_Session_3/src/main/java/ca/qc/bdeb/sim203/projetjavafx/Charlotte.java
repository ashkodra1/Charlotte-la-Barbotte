package ca.qc.bdeb.sim203.projetjavafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Charlotte extends Mouvement{
    private final static double VITESSE_MAXIMALE = 300;
    private ArrayList<Projectile> projectileChoisi = new ArrayList<>();
    private Image projectileMaintenant = new Image(Images.ETOILE.fichier);
    private int vie;
    private int numeroProjectile;
    private int numeroNiveau;
    private long dernierDommage;

    /**
     * CONSTRUCTEUR
     *
     * @param positionX Position en X de Charlotte
     * @param positionY Position en Y de Charlotte
     * @param numeroNiveau Le numéro du niveau
     */
    public Charlotte(double positionX, double positionY, int numeroNiveau) {
        super(positionX, positionY);
        this.numeroNiveau = numeroNiveau;
        vie = 4;
        largeur = 102;
        hauteur = 90;
        images = new Image[3];
        definirImages();
        imageChoisie = images[0];
        numeroProjectile = 0;
        acceleration = 1000;
        dernierDommage = -1; //arbitraire
    }

    /**
     * Définition des différentes images de Charlotte
     */
    @Override
    protected void definirImages() {
        images[0] = new Image(Images.CHARLOTTE.fichier);
        images[1] = new Image(Images.CHARLOTTE_AVANT.fichier);
        images[2] = new Image(Images.CHARLOTTE_OUTCH.fichier);
    }

    /**
     * Rafraîchir le mouvement de Charlotte
     *
     * @param diffTemps Différente de temps
     */
    @Override
    protected void update(double diffTemps) {
        positionX += diffTemps * vitesseX;
        positionY += diffTemps * vitesseY;
        if (etablirLimite()) {
            numeroNiveau++;
        }
        if (vitesseX == 0 && vitesseY == 0) {
            imageChoisie = images[0];
        } else {
            imageChoisie = images[1];
        }
    }

    /**
     * Changer les vitesses en Y et X
     *
     * @param diffTemps Différence de temps
     * @param directionX La direction du mouvement en X de Charlotte
     * @param directionY La direction du mouvement en Y de Charlotte
     */
    public void updateLesVitesses(double diffTemps, int directionX, int directionY) {
        vitesseX = updateVitesse(diffTemps, directionX, vitesseX);
        vitesseY = updateVitesse(diffTemps, directionY, vitesseY);
    }

    /**
     * Changement d'une vitesse donné
     *
     * @param diffTemps Différence de temps
     * @param direction  La direction du mouvement de Charlotte
     * @param vitesse Vitesse actuelle de Charlotte
     * @return Une nouvelle vitesse pour Charlotte
     */
    private double updateVitesse(double diffTemps, int direction, double vitesse) {
        double temp = vitesse;
        double vitesseArbitraire = acceleration * diffTemps;
        if (temp > 0) {
            if (direction == 1) {
                vitesse += vitesseArbitraire;
            } else if (direction == -1) {
                vitesse -= vitesseArbitraire;
            } else {
                vitesse -= vitesseArbitraire;
                if (vitesse < 0) {
                    vitesse = 0;
                }
            }
        } else if (temp < 0) {
            if (direction == 1) {
                vitesse += vitesseArbitraire;
            } else if (direction == -1) {
                vitesse -= vitesseArbitraire;
            } else {
                vitesse += vitesseArbitraire;
                if (vitesse > 0) {
                    vitesse = 0;
                }
            }
        } else {
            if (direction == 1) {
                vitesse += vitesseArbitraire;
            } else if (direction == -1) {
                vitesse -= vitesseArbitraire;
            } else {
                return 0;
            }
        }
        if (Math.abs(vitesse) > VITESSE_MAXIMALE) {
            if (vitesse >= 0) {
                return VITESSE_MAXIMALE;
            } else {
                return -1 * VITESSE_MAXIMALE;
            }
        }
        return vitesse;
    }

    /**
     * Dessiner Charlotte
     *
     * @param context Canvas
     */
    protected void dessiner(GraphicsContext context) {
        if (imageChoisie != null) {
            if (modeDeboggage) {
                dessinerBordure(context);
            }
            context.drawImage(imageChoisie, camera.transformer(positionX), positionY, largeur, hauteur);
        }
    }

    /**
     * @return Boolean déterminant si Charlotte dépasse les bordures de la camera
     */
    private boolean etablirLimite() {
        if (positionY <= 5) {
            positionY = 5;
        } else if (positionY + hauteur >= Main.HAUTEUR - 5) {
            positionY = Main.HAUTEUR - 5 - hauteur;
        }
        if (positionX <= camera.getPosX() + 5) {
            positionX = camera.getPosX() + 5;
        } else return positionX + largeur >= Main.LONGUEUR_MONDE - 5;
        return false;
    }

    public ArrayList<Projectile> getProjectileChoisi() {
        return projectileChoisi;
    }
    public void setProjectileChoisi(ArrayList<Projectile> projectileChoisi) {
        this.projectileChoisi = projectileChoisi;
    }
    public Image getProjectileMaintenant() {
        return projectileMaintenant;
    }
    public int getVie() {
        return vie;
    }
    public void setVie(int vie) {
        this.vie = vie;
    }
    public int getNumeroProjectile() {
        return numeroProjectile;
    }
    public void setNumeroProjectile(int numeroProjectile) {
        this.numeroProjectile = numeroProjectile;
        //changer l'image du projectile en haut de l'écran
        switch (numeroProjectile % 3) {
            case 0 -> projectileMaintenant = new Image(Images.ETOILE.fichier);
            case 1 -> projectileMaintenant = new Image(Images.HIPPOCAMPE.fichier);
            case 2 -> projectileMaintenant = new Image(Images.SARDINES.fichier);
        }
    }
    public int getNumeroNiveau() {
        return numeroNiveau;
    }
    public void setNumeroNiveau(int numeroNiveau) {
        this.numeroNiveau = numeroNiveau;
    }
    public long getDernierDommage() {
        return dernierDommage;
    }
    public void setDernierDommage(long dernierDommage) {
        this.dernierDommage = dernierDommage;
    }
}
