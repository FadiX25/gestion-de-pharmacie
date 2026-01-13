package database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe CSVHelper - Utilitaire pour lire et écrire des fichiers CSV
 * 
 * CSV = Comma Separated Values (Valeurs Séparées par des Virgules)
 * C'est un format simple pour stocker des données dans des fichiers texte.
 * 
 * Exemple de fichier CSV:
 * id;nom;prenom;login;password
 * 1;Dupont;Marie;mdupont;password123
 * 2;Martin;Pierre;pmartin;password123
 * 
 * @author Étudiant
 * @version 1.0
 */
public class CSVHelper {
    
    // Le séparateur utilisé dans nos fichiers CSV
    // On utilise ";" car la virgule peut apparaître dans les données
    public static final String SEPARATOR = ";";
    
    // Le dossier où sont stockés les fichiers CSV
    public static final String DATA_FOLDER = "data/";
    
    /**
     * Initialise le dossier de données s'il n'existe pas
     */
    public static void initDataFolder() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("✓ Dossier 'data' créé.");
        }
    }
    
    /**
     * Lit toutes les lignes d'un fichier CSV
     * 
     * @param fileName Le nom du fichier (ex: "pharmaciens.csv")
     * @return Une liste de tableaux de String (chaque ligne = un tableau)
     */
    public static List<String[]> lireFichier(String fileName) {
        List<String[]> lignes = new ArrayList<>();
        String cheminComplet = DATA_FOLDER + fileName;
        
        File file = new File(cheminComplet);
        
        // Si le fichier n'existe pas, retourner une liste vide
        if (!file.exists()) {
            return lignes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminComplet))) {
            String ligne;
            boolean premiereLigne = true;
            
            while ((ligne = reader.readLine()) != null) {
                // Ignorer la première ligne (en-têtes)
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }
                
                // Ignorer les lignes vides
                if (ligne.trim().isEmpty()) {
                    continue;
                }
                
                // Découper la ligne selon le séparateur
                String[] colonnes = ligne.split(SEPARATOR, -1);
                lignes.add(colonnes);
            }
            
        } catch (IOException e) {
            System.err.println("✗ Erreur lors de la lecture de " + fileName + ": " + e.getMessage());
        }
        
        return lignes;
    }
    
    /**
     * Écrit des données dans un fichier CSV
     * 
     * @param fileName Le nom du fichier
     * @param entetes  Les en-têtes des colonnes
     * @param donnees  Les données à écrire
     * @return true si l'écriture a réussi
     */
    public static boolean ecrireFichier(String fileName, String[] entetes, List<String[]> donnees) {
        initDataFolder();
        String cheminComplet = DATA_FOLDER + fileName;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(cheminComplet))) {
            // Écrire les en-têtes
            writer.println(String.join(SEPARATOR, entetes));
            
            // Écrire les données
            for (String[] ligne : donnees) {
                writer.println(String.join(SEPARATOR, ligne));
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("✗ Erreur lors de l'écriture de " + fileName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Ajoute une ligne à un fichier CSV existant
     * 
     * @param fileName Le nom du fichier
     * @param donnees  Les données de la nouvelle ligne
     * @return true si l'ajout a réussi
     */
    public static boolean ajouterLigne(String fileName, String[] donnees) {
        initDataFolder();
        String cheminComplet = DATA_FOLDER + fileName;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(cheminComplet, true))) {
            writer.println(String.join(SEPARATOR, donnees));
            return true;
            
        } catch (IOException e) {
            System.err.println("✗ Erreur lors de l'ajout dans " + fileName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Trouve le prochain ID disponible dans un fichier
     * 
     * @param fileName Le nom du fichier
     * @return Le prochain ID à utiliser
     */
    public static int getProchainId(String fileName) {
        List<String[]> lignes = lireFichier(fileName);
        int maxId = 0;
        
        for (String[] ligne : lignes) {
            try {
                int id = Integer.parseInt(ligne[0]);
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignorer les lignes avec un ID invalide
            }
        }
        
        return maxId + 1;
    }
    
    /**
     * Vérifie si un fichier CSV existe
     */
    public static boolean fichierExiste(String fileName) {
        File file = new File(DATA_FOLDER + fileName);
        return file.exists();
    }
    
    /**
     * Crée un fichier CSV avec seulement les en-têtes s'il n'existe pas
     */
    public static void creerFichierSiAbsent(String fileName, String[] entetes) {
        if (!fichierExiste(fileName)) {
            ecrireFichier(fileName, entetes, new ArrayList<>());
            System.out.println("✓ Fichier " + fileName + " créé.");
        }
    }
}
