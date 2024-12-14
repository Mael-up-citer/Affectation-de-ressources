package app;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import partie1.Colonie;

/**
 * Classe représentant l'interface utilisateur manuelle de gestion de colonie.
 * Cette classe permet à l'utilisateur de configurer une colonie en ajoutant des colons,
 * des relations entre les colons et leurs préférences, puis de finaliser la configuration.
 */
public class AppManuel {
    private Colonie colonie;

    /**
     * Constructeur de la classe AppManuel.
     * Initialise une nouvelle colonie vide.
     */
    public AppManuel() {
        colonie = new Colonie();
    }

    /**
     * Affiche l'interface utilisateur permettant à l'utilisateur de configurer la colonie.
     * Cette méthode crée la fenêtre principale, gère les actions des boutons et les dialogues associés
     * pour ajouter des colons, des relations et des préférences.
     * 
     * @param stage La fenêtre principale de l'application.
     */
    public void afficher(Stage stage) {
        // Créer la scène principale
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label instructionLabel = new Label("Entrez le nombre de colons (max 26) :");
        TextField colonsField = new TextField();
        Button confirmerButton = new Button("Confirmer");

        // Conteneur pour les menus après la configuration initiale
        VBox menuBox = new VBox(10);
        menuBox.setVisible(false);

        Label colonMenuLabel = new Label("Menu :");
        Button ajouterRelationButton = new Button("Ajouter une relation 'ne s’aiment pas'");
        Button ajouterPreferencesButton = new Button("Ajouter les préférences d’un colon");
        Button finConfigurationButton = new Button("Terminer la configuration");

        menuBox.getChildren().addAll(colonMenuLabel, ajouterRelationButton, ajouterPreferencesButton,
                finConfigurationButton);

        root.getChildren().addAll(instructionLabel, colonsField, confirmerButton, menuBox);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Gestion de Colonie");
        stage.setScene(scene);
        stage.show();

        // Action pour confirmer le nombre de colons
        confirmerButton.setOnAction(e -> {
            try {
                int n = Integer.parseInt(colonsField.getText());
                if (n <= 0 || n > 26) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le nombre de colons doit être entre 1 et 26.");
                } else {
                    // Ajouter les colons
                    for (int i = 0; i < n; i++) {
                        colonie.ajouterColon(String.valueOf((char) ('A' + i)));
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Succès", n + " colons ajoutés.");
                    colonsField.setDisable(true);
                    confirmerButton.setDisable(true);
                    menuBox.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre entier valide.");
            }
        });

        // Action pour ajouter une relation "ne s’aiment pas"
        ajouterRelationButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ajouter une relation");
            dialog.setHeaderText("Ajouter une relation 'ne s’aiment pas'");
            dialog.setContentText("Entrez les noms des deux colons séparés par un espace :");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    String[] noms = input.split(" ");
                    if (noms.length != 2) {
                        throw new IllegalArgumentException("Veuillez entrer exactement deux colons.");
                    }
                    colonie.ajouterRelation(noms[0], noms[1]);
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Relation ajoutée entre " + noms[0] + " et " + noms[1] + ".");
                } catch (IllegalArgumentException ex) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
                }
            });
        });

        // Action pour ajouter les préférences d'un colon
        ajouterPreferencesButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ajouter des préférences");
            dialog.setHeaderText("Ajouter les préférences d’un colon");
            dialog.setContentText("Entrez le nom du colon suivi de ses préférences (ex : A 1 2 3 ...) :");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    String[] parts = input.split(" ");
                    String nom = parts[0];
                    List<String> preferences = List.of(parts).subList(1, parts.length);
                    colonie.ajouterPreferences(nom, preferences);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Préférences ajoutées pour " + nom + ".");
                } catch (IllegalArgumentException ex) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
                }
            });
        });

        // Action pour terminer la configuration
        finConfigurationButton.setOnAction(e -> {
            boolean tousLesColonsOntPreferences = colonie.getColons().values().stream()
                    .allMatch(colon -> colon.getPreferences() != null);

            if (tousLesColonsOntPreferences) {
                colonie.afficherAffectations();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Configuration terminée.");
                // Transition vers une autre étape ou fermer la fenêtre si nécessaire
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les colons doivent avoir des préférences.");
            }
        });
    }

    /**
     * Affiche une alerte dans l'interface graphique.
     * 
     * @param alertType Le type d'alerte (par exemple, erreur ou information).
     * @param title Le titre de l'alerte.
     * @param message Le message à afficher dans l'alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
