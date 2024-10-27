package paa.projet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Colonie {

	private Map<String, Colon> colonie;
	private int nbr_ressource; // c'est le meme avec le nbr de la colonie du coup on peut lui donner le nom
								// qu'on veut

	public Colonie(int nbr_ressource) {
		setColonie(new HashMap<>());
		this.nbr_ressource = nbr_ressource;
		for (char nom = 'A'; nom < 'A' + nbr_ressource; nom++) {
			getColonie().put(String.valueOf(nom), new Colon(String.valueOf(nom)));// on transformee nom en string
		}
	}

	public void ajoutRelation(String colon1, String colon2) {
		Colon c1 = getColonie().get(colon1);
		Colon c2 = getColonie().get(colon2);
		if (c1 != null && c2 != null) {
			c1.ajoutRelation(c2);
			c2.ajoutRelation(c1);
		}
	}

	public void ajoutPreference(String nom, List<Integer> preferences) {
		Colon c = getColonie().get(nom);
		if (c != null && preferences.size() == nbr_ressource) {
			c.ajoutPreference(preferences);
		}
	}

	public void distribRessources() {
		Set<Integer> ressourcesUtilise = new HashSet<>();
		for (Colon c : getColonie().values()) {
			for (int preference : c.getPreferences()) {
				if (!ressourcesUtilise.contains(preference)) {
					c.setRessourceDonnee(preference);
					ressourcesUtilise.add(preference);
					break;
				}
			}
		}
	}

	public int calculeJalousie() {
		int jaloux = 0;
		for (Colon c : getColonie().values()) {
			for (Colon jalouse : getColonie().values()) {
				if (c.estJaloux(jalouse)) {
					jaloux++;
					break;
				}
			}
		}
		return jaloux;
	}

	public void echangerRessources(String colon1, String colon2) {
		Colon c1 = getColonie().get(colon1);
		Colon c2 = getColonie().get(colon2);
		if (c1 != null && c2 != null) {
			int temp = c1.getRessourceDonnee();
			c1.setRessourceDonnee(c2.getRessourceDonnee());
			c2.setRessourceDonnee(temp);
		}
	}

	public void afficheAffectations() {
		for (Colon c : getColonie().values()) {
			System.out.println(c.getNom() + ": " + c.getRessourceDonnee());
		}
	}

	public Map<String, Colon> getColonie() {
		return colonie;
	}

	public void setColonie(Map<String, Colon> colonie) {
		this.colonie = colonie;
	}
}
