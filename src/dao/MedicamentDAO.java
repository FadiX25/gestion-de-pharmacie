package dao;

import database.CSVHelper;
import models.Medicament;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe MedicamentDAO - Gère les opérations CRUD pour les Médicaments
 * 
 * Version CSV - Utilise des fichiers CSV au lieu de MySQL
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class MedicamentDAO {
    
    private static final String FICHIER = "medicaments.csv";
    private static final String[] ENTETES = {"id", "nom", "dosage", "stock", "prixUnitaire"};
    
    public MedicamentDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE ==========
    
    public boolean ajouter(Medicament medicament) {
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        medicament.setId(nouvelId);
        
        String[] donnees = {
            String.valueOf(medicament.getId()),
            medicament.getNom(),
            medicament.getDosage(),
            String.valueOf(medicament.getStock()),
            String.valueOf(medicament.getPrixUnitaire())
        };
        
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Médicament ajouté avec succès! ID: " + medicament.getId());
        }
        
        return succes;
    }
    
    // ========== READ ==========
    
    public Medicament trouverParId(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                return extraireMedicament(ligne);
            }
        }
        
        return null;
    }
    
    /**
     * Cherche des médicaments par nom (recherche partielle)
     */
    public List<Medicament> rechercherParNom(String nom) {
        List<Medicament> resultats = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            // Recherche insensible à la casse
            if (ligne[1].toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(extraireMedicament(ligne));
            }
        }
        
        return resultats;
    }
    
    public List<Medicament> trouverTous() {
        List<Medicament> medicaments = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            medicaments.add(extraireMedicament(ligne));
        }
        
        return medicaments;
    }
    
    /**
     * Récupère les médicaments avec un stock critique (< 10)
     */
    public List<Medicament> trouverStockCritique() {
        List<Medicament> resultats = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            int stock = Integer.parseInt(ligne[3]);
            if (stock < Medicament.SEUIL_STOCK_CRITIQUE) {
                resultats.add(extraireMedicament(ligne));
            }
        }
        
        return resultats;
    }
    
    // ========== UPDATE ==========
    
    public boolean mettreAJour(Medicament medicament) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == medicament.getId()) {
                nouvellesLignes.add(new String[]{
                    String.valueOf(medicament.getId()),
                    medicament.getNom(),
                    medicament.getDosage(),
                    String.valueOf(medicament.getStock()),
                    String.valueOf(medicament.getPrixUnitaire())
                });
                trouve = true;
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Médicament mis à jour avec succès!");
        }
        
        return trouve;
    }
    
    /**
     * Met à jour uniquement le stock d'un médicament
     */
    public boolean mettreAJourStock(int idMedicament, int nouveauStock) {
        Medicament med = trouverParId(idMedicament);
        if (med == null) {
            return false;
        }
        
        med.setStock(nouveauStock);
        
        // Réécrire le fichier avec le stock mis à jour
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == idMedicament) {
                ligne[3] = String.valueOf(nouveauStock);
            }
            nouvellesLignes.add(ligne);
        }
        
        CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
        System.out.println("✓ Stock mis à jour: " + nouveauStock + " unités");
        return true;
    }
    
    /**
     * Diminue le stock d'un médicament
     */
    public boolean diminuerStock(int idMedicament, int quantite) {
        Medicament med = trouverParId(idMedicament);
        if (med == null) {
            System.err.println("✗ Médicament non trouvé!");
            return false;
        }
        
        if (med.getStock() < quantite) {
            System.err.println("✗ Stock insuffisant! Stock actuel: " + med.getStock());
            return false;
        }
        
        int nouveauStock = med.getStock() - quantite;
        return mettreAJourStock(idMedicament, nouveauStock);
    }
    
    /**
     * Augmente le stock d'un médicament
     */
    public boolean augmenterStock(int idMedicament, int quantite) {
        Medicament med = trouverParId(idMedicament);
        if (med == null) {
            System.err.println("✗ Médicament non trouvé!");
            return false;
        }
        
        int nouveauStock = med.getStock() + quantite;
        return mettreAJourStock(idMedicament, nouveauStock);
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
            System.out.println("✓ Médicament supprimé avec succès!");
        }
        
        return trouve;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    private Medicament extraireMedicament(String[] ligne) {
        Medicament medicament = new Medicament();
        medicament.setId(Integer.parseInt(ligne[0]));
        medicament.setNom(ligne[1]);
        medicament.setDosage(ligne[2]);
        medicament.setStock(Integer.parseInt(ligne[3]));
        medicament.setPrixUnitaire(Double.parseDouble(ligne[4]));
        return medicament;
    }
}
