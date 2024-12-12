package partie2;

import partie1.Colonie;

import java.util.Map;
import java.util.Random;

public class SolutionOptimizer {
	private Colonie colonie;

	public SolutionOptimizer(Colonie colonie) {
		this.colonie = colonie;
	}

	public Map<String, Integer> optimizeSolution(int iterations) {
		Map<String, Integer> currentSolution = colonie.genererSolutionNaive();
		int currentCost = colonie.calculerCout();

		Random random = new Random();
		for (int i = 0; i < iterations; i++) {
			// Choisir deux colons au hasard
			String[] colonNames = currentSolution.keySet().toArray(new String[0]);
			String colon1 = colonNames[random.nextInt(colonNames.length)];
			String colon2 = colonNames[random.nextInt(colonNames.length)];

			// Échanger les ressources
			int resource1 = currentSolution.get(colon1);
			int resource2 = currentSolution.get(colon2);
			currentSolution.put(colon1, resource2);
			currentSolution.put(colon2, resource1);

			// Calculer le nouveau coût
			int newCost = colonie.calculerCout();

			// Accepter ou annuler l'échange
			if (newCost >= currentCost) {
				currentSolution.put(colon1, resource1);
				currentSolution.put(colon2, resource2);
			} else {
				currentCost = newCost;
			}
		}
		return currentSolution;
	}
}
