import java.util.Scanner;
import java.awt.Point;

public class App {    
    // Nombre de colons et de ressources, défini par l'utilisateur
    private static int nbColonEtRsc;

    // Scanner pour les entrées utilisateur
    private static Scanner sc = new Scanner(System.in);

    // Instance du capitaine de la simulation
    private static Capitaine cap;

    /**
     * Point d'entrée principal de l'application.
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        game();  // Lance la simulation
    }

    /**
     * Initialise et lance le jeu, en configurant le nombre de colons et le capitaine.
     */
    private static void game() {
        initNbColon();  // Initialise le nombre de colons
        cap = new Capitaine(nbColonEtRsc, new Colonie(nbColonEtRsc));
        cap.getColonie().generateRandomColo();  // Génère des relations sociales aléatoires entre colons

        mainMenu();  // Lance le menu principal
    }

    /**
     * Affiche et gère le menu principal du jeu.
     */
    private static void mainMenu() {
        while (true) {
            System.out.println("\nMenu :");
            System.out.println("1) Ajouter une relation de jalousie entre deux colons");
            System.out.println("2) Ajouter les préférences d'un colon");
            System.out.println("3) Fin");
            System.out.print("Choisissez une option : ");

            String choix = sc.nextLine();

            switch (choix) {
                case "1":
                    ajouterRelationJalousie();  // Ajoute une relation de jalousie entre deux colons
                    break;
                case "2":
                    ajouterPreferences();  // Ajoute les préférences d'un colon
                    break;
                case "3":
                    // Si les préférences sont valides, lance le menu d'échange
                    if (verifierPreferences())
                        menuEchange();

                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }

    /**
     * Affiche et gère le sous-menu d'échange des ressources.
     */
    private static void menuEchange() {
        while (true) {
            cap.dispSystemSate();  // Affiche l'état du système

            System.out.println("\nMenu d'échange :");
            System.out.println("1) Échanger les ressources de deux colons");
            System.out.println("2) Afficher le nombre de colons jaloux");
            System.out.println("3) Fin");
            System.out.print("Choisissez une option : ");

            String choixEchange = sc.nextLine();

            switch (choixEchange) {
                case "1":
                    echangerRessources();  // Échange les ressources entre deux colons
                    break;
                case "2":
                    afficherNombreColonsJaloux();  // Affiche le nombre de colons jaloux
                    break;
                case "3":
                    System.exit(0);  // Termine le programme
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }

    /**
     * Ajoute une relation de jalousie entre deux colons.
     */
    private static void ajouterRelationJalousie() {
        Point p = get2Colon();  // Récupère deux colons
        cap.getColonie().changeRelation(1, p.x, p.y);  // Crée la relation de jalousie
    }

    /**
     * Ajoute les préférences d'un colon en demandant des saisies utilisateur.
     */
    private static void ajouterPreferences() {
        Ressource[] rsc = null;  // Tableau pour stocker les préférences
        int colon;  // Numéro du colon concerné

        while (true) {
            cap.affRscDispo();  // Affiche les ressources disponibles
            System.out.print("Entrez le numéro du colon (0-" + (nbColonEtRsc - 1) + ") et " + nbColonEtRsc + " ressources dans l'ordre de préférence : ");
            String input = sc.nextLine();
            input = input.toUpperCase();
            String[] inputs = input.split(" ");

            if (inputs.length - 1 != nbColonEtRsc)  // Vérifie la validité de l'input
                System.out.println("Erreur : Vous devez entrer un numéro de colon et exactement " + nbColonEtRsc + " ressources.");
            else {
                try {
                    colon = Integer.parseInt(inputs[0]);
                    rsc = parsePreferences(inputs);  // Analyse et valide les préférences
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : Le numéro du colon doit être un entier.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        cap.getColonie().getPopulation()[colon].setPreference(rsc);  // Définit les préférences du colon
    }

    /**
     * Vérifie que chaque colon a bien défini ses préférences.
     * @return true si toutes les préférences sont définies, sinon false
     */
    private static boolean verifierPreferences() {
        Colonie colo = cap.getColonie();
        boolean isGood = true;
        Colon[] pop = colo.getPopulation();

        for (int i = 0; i < pop.length; i++) {
            if (pop[i].getPreference() == null) {
                isGood = false;
                System.out.println("Le colon " + i + " n'a pas de préférences.");
            }
        }
        return isGood;
    }

    /**
     * Échange les ressources entre deux colons.
     */
    private static void echangerRessources() {
        Point p = get2Colon();
        cap.getColonie().swapRsc(p.x, p.y);  // Échange les ressources
    }

    /**
     * Affiche le nombre de colons jaloux dans la colonie.
     */
    private static void afficherNombreColonsJaloux() {
        int nombreDeJaloux = cap.getColonie().countConflict();
        System.out.println("Il y a " + nombreDeJaloux + " colons jaloux.");
    }

    /**
     * Demande à l'utilisateur de définir le nombre de colons.
     */
    private static void initNbColon() {
        while (true) {
            System.out.print("Entrez le nombre de colons (>= 1) : ");
            try {
                nbColonEtRsc = Integer.parseInt(sc.nextLine());
                if (nbColonEtRsc >= 1) break;
                else System.out.println("Le nombre doit être supérieur ou égal à 1.");
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier.");
            }
        }
    }

    /**
     * Demande et obtient les numéros de deux colons différents.
     * @return une instance Point contenant les numéros des deux colons
     */
    private static Point get2Colon() {
        int colon1, colon2;

        while (true) {
            colon1 = demanderColon("premier");
            colon2 = demanderColon("deuxième");
            if (colon1 != colon2)
                break;
            else
                System.out.println("Les deux colons doivent être différents.");
        }

        return new Point(colon1, colon2);
    }

    /**
     * Demande à l'utilisateur un numéro de colon spécifique.
     * @param position position du colon (premier ou deuxième)
     * @return le numéro de colon saisi
     */
    private static int demanderColon(String position) {
        int colon;
        while (true) {
            System.out.print("Entrez le numéro du " + position + " colon (0-" + (nbColonEtRsc - 1) + ") : ");
            try {
                colon = Integer.parseInt(sc.nextLine());
                if (isValideColon(colon)) return colon;
                else System.out.println("Numéro de colon invalide.");
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier.");
            }
        }
    }

    /**
     * Vérifie si le numéro de colon est valide par rapport au nombre total de colons.
     * @param c numéro de colon à valider
     * @return true si le numéro est dans la plage valide, sinon false
     */
    private static boolean isValideColon(int c) {
        return (c >= 0 && c < nbColonEtRsc);
    }

    /**
     * Analyse et valide les préférences des colons à partir de l'entrée utilisateur.
     * Crée un tableau de préférences contenant les types de ressources dans l'ordre de préférence.
     * @param inputs tableau contenant les préférences sous forme de chaînes de caractères
     * @return un tableau Ressource contenant les préférences dans l'ordre saisi
     * @throws IllegalArgumentException si une ressource est invalide
     */
    private static Ressource[] parsePreferences(String[] inputs) {
        Ressource[] rsc = new Ressource[nbColonEtRsc];

        for (int i = 1; i < nbColonEtRsc; i++) {
            try {
                rsc[i] = cap.getRessources()[Ressource.getNameToId().get(inputs[i])];
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Ressource invalide : " + inputs[i + 1]);
            }
        }
        return rsc;
    }
}