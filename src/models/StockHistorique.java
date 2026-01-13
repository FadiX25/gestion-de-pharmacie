package models;

import java.util.Date;

/**
 * Classe StockHistorique - Enregistre l'historique des mouvements de stock
 * 
 * Cette classe garde une trace de toutes les opérations sur le stock :
 * - Les ventes (diminution)
 * - Les réapprovisionnements (augmentation)
 * - Les ajustements manuels
 * 
 * @author Étudiant
 * @version 1.0
 */
public class StockHistorique {
    
    // ========== ATTRIBUTS ==========
    
    private int id;             // Identifiant unique de l'entrée
    private int idMedicament;   // ID du médicament concerné
    private int quantite;       // Quantité du mouvement (+ ou -)
    private Date dateOperation; // Date de l'opération
    private String typeOperation; // Type: "VENTE", "REAPPROVISIONNEMENT", "AJUSTEMENT"
    
    // Constantes pour les types d'opérations
    public static final String TYPE_VENTE = "VENTE";
    public static final String TYPE_REAPPROVISIONNEMENT = "REAPPROVISIONNEMENT";
    public static final String TYPE_AJUSTEMENT = "AJUSTEMENT";
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public StockHistorique() {
        this.dateOperation = new Date();
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public StockHistorique(int id, int idMedicament, int quantite, 
                           Date dateOperation, String typeOperation) {
        this.id = id;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.dateOperation = dateOperation;
        this.typeOperation = typeOperation;
    }
    
    /**
     * Constructeur sans id (pour création)
     */
    public StockHistorique(int idMedicament, int quantite, String typeOperation) {
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.dateOperation = new Date();
        this.typeOperation = typeOperation;
    }
    
    // ========== MÉTHODES ==========
    
    /**
     * Affiche les informations de l'historique
     */
    public void afficherInfo() {
        System.out.println("========== HISTORIQUE STOCK ==========");
        System.out.println("ID: " + id);
        System.out.println("ID Médicament: " + idMedicament);
        System.out.println("Quantité: " + quantite);
        System.out.println("Date: " + dateOperation);
        System.out.println("Type: " + typeOperation);
        System.out.println("=======================================");
    }
    
    /**
     * Vérifie si c'est une opération de vente
     */
    public boolean estVente() {
        return TYPE_VENTE.equals(this.typeOperation);
    }
    
    /**
     * Vérifie si c'est un réapprovisionnement
     */
    public boolean estReapprovisionnement() {
        return TYPE_REAPPROVISIONNEMENT.equals(this.typeOperation);
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    public Date getDateOperation() {
        return dateOperation;
    }
    
    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }
    
    public String getTypeOperation() {
        return typeOperation;
    }
    
    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "StockHistorique{" +
                "id=" + id +
                ", idMedicament=" + idMedicament +
                ", quantite=" + quantite +
                ", dateOperation=" + dateOperation +
                ", typeOperation='" + typeOperation + '\'' +
                '}';
    }
}
