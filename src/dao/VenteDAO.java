package dao;

import database.CSVHelper;
import models.Vente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe VenteDAO - Gère les opérations CRUD pour les Ventes
 * 
 * Version CSV
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class VenteDAO {
    
    private static final String FICHIER = "ventes.csv";
    private static final String[] ENTETES = {"id", "idPharmacien", "idClient", "idMedicament", "quantite", "montantTotal", "dateVente"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public VenteDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE ==========
    
    public boolean ajouter(Vente vente) {
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        vente.setId(nouvelId);
        
        // Si pas de date, utiliser maintenant
        if (vente.getDateVente() == null) {
            vente.setDateVente(new Date());
        }
        
        String[] donnees = {
            String.valueOf(vente.getId()),
            String.valueOf(vente.getIdPharmacien()),
            String.valueOf(vente.getIdClient()),
            String.valueOf(vente.getIdMedicament()),
            String.valueOf(vente.getQuantite()),
            String.valueOf(vente.getMontantTotal()),
            DATE_FORMAT.format(vente.getDateVente())
        };
        
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Vente enregistrée avec succès! ID: " + vente.getId());
        }
        
        return succes;
    }
    
    // ========== READ ==========
    
    public Vente trouverParId(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                return extraireVente(ligne);
            }
        }
        
        return null;
    }
    
    public List<Vente> trouverTous() {
        List<Vente> ventes = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            ventes.add(extraireVente(ligne));
        }
        
        return ventes;
    }
    
    /**
     * Récupère les ventes d'un pharmacien
     */
    public List<Vente> trouverParPharmacien(int idPharmacien) {
        List<Vente> ventes = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[1]) == idPharmacien) {
                ventes.add(extraireVente(ligne));
            }
        }
        
        return ventes;
    }
    
    /**
     * Récupère les ventes d'aujourd'hui
     */
    public List<Vente> trouverVentesDuJour() {
        List<Vente> ventes = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String aujourdhui = dateOnlyFormat.format(new Date());
        
        for (String[] ligne : lignes) {
            // Comparer seulement la partie date
            if (ligne[6].startsWith(aujourdhui)) {
                ventes.add(extraireVente(ligne));
            }
        }
        
        return ventes;
    }
    
    /**
     * Calcule le chiffre d'affaires total
     */
    public double calculerChiffreAffairesTotal() {
        double total = 0;
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            total += Double.parseDouble(ligne[5]);
        }
        
        return total;
    }
    
    /**
     * Calcule le chiffre d'affaires du jour
     */
    public double calculerChiffreAffairesDuJour() {
        double total = 0;
        List<Vente> ventesDuJour = trouverVentesDuJour();
        
        for (Vente vente : ventesDuJour) {
            total += vente.getMontantTotal();
        }
        
        return total;
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
            System.out.println("✓ Vente annulée avec succès!");
        }
        
        return trouve;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    private Vente extraireVente(String[] ligne) {
        Vente vente = new Vente();
        vente.setId(Integer.parseInt(ligne[0]));
        vente.setIdPharmacien(Integer.parseInt(ligne[1]));
        vente.setIdClient(Integer.parseInt(ligne[2]));
        vente.setIdMedicament(Integer.parseInt(ligne[3]));
        vente.setQuantite(Integer.parseInt(ligne[4]));
        vente.setMontantTotal(Double.parseDouble(ligne[5]));
        
        try {
            vente.setDateVente(DATE_FORMAT.parse(ligne[6]));
        } catch (ParseException e) {
            vente.setDateVente(new Date());
        }
        
        return vente;
    }
}
