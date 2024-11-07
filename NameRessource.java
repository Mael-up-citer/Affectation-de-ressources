public enum NameRessource {
    EAU(0),       // Représente la ressource "EAU" avec une valeur associée de 0
    PAIN(1),      // Représente la ressource "PAIN" avec une valeur associée de 1
    CONSERVE(2),  // Représente la ressource "CONSERVE" avec une valeur associée de 2
    JOUET(3);     // Représente la ressource "JOUET" avec une valeur associée de 3

    private final int valeur;  // Variable pour stocker la valeur associée à chaque ressource

    /**
     * Constructeur de l'enum.
     * Associe une valeur à chaque élément de l'énumération.
     *
     * @param valeur La valeur associée à la ressource
     */
    NameRessource(int valeur) {
        this.valeur = valeur; // Initialise la valeur de la ressource
    }

    /**
     * Retourne la valeur associée à la ressource.
     *
     * @return La valeur de la ressource
     */
    public int getValeur() {
        return this.valeur;  // Retourne la valeur stockée pour cette ressource
    }

    // Tableau statique pour accéder à toutes les ressources de l'énumération rapidement
    private static final NameRessource[] valeurs = NameRessource.values();

    /**
     * Méthode pour convertir une valeur entière en une ressource correspondante.
     * Si l'entier passé ne correspond à aucune ressource valide, une exception est levée.
     *
     * @param valeur La valeur à convertir en ressource
     * @return La ressource correspondant à la valeur passée
     * @throws IllegalArgumentException Si la valeur est invalide (en dehors des valeurs possibles)
     */
    public static NameRessource fromValeur(int valeur) {
        // Vérifie si la valeur est valide, c'est-à-dire dans la plage des valeurs de l'énumération
        if (valeur < 0 || valeur >= valeurs.length)
            throw new IllegalArgumentException("Valeur invalide : " + valeur);  // Lève une exception si la valeur est hors plage

        // Retourne la ressource correspondant à la valeur
        return valeurs[valeur]; 
    }
}