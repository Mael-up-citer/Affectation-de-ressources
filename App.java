import java.util.Scanner;
import java.awt.Point;

public class App{
    private static final int DIVERSITER_RSC = NameRessource.values().length;  // Nb de ressources différentes
    private static int nbColonEtRsc; // Nb de colons et ressources
    private static Scanner sc = new Scanner(System.in);
    private static Capitaine cap;   //capitaine de la simulation

    public static void main(String[] args){
        game();  // Lance la simulation
    }

    // Methode principale du jeu
    private static void game(){
        initNbColon();  // Initialise le nombre de colons
        cap = new Capitaine(DIVERSITER_RSC, new Colonie(nbColonEtRsc, DIVERSITER_RSC));
        Colonie colo = cap.getColonie();  // Récupère la colonie
        colo.generateRandomColo();  // Génère les relations sociales entre colons

        mainMenu();  // Lance le menu principal
    }

    // Menu principal
    private static void mainMenu(){
        while(true){
            System.out.println("\nMenu :");
            System.out.println("1) Ajouter une relation de jalousie entre deux colons");
            System.out.println("2) Ajouter les préférences d'un colon");
            System.out.println("3) Fin");
            System.out.print("Choisissez une option : ");

            String choix = sc.nextLine();

            switch(choix){
                // Ajouter une relation entre deux colons
                case "1":
                    ajouterRelationJalousie();
                    break;
                // Ajouter les preferences d’un colon;
                case "2":
                    ajouterPreferences();
                    break;
                // Fin de l'initialisation passe a la resolution
                case "3":
                    // CHECK ME
                    for(int i = 0; i < cap.getColonie().population.length; i++)
                        cap.getColonie().population[i].setRandomPreference();

                    if(verifierPreferences()){
                        menuEchange();
                    }
                    break;
                // Si la saisi n'a pas de correspondance
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Sous-menu d'échange des ressources
    private static void menuEchange(){
        while(true){
            cap.dispSystemSate();  // Affiche l'etat du système

            System.out.println("\nMenu d'échange :");
            System.out.println("1) Échanger les ressources de deux colons");
            System.out.println("2) Afficher le nombre de colons jaloux");
            System.out.println("3) Fin");
            System.out.print("Choisissez une option : ");

            String choixEchange = sc.nextLine();

            switch(choixEchange){
                // Echanger les ressources de deux colons
                case "1":
                    echangerRessources();
                    break;
                // Afficher le nombre de colons jaloux
                case "2":
                    afficherNombreColonsJaloux();
                    break;
                // termine le programme
                case "3":
                    System.exit(0);
                // Si la saisi n'a pas de correspondance
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Methode pour ajouter une relation de jalousie
    private static void ajouterRelationJalousie(){
        Point p = get2Colon();  // Recupere deux colons
        cap.getColonie().relation(1, p.x, p.y);  // Cree la relation de jalousie
    }

    // Methode pour ajouter les preferences d'un colon
    private static void ajouterPreferences(){
        NameRessource[] rsc = null; // Tableau de ressource qui va reccevoir la saisie de l'user
        int colon;  // Colon concerne

        while(true){
            System.out.print("Entrez le numéro du colon (0-" + (nbColonEtRsc - 1) + ") et " + DIVERSITER_RSC + " ressources dans l'ordre de préférence : ");
            String input = sc.nextLine();
            // cree un tab qui contient la ligne coupe au espaces
            String[] inputs = input.split(" ");

            // Si la taille de l'input est insuffisante
            if(inputs.length - 1 != DIVERSITER_RSC)
                System.out.println("Erreur : Vous devez entrer un numéro de colon et exactement " + DIVERSITER_RSC + " ressources.");

            else{
                try{
                    colon = Integer.parseInt(inputs[0]);
                    rsc = parsePreferences(inputs);
                    break;
                // Catch les erreurs lie au numero du colon
                } catch (NumberFormatException e){
                    System.out.println("Erreur : Le numéro du colon doit être un entier.");
                // Catch les erreurs lie au ressources
                } catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        // Si tout c'est bien passe
        cap.getColonie().population[colon].setPreference(rsc);  // Definit les preferences du colon
    }

    // Methode pour verifier les preferences des colons
    private static boolean verifierPreferences(){
        Colonie colo = cap.getColonie();
        boolean isGood = true;

        for(int i = 0; i < colo.population.length; i++){
            if(colo.population[i].getPreference()[DIVERSITER_RSC - 1] == null){
                isGood = false;
                System.out.println("Le colon " + i + " n'a pas de préférences complètes.");
            }
        }
        return isGood;
    }

    // Methode pour echanger les ressources de deux colons
    private static void echangerRessources(){
        Point p = get2Colon();
        cap.getColonie().swapRsc(p.x, p.y);  // Echange les ressources
    }

    // Affiche le nombre de colons jaloux
    private static void afficherNombreColonsJaloux(){
        int nombreDeJaloux = cap.getColonie().countConflict();
        System.out.println("Il y a " + nombreDeJaloux + " colons jaloux.");
    }

    // Initialise le nombre de colons
    private static void initNbColon(){
        while(true){
            System.out.print("Entrez le nombre de colons (>= 1) : ");
            try{
                nbColonEtRsc = Integer.parseInt(sc.nextLine());
                if (nbColonEtRsc >= 1) break;
                else System.out.println("Le nombre doit être supérieur ou égal à 1.");
            } catch (NumberFormatException e){
                System.out.println("Veuillez entrer un nombre entier.");
            }
        }
    }

    // Obtient deux colons
    private static Point get2Colon(){
        int colon1, colon2;

        while (true){
            colon1 = demanderColon("premier");
            colon2 = demanderColon("deuxième");
            if (colon1 != colon2) 
                break;
            else 
                System.out.println("Les deux colons doivent être différents.");
        }

        return new Point(colon1, colon2);
    }

    // Demande à l'utilisateur un numero de colon valide
    private static int demanderColon(String position){
        int colon;
        while(true){
            System.out.print("Entrez le numéro du " + position + " colon (0-" + (nbColonEtRsc - 1) + ") : ");
            try{
                colon = Integer.parseInt(sc.nextLine());
                if (isValideColon(colon)) return colon;
                else System.out.println("Numéro de colon invalide.");
            } catch (NumberFormatException e){
                System.out.println("Veuillez entrer un nombre entier.");
            }
        }
    }

    // Valide un numero de colon
    private static boolean isValideColon(int c){
        return (c >= 0 && c < nbColonEtRsc);
    }

    // Analyse et valide les preferences
    private static NameRessource[] parsePreferences(String[] inputs){
        NameRessource[] rsc = new NameRessource[DIVERSITER_RSC];
        int[] suivi = new int[DIVERSITER_RSC];

        for(int i = 0; i < DIVERSITER_RSC; i++){
            NameRessource ressource = NameRessource.valueOf(inputs[i + 1].toUpperCase());

            if (suivi[ressource.getValeur()] == 1)
                throw new IllegalArgumentException("Erreur : La ressource '" + ressource + "' est déjà utilisée.");

            suivi[ressource.getValeur()] = 1;
            rsc[i] = ressource;
        }
        return rsc;
    }
}