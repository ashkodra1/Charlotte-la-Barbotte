package ca.qc.bdeb.sim203.projetjavafx;

public class Camera {
    private static Camera camera = null;
    private double posX;
    private Camera() {
        posX = 0;
    }
    public static Camera get(){
        if (camera == null){
            camera = new Camera();
        }
        return camera;
    }
    public void update(double posXCharlotte) {
        if (posXCharlotte - (Main.LONGUEUR/5.0) >= posX) {
            posX = posXCharlotte - (Main.LONGUEUR/5.0);
        }
        if (posX + Main.LONGUEUR > Main.LONGUEUR_MONDE) {
            posX = Main.LONGUEUR_MONDE - Main.LONGUEUR;
        } else if (posX < 0) {
            posX = 0;
        }
    }
    public double transformer(double positionX){
        return positionX - posX;
    }
    public double getPosX() {
        return posX;
    }
    public void setPosX(double posX) {
        this.posX = posX;
    }
}