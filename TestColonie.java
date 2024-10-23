public class TestColonie {
    private static Colonie colo;
    private static final int DIVERSITE_RSC = 4; // Nombre de types de ressources
    private static int nb = 123; // Nombre de colons dans la colonie

    public static void main(String[] args){
        colo = new Colonie(nb, DIVERSITE_RSC);  // Initialise une colonie

        if (testLinearisationSymetrique())
            System.out.println("Succès de la linéarisation symétrique");
        else
            System.out.println("Échec de la linéarisation symétrique");

        if (testGenerateRandomColo())
            System.out.println("Succès de la génération aléatoire des relations");
        else
            System.out.println("Échec de la génération aléatoire des relations");

        if (testCountConflict())
            System.out.println("Succès du comptage des conflits");
        else
            System.out.println("Échec du comptage des conflits");

        if (testSwapRsc())
            System.out.println("Succès de l'échange des ressources");
        else
            System.out.println("Échec de l'échange des ressources");

        if (testRelation())
            System.out.println("Succès du changement de relations de jalousie");
        else
            System.out.println("Échec du changement de relations de jalousie");
    }

    // Test de la linearisation symetrique
    private static boolean testLinearisationSymetrique(){
        int[][] mat = new int[colo.getPopulation().length][colo.getPopulation().length];

        // Remplit la matrice 2D avec des valeurs uniques pour chaque case
        for (int i = 0; i < mat.length; i++) {
            for (int j = i + 1; j < mat.length; j++){
                mat[i][j] = i + j;
            }
        }

        // Recupere le graphe d'interaction
        int[] graphe = colo.getGrapheInteraction();

        // Remplit le graphe comme la matrice
        for (int i = 0; i < mat.length; i++) {
            for (int j = i + 1; j < mat.length; j++){
                graphe[colo.getIndex(i, j)] = i + j;
            }
        }

        // Compare les valeurs dans le graphe et la matrice
        for (int i = 0; i < mat.length; i++){
            for (int j = i + 1; j < mat.length; j++){
                if (graphe[colo.getIndex(i, j)] != mat[i][j]){
                    return false;
                }
            }
        }

        return true;
    }

    // Test de la generation aleatoire des relations
    private static boolean testGenerateRandomColo(){
        colo.generateRandomColo();  // Génère aléatoirement les relations de jalousie

        // Vérifie que le graphe a été correctement initialisé avec des valeurs 0 ou 1
        int[] graphe = colo.getGrapheInteraction();
        for (int i = 0; i < graphe.length; i++)
            if (graphe[i] != 0 && graphe[i] != 1)
                return false;

        return true;
    }

    // Test du comptage des conflits
    private static boolean testCountConflict(){
        colo.initPreference();  // Initialise des préférences aléatoires pour la population
        colo.generateRandomColo();  // Génère des relations de jalousie

        // Appelle la méthode qui compte les conflits
        int conflictCount = colo.countConflict();

        // Le nombre de conflits devrait être un entier positif (potentiellement 0 si aucune jalousie)
        return conflictCount >= 0;
    }

    // Test de l'échange des ressources entre deux colons
    private static boolean testSwapRsc(){
        colo.initPreference();  // Initialise les préférences aléatoires

        // On prend deux colons arbitraires pour échanger leurs ressources
        int colon1 = 0;
        int colon2 = 1;

        // Sauvegarde les rangs initiaux
        int rank1Before = colo.getPopulation()[colon1].getRankAllocRsc();
        int rank2Before = colo.getPopulation()[colon2].getRankAllocRsc();

        // Effectue l'échange des ressources
        colo.swapRsc(colon1, colon2);

        // Vérifie que les ressources ont bien été échangées
        int rank1After = colo.getPopulation()[colon1].getRankAllocRsc();
        int rank2After = colo.getPopulation()[colon2].getRankAllocRsc();

        return rank1Before != rank1After && rank2Before != rank2After;
    }

    // Test de la modification des relations (jalousie)
    private static boolean testRelation(){
        colo.generateRandomColo();  // Génère des relations aléatoires

        int colon1 = 0;
        int colon2 = 1;

        // Modifie la relation entre colon1 et colon2 pour les rendre jaloux
        colo.relation(1, colon1, colon2);

        // Vérifie que la jalousie a bien été affectée
        if (colo.getGrapheInteraction()[colo.getIndex(colon1, colon2)] != 1) {
            return false;
        }

        // Modifie la relation pour enlever la jalousie
        colo.relation(0, colon1, colon2);

        // Vérifie que la jalousie a bien été enlevée
        return colo.getGrapheInteraction()[colo.getIndex(colon1, colon2)] == 0;
    }
}