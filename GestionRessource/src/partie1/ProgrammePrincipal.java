package partie1;

/**
 * Classe principale permettant de lancer l'application.
 */
public class ProgrammePrincipal {
    /**
     * Point d'entrée de l'application.
     * 
     * @param args les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        // Création et démarrage de l'interface utilisateur
        InterfaceUtilisateur u = new InterfaceUtilisateur();
        u.demarrer();
    }
}
