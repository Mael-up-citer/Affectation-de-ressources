import java.util.Random;

public class Colon{
    private NameRessource[] preference; // Contient les ressource ordonne cad ressource de t[0] > t[1]
    private int rankAllocRsc;    // Rang de la ressource recue par le colon entre 0 et diversiterRsc -1

    Colon(int diversiterRsc){
        preference = new NameRessource[diversiterRsc];  // Initialise l'espace memoir du tableau de preference
    }

    // Mets des preferences random au colon
    public void setRandomPreference(){
        Random ran = new Random();  // Objet de type random pour randomiser l'attribution des preferences
        int[] everAffect = new int[preference.length];   // Suivi des valeurs deja attribue pour eviter les doublon

        // Parcour le tab des preferences
        for(int i = 0; i < preference.length; i++){
            int value = (int)ran.nextInt(4);    // Donne un random entier entre 0 et 3

            // Tant que value est un nombre deja attribue
            while(everAffect[value] != 0)
                value = (int)ran.nextInt(4);    // Genere un autre

            preference[i] = NameRessource.fromValeur(value);    // Affecte la ressources
            everAffect[value] = 1;  // Met a jour everAffet
        }
    }

    // Getter des preferences
    public NameRessource[] getPreference(){
        return preference;  // retourne les preference d'un colon
    }

    // Setter des preferences
    public void setPreference(NameRessource[] preference){
        this.preference = preference;   //Set le tab des preferences
    }

    // Getter de rankAllocRsc
    public int getRankAllocRsc(){
        return rankAllocRsc;  // retourne les preference d'un colon
    }

    // Setter de rankAllocRsc
    public void setRankAllocRsc(int rankAllocRsc){
        this.rankAllocRsc = rankAllocRsc;   //Set le tab des preferences
    }

}
