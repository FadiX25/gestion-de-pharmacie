package dao;

import database.CSVHelper;
import models.Gestionnaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe GestionnaireDAO - Gère les opérations CRUD pour les Gestionnaires
 * 
 * Version CSV - Utilise des fichiers CSV au lieu de MySQL
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class GestionnaireDAO {
    
    private static final String FICHIER = "gestionnaires.csv";
    private static final String[] ENTETES = {"id", "nom", "prenom", "login", "password"};
    
    public GestionnaireDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE ==========
    
    public boolean ajouter(Gestionnaire gestionnaire) {
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        gestionnaire.setId(nouvelId);
        
        String[] donnees = {
            String.valueOf(gestionnaire.getId()),
            gestionnaire.getNom(),
            gestionnaire.getPrenom(),
            gestionnaire.getLogin(),
            gestionnaire.getPassword()
        };
        
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Gestionnaire ajouté avec succès! ID: " + gestionnaire.getId());
        }
        
        return succes;
    }
    
    // ========== READ ==========
    
    public Gestionnaire trouverParId(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                return extraireGestionnaire(ligne);
            }
        }
        
        return null;
    }
    
    public Gestionnaire trouverParLogin(String login) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (ligne[3].equals(login)) {
                return extraireGestionnaire(ligne);
            }
        }
        
        return null;
    }
    
    public List<Gestionnaire> trouverTous() {
        List<Gestionnaire> gestionnaires = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            gestionnaires.add(extraireGestionnaire(ligne));
        }
        
        return gestionnaires;
    }
    
    // ========== UPDATE ==========
    
    public boolean mettreAJour(Gestionnaire gestionnaire) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == gestionnaire.getId()) {
                nouvellesLignes.add(new String[]{
                    String.valueOf(gestionnaire.getId()),
                    gestionnaire.getNom(),
                    gestionnaire.getPrenom(),
                    gestionnaire.getLogin(),
                    gestionnaire.getPassword()
                });
                trouve = true;
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Gestionnaire mis à jour avec succès!");
        }
        
        return trouve;
    }
    
    // ========== DELETE ==========
    
    public boolean supprimer(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                trouve = true;
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Gestionnaire supprimé avec succès!");
        }
        
        return trouve;
    }
    
    // ========== AUTHENTIFICATION ==========
    
    public Gestionnaire authentifier(String login, String password) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (ligne[3].equals(login) && ligne[4].equals(password)) {
                System.out.println("✓ Authentification gestionnaire réussie!");
                return extraireGestionnaire(ligne);
            }
        }
        
        return null;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    private Gestionnaire extraireGestionnaire(String[] ligne) {
        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.setId(Integer.parseInt(ligne[0]));
        gestionnaire.setNom(ligne[1]);
        gestionnaire.setPrenom(ligne[2]);
        gestionnaire.setLogin(ligne[3]);
        gestionnaire.setPassword(ligne[4]);
        return gestionnaire;
    }
}
