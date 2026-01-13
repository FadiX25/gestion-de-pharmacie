package models;

import java.util.Date;

/**
 * Classe Commande - Représente une commande de réapprovisionnement
 * 
 * Une commande est créée par un gestionnaire pour réapprovisionner
 * le stock d'un médicament.
 * 
 * @author Étudiant
 * @version 1.0
 */
public class Commande {
    
    // ========== ATTRIBUTS ==========
    
    private int id;             // Identifiant unique de la commande
    private int idGestionnaire; // ID du gestionnaire qui a passé la commande
    private int idMedicament;   // ID du médicament commandé
    private int quantite;       // Quantité commandée
    private Date dateCommande;  // Date de la commande
    private String statut;      // Statut: "EN_ATTENTE", "LIVREE", "ANNULEE"
    
    // Constantes pour les statuts possibles
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_LIVREE = "LIVREE";
    public static final String STATUT_ANNULEE = "ANNULEE";
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public Commande() {
        this.dateCommande = new Date();
        this.statut = STATUT_EN_ATTENTE;
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public Commande(int id, int idGestionnaire, int idMedicament, 
                    int quantite, Date dateCommande, String statut) {
        this.id = id;
        this.idGestionnaire = idGestionnaire;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.dateCommande = dateCommande;
        this.statut = statut;
    }
    
    /**
     * Constructeur sans id (pour création)
     */
    public Commande(int idGestionnaire, int idMedicament, int quantite) {
        this.idGestionnaire = idGestionnaire;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.dateCommande = new Date();
        this.statut = STATUT_EN_ATTENTE;
    }
    
    // ========== MÉTHODES ==========
    
    /**
     * Marque la commande comme livrée
     */
    public void marquerLivree() {
        this.statut = STATUT_LIVREE;
        System.out.println("Commande #" + id + " marquée comme livrée.");
    }
    
    /**
     * Annule la commande
     */
    public void annuler() {
        this.statut = STATUT_ANNULEE;
        System.out.println("Commande #" + id + " annulée.");
    }
    
    /**
     * Vérifie si la commande est en attente
     */
    public boolean estEnAttente() {
        return STATUT_EN_ATTENTE.equals(this.statut);
    }
    
    /**
     * Affiche les informations de la commande
     */
    public void afficherInfo() {
        System.out.println("========== COMMANDE ==========");
        System.out.println("ID: " + id);
        System.out.println("ID Gestionnaire: " + idGestionnaire);
        System.out.println("ID Médicament: " + idMedicament);
        System.out.println("Quantité: " + quantite);
        System.out.println("Date: " + dateCommande);
        System.out.println("Statut: " + statut);
        System.out.println("==============================");
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdGestionnaire() {
        return idGestionnaire;
    }
    
    public void setIdGestionnaire(int idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }
    
    public int getIdMedicament() {
        return idMedicament;
    }
    
    public void setIdMedicament(int idMedicament) {
        this.idMedicament = idMedicament;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public Date getDateCommande() {
        return dateCommande;
    }
    
    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", idGestionnaire=" + idGestionnaire +
                ", idMedicament=" + idMedicament +
                ", quantite=" + quantite +
                ", dateCommande=" + dateCommande +
                ", statut='" + statut + '\'' +
                '}';
    }
}
