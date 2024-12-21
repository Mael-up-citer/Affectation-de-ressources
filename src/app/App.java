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
 * Cette application JavaFX permet de charger un fichier contenant les données d'une colonie,
 * de générer une solution optimisée et de sauvegarder cette solution dans un fichier.
 */
public class App extends Application {
    private Colonie colonie; // Représente la colonie chargée
    private Map<String, String> solution; // Stocke la solution optimisée
    private TextArea textArea; // Zone de texte pour afficher les messages à l'utilisateur

    /**
     * Point d'entrée principal de l'application JavaFX.
     *
     * @param primaryStage La fenêtre principale de l'application.
     */
    @Override
    public void start(Stage primaryStage) {
        colonie = new Colonie();
        solution = null;

        // Titre de la fenêtre principale
        primaryStage.setTitle("Gestion de Colonie");

        // Layout principal
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Bouton pour charger un fichier
        Button btnChargerFichier = new Button("Charger un fichier");
        Button btnOptimiserSolution = new Button("Résolution automatique");
        Button btnSauvegarderSolution = new Button("Sauvegarder la solution");

        // Configuration initiale des boutons
        btnOptimiserSolution.setDisable(true);
        btnSauvegarderSolution.setDisable(true);

        // Zone de texte pour afficher les informations
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(300);

        // Bouton pour accéder à l'interface utilisateur manuelle
        Button btnAccederAppManuel = new Button("Accéder à l'Interface Utilisateur");
        btnAccederAppManuel.setOnAction(event -> {
            Stage appManuelStage = new Stage();
            AppManuel appManuel = new AppManuel();
            appManuel.afficher(appManuelStage);
        });

        // Actions des boutons
        btnChargerFichier.setOnAction(event -> {
            chargerFichier(primaryStage);
            if (colonie != null && !colonie.getColons().isEmpty()) {
                btnOptimiserSolution.setDisable(false);
            }
        });

        btnOptimiserSolution.setOnAction(event -> {
            optimiserSolution();
            if (solution != null && !solution.isEmpty()) {
                btnSauvegarderSolution.setDisable(false);
            }
        });

        btnSauvegarderSolution.setOnAction(event -> sauvegarderSolution(primaryStage));

        // Ajouter les éléments au layout principal
        root.getChildren().addAll(btnChargerFichier, btnAccederAppManuel, btnOptimiserSolution,
                btnSauvegarderSolution, textArea);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Ouvre une boîte de dialogue pour permettre à l'utilisateur de charger un fichier.
     * Le fichier chargé doit contenir des données valides pour initialiser une colonie.
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

                if (colonie == null || colonie.getColons().isEmpty()) {
                    throw new IllegalArgumentException("Le fichier chargé ne contient pas de données valides.");
                }

                afficherMessage("Données de la colonie chargées depuis le fichier : " + fichier.getName());
            } catch (IOException | IllegalArgumentException e) {
                afficherMessage("Erreur lors de la lecture du fichier : " + e.getMessage());
            }
        }
    }

    /**
     * Génère une solution optimisée pour la colonie chargée.
     * Utilise la classe {@link SolutionOptimise} pour calculer une solution basée sur les données actuelles.
     */
    private void optimiserSolution() {
        if (colonie != null) {
            SolutionOptimise optimise = new SolutionOptimise(colonie);
            solution = optimise.optimiseSolution2(colonie.getColons().size() * 10);
            afficherMessage("Solution optimisée : " + solution);
            afficherMessage("Coût de la solution : " + colonie.calculerCout());
        }
    }

    /**
     * Sauvegarde la solution optimisée dans un fichier texte.
     * Ouvre une boîte de dialogue pour permettre à l'utilisateur de choisir l'emplacement et le nom du fichier.
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
     * Affiche un message dans la console et dans la zone de texte de l'interface utilisateur.
     *
     * @param message Le message à afficher.
     */
    private void afficherMessage(String message) {
        System.out.println(message); // Affiche dans la console
        textArea.appendText(message + "\n"); // Ajoute dans la zone de texte
    }

    /**
     * Méthode principale de l'application.
     * Lance l'interface graphique JavaFX.
     *
     * @param args Les arguments de ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
}