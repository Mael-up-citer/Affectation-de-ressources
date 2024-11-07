public class Colonie {
    // Graphe des interactions sociales entre les colons (0 = jaloux, 1 = non jaloux)
    private int[] grapheInteraction;

    // Tableau contenant la population de la colonie
    private Colon[] population;

    /**
     * Constructeur de la classe Colonie.
     * Initialise la population et le graphe d'interaction en fonction du nombre de colons et de la diversité des ressources.
     *
     * @param nbColon Nombre de colons dans la colonie
     * @param diversiterRsc Nombre de types de ressources différents pour chaque colon
     */
    public Colonie(int nbColon, int diversiterRsc) {
        // Alloue un tableau pour le graphe d'interactions sociales. Le graphe est une matrice triangulaire.
        grapheInteraction = new int[((nbColon * (nbColon - 1)) / 2)]; // Graphe sans la diagonale
        population = new Colon[nbColon]; // Alloue de l'espace mémoire pour la population

        // Remplie la population avec des instances de Colon
        for (int i = 0; i < population.length; i++)
            population[i] = new Colon(diversiterRsc); // Crée un colon avec les ressources données
    }

    /**
     * Génère de manière aléatoire les relations sociales entre les colons (jalousie ou non).
     * Associe des relations entre chaque paire de colons.
     */
    public void generateRandomColo() {
        // Parcourt les lignes du graphe, en excluant la dernière car elle correspond à la diagonale
        for (int i = 0; i < population.length - 1; i++) {
            // Parcourt les colonnes du graphe
            for (int j = i + 1; j < population.length; j++) {
                // 50% de chance que deux colons soient jaloux
                if (Math.random() > 0.5)
                    grapheInteraction[getIndex(i, j)] = 0; // Jaloux
                else
                    grapheInteraction[getIndex(i, j)] = 1; // Non jaloux
            }
        }
    }

    /**
     * Compte le nombre de conflits (jalousies) dans la colonie en fonction des ressources allouées.
     *
     * @return Le nombre de conflits (jalousies) dans la colonie
     */
    public int countConflict() {
        int cptConflit = 0; // Compteur de conflits

        // Parcours les lignes du graphe
        for (int i = 0; i < population.length - 1; i++) {
            int currentJalouse = 0; // Variable pour vérifier si le colon i est jaloux

            // Parcours les colonnes pour vérifier la relation entre colon i et les autres colons
            for (int j = i + 1; j < population.length; j++) {
                // Si les colons ont des ressources différentes, ils peuvent être en conflit (jalousie)
                if (population[i].getRankAllocRsc() != population[j].getRankAllocRsc())
                    currentJalouse = grapheInteraction[getIndex(i, j)]; // Récupère l'état de jalousie
            }
            cptConflit += currentJalouse; // Si un conflit est trouvé, on l'ajoute au compteur
        }
        return cptConflit; // Retourne le nombre total de conflits
    }

    /**
     * Échange les ressources de deux colons spécifiés.
     * Modifie les ressources allouées à chaque colon dans leurs préférences respectives.
     *
     * @param c1 Index du premier colon
     * @param c2 Index du second colon
     */
    public void swapRsc(int c1, int c2) {
        // Récupère les préférences des deux colons
        NameRessource[] tabRscC1 = population[c1].getPreference();
        NameRessource[] tabRscC2 = population[c2].getPreference();

        // Récupère les ressources allouées actuellement aux deux colons
        NameRessource rsc1 = tabRscC1[population[c1].getRankAllocRsc()];
        NameRessource rsc2 = tabRscC2[population[c2].getRankAllocRsc()];

        // Parcourt les préférences des colons pour mettre à jour leurs ressources
        for (int i = 0; i < tabRscC1.length; i++) {
            if (tabRscC1[i] == rsc2)
                population[c1].setRankAllocRsc(i); // Mise à jour de la ressource pour le colon 1

            if (tabRscC2[i] == rsc1)
                population[c2].setRankAllocRsc(i); // Mise à jour de la ressource pour le colon 2
        }
    }

    /**
     * Modifie la relation entre deux colons en fonction du mode (jaloux ou non).
     * Si mode = 1, les colons deviennent jaloux, sinon ils ne le sont pas.
     *
     * @param mode Le mode de jalousie (1 pour jaloux, 0 pour non jaloux)
     * @param c1 Index du premier colon
     * @param c2 Index du second colon
     */
    public void changeRelation(int mode, int c1, int c2) {
        // Change l'état de la relation entre deux colons (jaloux ou non)
        grapheInteraction[getIndex(c1, c2)] = mode;
    }

    /**
     * Calcule l'indice du graphe d'interaction entre deux colons à partir de leurs indices.
     * Utilise une formule pour linéariser la matrice triangulaire.
     *
     * @param a L'indice du premier colon
     * @param b L'indice du second colon
     * @return L'indice linéaire dans le tableau d'interactions
     */
    public int getIndex(int a, int b) {
        // S'assure que a < b pour éviter les doublons
        if (a > b) {
            // Échange les valeurs de a et b
            a = a + b;
            b = a - b;
            a = a - b;
        }

        // Calcule l'indice linéaire dans le graphe
        return (a * (population.length - 1)) - (a * (a + 1)) / 2 + (b - 1);
    }

    /**
     * Initialise les préférences de chaque colon avec des valeurs aléatoires.
     * Appelle la méthode `setRandomPreference` pour chaque colon.
     */
    public void initPreference() {
        // Parcourt chaque colon pour lui attribuer des préférences aléatoires
        for (int i = 0; i < population.length; i++)
            population[i].setRandomPreference();
    }

    /**
     * Retourne le graphe des interactions sociales entre les colons.
     *
     * @return Un tableau représentant le graphe d'interactions
     */
    public int[] getGrapheInteraction() {
        return grapheInteraction; // Retourne le graphe des interactions
    }

    /**
     * Modifie le graphe des interactions sociales.
     *
     * @param graphe Un tableau représentant le nouveau graphe d'interactions
     */
    public void setGrapheInteraction(int[] graphe) {
        grapheInteraction = graphe; // Met à jour le graphe d'interactions
    }

    /**
     * Retourne la population de la colonie.
     *
     * @return Un tableau des colons dans la colonie
     */
    public Colon[] getPopulation() {
        return population; // Retourne le tableau de population
    }

    /**
     * Modifie la population de la colonie.
     *
     * @param pop Un tableau de colons à affecter à la colonie
     */
    public void setPopulation(Colon[] pop) {
        population = pop; // Affecte un nouveau tableau de colons à la colonie
    }
}