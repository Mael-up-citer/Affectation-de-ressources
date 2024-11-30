import java.util.HashMap;

/**
 * La classe Colon représente un colon avec ses préférences de ressources,
 * ainsi qu'un identifiant unique et un nom. Elle permet de gérer l'allocation
 * de ressources selon un rang de préférence et d'assurer l'unicité du nom.
 */
public class Colon {
    // Map statique pour associer un nom de colon à son identifiant unique.
    private static HashMap<String, Integer> nameToId = new HashMap<>();

    // Compteur pour attribuer un identifiant unique à chaque colon.
    private static int numColon = -1;

    // Tableau qui contient les préférences du colon pour les ressources, ordonné par importance.
    private Ressource[] preference;

    // Rang de la ressource actuellement allouée au colon. -1 signifie qu'aucune ressource n'est allouée.
    private int rankAllocRsc = -1;

    // Nom du colon, chaque colon a un nom unique.
    private String name;


    /**
     * Constructeur de la classe Colon.
     * Initialise un colon avec un nom unique et un tableau de préférences de ressources.
     * Si un colon avec ce nom existe déjà, un suffixe est ajouté au nom pour garantir son unicité.
     *
     * @param nbRessource Le nombre de ressources disponibles, donc la taille du tableau de préférences.
     * @param name        Le nom du colon. Un suffixe sera ajouté si ce nom existe déjà.
     * @throws IllegalArgumentException Si le nom est null ou vide.
     */
    public Colon(int nbRessource, String name) {
        // Vérification si le nom est valide (non null et non vide)
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Le nom d'un colon ne peut pas être null ou vide.");

        // Création du nom unique pour le colon en ajoutant un suffixe si le nom existe déjà
        String baseName = name; // Stocke le nom de base pour ajouter le suffixe
        int suffix = 0;
        // Boucle pour vérifier si le nom existe déjà dans la map, et ajoute un suffixe si nécessaire
        while (nameToId.containsKey(name)) {
            suffix++;
            name = baseName + suffix; // Ajoute un suffixe au nom pour le rendre unique
        }

        // Enregistrement du nom unique dans la map et attribution d'un ID unique
        this.name = name;
        nameToId.put(name, numColon++);
        
        // Initialisation du tableau de préférences en fonction du nombre de ressources
        this.preference = new Ressource[nbRessource];
    }

    /**
     * Retourne la map qui associe les noms du colons à leurs IDs uniques.
     *
     * @return La map contenant les noms de colons et leurs IDs
     */
    public static HashMap<String, Integer> getNameToId() {
        return nameToId;
    }

    /**
     * Retourne les préférences de ressources du colon sous forme d'un tableau.
     *
     * @return Un tableau de Ressource représentant les préférences du colon.
     */
    public Ressource[] getPreference() {
        return preference; // Retourne le tableau des préférences
    }

    /**
     * Définit les préférences de ressources du colon en utilisant un tableau donné.
     * Vérifie que le tableau est valide (non null et rempli).
     *
     * @param preference Le tableau de Ressource à utiliser comme préférences.
     * @throws IllegalArgumentException Si le tableau de préférences est null ou de taille incorrecte.
     */
    public void setPreference(Ressource[] preference) {
        // Vérification que le tableau de préférences n'est ni null ni de taille incorrecte.
        if (preference == null || preference.length != this.preference.length)
            throw new IllegalArgumentException("Le tableau de préférences doit être non null et de taille " + this.preference.length);

        this.preference = preference; // Stocke le tableau de préférences.
    }

    /**
     * Retourne le rang de la ressource actuellement allouée au colon.
     * Si aucune ressource n'est allouée, le rang est -1.
     *
     * @return Le rang de la ressource allouée, ou -1 si aucune ressource n'est allouée.
     */
    public int getRankAllocRsc() {
        return rankAllocRsc;
    }

    /**
     * Attribue une ressource au colon en définissant son rang d'allocation.
     * Le rang doit être compris entre 0 et la taille du tableau de préférences - 1.
     *
     * @param rankAllocRsc Le rang de la ressource à allouer.
     * @throws IllegalArgumentException Si le rang est hors des bornes valides.
     */
    public void allocateResource(int rankAllocRsc) {
        // Vérification que le rang est dans les limites valides du tableau de préférences.
        if (rankAllocRsc < 0 || rankAllocRsc >= preference.length)
            throw new IllegalArgumentException("Erreur : le rang doit être entre 0 et " + (preference.length - 1));

        this.rankAllocRsc = rankAllocRsc; // Définit la ressource allouée en fonction du rang.
    }

    /**
     * Réinitialise l'allocation de ressource en définissant le rang alloué à -1.
     * Cela signifie qu'aucune ressource n'est allouée au colon.
     */
    public void clearResourceAllocation() {
        this.rankAllocRsc = -1;
    }

    /**
     * Retourne la ressource actuellement allouée au colon, basée sur le rang d'allocation.
     * Si aucune ressource n'est allouée, la méthode retourne null.
     *
     * @return La ressource allouée, ou null si aucune ressource n'est allouée.
     */
    public Ressource getAllocatedResource() {
        if (rankAllocRsc == -1)
            return null; // Aucun allocation de ressource.

        return preference[rankAllocRsc]; // Retourne la ressource correspondant au rang alloué.
    }

    /**
     * Retourne une représentation textuelle du colon
     * La méthode permet d'afficher facilement l'état du colon sous forme de chaîne de caractères.
     *
     * @return Une chaîne de caractères représentant l'état du colon.
     */
    @Override
    public String toString() {
        // Vérifie si le colon a une ressource allouée
        Ressource allocatedResource = getAllocatedResource();
        // Si la ressource est allouée, affiche son nom, sinon affiche "Aucune"
        String resourceString = (allocatedResource != null) ? allocatedResource.toString() : "Aucune";

        // Retourne une chaîne formatée avec le nom du colon, son rang d'allocation et la ressource associée
        return "Colon{name='" + name + "', rankAllocRsc=" + (rankAllocRsc == -1 ? "Aucune" : rankAllocRsc) +
               ", allocatedResource=" + resourceString + "}";
    }
    

    /**
     * Retourne une chaîne de caractères listant toutes les préférences du colon avec leur rang.
     * Cette méthode génère une représentation textuelle des préférences du colon.
     *
     * @return Une chaîne de caractères listant les préférences du colon.
     */
    public String listPreferences() {
        StringBuilder sb = new StringBuilder("Préférences du colon " + name + " : ");

        // Boucle à travers le tableau de préférences pour l'afficher.
        for (int i = 0; i < preference.length; i++)
            sb.append("\nRang ").append(i).append(": ").append(preference[i]);

        return sb.toString(); // Retourne la chaîne complète des préférences.
    }
}