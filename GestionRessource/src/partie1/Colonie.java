package partie1;

import java.util.*;

public class Colonie {
	private Map<String, Colon> colons;
	// Relations de "ne s’aiment pas"
	private Set<Set<String>> relations;
	private Map<String, Integer> affectations;

	public Colonie() {
		this.colons = new HashMap<>();
		this.relations = new HashSet<>();
		this.affectations = new HashMap<>();
	}

	// Ajouter un colon, vérifier si le nom est valide
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

	// Ajouter une relation "ne s'aiment pas" entre deux colons
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

	// Ajouter les préférences d’un colon, vérifier que les préférences sont
	// complètes
	public void ajouterPreferences(String nom, List<Integer> preferences) {
		if (nom == null || nom.isEmpty()) {
			throw new IllegalArgumentException("Le nom du colon ne peut pas être vide.");
		}
		if (!colons.containsKey(nom)) {
			throw new IllegalArgumentException("Le colon " + nom + " n'existe pas.");
		}
		if (preferences == null || preferences.size() != colons.size()) {
			System.out.println("Nous avons "+colons.size()+" ressources.");
			throw new IllegalArgumentException("Les préférences doivent être données pour chaque ressource.");
		}
		Set<Integer> preferencesSet = new HashSet<>(preferences);
		if (preferencesSet.size() != preferences.size()) {
			throw new IllegalArgumentException("Les préférences doivent être uniques.");
		}
		colons.get(nom).setPreferences(preferences);
	}

	// Générer une solution naïve
	public Map<String, Integer> genererSolutionNaive() {
		affectations.clear();
		Set<Integer> ressourcesAttribuees = new HashSet<>();

		for (Colon colon : colons.values()) {
			for (int ressource : colon.getPreferences()) {
				if (!ressourcesAttribuees.contains(ressource)) {
					affectations.put(colon.getNom(), ressource);
					ressourcesAttribuees.add(ressource);
					break;
				}
			}
		}
		return affectations;
	}

	// Calculer le coût (nombre de colons jaloux)
	public int calculerCout() {
		int cout = 0;
		for (Set<String> relation : relations) {
			Iterator<String> it = relation.iterator();
			String colon1 = it.next();
			String colon2 = it.next();

			if (affectations.containsKey(colon1) && affectations.containsKey(colon2)) {
				Colon c1 = colons.get(colon1);
				Colon c2 = colons.get(colon2);

				int ressource1 = affectations.get(colon1);
				int ressource2 = affectations.get(colon2);

				// Vérifier si un des colons est jaloux
				if (c1.getPreferences().indexOf(ressource2) < c1.getPreferences().indexOf(ressource1)
						|| c2.getPreferences().indexOf(ressource1) < c2.getPreferences().indexOf(ressource2)) {
					cout++;
				}
			}
		}
		return cout;
	}

	// Afficher les affectations actuelles
	public void afficherAffectations() {
		for (Map.Entry<String, Integer> entry : affectations.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	// Afficher les colons existants
	public void afficherColons() {
		for (Map.Entry<String, Colon> entry : colons.entrySet()) {
			System.out.print(entry.getKey()+"\t");
		}
		System.out.println();
	}

	// Vérifier si un colon existe dans la colonie
	public boolean colonExiste(String nom) {
		return colons.containsKey(nom);
	}
	public Map<String, Colon> getColons() {
		return colons;
	}
	public Set<Set<String>> getRelations(){
		return relations;
	}
}
