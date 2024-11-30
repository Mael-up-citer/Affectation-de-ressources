import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * La classe Capitaine représente le gestionnaire des ressources dans une colonie.
 * Elle est responsable de la répartition des ressources parmi les colons, ainsi que de l'optimisation de cette répartition pour minimiser les conflits de jalousie.
 * Le capitaine peut effectuer une répartition naïve des ressources ou utiliser une approche plus intelligente, en analysant les conflits et en échangeant les ressources entre colons pour améliorer l'allocation.
 */
public class Capitaine {
    private final float PROBA = 0.27f;  // Proba d'accepter une affectation qui fasse augmenter le nombre de jalousie
    private final int ITERATION;    // Nombre d'itérations pour réaliser des échanges de ressources
    private final int NBCONSERNE;  // Nombre de colons impliqués dans un échange de ressources (doit être un nombre pair)

    // Ressources à répartir, chaque index représente une ressource
    private Ressource[] ressources;

    // Colonie associée au capitaine
    private Colonie colo;


    /**
     * Constructeur de la classe Capitaine.
     * Initialise les ressources et assigne la colonie.
     * @param nbRsc Nombre de types différents de ressources
     * @param c Colonie qui sera gérée par le capitaine
     * @throws IllegalArgumentException Si le nombre de ressources est négatif ou si la colonie est null
     */
    public Capitaine(int nbRsc, Colonie c) {
        // Vérifie que le nombre de ressources est positif
        if (nbRsc <= 0)
            throw new IllegalArgumentException("Le nombre de ressources doit être positif.");

        // Vérifie que la colonie n'est pas null
        if (c == null)
            throw new IllegalArgumentException("La colonie ne peut pas être null.");
        
        ressources = new Ressource[nbRsc]; // Initialise le tableau des ressources
        colo = c;  // Associe cette colonie au capitaine
        ITERATION = (int)(Math.floor(ressources.length * 1.33)); // Calcul du nombre d'itérations basé sur le nombre de ressources
        // Détermine NBCONSERNE en fonction de la taille de la population
        NBCONSERNE = (colo.getPopulation().size() > 16) ? 4 : 2; // Si la population est grande (> 8), NBCONSERNE = 4, sinon NBCONSERNE = 2
    }

    /**
     * Répartit les ressources entre les colons de manière naïve.
     * Chaque colon reçoit la première ressource dans sa liste de préférences encore disponible.
     */
    public Set<Colon> repartitionNaive() {
        ArrayList<Colon> pop = colo.getPopulation();  // Récupère la population de colons
        int cpt;  // Index de la ressource préférée

        // Si la colonie contient un seul colon
        if (pop.size() == 1) {
            cpt = 0;

            // Trouve la première ressource dans les préférences encore disponible
            while (cpt < pop.get(0).getPreference().length && ressources[pop.get(0).getPreference()[cpt].getId()].getQuantite() == 0)
                cpt++;  // Passe à la préférence suivante si la ressource est épuisée

            pop.get(0).allocateResource(cpt);  // Alloue la ressource au colon
            ressources[pop.get(0).getPreference()[cpt].getId()].UseRessource();  // Diminue la quantité de la ressource allouée

            return new HashSet<Colon>(); // Pas de jalousie
        }

        // Parcourt chaque colon pour affecter les ressources
        for (int i = 0; i < pop.size(); i++) {
            cpt = 0;  // Commence à la première préférence

            // Trouve la première ressource dans les préférences encore disponible
            while (cpt < pop.get(i).getPreference().length && ressources[pop.get(i).getPreference()[cpt].getId()].getQuantite() == 0)
                cpt++;  // Passe à la préférence suivante si la ressource est épuisée

            pop.get(i).allocateResource(cpt);  // Alloue la ressource au colon
            ressources[pop.get(i).getPreference()[cpt].getId()].UseRessource();  // Diminue la quantité de la ressource allouée
        }
        return getColonie().countConflict(); // Retourne le nombre de conflits de jalousie dans la colonie après la répartition
    }

    /**
     * Répartit les ressources de façon plus intelligente.
     * Utilise une répartition naïve au départ, puis analyse les conflits de jalousie et effectue des échanges pour minimiser ces conflits.
     */
    @SuppressWarnings("unchecked")
    public Set<Colon> repartitionIntelligente() {
        // Effectue une répartition naïve des ressources
        Set<Colon> jalou = repartitionNaive(); 
        ArrayList<Colon> pop = colo.getPopulation();    // Récupere la population

        // Si il y a un colon il n'y a pas de conflits
        if (pop.size() == 1)
            return new HashSet<>();

        int minNbConflits = jalou.size();   // Nombre de conflict minimum
        int newNbConflits;  // Nombre de conflict de la nouvelle configuration
        Random ran = new Random();  // Nouvelle objet random
        // Clonage de la population actuelle pour conserver la meilleure configuration
        ArrayList<Colon> bestConfig = (ArrayList<Colon>)(pop.clone());

        // Effectue un nombre d'itérations pour essayer d'améliorer la configuration des ressources
        for (int i = 0; i < ITERATION; i++) {
            Set<Colon> toChange;  // Set pour stocker les colons choisi pour un échange

            toChange = pickColon(jalou);  // Choisit aléatoirement un groupe de colons à échanger
            change(toChange);  // Échange des ressources entre les colons choisis

            jalou = colo.countConflict();  // Met à jour les colons jalou
            newNbConflits = jalou.size();

            // Si le nombre de conflits a diminué, on garde cette configuration comme étant la meilleure
            if (newNbConflits < minNbConflits) {
                minNbConflits = newNbConflits;
                bestConfig = (ArrayList<Colon>)(pop.clone());  // Sauvegarde la meilleure configuration
            }
            // Si l'échange a augmenté les conflits, on garde la configuration avec une certaine probabilité
            else if ((ran.nextFloat() >= PROBA)) {
                // L'échange n'est pas gardé
                change(toChange);   // Annule l'échange
            }
        }
        colo.setPopulation(bestConfig);  // Applique la meilleure configuration trouvée

        return jalou;  // Retourne le nombre minimal de conflits
    }

    /**
     * Sélectionne un certain nombre de colons au hasard de la population de la colonie
     * avec plus de chance de prendre des colons jaloux.
     * @return Un tableau contenant les colons choisis aléatoirement
     */
    private Set<Colon> pickColon(Set<Colon> jalou) {
        Random random = new Random();  // Crée une instance de Random pour générer des valeurs aléatoires
        ArrayList<Colon> pop = colo.getPopulation();  // Récupère la population de colons
        Set<Colon> selectedColon = new HashSet<>();  // Set pour garder une trace des colons déjà choisis
    
        // Tant que la taille du Set selectedColon est différente de NBCONSERNE, on continue de sélectionner
        while (selectedColon.size() < NBCONSERNE) {
            // 75% de chance de choisir dans le Set des colons jaloux
            if (random.nextFloat() > 0.25 && !jalou.isEmpty()) {
                // Choisir un colon jaloux dans le set
                // Recupere un index valide de colon dans le set
                int index = random.nextInt(jalou.size());
                // Récupère le colon
                Colon colon = (Colon) jalou.toArray()[index];
                // L'ajoute au set
                selectedColon.add(colon);
            }
            else {
                // Choisir un colon aléatoire dans la population
                Colon colon = pop.get(random.nextInt(pop.size()));
                // L'ajoute au set
                selectedColon.add(colon);
            }            
        }
        return selectedColon;  // Retourne les colons choisis
    }    

    /**
     * Effectue un échange de ressources entre les colons choisis.
     * @param toChange Tableau de colons à échanger leurs ressources
     */
    private void change(Set<Colon> toChange) {
        ArrayList<Colon> colon = new ArrayList<>(toChange);

        // Boucle pour effectuer les échanges par paire
        for (int i = 0; i < NBCONSERNE; i += 2)
            // Effectuer l'échange entre 2 colons
            colo.swapRsc(colon.get(i), colon.get(i+1));
    }

    /**
     * Affiche la quantité disponible de chaque ressource.
     */
    public void displayRsc() {
        // Affiche chaque ressource et sa quantité disponible
        for (int i = 0; i < ressources.length; i++)
            System.out.println("Ressource " + i + ": " + ressources[i]);
    }

    /**
     * Affiche l'état actuel des ressources allouées pour chaque colon.
     */
    public void dispSystemSate() {
        ArrayList<Colon> pop = colo.getPopulation();  // Récupère la population des colons

        System.out.println("État du système:");
        // Affiche l'état des ressources allouées pour chaque colon
        for (int i = 0; i < pop.size(); i++)
            System.out.println(i + ": " + pop.get(i).getPreference()[pop.get(i).getRankAllocRsc()]);
    }

    /**
     * Affiche les types de ressources disponibles sans les quantités.
     */
    public void affRscDispo() {
        System.out.println("Ressources à répartir:");
        // Affiche chaque ressource disponible
        for (int i = 0; i < ressources.length; i++) {
            if (ressources[i].getQuantite() != 0) {  // Vérifie si la ressource est encore disponible
                System.out.println(ressources[i]);
            }
        }
    }

    /**
     * Accesseur pour les ressources.
     * @return Le tableau des ressources
     */
    public Ressource[] getRessources() {
        return ressources;
    }

    /**
     * Mutateur pour les ressources.
     * @param rsc Nouveau tableau de ressources
     */
    public void setRessources(Ressource[] rsc) {
        ressources = rsc;
    }

    /**
     * Accesseur pour la colonie associée au capitaine.
     * @return La colonie gérée par le capitaine
     */
    public Colonie getColonie() {
        return colo;
    }

    /**
     * Mutateur pour la colonie associée.
     * @param c Nouvelle colonie à associer au capitaine
     */
    public void setColonie(Colonie c) {
        colo = c;
    }
}