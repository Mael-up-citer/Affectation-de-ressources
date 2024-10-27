package paa.projet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principale {
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Entrez le nombre de colons : ");
		int nbr_colons = scanner.nextInt();
		scanner.nextLine();
		Colonie colonie = new Colonie(nbr_colons);
		while (true) {
			System.out.println("\nMenu :");
			System.out.println("(1) Ajouter une relation entre deux colons");
			System.out.println("(2) Ajouter les préférences d'un colon");
			System.out.println("(3) Fin");
			int choix = scanner.nextInt();
			scanner.nextLine();
			if (choix == 1) {
				System.out.println(
						"Entrez les noms des deux colons (ex: A B) pensez à laisser un espace entre les deux colons : ");
				String colon1 = scanner.next();
				String colon2 = scanner.next();
				scanner.nextLine();
				colonie.ajoutRelation(colon1, colon2);
			} else if (choix == 2) {
				System.out.println(
						"Entrez le nom du colon suivi de ses préférences séparées par des espaces (ex: A 1 2 3): ");
				String nom = scanner.next();
				List<Integer> preferences = new ArrayList<>();
				for (int i = 0; i < nbr_colons; i++) {
					preferences.add(scanner.nextInt());
				}
				scanner.nextLine();
				colonie.ajoutPreference(nom, preferences);
			} else if (choix == 3) {
				List<String> colonsSansPreferences = new ArrayList<>();
				for (Colon c : colonie.getColonie().values()) {
					if (!c.hasPreferences()) {
						colonsSansPreferences.add(c.getNom());
					}
				}
				if (!colonsSansPreferences.isEmpty()) {
					System.out.println(
							"Les colons suivants n'ont pas de préférences définies : " + colonsSansPreferences);
				} else {
					System.out.println("Configuration terminée.");
				}
				break;
			} else {
				System.out.println("Choix invalide. Veuillez réessayer.");
			}
		}
		colonie.distribRessources();
		colonie.afficheAffectations();
		while (true) {
			System.out.println("\nMenu d'affectation :");
			System.out.println("(1) Échanger les ressources de deux colons");
			System.out.println("(2) Afficher le nombre de colons jaloux");
			System.out.println("(3) Fin");

			int choix = scanner.nextInt();
			scanner.nextLine();

			if (choix == 1) {
				System.out.println("Entrez les noms des deux colons à échanger (ex: A B): ");
				String colon1 = scanner.next();
				String colon2 = scanner.next();
				scanner.nextLine();
				colonie.echangerRessources(colon1, colon2);
				colonie.afficheAffectations();

			} else if (choix == 2) {
				int jalousie = colonie.calculeJalousie();
				System.out.println("Nombre de colons jaloux: " + jalousie);

			} else if (choix == 3) {
				System.out.println("Fin du programme.");
				break;

			} else {
				System.out.println("Choix invalide. Veuillez réessayer.");
			}
		}
		scanner.close();
	}
}