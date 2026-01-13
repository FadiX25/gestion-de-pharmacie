package models;

import java.util.Date;

/**
 * Classe Vente - Représente une vente de médicament
 * 
 * Une vente lie :
 * - Un pharmacien (qui fait la vente)
 * - Un client (qui achète)
 * - Un médicament (ce qui est vendu)
 * - Une quantité
 * - Une date
 * 
 * @author Étudiant
 * @version 1.0
 */
public class Vente {
    
    // ========== ATTRIBUTS ==========
    
    private int id;             // Identifiant unique de la vente
    private int idPharmacien;   // ID du pharmacien qui a fait la vente
    private int idClient;       // ID du client
    private int idMedicament;   // ID du médicament vendu
    private int quantite;       // Quantité vendue
    private Date dateVente;     // Date de la vente
    private double montantTotal; // Montant total de la vente
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public Vente() {
        this.dateVente = new Date(); // Date actuelle par défaut
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public Vente(int id, int idPharmacien, int idClient, int idMedicament, 
                 int quantite, Date dateVente, double montantTotal) {
        this.id = id;
        this.idPharmacien = idPharmacien;
        this.idClient = idClient;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.dateVente = dateVente;
        this.montantTotal = montantTotal;
    }
    
    /**
     * Constructeur sans id (pour création)
     */
    public Vente(int idPharmacien, int idClient, int idMedicament, 
                 int quantite, double montantTotal) {
        this.idPharmacien = idPharmacien;
        this.idClient = idClient;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.dateVente = new Date(); // Date actuelle
        this.montantTotal = montantTotal;
    }
    
    // ========== MÉTHODES ==========
    
    /**
     * Calcule le montant total en fonction du prix unitaire
     * 
     * @param prixUnitaire Le prix d'une unité
     */
    public void calculerMontant(double prixUnitaire) {
        this.montantTotal = prixUnitaire * this.quantite;
    }
    
    /**
     * Affiche les détails de la vente
     */
    public void afficherInfo() {
        System.out.println("========== VENTE ==========");
        System.out.println("ID Vente: " + id);
        System.out.println("ID Pharmacien: " + idPharmacien);
        System.out.println("ID Client: " + idClient);
        System.out.println("ID Médicament: " + idMedicament);
        System.out.println("Quantité: " + quantite);
        System.out.println("Date: " + dateVente);
        System.out.println("Montant Total: " + montantTotal + " DT");
        System.out.println("===========================");
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdPharmacien() {
        return idPharmacien;
    }
    
    public void setIdPharmacien(int idPharmacien) {
        this.idPharmacien = idPharmacien;
    }
    
    public int getIdClient() {
        return idClient;
    }
    
    public void setIdClient(int idClient) {
        this.idClient = idClient;
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
    
    public Date getDateVente() {
        return dateVente;
    }
    
    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }
    
    public double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Vente{" +
                "id=" + id +
                ", idPharmacien=" + idPharmacien +
                ", idClient=" + idClient +
                ", idMedicament=" + idMedicament +
                ", quantite=" + quantite +
                ", dateVente=" + dateVente +
                ", montantTotal=" + montantTotal +
                '}';
    }
}
