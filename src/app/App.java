package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import partie1.Colonie;
import partie2.AnaliseFichier;
import partie2.SolutionOptimise;
import partie2.SolutionSauvegarde;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Classe principale de l'application de gestion de colonie.
 * Cette classe représente l'interface utilisateur principale avec les fonctionnalités permettant de charger un fichier,
 * générer une solution optimisée et sauvegarder la solution dans un fichier.
 */
public class App extends Application {
    private Colonie colonie;
    private Map<String, String> solution;

    /**
     * Méthode principale d'entrée de l'application JavaFX. Elle initialise la fenêtre principale et les composants de l'interface.
     * 
     * @param primaryStage La fenêtre principale de l'application.
     */
    @Override
    public void start(Stage primaryStage) {
        colonie = new Colonie();
        solution = null;

        // Création de la fenêtre principale
        primaryStage.setTitle("Gestion de Colonie");

        // Layout principal
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Bouton pour charger un fichier
        Button btnChargerFichier = new Button("Charger un fichier");
        btnChargerFichier.setOnAction(event -> chargerFichier(primaryStage));

        // Bouton pour générer une solution optimisée
        Button btnOptimiserSolution = new Button("Résolution automatique");
        btnOptimiserSolution.setDisable(true); // Désactivé tant qu'aucune colonie n'est chargée
        btnOptimiserSolution.setOnAction(event -> optimiserSolution());

        // Bouton pour sauvegarder la solution actuelle
        Button btnSauvegarderSolution = new Button("Sauvegarder la solution");
        btnSauvegarderSolution.setDisable(true); // Désactivé tant qu'aucune solution n'est générée
        btnSauvegarderSolution.setOnAction(event -> sauvegarderSolution(primaryStage));

        // Zone de texte pour afficher les informations
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(300);

        Button btnAccederAppManuel= new Button("Accéder à l'Interface Utilisateur");

        // Action du bouton pour afficher InterfaceUtilisateurJavaFX
        btnAccederAppManuel.setOnAction(event -> {
            Stage appManuelStage = new Stage();
            AppManuel appManuel = new AppManuel();
            appManuel.afficher(appManuelStage);
        });

        // Ajouter les éléments au layout principal
        root.getChildren().addAll(btnChargerFichier, btnAccederAppManuel, btnOptimiserSolution,
                btnSauvegarderSolution, textArea);

        // Configurer la scène
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Permet de charger un fichier contenant les données de la colonie.
     * Cette méthode ouvre une boîte de dialogue permettant de sélectionner un fichier texte,
     * puis charge les données de la colonie à partir du fichier choisi.
     * 
     * @param stage La fenêtre principale de l'application.
     */
    private void chargerFichier(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier de colonie");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
        File fichier = fileChooser.showOpenDialog(stage);

        if (fichier != null) {
            try {
                AnaliseFichier analyse = new AnaliseFichier(fichier.getAbsolutePath());
                colonie = analyse.analiseFichier();
                afficherMessage("Données de la colonie chargées depuis le fichier : " + fichier.getName());
            } catch (IOException | IllegalArgumentException e) {
                afficherMessage("Erreur lors de la lecture du fichier : " + e.getMessage());
            }
        }
    }

    /**
     * Permet de générer une solution optimisée pour la colonie chargée.
     * Cette méthode crée une instance de la classe SolutionOptimise et génère une solution en fonction des données de la colonie.
     */
    private void optimiserSolution() {
        if (colonie != null) {
            SolutionOptimise optimise = new SolutionOptimise(colonie);
            solution = optimise.optimiseSolution(colonie.getColons().size() * 10);
            afficherMessage("Solution optimisée : " + solution);
            afficherMessage("Coût de la solution : " + colonie.calculerCout());
        }
    }

    /**
     * Permet de sauvegarder la solution optimisée dans un fichier texte.
     * Cette méthode ouvre une boîte de dialogue permettant de sélectionner l'emplacement et le nom du fichier de sauvegarde.
     * 
     * @param stage La fenêtre principale de l'application.
     */
    private void sauvegarderSolution(Stage stage) {
        if (solution != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la solution");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
            File fichier = fileChooser.showSaveDialog(stage);

            if (fichier != null) {
                try {
                    SolutionSauvegarde.sauvegardeSolution(fichier.getAbsolutePath(), solution);
                    afficherMessage("Solution sauvegardée dans le fichier : " + fichier.getName());
                } catch (IOException e) {
                    afficherMessage("Erreur lors de la sauvegarde : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Affiche un message dans la console.
     * 
     * @param message Le message à afficher.
     */
    private void afficherMessage(String message) {
        System.out.println(message);
    }

    /**
     * Méthode principale d'entrée de l'application JavaFX.
     * 
     * @param args Les arguments de ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
