package dao;

import database.CSVHelper;
import models.Commande;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe CommandeDAO - Gère les opérations CRUD pour les Commandes
 * 
 * Version CSV
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class CommandeDAO {
    
    private static final String FICHIER = "commandes.csv";
    private static final String[] ENTETES = {"id", "idGestionnaire", "idMedicament", "quantite", "statut", "dateCommande"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public CommandeDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE ==========
    
    public boolean ajouter(Commande commande) {
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        commande.setId(nouvelId);
        
        if (commande.getDateCommande() == null) {
            commande.setDateCommande(new Date());
        }
        
        String[] donnees = {
            String.valueOf(commande.getId()),
            String.valueOf(commande.getIdGestionnaire()),
            String.valueOf(commande.getIdMedicament()),
            String.valueOf(commande.getQuantite()),
            commande.getStatut(),
            DATE_FORMAT.format(commande.getDateCommande())
        };
        
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Commande créée avec succès! ID: " + commande.getId());
        }
        
        return succes;
    }
    
    // ========== READ ==========
    
    public Commande trouverParId(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                return extraireCommande(ligne);
            }
        }
        
        return null;
    }
    
    public List<Commande> trouverTous() {
        List<Commande> commandes = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            commandes.add(extraireCommande(ligne));
        }
        
        return commandes;
    }
    
    /**
     * Récupère les commandes en attente
     */
    public List<Commande> trouverCommandesEnAttente() {
        List<Commande> commandes = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (ligne[4].equals(Commande.STATUT_EN_ATTENTE)) {
                commandes.add(extraireCommande(ligne));
            }
        }
        
        return commandes;
    }
    
    /**
     * Récupère les commandes d'un gestionnaire
     */
    public List<Commande> trouverParGestionnaire(int idGestionnaire) {
        List<Commande> commandes = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[1]) == idGestionnaire) {
                commandes.add(extraireCommande(ligne));
            }
        }
        
        return commandes;
    }
    
    // ========== UPDATE ==========
    
    /**
     * Met à jour le statut d'une commande
     */
    public boolean mettreAJourStatut(int idCommande, String nouveauStatut) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == idCommande) {
                ligne[4] = nouveauStatut;
                trouve = true;
            }
            nouvellesLignes.add(ligne);
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Statut de la commande mis à jour: " + nouveauStatut);
        }
        
        return trouve;
    }
    
    /**
     * Marque une commande comme livrée
     */
    public boolean marquerLivree(int idCommande) {
        return mettreAJourStatut(idCommande, Commande.STATUT_LIVREE);
    }
    
    /**
     * Annule une commande
     */
    public boolean annuler(int idCommande) {
        return mettreAJourStatut(idCommande, Commande.STATUT_ANNULEE);
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
            System.out.println("✓ Commande supprimée avec succès!");
        }
        
        return trouve;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    private Commande extraireCommande(String[] ligne) {
        Commande commande = new Commande();
        commande.setId(Integer.parseInt(ligne[0]));
        commande.setIdGestionnaire(Integer.parseInt(ligne[1]));
        commande.setIdMedicament(Integer.parseInt(ligne[2]));
        commande.setQuantite(Integer.parseInt(ligne[3]));
        commande.setStatut(ligne[4]);
        
        try {
            commande.setDateCommande(DATE_FORMAT.parse(ligne[5]));
        } catch (ParseException e) {
            commande.setDateCommande(new Date());
        }
        
        return commande;
    }
}
