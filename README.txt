Auteurs:
    Céline Ye, Ramzy Chibani, Maël Lecene

---

Description:
    Ce projet consiste à développer un logiciel capable de répartir les ressources critiques
    entre les colons d’une colonie spatiale tout en minimisant les conflits.
    Les colons expriment leurs préférences sur les ressources disponibles,
    mais des relations conflictuelles entre certains individus rendent la tâche complexe.  

---

Tâches à réaliser:
    Le programme doit :  
        1. Modéliser les relations entre colons sous forme de graphe.  
        2. Simuler les affectations de ressources et calculer le coût en termes de jalousie.  
        3. Optimiser la distribution pour minimiser les conflits.  

    Fonctionnalités clés :  
        - Importation des données depuis un fichier.  
        - Recherche automatisée d’une solution optimisée.  
        - Gestion des erreurs pour assurer la robustesse du programme.
	- Sauvegarde d'une solution dans un fichier

---

Prérequis  
    1. Java : Version 11 ou supérieure, configurée dans les variables d'environnement.  
    2. Bash : Pour exécuter les scripts de compilation et d’exécution.  
    3. JDK : Compatible avec la version de Java installée.  
    4. (Optionnel) : Éditeur/IDE tel que VS Code, IntelliJ IDEA, ou Eclipse pour consulter ou modifier le code source.  

---

Exécution
    1. Assurez-vous de rendre les scripts exécutables avec `chmod u+x script.sh scriptUI.sh`.  
    2. Deux options sont disponibles pour lancer le programme :  
        - Sans affichage graphique : Utilisez `./script.sh` (un fichier de colonie peut être fourni en argument).
        - Avec affichage graphique : Utilisez `./scriptUI.sh` (les arguments passés seront ignorés).

    3. Les tests unitaires sont exécutables avec ./runTest.sh.

---

Les Algorithmes d'optimisation:  
L'algorithme, situé dans la classe `SolutionOptimise` (méthode `optimiseSolution2`), est basé sur une approche heuristique pour minimiser les jalousies :  
    1. Les ressources des colons jaloux sont échangées en priorité, augmentant ainsi les chances de réduire les conflits.  
    2. Une probabilité contrôlée permet d’accepter temporairement des configurations avec plus de jalousie, favorisant l’exploration de l’espace de recherche et évitant les minima locaux.  
    3. La meilleure configuration rencontrée au cours des itérations est conservée comme solution finale.
    4. Selon le rapport qualité/temps, cette méthode prend en entrée un nombre d'itérations. Plus ce nombre est grand, plus le nombre de solutions testées sera élevé, mais cela entraînera également un temps de calcul plus long.

L'algorithme, situé dans la classe SolutionOptimise (méthode rechercheExhaustive), est basé sur une approche combinatoire visant à minimiser les jalousies :
On teste toutes les combinaisons possibles afin de trouver la meilleure. Uniquement si la colonie est <= 10 colons en raison d'un coup de calcul exponentiel.