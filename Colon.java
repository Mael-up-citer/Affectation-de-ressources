import java.util.Random;

public class Colon {
    // Contient les préférences de ressources du colon, ordonnées par importance (préférence[0] est la préférée)
    private NameRessource[] preference;

    // Rang de la ressource actuellement allouée au colon, variant entre 0 et diversiterRsc - 1
    private int rankAllocRsc;

    /**
     * Constructeur de la classe Colon.
     * Initialise le tableau de préférences en fonction de la diversité de ressources disponible.
     *
     * @param diversiterRsc Nombre de types de ressources différents (taille du tableau de préférences)
     */
    public Colon(int diversiterRsc) {
        preference = new NameRessource[diversiterRsc];  // Alloue l'espace pour stocker les préférences de ressources
    }

    /**
     * Définit les préférences du colon de manière aléatoire sans doublons.
     * Utilise un tableau de suivi pour éviter de réattribuer la même ressource plusieurs fois.
     */
    public void setRandomPreference() {
        Random ran = new Random();  // Crée un objet Random pour générer des indices aléatoires
        int[] everAffect = new int[preference.length];   // Tableau de suivi des valeurs déjà attribuées

        // Parcourt le tableau de préférences pour remplir chaque position
        for (int i = 0; i < preference.length; i++) {
            int value = ran.nextInt(preference.length);  // Génère un nombre aléatoire entre 0 et la longueur du tableau

            // Si la ressource est déjà attribuée, génère un autre nombre jusqu'à en trouver un non attribué
            while (everAffect[value] != 0)
                value = ran.nextInt(preference.length);  // Génère une nouvelle tentative pour éviter les doublons

            preference[i] = NameRessource.fromValeur(value);  // Assigne la ressource au tableau de préférences
            everAffect[value] = 1;  // Met à jour le tableau de suivi pour indiquer que cette ressource est utilisée
        }
    }

    /**
     * Retourne les préférences de ressources du colon.
     *
     * @return Un tableau de NameRessource représentant les préférences du colon
     */
    public NameRessource[] getPreference() {
        return preference;  // Retourne le tableau de préférences du colon
    }

    /**
     * Définit les préférences du colon en utilisant un tableau donné.
     *
     * @param preference Le tableau de NameRessource à utiliser comme préférences du colon
     */
    public void setPreference(NameRessource[] preference) {
        this.preference = preference;   // Définit le tableau de préférences du colon
    }

    /**
     * Retourne le rang de la ressource actuellement allouée au colon.
     *
     * @return Un entier représentant le rang de la ressource allouée
     */
    public int getRankAllocRsc() {
        return rankAllocRsc;  // Retourne le rang de la ressource allouée au colon
    }

    /**
     * Définit le rang de la ressource allouée au colon.
     *
     * @param rankAllocRsc Un entier représentant le rang de la ressource dans les préférences
     */
    public void setRankAllocRsc(int rankAllocRsc) {
        this.rankAllocRsc = rankAllocRsc;   // Définit le rang de la ressource allouée au colon
    }
}