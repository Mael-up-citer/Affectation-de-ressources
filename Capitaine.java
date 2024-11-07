public class Capitaine {
    // Ressources à répartir; chaque index représente une ressource selon l'ordre de l'enum NameRessource
    private int[] ressources;

    // Colonie associée au capitaine
    private Colonie colo;

    /**
     * Constructeur de la classe Capitaine.
     * Initialise les ressources et assigne la colonie.
     * 
     * @param divRsc Nombre de types différents de ressources
     * @param c Colonie qui sera gérée par le capitaine
     */
    public Capitaine(int divRsc, Colonie c) {
        ressources = new int[divRsc]; // Initialise le tableau de ressources
        colo = c;  // Associe cette colonie au capitaine
    }

    /**
     * Remplit aléatoirement les ressources disponibles après une mission.
     * La dernière ressource reçoit le reste pour assurer un total fixe.
     */
    public void retourDeMission() {
        int qtt;  // Quantité actuelle pour chaque ressource
        int nbRsc = colo.getPopulation().length;  // Nombre total de ressources à répartir

        // Remplit chaque ressource sauf la dernière
        for (int i = 0; i < ressources.length - 1; i++) {
            qtt = (int)(Math.random() * nbRsc);  // Génère une quantité aléatoire pour la ressource actuelle
            nbRsc -= qtt;  // Réduit le nombre total de ressources restantes
            ressources[i] = qtt;  // Assigne la quantité générée
        }
        ressources[ressources.length - 1] = nbRsc;  // Affecte le reste à la dernière ressource
    }

    /**
     * Répartit les ressources entre les colons de manière naïve.
     * Chaque colon reçoit la première ressource dans sa liste de préférences encore disponible.
     */
    public void repartitionNaive() {
        Colon[] pop = colo.getPopulation();  // Récupère la population de colons
        int cpt;  // Index de la ressource préférée

        // Parcourt chaque colon pour affecter les ressources
        for (int i = 0; i < pop.length; i++) {
            cpt = 0;  // Commence à la première préférence

            // Trouve la première ressource dans les préférences encore disponible
            while (ressources[pop[i].getPreference()[cpt].getValeur()] == 0)
                cpt++;  // Passe à la préférence suivante

            pop[i].setRankAllocRsc(cpt);  // Enregistre l'index de la ressource allouée
            ressources[pop[i].getPreference()[cpt].getValeur()]--;  // Diminue la quantité de cette ressource
        }
    }

    /**
     * Répartit les ressources de façon intelligente.
     * Utilise une première répartition naïve, puis analyse les conflits de jalousie.
     */
    public void repartitionIntelligente() {
        repartitionNaive();  // Réalise une répartition initiale
        int nbConflits = colo.countConflict();  // Compte les conflits de jalousie après répartition
        // La logique supplémentaire pour optimiser la répartition pourrait être ajoutée ici
    }

    /**
     * Affiche la quantité disponible de chaque ressource.
     */
    public void displayRsc() {
        for (int i = 0; i < ressources.length; i++)
            System.out.println("Nous avons " + ressources[i] + " " + NameRessource.values()[i]);
    }

    /**
     * Affiche l'état actuel des ressources allouées pour chaque colon.
     */
    public void dispSystemSate() {
        Colon[] pop = colo.getPopulation();

        System.out.println("État du système:");
        // Affiche les ressources affectées pour chaque colon
        for (int i = 0; i < pop.length; i++)
            System.out.println(i + ":" + pop[i].getPreference()[pop[i].getRankAllocRsc()]);
    }

    /**
     * Affiche les types de ressources disponibles sans les quantités.
     */
    public void affRscDispo() {
        NameRessource[] rsc = NameRessource.values();

        System.out.println("Ressources à répartir:");
        // Affiche chaque type de ressource
        for (int i = 0; i < rsc.length; i++)
            System.out.println(rsc[i]);
    }

    /**
     * Accesseur pour les ressources.
     * @return Le tableau des quantités de chaque ressource
     */
    public int[] getRessources() {
        return ressources;
    }

    /**
     * Mutateur pour les ressources.
     * @param rsc Nouveau tableau des quantités de ressources
     */
    public void setRessources(int[] rsc) {
        ressources = rsc;
    }

    /**
     * Accesseur pour la colonie associée au capitaine.
     * @return La colonie gérée par le capitaine
     */
    public Colonie getColonie() {
        return colo;
    }

    /**
     * Mutateur pour la colonie associée.
     * @param c Nouvelle colonie à associer au capitaine
     */
    public void setColonie(Colonie c) {
        colo = c;
    }
}