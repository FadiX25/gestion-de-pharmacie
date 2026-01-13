package models;

/**
 * Classe Medicament - Représente un médicament dans la pharmacie
 * 
 * Cette classe contient toutes les informations sur un médicament :
 * - Son identifiant unique
 * - Son nom
 * - Son dosage (ex: "500mg", "10ml")
 * - La quantité en stock
 * - Son prix unitaire
 * 
 * @author Étudiant
 * @version 1.0
 */
public class Medicament {
    
    // ========== ATTRIBUTS ==========
    
    private int id;             // Identifiant unique du médicament
    private String nom;         // Nom du médicament (ex: "Paracétamol")
    private String dosage;      // Dosage (ex: "500mg")
    private int stock;          // Quantité disponible en stock
    private double prixUnitaire; // Prix d'une unité
    
    // Constante pour le seuil de stock critique
    public static final int SEUIL_STOCK_CRITIQUE = 10;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public Medicament() {
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public Medicament(int id, String nom, String dosage, int stock, double prixUnitaire) {
        this.id = id;
        this.nom = nom;
        this.dosage = dosage;
        this.stock = stock;
        this.prixUnitaire = prixUnitaire;
    }
    
    /**
     * Constructeur sans id (pour création d'un nouveau médicament)
     */
    public Medicament(String nom, String dosage, int stock, double prixUnitaire) {
        this.nom = nom;
        this.dosage = dosage;
        this.stock = stock;
        this.prixUnitaire = prixUnitaire;
    }
    
    // ========== MÉTHODES MÉTIER ==========
    
    /**
     * Vérifie si le stock est suffisant pour une quantité demandée
     * 
     * @param quantiteDemandee La quantité souhaitée
     * @return true si le stock est suffisant, false sinon
     */
    public boolean verifierStock(int quantiteDemandee) {
        return this.stock >= quantiteDemandee;
    }
    
    /**
     * Vérifie si le médicament est en stock critique
     * 
     * @return true si le stock est inférieur au seuil critique
     */
    public boolean estStockCritique() {
        return this.stock < SEUIL_STOCK_CRITIQUE;
    }
    
    /**
     * Diminue le stock après une vente
     * 
     * @param quantite La quantité vendue
     * @return true si l'opération a réussi, false si stock insuffisant
     */
    public boolean diminuerStock(int quantite) {
        if (quantite <= 0) {
            System.out.println("Erreur: La quantité doit être positive.");
            return false;
        }
        
        if (verifierStock(quantite)) {
            this.stock -= quantite;
            System.out.println("Stock diminué de " + quantite + ". Nouveau stock: " + this.stock);
            return true;
        } else {
            System.out.println("Erreur: Stock insuffisant. Stock actuel: " + this.stock);
            return false;
        }
    }
    
    /**
     * Augmente le stock après un réapprovisionnement
     * 
     * @param quantite La quantité ajoutée
     * @return true si l'opération a réussi
     */
    public boolean augmenterStock(int quantite) {
        if (quantite <= 0) {
            System.out.println("Erreur: La quantité doit être positive.");
            return false;
        }
        
        this.stock += quantite;
        System.out.println("Stock augmenté de " + quantite + ". Nouveau stock: " + this.stock);
        return true;
    }
    
    /**
     * Calcule le prix total pour une quantité donnée
     * 
     * @param quantite La quantité
     * @return Le prix total
     */
    public double calculerPrixTotal(int quantite) {
        return this.prixUnitaire * quantite;
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dosage='" + dosage + '\'' +
                ", stock=" + stock +
                ", prixUnitaire=" + prixUnitaire +
                '}';
    }
    
    /**
     * Affiche les informations du médicament de manière formatée
     */
    public void afficherInfo() {
        System.out.println("========== MÉDICAMENT ==========");
        System.out.println("ID: " + id);
        System.out.println("Nom: " + nom);
        System.out.println("Dosage: " + dosage);
        System.out.println("Stock: " + stock + (estStockCritique() ? " (CRITIQUE!)" : ""));
        System.out.println("Prix unitaire: " + prixUnitaire + " €");
        System.out.println("=================================");
    }
}
