package partie2;

import partie1.Colonie;
import partie1.InterfaceUtilisateur;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class MainHandler {
	public static void main(String[] args) {
		Colonie colonie = new Colonie();
		Map<String, Integer> solution = null;

		if (args.length > 0) {
			String inputFile = args[0];
			try {
				FileParser parser = new FileParser(inputFile);
				colonie = parser.parseFile();
				System.out.println("Données de la colonie chargées depuis le fichier : " + inputFile);

				// Menu pour partie 2
				Scanner scanner = new Scanner(System.in);
				boolean exit = false;
				while (!exit) {
					System.out.println("Menu :");
					System.out.println("1) Résolution automatique");
					System.out.println("2) Sauvegarder la solution actuelle");
					System.out.println("3) Quitter");

					int choice = getUserChoice(scanner);
					switch (choice) {
					case 1 -> {
						SolutionOptimizer optimizer = new SolutionOptimizer(colonie);
						solution = optimizer.optimizeSolution(colonie.getColons().size()*10);
						System.out.println("Solution optimisée : " + solution);
						System.out.println("Coût de la solution : " + colonie.calculerCout());
					}
					case 2 -> {
						System.out.println("Entrez le nom du fichier de sortie :");
						String outputFile = scanner.nextLine();
						if (solution == null) {
							try {
								SolutionSaver.saveSolution(outputFile, colonie.genererSolutionNaive());
							} catch (IOException e) {
								System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
							}
						} else {
							try {
								SolutionSaver.saveSolution(outputFile, solution);
							} catch (IOException e) {
								System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
							}
						}
					}
					case 3 -> {
						exit = true;
						System.out.println("Programme terminé.");
					}
					default -> System.out.println("Choix invalide. Veuillez réessayer.");
					}
				}
				scanner.close();
			} catch (IOException | IllegalArgumentException e) {
				System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
				return;
			}
		} else {
			InterfaceUtilisateur u = new InterfaceUtilisateur();
			u.demarrer();
		}
	}

	private static int getUserChoice(Scanner scanner) {
		int choice = -1;
		while (choice < 1 || choice > 3) {
			try {
				System.out.print("Votre choix : ");
				choice = Integer.parseInt(scanner.nextLine());
				if (choice < 1 || choice > 3) {
					System.out.println("Votre choix doit être entre 1 (inclus) et 3 (inclus) .");
				}
			} catch (NumberFormatException e) {
				System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
			}
		}
		return choice;
	}
}
