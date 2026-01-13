package dao;

import database.CSVHelper;
import models.StockHistorique;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe StockHistoriqueDAO - Gère l'historique des mouvements de stock
 * 
 * Version CSV
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class StockHistoriqueDAO {
    
    private static final String FICHIER = "stock_historique.csv";
    private static final String[] ENTETES = {"id", "idMedicament", "quantite", "typeOperation", "dateOperation"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public StockHistoriqueDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE ==========
    
    public boolean ajouter(StockHistorique historique) {
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        historique.setId(nouvelId);
        
        if (historique.getDateOperation() == null) {
            historique.setDateOperation(new Date());
        }
        
        String[] donnees = {
            String.valueOf(historique.getId()),
            String.valueOf(historique.getIdMedicament()),
            String.valueOf(historique.getQuantite()),
            historique.getTypeOperation(),
            DATE_FORMAT.format(historique.getDateOperation())
        };
        
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Mouvement de stock enregistré!");
        }
        
        return succes;
    }
    
    /**
     * Enregistre une vente dans l'historique
     */
    public boolean enregistrerVente(int idMedicament, int quantite) {
        StockHistorique historique = new StockHistorique(
            idMedicament,
            -quantite, // Négatif car sortie de stock
            StockHistorique.TYPE_VENTE
        );
        return ajouter(historique);
    }
    
    /**
     * Enregistre un réapprovisionnement
     */
    public boolean enregistrerReapprovisionnement(int idMedicament, int quantite) {
        StockHistorique historique = new StockHistorique(
            idMedicament,
            quantite, // Positif car entrée de stock
            StockHistorique.TYPE_REAPPROVISIONNEMENT
        );
        return ajouter(historique);
    }
    
    // ========== READ ==========
    
    /**
     * Récupère l'historique d'un médicament
     */
    public List<StockHistorique> trouverParMedicament(int idMedicament) {
        List<StockHistorique> historiques = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[1]) == idMedicament) {
                historiques.add(extraireHistorique(ligne));
            }
        }
        
        return historiques;
    }
    
    /**
     * Récupère tout l'historique
     */
    public List<StockHistorique> trouverTous() {
        List<StockHistorique> historiques = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            historiques.add(extraireHistorique(ligne));
        }
        
        return historiques;
    }
    
    /**
     * Récupère l'historique par type d'opération
     */
    public List<StockHistorique> trouverParType(String typeOperation) {
        List<StockHistorique> historiques = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (ligne[3].equals(typeOperation)) {
                historiques.add(extraireHistorique(ligne));
            }
        }
        
        return historiques;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    private StockHistorique extraireHistorique(String[] ligne) {
        StockHistorique historique = new StockHistorique();
        historique.setId(Integer.parseInt(ligne[0]));
        historique.setIdMedicament(Integer.parseInt(ligne[1]));
        historique.setQuantite(Integer.parseInt(ligne[2]));
        historique.setTypeOperation(ligne[3]);
        
        try {
            historique.setDateOperation(DATE_FORMAT.parse(ligne[4]));
        } catch (ParseException e) {
            historique.setDateOperation(new Date());
        }
        
        return historique;
    }
}
