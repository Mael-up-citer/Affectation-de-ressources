package partie1;

import java.util.*;

public class InterfaceUtilisateur {
	private Scanner scanner;
	private Colonie colonie;

	public InterfaceUtilisateur() {
		scanner = new Scanner(System.in);
		colonie = new Colonie();
	}

	public void demarrer() {
		int n = 0;
		while (n <= 0 || n > 26) {
			try {
				System.out.println("Entrez le nombre de colons (max 26) :");
				n = scanner.nextInt();
				scanner.nextLine(); // Consommer le retour à la ligne
				if (n <= 0 || n > 26) {
					System.out.println("Erreur : Le nombre de colons doit être entre 1 et 26.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
				scanner.nextLine(); // Consommer l'entrée invalide
			}
		}

		// Ajouter les colons
		for (int i = 0; i < n; i++) {
			colonie.ajouterColon(String.valueOf((char) ('A' + i)));
		}

		boolean finConfiguration = false;
		while (!finConfiguration) {
			System.out.println("Menu :");
			System.out.println("1) Ajouter une relation 'ne s’aiment pas'");
			System.out.println("2) Ajouter les préférences d’un colon");
			System.out.println("3) Fin");

			int choix = -1;
			while (choix < 1 || choix > 3) {
				try {
					choix = scanner.nextInt();
					scanner.nextLine(); // Consommer le retour à la ligne
					if (choix < 1 || choix > 3) {
						System.out.println("Erreur : Choix incorrect. Veuillez choisir entre 1 et 3.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
					scanner.nextLine(); // Consommer l'entrée invalide
				}
			}

			switch (choix) {
			case 1 -> {
				// Ajouter une relation "ne s’aiment pas"
				boolean relationAjoutee = false;
				while (!relationAjoutee) {
					try {
						System.out.println("Entrez les noms des deux colons séparés par un espace :");
						String[] noms = scanner.nextLine().split(" ");
						if (noms.length != 2) {
							throw new IllegalArgumentException("Veuillez entrer exactement deux colons.");
						}
						colonie.ajouterRelation(noms[0], noms[1]);
						relationAjoutee = true; // Relation ajoutée avec succès
					} catch (IllegalArgumentException e) {
						System.out.println("Erreur : " + e.getMessage());
					}
				}
			}
			case 2 -> {
				// Ajouter les préférences d'un colon
				boolean preferencesAjoutees = false;
				while (!preferencesAjoutees) {
					try {
						System.out.println("Entrez le nom du colon suivi de ses préférences (ex : A 1 2 3) :");
						String[] input = scanner.nextLine().split(" ");
						String nom = input[0];
						List<Integer> preferences = new ArrayList<>();
						for (int i = 1; i < input.length; i++) {
							preferences.add(Integer.parseInt(input[i]));
						}
						colonie.ajouterPreferences(nom, preferences);
						preferencesAjoutees = true; // Préférences ajoutées avec succès
					} catch (NumberFormatException e) {
						System.out.println("Erreur : Veuillez entrer des numéros de ressources valides.");
					} catch (IllegalArgumentException e) {
						System.out.println("Erreur : " + e.getMessage());
					}
				}
			}
			case 3 -> {
				boolean tousLesColonsOntPreferences = true;
				for (String colonNom : colonie.getColons().keySet()) {
					if (colonie.getColons().get(colonNom).getPreferences() == null) {
						tousLesColonsOntPreferences = false;
						break;
					}
				}
				if (tousLesColonsOntPreferences) {
					finConfiguration = true; // Fin de la configuration si tous les colons ont des préférences
				} else {
					System.out.println(
							"Erreur : Tous les colons doivent avoir des préférences avant de terminer la configuration.");
				}
			}
			}
		}

		Map<String, Integer> solution = colonie.genererSolutionNaive();
		colonie.afficherAffectations();

		// Simulation avec échange des ressources
		boolean finSimulation = false;
		while (!finSimulation) {
			System.out.println("Menu :");
			System.out.println("1) Échanger les ressources de deux colons");
			System.out.println("2) Afficher le nombre de colons jaloux");
			System.out.println("3) Fin");

			int choix = -1;
			while (choix < 1 || choix > 3) {
				try {
					choix = scanner.nextInt();
					scanner.nextLine(); // Consommer le retour à la ligne
					if (choix < 1 || choix > 3) {
						System.out.println("Erreur : Choix incorrect. Veuillez choisir entre 1 et 3.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
					scanner.nextLine(); // Consommer l'entrée invalide
				}
			}

			switch (choix) {
			case 1 -> {
				// Échanger les ressources de deux colons
				boolean echangeEffectue = false;
				while (!echangeEffectue) {
					try {
						System.out.println("Entrez les noms des deux colons à échanger :");
						String[] noms = scanner.nextLine().split(" ");
						if (noms.length != 2) {
							throw new IllegalArgumentException("Veuillez entrer exactement deux colons.");
						}
						if (!colonie.colonExiste(noms[0]) || !colonie.colonExiste(noms[1])) {
							throw new IllegalArgumentException("Les colons doivent exister.");
						}

						Integer ressource1 = solution.get(noms[0]);
						Integer ressource2 = solution.get(noms[1]);

						if (ressource1 != null && ressource2 != null) {
							solution.put(noms[0], ressource2);
							solution.put(noms[1], ressource1);
							echangeEffectue = true; // Échange effectué avec succès
						} else {
							throw new IllegalArgumentException("Les ressources des colons sont inexistantes.");
						}
						colonie.afficherAffectations();
					} catch (IllegalArgumentException e) {
						System.out.println("Erreur : " + e.getMessage());
					}
				}
			}
			case 2 -> {
				// Afficher le nombre de colons jaloux
				System.out.println("Nombre de colons jaloux : " + colonie.calculerCout());
			}
			case 3 -> {
				finSimulation = true;
			}
			}
		}

		scanner.close();

	}
}
