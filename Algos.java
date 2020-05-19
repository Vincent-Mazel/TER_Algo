import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class Algos {

    private static final String HOST_NAME = "mysql-mrvincent13.alwaysdata.net";
    private static final String DB_NAME = "mrvincent13_bd_ter";
    private static final String USER = "204815";
    private static final String PASSWORD = "Azertyuiop123!";

    // SELECT id_scenario, COUNT(*) FROM RESULTAT GROUP BY id_scenario
    private static final String SELECT_ALL_RESULTS_QUERY = "SELECT * FROM RESULTAT R, UTILISATEUR U WHERE R.id_utilisateur = U.id_utilisateur";

    private static final String SELECT_ALL_SITUATIONS_2CHOIX_QUERY = "SELECT * FROM SITUATION S, CHOIX C1, CHOIX C2 WHERE S.id_scenario = C1.id_scenario AND S.id_scenario = C2.id_scenario AND S.id_choix1 = C1.id_choix AND S.id_choix2 = C2.id_choix";
    private static final String SELECT_ALL_SITUATIONS_3CHOIX_QUERY = "SELECT * FROM SITUATION S, CHOIX C WHERE S.id_scenario = C.id_scenario AND S.id_choix3 = C.id_choix";


    private static List<Situation> listSituationsWalter;
    private static List<Choix> listChoixWalter;
    private static List<Pair<List<Choix>,Integer>> sortedMostFrequentPathsWalter;
    private static HashMap<Pair<List<Choix>, Integer>, Integer> mostFrequentPathsWalter;
    
    private static List<Situation> listSituationsJesse;
    private static List<Choix> listChoixJesse;
    private static List<Pair<List<Choix>,Integer>> sortedMostFrequentPathsJesse;
    private static HashMap<Pair<List<Choix>, Integer>, Integer> mostFrequentPathsJesse;

    private static List<Situation> listSituationsHank;
    private static List<Choix> listChoixHank;
    private static List<Pair<List<Choix>,Integer>> sortedMostFrequentPathsHank;
    private static HashMap<Pair<List<Choix>, Integer>, Integer> mostFrequentPathsHank;

    private static List<Situation> listSituationsGus;
    private static List<Choix> listChoixGus;
    private static List<Pair<List<Choix>,Integer>> sortedMostFrequentPathsGus;
    private static HashMap<Pair<List<Choix>, Integer>, Integer> mostFrequentPathsGus;


    private static int numberOfChoix;


    public static void execAlgsProfondeur() {
        numberOfChoix = 0;
        execAlgProfondeurRecurs(listSituationsWalter.get(0));
        System.out.println("Nombre de chemins scénario Walter : " + numberOfChoix);

        numberOfChoix = 0;
        execAlgProfondeurRecurs(listSituationsJesse.get(0));
        System.out.println("Nombre de chemins scénario Jesse : " + numberOfChoix);

        numberOfChoix = 0;
        execAlgProfondeurRecurs(listSituationsHank.get(0));
        System.out.println("Nombre de chemins scénario Hank : " + numberOfChoix);

        numberOfChoix = 0;
        execAlgProfondeurRecurs(listSituationsGus.get(0));
        System.out.println("Nombre de chemins scénario Gus : " + numberOfChoix);

        System.out.println("");
    }


    private static void execAlgProfondeurRecurs(Situation currentSituation) {
        for (Choix c : currentSituation.getListChoix()) {
            if (!isCasParticulier(c)) {
                if (c.getNextSituation() == null || c.getNextSituation().getListChoix().size() == 0)
                numberOfChoix += 1;
            else
                execAlgProfondeurRecurs(c.getNextSituation());  
            }  
        }
    }


    private static boolean isCasParticulier(Choix choix) {
        return choix.getNomChoix().substring(choix.getNomChoix().length() -1).equals("3") && (isCasParticulierJesse(choix) || isCasParticulierHank(choix));
    }


    private static boolean isCasParticulierJesse(Choix choix) {
        return choix.getIdScenario().equals("scenario_jesse");
    }


    private static boolean isCasParticulierHank(Choix choix) {
        return choix.getIdScenario().equals("scenario_hank") && choix.getNomChoix().equals("C1.2.1.3");
    }


    public static void execAlgAnalyse() {
        initializeLists();
        getDatas();
    }


    private static void initializeLists() {
        listSituationsWalter = new ArrayList<Situation>(Collections.nCopies(17, null));
        listChoixWalter = new ArrayList<Choix>();
        mostFrequentPathsWalter = new HashMap<Pair<List<Choix>, Integer>, Integer>();
        
        listSituationsJesse = new ArrayList<Situation>(Collections.nCopies(16, null));
        listChoixJesse = new ArrayList<Choix>();
        mostFrequentPathsJesse = new HashMap<Pair<List<Choix>, Integer>, Integer>();

        listSituationsHank = new ArrayList<Situation>(Collections.nCopies(17, null));
        listChoixHank = new ArrayList<Choix>();
        mostFrequentPathsHank = new HashMap<Pair<List<Choix>, Integer>, Integer>();

        listSituationsGus = new ArrayList<Situation>(Collections.nCopies(17, null));
        listChoixGus = new ArrayList<Choix>();
        mostFrequentPathsGus = new HashMap<Pair<List<Choix>, Integer>, Integer>();
    }


    private static void getDatas() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (final ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        
        final String connectionUrl = "jdbc:mysql://" + HOST_NAME + "/" + DB_NAME;

        try (Connection connection = DriverManager.getConnection(connectionUrl, USER, PASSWORD);
            Statement statement = connection.createStatement();) {

            ResultSet result2choix = statement.executeQuery(SELECT_ALL_SITUATIONS_2CHOIX_QUERY);
            treatSituations2ChoixDatas(result2choix);

            ResultSet result3choix = statement.executeQuery(SELECT_ALL_SITUATIONS_3CHOIX_QUERY);
            treatSituations3ChoixDatas(result3choix);
                  
            ResultSet resultSetResultats = statement.executeQuery(SELECT_ALL_RESULTS_QUERY);
            treatResultsDatas(resultSetResultats);
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    private static void treatSituations2ChoixDatas(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                Choix choix1 = new Choix(resultSet.getString(4), resultSet.getString(1));
                Choix choix2 = new Choix(resultSet.getString(5), resultSet.getString(1));

                List<Choix> listChoix = new ArrayList<Choix>();
                listChoix.add(choix1);
                listChoix.add(choix2);
        
                switch (resultSet.getString(1)) {
                    case "scenario_walter" :
                        addToLists2Choix(resultSet, listSituationsWalter, listChoixWalter, listChoix, choix1, choix2);
                        break;
                    
                    case "scenario_jesse" :
                        addToLists2Choix(resultSet, listSituationsJesse, listChoixJesse, listChoix, choix1, choix2);
                        break;

                    case "scenario_hank" :
                        addToLists2Choix(resultSet, listSituationsHank, listChoixHank, listChoix, choix1, choix2);
                        break;   
                        
                    case "scenario_gus" :
                        addToLists2Choix(resultSet, listSituationsGus, listChoixGus, listChoix, choix1, choix2);
                        break;    
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    private static void addToLists2Choix(ResultSet resultSet, List<Situation> listSituations, List<Choix> listChoix, List<Choix> listChoixToAdd, Choix choix1, Choix choix2) {
        try {
            if (listSituations.get(resultSet.getInt(2)) != null)
                listSituations.get(resultSet.getInt(2)).setListChoix(listChoixToAdd);
            else {
                Situation situation = new Situation(resultSet.getInt(2), resultSet.getString(1));
                situation.setListChoix(listChoixToAdd);

                listSituations.set(situation.getIdSituation(), situation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setNextSituation2Choix(listSituations, listChoix, resultSet, choix1, 11);
        setNextSituation2Choix(listSituations, listChoix, resultSet, choix2, 16);
    }


    private static void setNextSituation2Choix(List<Situation> listSituations, List<Choix> listChoix, ResultSet resultSet, Choix choix, int numColumn) {
        try {
            if (resultSet.getInt(numColumn) != 0) {
                if (listSituations.get(resultSet.getInt(numColumn)) != null)
                    choix.setNextSituation(listSituations.get(resultSet.getInt(numColumn)));
                else {
                    Situation nextSituationChoix1 = new Situation(resultSet.getInt(numColumn), resultSet.getString(1));
                    choix.setNextSituation(nextSituationChoix1);

                    listSituations.set(nextSituationChoix1.getIdSituation(), nextSituationChoix1);
                }
            }

            listChoix.add(choix);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void treatSituations3ChoixDatas(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                Choix choix3 = new Choix(resultSet.getString(6), resultSet.getString(1));
                
                switch (resultSet.getString(1)) {
                    case "scenario_walter" :
                        listSituationsWalter.get(resultSet.getInt(2)).addChoix(choix3);
                        listChoixWalter.add(choix3);

                        break;
                    
                    case "scenario_jesse" :
                        listSituationsJesse.get(resultSet.getInt(2)).addChoix(choix3);
                        listChoixJesse.add(choix3);

                        break;
        
                    case "scenario_hank" :
                        if (resultSet.getInt(2) == 6)
                            choix3.setNextSituation(listSituationsHank.get(14));
        
                        listSituationsHank.get(resultSet.getInt(2)).addChoix(choix3);

                        if (!resultSet.getString(6).equals("C1.2.1.2.3"))
                            listChoixHank.add(choix3);

                        break;     
                }
            }  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private static void treatResultsDatas(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                switch (resultSet.getString(2)) {
                    case "scenario_walter" :
                        addResultsToList(resultSet, mostFrequentPathsWalter, listChoixWalter);
                        break;
                    
                    case "scenario_jesse" :
                        addResultsToList(resultSet, mostFrequentPathsJesse, listChoixJesse);
                        break;

                    case "scenario_hank" :
                        addResultsToList(resultSet, mostFrequentPathsHank, listChoixHank);
                        break;   
                        
                    case "scenario_gus" :
                        addResultsToList(resultSet, mostFrequentPathsGus, listChoixGus);
                        break;    
                }
            }

            sortAllLists();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    private static void sortAllLists() {
        listChoixWalter = sortListChoix(listChoixWalter);
        sortedMostFrequentPathsWalter = sortMostFrequentPathsList(mostFrequentPathsWalter);

        listChoixJesse = sortListChoix(listChoixJesse);
        sortedMostFrequentPathsJesse = sortMostFrequentPathsList(mostFrequentPathsJesse);

        listChoixHank = sortListChoix(listChoixHank);
        sortedMostFrequentPathsHank = sortMostFrequentPathsList(mostFrequentPathsHank);

        listChoixGus = sortListChoix(listChoixGus);
        sortedMostFrequentPathsGus = sortMostFrequentPathsList(mostFrequentPathsGus);
    }


    private static List<Choix> sortListChoix(List<Choix> listChoix) {
        List<Choix> sortedList = new ArrayList<Choix>();
        
        while (listChoix.size() >= 1) {
            Choix choixToAdd = listChoix.get(0);
            int indexToRemove = 0;

            for (int j = 0; j < listChoix.size(); j++) {
                if (listChoix.get(j).getNbFoisChoisi() > choixToAdd.getNbFoisChoisi()) {
                    choixToAdd = listChoix.get(j);
                    indexToRemove = j;
                }
            }

            listChoix.remove(indexToRemove);

            sortedList.add(choixToAdd);
        }

        return sortedList;
    }


    private static List<Pair<List<Choix>,Integer>> sortMostFrequentPathsList(HashMap<Pair<List<Choix>, Integer>, Integer> listPaths) {
        List<Pair<List<Choix>,Integer>> sortedList = new ArrayList<Pair<List<Choix>,Integer>>();
        
        while (listPaths.size() >= 1) {
            List<Pair<List<Choix>, Integer>> listKeys = new ArrayList<>(listPaths.keySet());

            int valueToAdd = listPaths.get(listKeys.get(0));

            int indexToRemove = 0;

            for (int j = 0; j < listKeys.size(); j++) {
                if (listPaths.get(listKeys.get(j)) > valueToAdd) {

                    valueToAdd = listPaths.get(listKeys.get(j));

                    indexToRemove = j;
                }
            }

            listPaths.remove(listKeys.get(indexToRemove));

            sortedList.add(new Pair<List<Choix>,Integer>(listKeys.get(indexToRemove).getKey(), valueToAdd));
        }

        return sortedList;
    }


    private static void addResultsToList(ResultSet resultSet, HashMap<Pair<List<Choix>, Integer>, Integer> listResults, List<Choix> listChoix) {
        try {
            boolean hasToCreateNew = true;
            List<Pair<List<Choix>, Integer>> listKeys = new ArrayList<>(listResults.keySet());

            for (int index = 0; index < listResults.size(); index++) {
                int i = 3;
                boolean isSameChoice = true;

                int sizePathResultSet = getSizePathFromResultSet(resultSet);

                if (listKeys.get(index).getValue() == sizePathResultSet) {
                    while ((i - 3) < sizePathResultSet && resultSet.getString(i) != null) {
                        if (!(resultSet.getString(i).equals(listKeys.get(index).getKey().get(i - 3).getNomChoix()))) {
                            isSameChoice = false;
                            break;
                        }
    
                        i += 1;
                    }
                }
                else {
                    isSameChoice = false;
                } 
                    
                if (isSameChoice) {
                    listResults.put(listKeys.get(index), listResults.get(listKeys.get(index)) + 1);

                    for (Choix c : listKeys.get(index).getKey())
                        c.incrementNbFoisChoisi();

                    hasToCreateNew = false;
               
                    break;
                }
            }

            if (hasToCreateNew) {
                listResults.put(getResultInPair(resultSet, listChoix), 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static int getSizePathFromResultSet(ResultSet resultSet) {
        int size = 0;
        int i = 3;

        try {
            while (resultSet.getString(i) != null) {
                i += 1;
                size += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return size;
    }


    private static Pair<List<Choix>, Integer> getResultInPair(ResultSet resultSet, List<Choix> listChoix) {
        List<Choix> listResult = new ArrayList<Choix>();
        int i = 3;
        int size = 0;

        try {
            while (resultSet.getString(i) != null) {
                listResult.add(getChoixFromName(resultSet.getString(i), listChoix));

                i += 1;
                size += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Pair<List<Choix>, Integer> pairToReturn = new Pair<List<Choix>, Integer>(listResult, size);

        return pairToReturn;
    }


    private static Choix getChoixFromName(String name, List<Choix> listChoix) {
        Choix choix = null;

        for (int i = 0; i < listChoix.size(); i++)
            if (listChoix.get(i).getNomChoix().equals(name)) {
                choix = listChoix.get(i);
                listChoix.get(i).incrementNbFoisChoisi();
            } 
                
        return choix;
    }


    public static boolean getChancesChoixKnowingAnotherChoix(String strChoix1, String idScenario) {
        List<Choix> listChoix = null;
        List<Pair<List<Choix>,Integer>> listPaths = null;

        Choix choix = null;

        boolean returnStatus = true;

        switch (idScenario) {
            case "scenario_walter" :
                listChoix = listChoixWalter;
                listPaths = sortedMostFrequentPathsWalter;

                break;
            
            case "scenario_jesse" :
                listChoix = listChoixJesse;
                listPaths = sortedMostFrequentPathsJesse;

                break;

            case "scenario_hank" :
                listChoix = listChoixHank;
                listPaths = sortedMostFrequentPathsHank;

                break;   
                
            case "scenario_gus" :
                listChoix = listChoixGus;
                listPaths = sortedMostFrequentPathsGus;

                break;    
        }

        boolean isChoixPresent = false;

        for (Choix c : listChoix) {
            if (c.getNomChoix().equals(strChoix1)) {
                isChoixPresent = true;
                choix = c;

                break;
            }
        }

        if (!isChoixPresent) {
            System.out.println("Le choix que vous avez renseigné n'est pas présent dans ce scénario, il y a peut-être un problème sur l'orthographe :) !");
            returnStatus = false;
        }
        else {
            if (choix.getNextSituation().equals(null))
                System.out.println("Vous avez renseigné un choix menant vers une des fins du scénario !");
            else {
                List<MyPair> list = new ArrayList<MyPair>();
                int nbTotalChoix = 0;

                for (Pair<List<Choix>, Integer> p : listPaths) {
                    for (int i = 0 ; i < p.getKey().size() ; i++) {
                        if (p.getKey().get(i).getNomChoix().equals(strChoix1)) {
                            for (int j = i + 1 ; j < p.getKey().size() ; j++) {
                                boolean isMPairPresent = false;

                                for (MyPair mp : list) {
                                    if (mp.getChoix().getNomChoix().equals(p.getKey().get(j).getNomChoix())) {
                                        for (int k = 0 ; k < p.getValue(); k++) {
                                            mp.incrNbChoixTaken();
                                            nbTotalChoix += 1;
                                        }
                                            

                                        isMPairPresent = true;

                                        break;
                                    }
                                }

                                if (!isMPairPresent) {
                                    MyPair pairToAdd = new MyPair(p.getKey().get(j));
                                    nbTotalChoix += 1;

                                    for (int k = 0 ; k < p.getValue() - 1; k++) {
                                        pairToAdd.incrNbChoixTaken();
                                        nbTotalChoix += 1;
                                    }

                                    list.add(pairToAdd);
                                }    
                            }
                        }
                    }
                }

                list = sortListPairPath(list);

                if (nbTotalChoix > 0) {
                    System.out.println("Choix empruntés par les joueurs à la suite du choix " + strChoix1 + " : \n"); 

                    for (MyPair mp : list)
                        System.out.println(mp.getChoix().getNomChoix() + " : " + Math.round(100 * ((double) mp.getNbChoixTaken() / (double) nbTotalChoix) * 100d) / 100d + " % -> " + mp.getNbChoixTaken() + " choix");
                }
                else
                    System.out.println("Il semblerait que personne n'ait encore emprunté le sommet " + strChoix1 + " !"); 
            }    
        }

        return returnStatus;
    }


    private static List<MyPair> sortListPairPath(List<MyPair> listChoix) {
        List<MyPair> sortedList = new ArrayList<MyPair>();
        
        while (listChoix.size() >= 1) {
            MyPair pairToAdd = listChoix.get(0);
            int indexToRemove = 0;

            for (int j = 0; j < listChoix.size(); j++) {
                if (listChoix.get(j).getNbChoixTaken() > pairToAdd.getNbChoixTaken()) {
                    pairToAdd = listChoix.get(j);
                    indexToRemove = j;
                }
            }

            sortedList.add(pairToAdd);

            listChoix.remove(indexToRemove);
        }

        return sortedList;
    }


    private static void printMostFrequentPathsAllScenarios() {
        printMostFrequentPath(sortedMostFrequentPathsWalter, "Walter");
        printMostFrequentPath(sortedMostFrequentPathsJesse, "Jesse");
        printMostFrequentPath(sortedMostFrequentPathsHank, "Hank");
        printMostFrequentPath(sortedMostFrequentPathsGus, "Gus");
    }


    private static void printMostFrequentPath(List<Pair<List<Choix>, Integer>> mostFrequentPaths, String scenario) {
        for (Pair<List<Choix>,Integer> p : mostFrequentPaths) {
            String strToPrint = "Chemin scénario " + scenario + " : ";

            for (Choix c : p.getKey())
                strToPrint += c.getNomChoix() + ", "; //strToPrint += c.getNomChoix() + ", ";

            strToPrint += "NB de fois pris : " + p.getValue(); 

            System.out.println(strToPrint);    
        }

        System.out.println("");
    }


    private static void printChoixFromAllScenarios() {
        printChoix(listChoixWalter, "Walter");
        printChoix(listChoixJesse, "Jesse");
        printChoix(listChoixHank, "Hank");
        printChoix(listChoixGus, "Gus");
    }


    private static void printChoix(List<Choix> listChoix, String scenario) {
        for (Choix c : listChoix)
            System.out.println("Choix scénario " + scenario + " : " + c.getNomChoix() + " -> " + c.getNbFoisChoisi());
        
        System.out.println("");    
    }


    public static void printDatas() {
        printMostFrequentPathsAllScenarios();
        printChoixFromAllScenarios();
    }
}