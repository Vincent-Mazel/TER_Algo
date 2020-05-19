public class Choix {

    private String nomChoix;
    private String idScenario;

    private Situation nextSituation;

    private int nbFoisChoisi;

    public Choix (String nomChoix, String idScenario, Situation nextSituation) {
        this.nomChoix = nomChoix;
        this.idScenario = idScenario;
        this.nextSituation = nextSituation;

        nbFoisChoisi = 0;
    }

    public Choix (String nomChoix, String idScenario) {
        this.nomChoix = nomChoix;
        this.idScenario = idScenario;

        nbFoisChoisi = 0;
    }

    public void setNextSituation(Situation nextSituation) {
        this.nextSituation = nextSituation;
    }

    public String getIdScenario() {
        return this.idScenario;
    }

    public void incrementNbFoisChoisi () {
        this.nbFoisChoisi ++;
    }

    public String getNomChoix() {
        return this.nomChoix;
    }

    public int getNbFoisChoisi() {
        return this.nbFoisChoisi;
    }

    public Situation getNextSituation() {
        return this.nextSituation;
    }
}