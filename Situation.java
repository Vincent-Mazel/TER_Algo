import java.util.ArrayList;
import java.util.List;

public class Situation {
    private int idSituation;
    private String idScenario;
    private List<Choix> listChoix;

    public Situation (int idSituation, String idScenario) {
        this.idSituation = idSituation;
        this.idScenario = idScenario;
        this.listChoix = new ArrayList<Choix>();
    }

    public void setListChoix (List<Choix> listChoix) {
        this.listChoix = listChoix;
    }

    public int getIdSituation () {
        return this.idSituation;
    }

    public List<Choix> getListChoix() {
        return this.listChoix;
    }

    public void addChoix (Choix choix) {
        this.listChoix.add(choix);
    }
}