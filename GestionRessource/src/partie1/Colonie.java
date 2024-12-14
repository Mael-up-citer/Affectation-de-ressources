package partie1;

import java.util.*;

/**
 * Représente une colonie avec des colons, des ressources et des relations.
 */
public class Colonie {

    /**
     * La map des colons associés à leur nom.
     */
    private Map<String, Colon> colons;

    /**
     * Les relations "ne s'aiment pas" entre les colons.
     */
    private Set<Set<String>> relations;

    /**
     * Les affectations des ressources aux colons.
     */
    private Map<String, String> affectations;

    /**
     * Les ressources disponibles dans la colonie.
     */
    private Set<String> ressources;

    /**
     * Constructeur par défaut de la classe Colonie.
     */
    public Colonie() {
        this.colons = new HashMap<>();
        this.relations = new HashSet<>();
        this.affectations = new HashMap<>();
        this.ressources = new HashSet<>();
    }

    /**
     * Ajoute un colon à la colonie.
     *
     * @param nom le nom du colon.
     * @throws IllegalArgumentException si le nom est null, vide, ou déjà présent.
     */
    public void ajouterColon(String nom) {
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom du colon ne peut pas être vide.");
        }
        if (!colons.containsKey(nom)) {
            colons.put(nom, new Colon(nom));
        } else {
            throw new IllegalArgumentException("Le colon " + nom + " existe déjà.");
        }
    }

    /**
     * Ajoute une ressource à la colonie.
     *
     * @param ressource le nom de la ressource.
     * @throws IllegalArgumentException si le nom est null, vide, ou déjà présent.
     */
    public void ajouterRessource(String ressource) {
        if (ressource == null || ressource.isEmpty()) {
            throw new IllegalArgumentException("Le nom de la ressource ne peut pas être vide.");
        }
        if (ressources.contains(ressource)) {
            throw new IllegalArgumentException("La ressource " + ressource + " existe déjà.");
        }
        ressources.add(ressource);
    }

    /**
     * Ajoute une relation "ne s'aiment pas" entre deux colons.
     *
     * @param nom1 le nom du premier colon.
     * @param nom2 le nom du deuxième colon.
     * @throws IllegalArgumentException si les noms sont invalides ou identiques.
     */
    public void ajouterRelation(String nom1, String nom2) {
        if (!colons.containsKey(nom1) || !colons.containsKey(nom2)) {
            System.out.println("Colons existants :");
            afficherColons();
            throw new IllegalArgumentException("Les colons spécifiés n'existent pas.");
        }
        if (nom1.equals(nom2)) {
            throw new IllegalArgumentException("Un colon ne peut pas être en relation avec lui-même.");
        }
        Set<String> relation = new HashSet<>(Arrays.asList(nom1, nom2));
        relations.add(relation);
    }

    /**
     * Ajoute les préférences d'un colon pour les ressources.
     *
     * @param nom le nom du colon.
     * @param preferences une liste de préférences.
     * @throws IllegalArgumentException si les préférences sont invalides ou incomplètes.
     */
    public void ajouterPreferences(String nom, List<String> preferences) {
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom du colon ne peut pas être vide.");
        }
        if (!colons.containsKey(nom)) {
            throw new IllegalArgumentException("Le colon " + nom + " n'existe pas.");
        }
        if (preferences == null || preferences.size() != ressources.size()) {
            System.out.println("Nous avons " + ressources.size() + " ressources.");
            throw new IllegalArgumentException("Les préférences doivent être données pour chaque ressource.");
        }
        Set<String> preferencesSet = new HashSet<>(preferences);
        if (preferencesSet.size() != preferences.size()) {
            throw new IllegalArgumentException("Les préférences doivent être uniques.");
        }
        colons.get(nom).setPreferences(preferences);
    }

    /**
     * Génère une solution naïve d'affectation des ressources aux colons.
     *
     * @return une map des affectations des colons aux ressources.
     */
    public Map<String, String> genererSolutionNaive() {
        affectations.clear();
        Set<String> ressourcesAttribuees = new HashSet<>();

        for (Colon colon : colons.values()) {
            for (String ressource : colon.getPreferences()) {
                if (!ressourcesAttribuees.contains(ressource)) {
                    affectations.put(colon.getNom(), ressource);
                    ressourcesAttribuees.add(ressource);
                    break;
                }
            }
        }
        return affectations;
    }

    /**
     * Calcule le coût de l'affectation actuelle.
     *
     * @return le nombre de colons jaloux.
     */
    public int calculerCout() {
        Set<String> colonsJaloux = new HashSet<>();

        for (Set<String> relation : relations) {
            Iterator<String> it = relation.iterator();
            String colon1 = it.next();
            String colon2 = it.next();

            if (affectations.containsKey(colon1) && affectations.containsKey(colon2)) {
                Colon c1 = colons.get(colon1);
                Colon c2 = colons.get(colon2);

                int indcolon1 = c1.getPreferences().indexOf(affectations.get(colon1));
                int indcolon2 = c2.getPreferences().indexOf(affectations.get(colon2));

                if (indcolon2 < indcolon1) {
                    colonsJaloux.add(colon1);
                }

                if (indcolon2 > indcolon1) {
                    colonsJaloux.add(colon2);
                }
            }
        }

        return colonsJaloux.size();
    }

    /**
     * Affiche les affectations actuelles des colons.
     */
    public void afficherAffectations() {
        for (Map.Entry<String, String> entry : affectations.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    /**
     * Affiche les noms des colons existants.
     */
    public void afficherColons() {
        for (Map.Entry<String, Colon> entry : colons.entrySet()) {
            System.out.print(entry.getKey() + "\t");
        }
        System.out.println();
    }

    /**
     * Affiche les ressources disponibles.
     */
    public void afficherRessources() {
        for (String ressource : ressources) {
            System.out.print(ressource + "\t");
        }
        System.out.println();
    }

    /**
     * Vérifie si un colon existe dans la colonie.
     *
     * @param nom le nom du colon.
     * @return true si le colon existe, false sinon.
     */
    public boolean colonExiste(String nom) {
        return colons.containsKey(nom);
    }

    /**
     * Obtient la map des colons.
     *
     * @return la map des colons.
     */
    public Map<String, Colon> getColons() {
        return colons;
    }

    /**
     * Obtient les relations "ne s'aiment pas".
     *
     * @return un ensemble des relations.
     */
    public Set<Set<String>> getRelations() {
        return relations;
    }

    /**
     * Obtient les ressources disponibles.
     *
     * @return un ensemble des ressources.
     */
    public Set<String> getRessources() {
        return ressources;
    }

    /**
     * Obtient les affectations actuelles des ressources.
     *
     * @return une map des affectations.
     */
    public Map<String, String> getAffectations() {
        return affectations;
    }
}
