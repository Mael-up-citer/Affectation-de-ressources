
public class Capitaine{
    // ressources qui seront a repartir. rsc[0] = qttEAU; rsc[1] = qttPAIN; etc  suis l'ordre de l'enum
    private int[] ressources;
    private Colonie colo;   // Contient la colonie du capitaine

    Capitaine(int divRsc, Colonie c){
        ressources = new int[divRsc];   // Alloue un tableau de longueur = diversiter des ressources
        colo = c;  // Associer le capitaine a cette colonie
    }

    // Remplie les ressources de facon aleatoire
    public void retourDeMission(){
        int qtt;    // qtt pour une ressource
        int nbRsc = colo.getPopulation().length;    // Nombre de rsc a repartir

        // Itere autant qu'il y a de ressource differente -1
        for(int i = 0; i < ressources.length-1; i++){
            qtt = (int)(Math.random() * nbRsc); // Qtt courante que l'on va affecter
            nbRsc -= qtt; // decompte le rsc que l'on vient de mettre
            ressources[i] = qtt; // Ajoute la qtt a cette ressource
        }
        ressources[ressources.length-1] = nbRsc;  // Met le reste au dernier elmt
    }

    // Repartie naivement les ressources entre colon et retourne le nombre de jalou
    public void repartitionNaive(){
        Colon[] pop = colo.getPopulation();
        int cpt = 0;

        // Pour chaque colon faire
        for(int i = 0; i < pop.length; i++){
            cpt = 0;    // Index de la ressource que l'on tente d'allouer

            // Tant que la ressource que l'on veut affecter n'est plus dispo
            while(ressources[pop[i].getPreference()[cpt].getValeur()] == 0)
                cpt++; // Passe a la suivante

            pop[i].setRankAllocRsc(cpt);  // Set le rang de sa ressource
            ressources[pop[i].getPreference()[cpt].getValeur()]--;  // Decremente le nombre de cette ressource
        }
    }

    // Affiche les ressources disponible
    public void displayRsc(){
        for (int i = 0; i < ressources.length; i++)
            System.out.println("Nous avons " + ressources[i] + " " + NameRessource.values()[i]);
    }

    public void dispSystemSate(){
        Colon[] pop = colo.getPopulation();

        System.out.println("État du system:");
        // Parcour la population
        for(int i = 0; i < pop.length; i++)
            // Affiche la ressource affecte au colon i
            System.out.println(i+":"+pop[i].getPreference()[pop[i].getRankAllocRsc()]);
    }

    // Getter des ressources
    public int[] getRessources(){
        return ressources;
    }

    // Setter des ressources
    public void setRessources(int[] rsc){
        ressources = rsc;
    }

    // Getter de la colo
    public Colonie getColonie(){
        return colo;
    }

    // Setter de la colo
    public void setColonie(Colonie c){
        colo = c;
    }
}
