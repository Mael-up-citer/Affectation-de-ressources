package partie2;

import partie1.Colonie;
import partie1.Colon;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe est responsable de l'optimisation des affectations entre les colons et les ressources dans une colonie.
 * Elle utilise plusieurs approches pour minimiser le coût global des affectations.
 */
public class SolutionOptimise {
    private Colonie colonie;

    private static Map<String, String> meilleureSolutionConnue = new HashMap<>();
    static {
        meilleureSolutionConnue.put("0", "o6");
        meilleureSolutionConnue.put("1", "o3");
        meilleureSolutionConnue.put("2", "o1");
        meilleureSolutionConnue.put("3", "o2");
        meilleureSolutionConnue.put("4", "o10");
        meilleureSolutionConnue.put("5", "o4");
        meilleureSolutionConnue.put("6", "o7");
        meilleureSolutionConnue.put("7", "o8");
        meilleureSolutionConnue.put("8", "o9");
        meilleureSolutionConnue.put("9", "o5");
    }

    int meilleurCoutAttendu = 1;


    /**
     * Constructeur de la classe SolutionOptimise.
     * @param colonie La colonie contenant les colons, les ressources, et les préférences à optimiser.
     */
    public SolutionOptimise(Colonie colonie) {
        this.colonie = colonie;
    }
    
    public Map<String, String> genererSolutionAdaptative() {
        int nombreColons = colonie.getColons().size();

        if (nombreColons <= 10) {
            // Utiliser un algorithme exhaustif pour les petits cas
            return rechercheExhaustive();
        } else {
            // Utiliser une optimisation probabiliste pour les grands cas
            //return optimiseSolution2(nombreColons * nombreColons); // Nombre d'itérations arbitraire
            return null;
        }
    }

    /**
     * Optimise les affectations des ressources aux colons en minimisant les jalousies
     * avec une stratégie probabiliste. Basé sur des échange guidé
     *
     * @param iterations Le nombre d'itérations pour l'optimisation.
     * @return La map des affectations optimisées.
     *//*
    public Map<String, String> optimiseSolution2(int iterations) {
        Map<String, String> actuelleSolution = colonie.genererSolutionNaive();
        int actuelleCout = colonie.calculerCout();

        Map<String, String> meilleureSolution = new HashMap<>(actuelleSolution);
        int meilleurCout = actuelleCout;

        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            String colon1 = choisirColon(random);

            String colon2;
            do {
                colon2 = choisirColon(random);
            } while (colon1.equals(colon2));

            String resource1 = actuelleSolution.get(colon1);
            String resource2 = actuelleSolution.get(colon2);

            actuelleSolution.put(colon1, resource2);
            actuelleSolution.put(colon2, resource1);

            colonie.setAffectations(actuelleSolution);
            int nouveauCout = colonie.calculerCout();

            if (nouveauCout < actuelleCout) {
                actuelleCout = nouveauCout;
                if (nouveauCout < meilleurCout) {
                    meilleureSolution = new HashMap<>(actuelleSolution);
                    meilleurCout = nouveauCout;
                }
            } else if (random.nextDouble() > 0.7) {
                actuelleCout = nouveauCout;
            } else {
                actuelleSolution.put(colon1, resource1);
                actuelleSolution.put(colon2, resource2);
            }
        }
        colonie.setAffectations(meilleureSolution);
        return meilleureSolution;
    }

    /**
     * Optimise les affectations des ressources aux colons pour réduire le coût global.
     * L'algorithme effectue un nombre défini d'itérations pour tenter d'améliorer la solution initiale en échangeant des ressources.
     * 
     * @param iterations Le nombre d'itérations à effectuer pour l'optimisation.
     * @return Une Map représentant la solution optimisée où chaque clé est un nom de colon
     *         et la valeur correspond à la ressource qui lui est assignée.
     */
    public Map<String, String> optimiseSolution(int iterations) {
        Map<String, String> actuelleSolution = colonie.genererSolutionNaive();
        int actuelleCout = colonie.calculerCout();

        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            String[] colonNoms = actuelleSolution.keySet().toArray(new String[0]);
            String colon1 = colonNoms[random.nextInt(colonNoms.length)];
            String colon2;

            do {
                colon2 = colonNoms[random.nextInt(colonNoms.length)];
            } while (colon1.equals(colon2));

            String resource1 = actuelleSolution.get(colon1);
            String resource2 = actuelleSolution.get(colon2);
            actuelleSolution.put(colon1, resource2);
            actuelleSolution.put(colon2, resource1);

            int nouveauCost = colonie.calculerCout();

            if (nouveauCost >= actuelleCout) {
                actuelleSolution.put(colon1, resource1);
                actuelleSolution.put(colon2, resource2);
            } else {
                actuelleCout = nouveauCost;
            }
        }
        return actuelleSolution;
    }

    /**
     * Méthode pour choisir un colon avec 80% de chance parmi les jaloux,
     * et 20% parmi la population totale.
     *//*
    private String choisirColon(Random random) {
        if (!colonie.getColonsJaloux().isEmpty() && random.nextDouble() < 0.8) {
            List<Colon> jaloux = new ArrayList<>(colonie.getColonsJaloux());
            return jaloux.get(random.nextInt(jaloux.size())).getNom();
        } else {
            List<String> tousLesColons = new ArrayList<>(colonie.getColons().keySet());
            return tousLesColons.get(random.nextInt(tousLesColons.size()));
        }
    }
*/
    /**
     * Effectue une recherche exhaustive pour trouver l'affectation optimale des ressources
     * minimisant les jalousies parmi les colons.
     */
    public Map<String, String> rechercheExhaustive() {
        List<String> nomsColons = new ArrayList<>(colonie.getColons().keySet());
        List<String> nomsRessources = new ArrayList<>(colonie.getRessources());

        if (nomsColons.size() != nomsRessources.size())
            throw new IllegalArgumentException("Le nombre de colons et de ressources doit être égal.");

        List<List<String>> permutations = genererPermutations(nomsRessources);
        Map<String, String> meilleureAffectation = null;
        int minJalousie = Integer.MAX_VALUE;

        for (List<String> permutation : permutations) {
            Map<String, String> affectationTemporaire = new HashMap<>();
            for (int i = 0; i < nomsColons.size(); i++) {
                affectationTemporaire.put(nomsColons.get(i), permutation.get(i));
            }

            colonie.setAffectations(affectationTemporaire);
            int jalousie = colonie.calculerCout();

             // Vérification explicite
            if (affectationTemporaire.equals(meilleureSolutionConnue)) {
                System.out.println("Solution attendue rencontrée : " + affectationTemporaire);
                System.out.println("Coût calculé : " + jalousie + ", coût attendu : " + meilleurCoutAttendu);
            }

            if (jalousie < minJalousie) {
                minJalousie = jalousie;
                meilleureAffectation = new HashMap<>(affectationTemporaire);

                // Arrêt si jalousie minimale possible
                if (minJalousie == 0)
                    break;
            }
        }
        if (meilleureAffectation != null) {
            colonie.setAffectations(meilleureAffectation);
        }
        return meilleureAffectation;
    }

    /**
     * Crée une affectation à partir des noms des colons et d'une permutation de ressources.
     *
     * @param nomsColons La liste des noms des colons.
     * @param permutation La permutation actuelle des ressources.
     * @return Une Map représentant l'affectation actuelle.
     */
    private Map<String, String> creerAffectation(List<String> nomsColons, List<String> permutation) {
        Map<String, String> affectation = new HashMap<>();
        for (int i = 0; i < nomsColons.size(); i++) {
            affectation.put(nomsColons.get(i), permutation.get(i));
        }
        return affectation;
    }

    /**
     * Génère toutes les permutations possibles d'une liste.
     *
     * @param liste La liste d'entrée pour laquelle générer les permutations.
     * @return Une liste de listes contenant toutes les permutations.
     */
    private List<List<String>> genererPermutations(List<String> liste) {
        if (liste.isEmpty()) {
            List<List<String>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        List<List<String>> permutations = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            String element = liste.remove(i);
            List<List<String>> sousPermutations = genererPermutations(liste);
            for (List<String> sousPerm : sousPermutations) {
                sousPerm.add(0, element);
            }
            permutations.addAll(sousPermutations);
            liste.add(i, element);
        }
        return permutations;
    }
}