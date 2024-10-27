package paa.projet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Colon {
	private String nom;
	private Set<Colon> relation;
	private List<Integer> preferences;
	private int possede_ressources;

	public Colon(String nom) {
		this.nom = nom;
		this.relation = new HashSet<>();
		this.preferences = new ArrayList<>();
		this.possede_ressources = -1;
	}

	public void ajoutRelation(Colon avec) {
		relation.add(avec);
	}

	public void ajoutPreference(List<Integer> preferences) {
		this.preferences = new ArrayList<>(preferences);
	}

	public boolean estJaloux(Colon avec) {
		if (relation.contains(avec)) {
			return preferences.indexOf(avec.possede_ressources) < preferences.indexOf(this.possede_ressources);
		}
		return false;
	}

	public String getNom() {
		return nom;
	}

	public int getRessourceDonnee() {
		return possede_ressources;
	}
	
	public void setRessourceDonnee(int res) {
		this.possede_ressources = res;
	}

	public List<Integer> getPreferences() {
		return preferences;
	}

	public boolean hasPreferences() {
		return preferences.size() > 0;
	}

}
