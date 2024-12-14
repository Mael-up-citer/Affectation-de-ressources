package partie2;

import partie1.Colonie;

import java.util.Map;
import java.util.Random;

/**
 * Cette classe est responsable de l'optimisation des affectations entre les colons et les ressources dans une colonie.
 * Elle utilise une approche heuristique pour minimiser le coût global des affectations.
 */
public class SolutionOptimise {
    private Colonie colonie;

    /**
     * Constructeur de la classe SolutionOptimise.
     * @param colonie La colonie contenant les colons, les ressources, et les préférences à optimiser.
     */
    public SolutionOptimise(Colonie colonie) {
        this.colonie = colonie;
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
        // Générer une solution initiale
        Map<String, String> actuelleSolution = colonie.genererSolutionNaive(); // Génère une solution naïve

        // Calculer le coût initial
        int actuelleCout = colonie.calculerCout(); // Méthode pour calculer le coût initial de la solution

        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            // Choisir deux colons différents au hasard
            String[] colonNoms = actuelleSolution.keySet().toArray(new String[0]);
            String colon1 = colonNoms[random.nextInt(colonNoms.length)];
            String colon2;

            // S'assurer que colon1 et colon2 sont différents
            do {
                colon2 = colonNoms[random.nextInt(colonNoms.length)];
            } while (colon1.equals(colon2));

            // Échanger leurs ressources
            String resource1 = actuelleSolution.get(colon1);
            String resource2 = actuelleSolution.get(colon2);
            actuelleSolution.put(colon1, resource2);
            actuelleSolution.put(colon2, resource1);

            // Calculer le nouveau coût
            int nouveauCost = colonie.calculerCout();

            // Accepter ou annuler l'échange
            if (nouveauCost >= actuelleCout) {
                // Annuler l'échange si le coût augmente ou reste le même
                actuelleSolution.put(colon1, resource1);
                actuelleSolution.put(colon2, resource2);
            } else {
                // Accepter la solution avec un coût réduit
                actuelleCout = nouveauCost;
            }
        }
        return actuelleSolution; // Retourner la solution optimisée
    }
}
