public class MyPair {
    private Choix choix;
    private int nbChoixTaken;

    public MyPair(Choix choix) {
        this.choix = choix;
        this.nbChoixTaken = 1;
    }

    public Choix getChoix() {
        return this.choix;
    }

    public int getNbChoixTaken() {
        return this.nbChoixTaken;
    }

    public void incrNbChoixTaken() {
        this.nbChoixTaken += 1;
    }
}