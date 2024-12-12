package partie1;

import java.util.List;

public class Colon {
    private String nom;
    private List<Integer> preferences;

    public Colon(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setPreferences(List<Integer> preferences) {
        this.preferences = preferences;
    }

    public List<Integer> getPreferences() {
        return preferences;
    }
}


