package dao;

import database.CSVHelper;
import models.Pharmacien;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe PharmacienDAO - Gère les opérations CRUD pour les Pharmaciens
 * 
 * Version CSV - Utilise des fichiers CSV au lieu de MySQL
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class PharmacienDAO {
    
    // Nom du fichier CSV
    private static final String FICHIER = "pharmaciens.csv";
    
    // En-têtes du fichier CSV
    private static final String[] ENTETES = {"id", "nom", "prenom", "login", "password"};
    
    /**
     * Constructeur - initialise le fichier si nécessaire
     */
    public PharmacienDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE (Créer) ==========
    
    /**
     * Ajoute un nouveau pharmacien
     */
    public boolean ajouter(Pharmacien pharmacien) {
        // Générer un nouvel ID
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        pharmacien.setId(nouvelId);
        
        // Préparer les données
        String[] donnees = {
            String.valueOf(pharmacien.getId()),
            pharmacien.getNom(),
            pharmacien.getPrenom(),
            pharmacien.getLogin(),
            pharmacien.getPassword()
        };
        
        // Ajouter au fichier
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Pharmacien ajouté avec succès! ID: " + pharmacien.getId());
        }
        
        return succes;
    }
    
    // ========== READ (Lire) ==========
    
    /**
     * Trouve un pharmacien par son ID
     */
    public Pharmacien trouverParId(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                return extrairePharmacien(ligne);
            }
        }
        
        return null;
    }
    
    /**
     * Trouve un pharmacien par son login
     */
    public Pharmacien trouverParLogin(String login) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (ligne[3].equals(login)) {
                return extrairePharmacien(ligne);
            }
        }
        
        return null;
    }
    
    /**
     * Récupère tous les pharmaciens
     */
    public List<Pharmacien> trouverTous() {
        List<Pharmacien> pharmaciens = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            pharmaciens.add(extrairePharmacien(ligne));
        }
        
        return pharmaciens;
    }
    
    // ========== UPDATE (Mettre à jour) ==========
    
    /**
     * Met à jour un pharmacien
     */
    public boolean mettreAJour(Pharmacien pharmacien) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == pharmacien.getId()) {
                // Remplacer par les nouvelles données
                nouvellesLignes.add(new String[]{
                    String.valueOf(pharmacien.getId()),
                    pharmacien.getNom(),
                    pharmacien.getPrenom(),
                    pharmacien.getLogin(),
                    pharmacien.getPassword()
                });
                trouve = true;
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Pharmacien mis à jour avec succès!");
        }
        
        return trouve;
    }
    
    // ========== DELETE (Supprimer) ==========
    
    /**
     * Supprime un pharmacien
     */
    public boolean supprimer(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                trouve = true; // On ne l'ajoute pas = suppression
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Pharmacien supprimé avec succès!");
        }
        
        return trouve;
    }
    
    // ========== AUTHENTIFICATION ==========
    
    /**
     * Vérifie les identifiants de connexion
     */
    public Pharmacien authentifier(String login, String password) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            // ligne[3] = login, ligne[4] = password
            if (ligne[3].equals(login) && ligne[4].equals(password)) {
                System.out.println("✓ Authentification réussie!");
                return extrairePharmacien(ligne);
            }
        }
        
        return null;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    /**
     * Crée un objet Pharmacien à partir d'une ligne CSV
     */
    private Pharmacien extrairePharmacien(String[] ligne) {
        Pharmacien pharmacien = new Pharmacien();
        pharmacien.setId(Integer.parseInt(ligne[0]));
        pharmacien.setNom(ligne[1]);
        pharmacien.setPrenom(ligne[2]);
        pharmacien.setLogin(ligne[3]);
        pharmacien.setPassword(ligne[4]);
        return pharmacien;
    }
}
