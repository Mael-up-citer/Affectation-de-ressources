import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente une colonie de colons avec des relations sociales entre eux.
 * Cette classe gère la population de la colonie, les interactions sociales entre les colons,
 * les ressources allouées et les conflits (jalousies) qui peuvent survenir en fonction des ressources.
 * Elle permet aussi de générer et de modifier les relations sociales entre les colons, d'ajouter des colons,
 * et de gérer les échanges de ressources entre eux.
 */
public class Colonie {
    private static int numColon = 0;  // Compteur statique pour gérer l'index des colons

    // Graphe des interactions sociales entre les colons (1 = jaloux, 0 = non jaloux)
    private int[] grapheInteraction;

    // Liste contenant la population de la colonie
    private ArrayList<Colon> population;


    /**
     * Constructeur de la classe Colonie.
     * Initialise la population et le graphe d'interaction en fonction du nombre de colons et de la diversité des ressources.
     *
     * @param nbColon Nombre de colons dans la colonie
     * @param colo Liste de colons à initialiser dans la population
     * @throws IllegalArgumentException Si le nombre de colons est inférieur ou égal à 0, ou si la liste de colons est null
     */
    public Colonie(ArrayList<Colon> colo) {
        // Vérification que la liste des colons n'est pas null
        if (colo == null)
            throw new IllegalArgumentException("La liste des colons ne peut pas être null.");

        // Alloue un tableau pour le graphe d'interactions sociales. Le graphe est une matrice triangulaire.
        grapheInteraction = new int[((colo.size() * (colo.size() - 1)) / 2)];  // Calcul de la taille du graphe
        population = colo;  // Affecte la liste de colons passée en paramètre à la population
    }

    /**
     * Ajoute un colon à la colonie.
     * Assure que le colon n'est pas null avant d'ajouter à la population.
     *
     * @param c Le colon à ajouter à la colonie
     * @throws IllegalArgumentException Si le colon est null
     */
    public void addColon(Colon c) {
        // Vérification que le colon n'est pas null
        if (c == null)
            throw new IllegalArgumentException("Erreur le colon est null");

        // Ajoute le colon à la liste population
        population.add(c);  // Ajout du colon à la liste
    }

    /**
     * Génère aléatoirement les relations sociales entre les colons (jalousie ou non).
     * Cette méthode affecte un état de jalousie entre chaque paire de colons avec une probabilité de 50%.
     */
    public void generateRandomColo() {
        // Parcourt les lignes du graphe
        for (int i = 0; i < population.size() - 1; i++) {
            // Parcourt les colonnes du graphe
            for (int j = i + 1; j < population.size(); j++) {
                // 50% de chance que deux colons soient jaloux
                if (Math.random() > 0.5)
                    grapheInteraction[getIndex(i, j)] = 0; // Jaloux
                else
                    grapheInteraction[getIndex(i, j)] = 1; // Non jaloux
            }
        }
    }

    /**
     * Compte et retourne l'ensemble des colons jaloux dans la colonie.
     * Un colon est considéré jaloux s'il a un voisin avec une ressource de rang supérieur à la sienne.
     * Un conflit (jalousie) est détecté si deux colons voisins ont des ressources allouées de rang différent.
     * 
     * @return Un Set contenant les colons jaloux de la colonie. Chaque colon apparaît au plus une fois dans le Set.
     */
    public Set<Colon> countConflict() {
        Set<Colon> jalou = new HashSet<>(); // Set des colons jaloux

        // Parcours de chaque colon pour vérifier les conflits avec ses voisins
        for (int i = 0; i < population.size() - 1; i++) {
            // Vérifie chaque colon avec tous ses voisins
            for (int j = i + 1; j < population.size(); j++) {
                // Si les colons ont des ressources allouées différentes et sont voisins dans le graphe
                if ((population.get(i).getRankAllocRsc() != population.get(j).getRankAllocRsc()) && (grapheInteraction[getIndex(i, j)] == 1)) {
                    // Ajoute les colons dans le Set des colons jaloux
                    jalou.add(population.get(i));
                    jalou.add(population.get(j));
                }
            }
        }
        
        // Retourne le Set des colons jaloux
        return jalou;
    }

    /**
     * Échange les ressources de deux colons spécifiés.
     * Modifie les ressources allouées à chaque colon dans leurs préférences respectives.
     *
     * @param c1 Le premier colon
     * @param c2 Le second colon
     * @throws IllegalArgumentException Si un des colons est null ou si les colons ont des préférences vides
     */
    public void swapRsc(Colon c1, Colon c2) {
        // Vérification que les colons ne sont pas null
        if (c1 == null || c2 == null)
            throw new IllegalArgumentException("Les deux colons doivent être non null.");

        // Vérification que les colons ont des préférences valides
        Ressource[] tabRscC1 = c1.getPreference();
        Ressource[] tabRscC2 = c2.getPreference();

        if (tabRscC1 == null || tabRscC2 == null)
            throw new IllegalArgumentException("Les préférences des colons doivent être initialisé");

        Ressource rsc1 = tabRscC1[c1.getRankAllocRsc()];
        Ressource rsc2 = tabRscC2[c2.getRankAllocRsc()];

        // Parcourt les préférences des colons pour mettre à jour leurs ressources
        for (int i = 0; i < tabRscC1.length; i++) {
            // Si la ressource du colon 1 correspond à celle du colon 2, met à jour la ressource
            if (tabRscC1[i] == rsc2)
                c1.allocateResource(i);  // Mise à jour de la ressource pour le colon 1

            // Si la ressource du colon 2 correspond à celle du colon 1, met à jour la ressource
            if (tabRscC2[i] == rsc1)
                c2.allocateResource(i);  // Mise à jour de la ressource pour le colon 2
        }
    }

    /**
     * Calcule l'indice du graphe d'interaction entre deux colons à partir de leurs indices.
     * Utilise une formule pour linéariser la matrice triangulaire.
     *
     * @param a L'indice du premier colon
     * @param b L'indice du second colon
     * @return L'indice linéaire dans le tableau d'interactions
     * @throws IllegalArgumentException Si les indices sont hors limites
     */
    public int getIndex(int a, int b) {
        // Vérification que les indices sont dans les limites valides
        if (a < 0 || b < 0 || a >= population.size() || b >= population.size())
            throw new IllegalArgumentException("Les indices sont hors limites.");

        // S'assure que a < b pour éviter les doublons
        if (a > b) {
            // Échange les valeurs de a et b
            a = a + b;
            b = a - b;
            a = a - b;
        }

        // Calcule l'indice linéaire dans le graphe
        return (a * (population.size() - 1)) - (a * (a + 1)) / 2 + (b - 1);
    }

    /**
     * Retourne la population de la colonie.
     *
     * @return Un tableau représentant la population de la colonie
     */
    public ArrayList<Colon> getPopulation() {
        return population;  // Retourne la liste des colons
    }

    /**
     * Modifie la population de la colonie.
     *
     * @param pop Un tableau de colons à affecter à la colonie
     * @throws IllegalArgumentException Si la nouvelle population est null
     */
    public void setPopulation(ArrayList<Colon> pop) {
        if (pop == null)
            throw new IllegalArgumentException("La nouvelle population ne peut pas être null.");

        population = pop;  // Met à jour la population de la colonie
    }
}