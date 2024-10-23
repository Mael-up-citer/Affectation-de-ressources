
public class Colonie{
    private int[] grapheInteraction; // Graphe sous forme de tableau des relations social
    private Colon[] population;   // Stock la population de la colonie

    Colonie(int nbColon, int diversiterRsc){
        // Alloue l'espace memoir du graphe
        grapheInteraction = new int[((nbColon * (nbColon - 1)) / 2)];   // On veut stocker que Matn*n / 2 - n (diagonal)
        population = new Colon[nbColon];  // Alloue l'espace memoir de la population

        // Remplie le tableau population de Colons
        for(int i = 0; i < population.length; i++)
            population[i] = new Colon(diversiterRsc);   // Initialise les Colons

    }

    // Genere les liens entre colon de maniere aleatoire
    public void generateRandomColo(){
        // Parcour les ligne du graphe sauf la derniere car c'est une diagonal
        for(int i = 0; i < population.length-1; i++){
            // Parcour les colonnes du graphe
            for(int j = i+1; j < population.length; j++){
                // 1 chance sur 2 de prendre 0 ou alors si c'est une diagonal
                if(Math.random() > 0.5)
                    // On dit que i j Sont jalou
                    grapheInteraction[getIndex(i, j)] = 0;
                else
                    // On dit que i j ne sont pas jalou
                    grapheInteraction[getIndex(i, j)] = 1;
            }
        }
    }

    // Compte le nombre de conflit
    public int countConflict(){
        int cptConflit = 0; // Nb courrant de conflit

        // Parcour les lignes
        for(int i = 0; i < population.length-1; i++){
            int currentJalouse = 0; // Jalousie du colon i

            // Parcour les colones
            for(int j = i+1; j < population.length; j++){
                // Si l'un des 2 colons a une meilleur ressources qu'un autre
                if(population[i].getRankAllocRsc() != population[j].getRankAllocRsc())
                    currentJalouse = grapheInteraction[getIndex(i, j)]; // Si i est jalou la variables resoit 1
            }
        cptConflit += currentJalouse;   // Si le colon i a ete jalou on augmente les compteur de jalousie sinno on ajoute 0
        }
        return cptConflit;
    }

    // Echange les ressources de ces 2 colons
    public void swapRsc(int c1, int c2){
        NameRessource[] tabRscC1 = population[c1].getPreference();   // Recupere le tab de ressource du colon1
        NameRessource[] tabRscC2 = population[c2].getPreference();   // Recupere le tab de ressource du colon2

        // Met de coter la ressource de c1 et de c2
        NameRessource rsc1 = tabRscC1[population[c1].getRankAllocRsc()];
        NameRessource rsc2 = tabRscC2[population[c2].getRankAllocRsc()];


        // Cherche dans les preferences de c1 et c2 la ressources alloue a l'autre colon pour lui mettre et ajuster le rang
        for(int i = 0; i < tabRscC1.length; i++){
            // Si dans le tableau de ressource du colon 1 l'indice i vaut la ressource du colon 2
            if(tabRscC1[i] == rsc2)
                population[c1].setRankAllocRsc(i);  // Met a jour le ressource alloue au colon1

            // Si dans le tableau de ressource du colon 2 l'indice i vaut la ressource du colon 1
            if(tabRscC2[i] == rsc1)
                population[c2].setRankAllocRsc(i);  // Met a jour le ressource alloue au colon2
        }
    }

    // Rend les 2 colons passe en parametre jaloux si mode = 1 sinon si mode = 0 on enleve la jalousie
    public void relation(int mode , int c1, int c2){
        // On les rends jaloux ou l'inverse
        grapheInteraction[getIndex(c1, c2)] = mode;
    }

    // Soit 2 indices un en retourne 1 correspondant (linéarisation de la matrice du graphe)
    public int getIndex(int a, int b){
        // On s'assure que a < b
        if(a > b){
            // Echange les valeurs de a et b
            a = a + b;
            b = a - b;
            a = a - b;
        }
        
        // Calcul de l'index
        return (a * (population.length - 1)) - (a * (a + 1)) / 2 + (b - 1);
    }

    // Setter des preferences de tout les colons de la colonie
    public void initPreference(){
        // Parcour les colon
        for(int i = 0; i < population.length; i++)
            population[i].setRandomPreference();    // Appelle la methode qui met des preference random a un colon
    }

    // Getter du graphe d'interaction
    public int[] getGrapheInteraction(){
        return grapheInteraction;   // Retourne le graphe
    }

    // Setter du graphe d'interaction
    public void getGrapheInteraction(int[] graphe){
        grapheInteraction = graphe;   // Affecte le graphe
    }

    // Getter de la population
    public Colon[] getPopulation(){
        return population;   // Retourne la population
    }

    // Setteur de la population
    public void setPopulation(Colon[] pop){
        population = pop;   // Affecte la population
    }
}
