public class Main {
    public static void main(final String[] args) throws ClassNotFoundException {
        Algos.execAlgAnalyse();  

        Algos.printDatas();

        Algos.execAlgsProfondeur();

        Algos.getChancesChoixKnowingAnotherChoix("C1", "scenario_walter");
    }
}