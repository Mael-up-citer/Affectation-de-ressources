public enum NameRessource{
    EAU(0),
    PAIN(1),
    CONSERVE(2),
    JOUET(3);

    private final int valeur; // Variable pour stocker la valeur associee

    // Constructeur de l'enum
    NameRessource(int valeur){
        this.valeur = valeur;
    }

    // Getter de la valeur associee
    public int getValeur(){
        return this.valeur;
    }

    // Tableau statique pour un acces direct
    private static final NameRessource[] valeurs = NameRessource.values();

    // Methode pour convertir un entier en NameRessource grace a valeurs
    public static NameRessource fromValeur(int valeur){
        // Si l'int est invalide
        if (valeur < 0 || valeur >= valeurs.length)
            throw new IllegalArgumentException("Valeur invalide : " + valeur);

        return valeurs[valeur];  // Retourne la ressource correspondante
    }
}