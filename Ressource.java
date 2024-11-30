import java.util.HashMap;

/**
 * Classe représentant une ressource dans le système.
 * Chaque ressource possède un nom unique, un ID unique et une quantité associée.
 */
public class Ressource {
    // Map statique pour associer le nom d'une ressource à son ID unique
    private static HashMap<String, Integer> nameToId = new HashMap<>();
    
    // Compteur pour générer des IDs uniques pour chaque instance de ressource
    private static int numInstance = -1;

    // ID unique de la ressource (chaque instance aura un ID différent)
    private int id;
    
    // Nom de la ressource
    private String nom;
    
    // Quantité de la ressource disponible
    private int quantite;


    /**
     * Constructeur de la classe Ressource.
     * Permet d'initialiser une ressource avec un nom et une quantité par défaut de 1.
     * 
     * @param nom Le nom de la ressource (doit être unique)
     * @throws IllegalArgumentException Si le nom est déjà attribué à une autre ressource
     *                                  ou si le nom est vide ou null
     */
    public Ressource(String nom) {
        // Vérification que le nom de la ressource est valide
        if (nom == null || nom.isEmpty())
            throw new IllegalArgumentException("Le nom de la ressource ne peut pas être null ou vide.");

        // Vérification si une ressource avec le même nom existe déjà
        if (nameToId.containsKey(nom))
            throw new IllegalArgumentException("La ressource avec le nom '" + nom + "' a déjà été créé.");

        // Initialisation de l'ID unique et des autres propriétés de la ressource
        this.id = numInstance++;
        this.nom = nom;
        this.quantite = 1;  // Par défaut, la quantité est fixée à 1

        // Enregistrement du nom dans la map avec l'ID unique
        nameToId.put(nom, this.id);
    }

    /**
     * Retourne une représentation textuelle de la ressource sous forme de chaîne de caractères.
     * Affiche l'ID, le nom et la quantité de la ressource.
     *
     * @return Une chaîne de caractères représentant la ressource
     */
    @Override
    public String toString() {
        return "Ressource{id=" + id + ", nom='" + nom + "', quantite=" + quantite + "}";
    }

    /**
     * Retourne la map qui associe les noms de ressources à leurs IDs uniques.
     *
     * @return La map contenant les noms de ressources et leurs IDs respectifs
     */
    public static HashMap<String, Integer> getNameToId() {
        return nameToId;
    }

    /**
     * Retourne l'ID unique de la ressource.
     *
     * @return L'ID de la ressource
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom de la ressource.
     *
     * @return Le nom de la ressource
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la quantité actuelle de la ressource.
     *
     * @return La quantité de la ressource
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Modifie le nom de la ressource.
     * Vérifie que le nom est valide (non nul et non vide) et met à jour la map des noms.
     *
     * @param newNom Le nouveau nom à attribuer à la ressource
     * @throws IllegalArgumentException Si le nom est nul ou vide
     */
    public void setNom(String newNom) {
        // Vérifie que le nouveau nom est valide
        if (newNom == null || newNom.isEmpty())
            throw new IllegalArgumentException("Le nom de la ressource ne peut pas être null ou vide.");

        // Supprimer l'ancienne entrée de la map
        nameToId.remove(this.nom);

        // Met à jour le nom de la ressource
        this.nom = newNom;

        // Ajouter la nouvelle entrée dans la map avec l'ID de la ressource
        nameToId.put(nom, this.id);
    }

    /**
     * Réinitialise la quantité de la ressource à 1.
     * Cela signifie que la ressource est désormais disponible pour une nouvelle utilisation,
     * en supposant que la quantité de la ressource est limitée à 1.
     */
    public void FreeRessource() {
        quantite = 1;  // Réinitialise la quantité de la ressource à 1
    }

    /**
     * Marque la ressource comme utilisée en réduisant sa quantité à 0.
     * Cela signifie que la ressource n'est plus disponible jusqu'à ce qu'elle soit réinitialisée.
     */
    public void UseRessource() {
        quantite = 0;  // Réduit la quantité de la ressource à 0, indiquant qu'elle est utilisée
    }
}