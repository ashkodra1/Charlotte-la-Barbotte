package ca.qc.bdeb.sim203.projetjavafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static final double LONGUEUR = 900.0;
    public static final double LONGUEUR_MONDE = LONGUEUR * 8.0;
    public static final double HAUTEUR = 520.0;
    public static final long UNE_SECONDE = (int) 1e9;
    public static final double UNE_NANOSECONDE = 1.0 / UNE_SECONDE;
    private static final String NIVEAU = "NIVEAU ";
    private static final String FIN_DE_PARTIE = "FIN DE PARTIE";
    private static final int SCENE_ACCUEIL = 0;
    private static final int SCENE_INFOS = 1;
    private static final int SCENE_JEU = 2;
    private static final int ESPACEMENT = 15;
    private Charlotte charlotte;
    private ArrayList<Ennemis> ennemis = new ArrayList<>();
    private Camera camera = Camera.get();
    private ArrayList<Decor> decors = new ArrayList<>();
    private Baril baril;
    private GraphicsContext gc;
    private ImageView logoInfo;
    private Random random = new Random();
    private double diff;
    private int numeroNiveau = 1;
    private int directionX = 0;
    private int directionY = 0;
    private long dernierTemps = 0;
    private long derniereVague = 0;
    private long dernierLance = 0;
    private long debut = 0;
    private long fin = 0;
    private long maintenant = 0;
    private long constante = 0;
    private boolean modeDeboggage = false;

    @Override
    public void start(Stage stage) {
        // --- VISUELS ---

        //les éléments qui se trouvent dans le rootAcceuil
        var rootAcceuil = new VBox();
        rootAcceuil.setBackground(Background.fill(Color.valueOf("#2A7FFF"))); //couleur du fond
        rootAcceuil.setSpacing(ESPACEMENT);
        rootAcceuil.setAlignment(Pos.CENTER);
        var logoAcceuil = new ImageView(Images.LOGO.fichier);
        logoAcceuil.setPreserveRatio(true); //pour garder les proportions du logo
        logoAcceuil.setFitHeight(HAUTEUR - 100);
        rootAcceuil.getChildren().add(logoAcceuil);
        //section du bas de la fenêtre pour les boutons
        HBox boutons = new HBox();
        Button jouer = new Button("Jouer!");
        Button infos = new Button("Infos");
        //ajouter dans la scène
        boutons.getChildren().add(jouer);
        boutons.getChildren().add(infos);
        boutons.setAlignment(Pos.CENTER);
        boutons.setSpacing(ESPACEMENT);
        rootAcceuil.getChildren().add(boutons);

        //les éléments qui se trouvent dans le rootInfo
        var rootInfo = new VBox();
        rootInfo.setBackground(Background.fill(Color.valueOf("#2A7FFF"))); //couleur du fond
        rootInfo.setSpacing(ESPACEMENT);
        rootInfo.setAlignment(Pos.CENTER);
        logoInfo = imageInfoAuHazard(); //L'image de l'ennemi qui est affiché est au hazard
        Text charlotteText = new Text("Charlotte la Barbotte");
        charlotteText.setFont(Font.font(30));
        rootInfo.getChildren().add(charlotteText);
        rootInfo.getChildren().add(logoInfo);
        Text nomsText = new Text("Par Allison Shkodra \n et Raphaël Charabaty");
        nomsText.setFont(Font.font(25));
        rootInfo.getChildren().add(nomsText);
        Text explicationText = new Text("Travail remis à Georges Côté. " +
                "Graphismes adaptés de https://game-icons.net/ et de https://openclipart.org/. " +
                "Développé dans le cadre du cours 420-203-RE - " +
                "Développement de programmes dans un environnement graphique, au Collège de Bois-de-Boulogne");
        explicationText.setWrappingWidth(LONGUEUR - 100); //pour que tout le texte soit sur plusieurs lignes
        rootInfo.getChildren().add(explicationText);
        Button retour = new Button("Retour");
        rootInfo.getChildren().add(retour);

        //les éléments qui se trouvent dans le rootJeu
        var rootJeu = new Group();
        var canvas = new Canvas(LONGUEUR, HAUTEUR);
        gc = canvas.getGraphicsContext2D();
        rootJeu.getChildren().add(canvas);



        //--- LES SCÈNES ---
        Scene[] scene = new Scene[3];
        scene[SCENE_ACCUEIL] = new Scene(rootAcceuil, LONGUEUR, HAUTEUR);
        scene[SCENE_INFOS] = new Scene(rootInfo, LONGUEUR, HAUTEUR);
        scene[SCENE_JEU] = new Scene(rootJeu, LONGUEUR, HAUTEUR);
        scene[SCENE_JEU].setFill(Color.hsb((190 + random.nextInt(81)), 0.84, 1));//couleur changeable
        stage.setTitle("Charlotte la Barbotte");
        stage.setScene(scene[SCENE_ACCUEIL]);
        stage.show();


        //--- TIMER ---
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                maintenant = now - constante;
                gc.clearRect(0, 0, LONGUEUR, HAUTEUR); // déclaration de la taille du canvas
                long frequenceEnnemis = (long) (0.75 + (1 / Math.sqrt(numeroNiveau))); //calcul de la fréquence des vagues d'ennemis

                // AU DÉBUT DU JEU
                if (dernierTemps == 0) {
                    //création de charlotte, du décors et des barils
                    debut = maintenant;
                    dernierTemps = maintenant;
                    charlotte = new Charlotte(0, HAUTEUR / 2.0, numeroNiveau);
                    charlotte.setModeDeboggage(modeDeboggage);
                    creerBarilsEtDecors();
                }

                //AFFICHAGE DU NIVEAU AU DÉBUT
                if ((maintenant - debut) * UNE_NANOSECONDE <= 4) {//si on se trouve dans les 4 premières secondes d'un niveau
                    //affichage du numéro du niveau
                    gc.setFont(Font.font(80));
                    gc.setFill(Color.WHITE);
                    gc.fillText(NIVEAU + numeroNiveau, LONGUEUR / 3, HAUTEUR / 2);
                }

                //MODE DÉBOGAGGE (juste le texte)
                if (modeDeboggage) {
                    /* affichage du:
                     * -nb de poisson ennemi
                     * -nb de projectile
                     * -la position de Charlotte
                     */
                    gc.setFont(Font.font(15));
                    gc.setFill(Color.WHITE);
                    gc.fillText(("Nombres de poissons ennemis: " + ennemis.size()), 20, 60);
                    gc.fillText(("Nombres de projectiles: " + (charlotte.getProjectileChoisi()).size()), 20, 80);
                    gc.fillText(("Position de Charlotte: " + ((charlotte.positionX / LONGUEUR_MONDE) * 100) + "%"), 20, 100);
                }
                //VAGUE D'ENNEMIS
                if ((maintenant - derniereVague) * UNE_NANOSECONDE >= frequenceEnnemis) { //s'il est temps pour une vague d'ennemis
                    derniereVague = maintenant; // il y a une vague d'ennemis maintenant
                    int nombreDEnnemis = (1 + (random.nextInt(5))); //génère une quantité aléatoire d'ennemis
                    for (int i = 0; i < nombreDEnnemis; i++) { //ajout d'ennemis
                        ennemis.add(new Ennemis(camera.getPosX() + LONGUEUR, (HAUTEUR / 5.0) +
                                (random.nextDouble((4 * HAUTEUR) / 5) - (HAUTEUR / 5.0)), numeroNiveau, modeDeboggage));
                    }
                }

                diff = (maintenant - dernierTemps) * UNE_NANOSECONDE;

                //BARRE DE VIE
                gc.setFill(Color.WHITE);
                gc.fillRect(20, 10, 150, 35); //20 à 170 rempli
                switch (charlotte.getVie()) {
                    case 3 -> gc.clearRect(133, 12, 36, 32); //133 à 169 vide
                    case 2 -> gc.clearRect(96, 12, 73, 32); //96 à 169 vide
                    case 1 -> gc.clearRect(59, 12, 110, 32); //59 à 169 vide
                    case 0 -> gc.clearRect(22, 12, 147, 32); //22 à 169 vide
                }

                //CAMERA
                dernierTemps = maintenant;
                camera.update(charlotte.positionX);
                charlotte.setCamera(camera);
                for (Decor decor : decors) { //Decors
                    decor.setCamera(camera);
                    decor.dessiner(gc);
                }
                for (Ennemis ennemi : ennemis) { //Ennemis
                    ennemi.setCamera(camera);
                }

                //PROJECTILES
                if (!(charlotte.getProjectileChoisi()).isEmpty() && fin == 0) { //si Charlotte a au moin un projectile  et qu'on est au début du niveau
                    ArrayList<Projectile> temp = charlotte.getProjectileChoisi();
                    for (int i = 0; i < temp.size(); i++) {
                        if ((temp.get(i)).getClass() == Hippocampes.class) { //les projectiles sont des hippocampes
                            //formule pour la position de l'hippocampe
                            double tempY = (temp.get(i)).positionInitial + ((temp.get(i)).amplitude * (Math.sin((2 * Math.PI * (maintenant - (temp.get(i)).tempsDuLancer) * UNE_NANOSECONDE) / (temp.get(i)).periode)));
                            (temp.get(i)).setPositionY(tempY);
                        }
                        (temp.get(i)).setCamera(camera);
                        (temp.get(i)).update(diff);
                        //si le projectile n'est plus visible par la camera on l'enlève de l'ArrayList
                        if ((temp.get(i)).positionX >= camera.getPosX() + LONGUEUR) {
                            temp.remove((temp.get(i)));
                            i--;
                        } else {
                            (temp.get(i)).dessiner(gc); //dessiner le projectile
                        }
                    }
                    charlotte.setProjectileChoisi(temp);
                }
                gc.drawImage(charlotte.getProjectileMaintenant(), 180, 10);

                double differenceContact = (maintenant - charlotte.getDernierDommage()) * UNE_NANOSECONDE;
                if (fin == 0) { //au début du niveau
                    charlotte.updateLesVitesses(diff, directionX, directionY);
                    charlotte.update(diff);
                    if (!ennemis.isEmpty() && differenceContact > 2) {
                        for (Ennemis ennemi : ennemis) {
                            if (charlotte.collision(ennemi)) {
                                charlotte.setVie(charlotte.getVie() - 1);
                                charlotte.setDernierDommage(maintenant);
                                if (charlotte.getVie() == 0) { //lorsque Charlotte est morte
                                    fin = maintenant;
                                }
                                break;
                            }
                        }
                    }
                }

                if (differenceContact <= 2 && charlotte.getDernierDommage() != -1) {
                    charlotte.setImageChoisie(charlotte.images[2]);
                    if ((differenceContact > 0.25 && differenceContact <= 0.50) || (differenceContact > 0.75 && differenceContact <= 1.0) || (differenceContact > 1.25 && differenceContact <= 1.50) || differenceContact > 1.75) {
                        charlotte.dessiner(gc);
                    }
                } else {
                    charlotte.dessiner(gc);
                }

                if (ennemis != null) {
                    for (int i = 0; i < ennemis.size(); i++) {
                        (ennemis.get(i)).update(diff);
                        if (ennemis.get(i).positionX + ennemis.get(i).largeur <= camera.getPosX()) {
                            ennemis.remove(ennemis.get(i));
                            i--;
                        } else {
                            (ennemis.get(i)).dessiner(gc);
                        }
                    }
                }
                if (baril != null) {
                    baril.update((maintenant - debut) * UNE_NANOSECONDE);
                    baril.setCamera(camera);
                    if (!baril.isOuvert()) {
                        baril.essaiDOuvrir(charlotte);
                    }
                    baril.dessiner(gc);
                }
                //LORSQUE CHARLOTTE MEURT
                if ((maintenant - fin) * UNE_NANOSECONDE <= 3 && fin != 0) {
                    gc.setFont(Font.font(80));
                    gc.setFill(Color.RED);
                    gc.fillText(FIN_DE_PARTIE, LONGUEUR / 4, HAUTEUR / 2);
                } else if (fin != 0) {
                    this.stop();
                    recommencerPartiellement();
                    stage.setScene(scene[SCENE_ACCUEIL]);
                }
                if (charlotte != null && charlotte.getNumeroNiveau() > numeroNiveau) {//nouveau niveau
                    prochainNiveau();
                }
            }
        };

        // --- ACTIONS ---

        //FERMETURE DU JEU LORSQUE ESC EST APPUYÉ
        for (int i = 0; i < scene.length - 1; i++) {
            scene[i].setOnKeyPressed((event) -> {
                if (KeyCode.ESCAPE == event.getCode()) {
                    Platform.exit();
                }
            });
        }
        //bouton jouer
        jouer.setOnAction((event) -> {
            stage.setScene(scene[SCENE_JEU]);
            timer.start();
        });
        //bouton info
        infos.setOnAction((event) -> {
            stage.setScene(scene[SCENE_INFOS]);
            logoInfo.setImage(imageInfoAuHazard().getImage()); //changer l'image du poisson à chaque fois
        });
        //bouton retour
        retour.setOnAction((event) -> {
            stage.setScene(scene[SCENE_ACCUEIL]);
        });
        scene[SCENE_JEU].setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
                mouvement(event.getCode());
            } else {
                methode(event.getCode(), maintenant);
            }
        });
        scene[SCENE_JEU].setOnKeyReleased((event) -> {
            annulerMouvement(event.getCode());
        });
    }

    /**
     * @return Une image d'ennemi au hazard
     */
    private ImageView imageInfoAuHazard() {
        ImageView logoInfo;
        switch (random.nextInt(5)) { //génération de logo au hazard
            case 0 -> logoInfo = new ImageView(Images.POISSON1.fichier);
            case 1 -> logoInfo = new ImageView(Images.POISSON2.fichier);
            case 2 -> logoInfo = new ImageView(Images.POISSON3.fichier);
            case 3 -> logoInfo = new ImageView(Images.POISSON4.fichier);
            default -> logoInfo = new ImageView(Images.POISSON5.fichier);
        }
        return logoInfo;
    }
    /** TOUTS LES ÉVÉNEMENTS LIÉS AU CLAVIER
     * @param keyCode la touche qui a été appuyé
     * @param now le temps
     */
    private void methode(KeyCode keyCode, long now) {
        if (KeyCode.ESCAPE == keyCode) { //quitter platforme
            Platform.exit();
        }
        if (KeyCode.D == keyCode) { //activer déboggage
            modeDeboggage = !modeDeboggage;
            charlotte.setModeDeboggage(modeDeboggage);
            baril.setModeDeboggage(modeDeboggage);
            if (!ennemis.isEmpty()) {
                for (Ennemis ennemi : ennemis) {
                    ennemi.setModeDeboggage(modeDeboggage);
                }
            }
            if (!(charlotte.getProjectileChoisi()).isEmpty()) {
                for (Projectile projectile : charlotte.getProjectileChoisi()) {
                    projectile.setModeDeboggage(modeDeboggage);
                }
            }
        }
        if (modeDeboggage) { //touches du déboggage
            if (KeyCode.Q == keyCode) { //etoiles
                charlotte.setNumeroProjectile(0);
            }
            if (KeyCode.W == keyCode) { //hippocampes
                charlotte.setNumeroProjectile(1);
            }
            if (KeyCode.E == keyCode) { //sardines
                charlotte.setNumeroProjectile(2);
            }
            if (KeyCode.R == keyCode) {
                charlotte.setVie(4);
            }
            if (KeyCode.T == keyCode) {
                prochainNiveau();
            }
        }
        if (KeyCode.SPACE == keyCode && (now - dernierLance) * UNE_NANOSECONDE >= 0.5) { //attaquer
            dernierLance = now;
            ArrayList<Projectile> temp = charlotte.getProjectileChoisi();
            switch (charlotte.getNumeroProjectile() % 3) { //déterminer le projectile utilisé
                case 0:
                    temp.add(new Etoiles((charlotte.positionX + charlotte.largeur), (charlotte.positionY + (charlotte.hauteur / 2)), charlotte, ennemis));
                    break;
                case 1:
                    for (int i = 0; i < 3; i++) {
                        temp.add(new Hippocampes((charlotte.positionX + charlotte.largeur), (charlotte.positionY + (charlotte.hauteur / 2)), charlotte, ennemis, now));
                    }
                    break;
                case 2:
                    temp.add(new Sardines((charlotte.positionX + charlotte.largeur), (charlotte.positionY + (charlotte.hauteur / 2)), charlotte, ennemis));
                    break;
            }
            charlotte.setProjectileChoisi(temp);
        }
    }
    /** LES TOUCHES APPUYÉS POUR BOUGER CHARLOTTE
     * @param keyCode touche appuyé
     */
    private void mouvement(KeyCode keyCode) {
        if (KeyCode.UP == keyCode) {  //HAUT
            if (directionY == 1) {
                directionY = 0;
            } else {
                directionY = -1;
            }
        }
        if (KeyCode.DOWN == keyCode) { //BAS
            if (directionY == -1) {
                directionY = 0;
            } else {
                directionY = 1;
            }
        }
        if (KeyCode.RIGHT == keyCode) { //DROITE
            if (directionX == -1) {
                directionX = 0;
            } else {
                directionX = 1;
            }
        }
        if (KeyCode.LEFT == keyCode) { //GAUCHE
            if (directionX == 1) {
                directionX = 0;
            } else {
                directionX = -1;
            }
        }
    }
    /** ANNULATION DU MOUVEMENT
     * @param keyCode touche appuyé
     */
    private void annulerMouvement(KeyCode keyCode) {
        if (KeyCode.UP == keyCode || KeyCode.DOWN == keyCode) {
            directionY = 0;
        }
        if (KeyCode.RIGHT == keyCode || KeyCode.LEFT == keyCode) {
            directionX = 0;
        }
    }
    /**
     * TOUT METTRE À ZERO POUR LE PROCHAIN NIVEAU
     */
    private void prochainNiveau() {
        numeroNiveau += 1;
        recommencer();
        constante += maintenant;
        maintenant = 0;
        debut = 0;
        derniereVague = 0;
        dernierLance = 0;
        creerBarilsEtDecors();
    }
    private void recommencerPartiellement() {
        numeroNiveau = 1;
        dernierTemps = 0;
        recommencer();
    }
    private void recommencer() {
        ennemis.clear();
        fin = 0;
        directionX = 0;
        directionY = 0;
        camera.setPosX(0);
        charlotte.setCamera(camera);
        charlotte.resetPositions();
        charlotte.setProjectileChoisi(new ArrayList<>());
        charlotte.setNumeroNiveau(numeroNiveau);
        charlotte.setVie(4);
        charlotte.setDernierDommage(-1); //arbitraire
        charlotte.setModeDeboggage(modeDeboggage);
    }
    /**
     * CREATION DE BARILS ET DE DÉCORS
     */
    private void creerBarilsEtDecors() {
        baril = new Baril((LONGUEUR_MONDE / 5.0) + random.nextDouble(((4 * LONGUEUR_MONDE) / 5) - (LONGUEUR_MONDE / 5.0)), 0);
        baril.setCamera(camera);
        baril.setModeDeboggage(true);
        decors.clear();
        double dernierePositionX = 0;
        while (dernierePositionX < LONGUEUR_MONDE) {
            decors.add(new Decor(dernierePositionX + 50 + random.nextInt(51), HAUTEUR + 10)); //mets espace random ici pour que premier decor n'est pas collé à gauche.
            dernierePositionX = (decors.get(decors.size() - 1)).getPositionX() + (decors.get(decors.size() - 1)).largeur;
        }
    }
}