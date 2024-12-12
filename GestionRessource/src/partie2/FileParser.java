package partie2;

import partie1.Colonie;

import java.io.*;
import java.util.*;

public class FileParser {
	private String filePath;

	public FileParser(String filePath) {
		this.filePath = filePath;
	}

	public Colonie parseFile() throws IOException {
		Colonie colonie = new Colonie();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		int lineNumber = 0;

		while ((line = reader.readLine()) != null) {
			lineNumber++;
			line = line.trim();
			
			//ignore ligne vide
			if (line.isEmpty())
				continue;
			try {
				if (line.startsWith("colon(")) {
					String colonName = line.substring(6, line.length() - 2).trim();
					if (colonName.isEmpty()) {
						throw new IllegalArgumentException("Nom du colon vide à la ligne " + lineNumber + ".");
					}
					colonie.ajouterColon(colonName);
				} else if (line.startsWith("ressource(")) {
					// Traiter les ressources si nécessaire
				} else if (line.startsWith("deteste(")) {
					String[] parts = line.substring(8, line.length() - 2).split(",");
					if (parts.length != 2) {
						throw new IllegalArgumentException(
								"Relation 'deteste' mal formée à la ligne " + lineNumber + ".");
					}
					colonie.ajouterRelation(parts[0].trim(), parts[1].trim());
				} else if (line.startsWith("preferences(")) {
					String[] parts = line.substring(12, line.length() - 2).split(",");
					if (parts.length < 2) {
						throw new IllegalArgumentException("Préférences incomplètes à la ligne " + lineNumber + ".");
					}
					String colonName = parts[0].trim();
					List<Integer> preferences = new ArrayList<>();
					for (int i = 1; i < parts.length; i++) {
						preferences.add(Integer.parseInt(parts[i].trim()));
					}
					colonie.ajouterPreferences(colonName, preferences);
				} else {
					throw new IllegalArgumentException("Syntaxe inconnue à la ligne " + lineNumber + ": " + line);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Erreur à la ligne " + lineNumber + " : " + line + " -> " + e.getMessage());
			}
		}

		reader.close();
		return colonie;
	}

}
