public class TestCapitaine{
    private static final int DIVERSITE_RSC = 4; // Nombre de types de ressources
    private static int nb = 123; // Nombre de colons dans la colonie

    public static void main(String[] args){
        
        // Verifie le test de retour de mission et affiche un message si reussi
        if(testRetourDeMission())
            System.out.println("Test de retour de mission réussi.");
        else
            System.out.println("Test de retour de mission échoué.");

        // Verifie le test de repartition naive et affiche un message si reussi
        if(testRepartitionNaive())
            System.out.println("Test de répartition naïve réussi.");
        else
            System.out.println("Test de répartition naïve échoué.");
    }

    // Teste le retour de mission
    private static boolean testRetourDeMission(){
        Colonie colonie = new Colonie(nb, DIVERSITE_RSC);
        Capitaine capitaine = new Capitaine(DIVERSITE_RSC, colonie);

        capitaine.retourDeMission();
        capitaine.displayRsc(); // Affiche les ressources apres le retour de mission

        // Verifie que les ressources sont positives
        int[] ressources = capitaine.getRessources();
        int sum = 0;    // Somme des elmt du tab de rsc

        for(int rsc : ressources){
            if(rsc < 0){
                System.out.println("Erreur : la quantité de ressources doit être positive.");
                return false; // Retourne false si une ressource est negative
            }
            sum += rsc; // Fait la sum du tab
        }
        
        return (sum == nb)?true:false; // Retourne true si on a autant de rsc que de colons
    }

    // Teste la repartition naive des ressources
    private static boolean testRepartitionNaive(){
        Colonie colonie = new Colonie(nb, DIVERSITE_RSC);
        Capitaine capitaine = new Capitaine(DIVERSITE_RSC, colonie);
        
        // Remplir les ressources avant la repartition
        capitaine.retourDeMission();
        colonie.initPreference();   // Init les preferences des colons
        int[] ressourcesAvant = capitaine.getRessources().clone();

        capitaine.repartitionNaive();   // Fait la repartition des ressoirces
        capitaine.displayRsc(); // Affiche les ressources apres la repartition

        // Verifie que les ressources allouees ont ete decrementees correctement
        int[] ressourcesApres = capitaine.getRessources();

        for(int i = 0; i < ressourcesAvant.length; i++){
            if (ressourcesApres[i] < 0){
                System.out.println("Erreur : la quantité de ressources doit être positive après répartition.");
                return false; // Retourne false si une ressource est negative
            }
            if (ressourcesAvant[i] < ressourcesApres[i]){
                System.out.println("Erreur : les ressources doivent avoir diminué.");
                return false; // Retourne false si une ressource a augmente
            }
        }
        return true; // Retourne true si toutes les verifications passent
    }
}