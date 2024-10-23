public class TestColon{
    private static final int DIVERSITE_RSC = 4; // Constante pour la diversite des ressources
    private static Colon colon = new Colon(DIVERSITE_RSC);

    public static void main(String[] args){
        // Execute le test d'initialisation aleatoire des preferences    
        if(testSetRandomPreference())
            System.out.println("Réussite de l'initialisation aléatoire des préférences");
        else
            System.out.println("Échec de l'initialisation aléatoire des préférences");
    }

    // Verifie si les valeurs sont toutes distinctes
    private static boolean testSetRandomPreference(){
        colon.setRandomPreference();    // Appelle la methode qui mets des preference aleatoire
        NameRessource[] preferences = colon.getPreference();
        boolean[] everAffect = new boolean[DIVERSITE_RSC]; // Suivi des valeurs déjà attribuées

        for(int i = 0; i < preferences.length; i++){
            int valeur = preferences[i].getValeur();

            // Verifie si une ressource est presente plusieurs fois
            if(everAffect[valeur]){
                System.out.println("Erreur : La ressource " + valeur + " est présente plusieurs fois.");
                return false; // Retourne faux c'est une erreur
            }
            // Marque la ressource comme affectee
            everAffect[valeur] = true;
            System.out.println("Préférence " + (i + 1) + ": " + preferences[i]);
        }

        // Verifie que toutes les ressources ont ete affectees
        for(int i = 0; i < everAffect.length; i++){
            if(!everAffect[i]){
                System.out.println("Erreur : La ressource " + i + " n'a pas été attribuée.");
                return false; // Retourne faux si une ressource n'a pas été affectée
            }
        }
        return true;
    }
}