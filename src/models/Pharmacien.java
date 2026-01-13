package models;

/**
 * Classe Pharmacien - Représente un pharmacien dans le système
 * 
 * Le pharmacien peut :
 * - Enregistrer des ventes
 * - Consulter les ventes
 * - Gérer le stock
 * - Annuler des ventes
 * 
 * Cette classe HÉRITE de Utilisateur (mot-clé "extends")
 * 
 * @author Étudiant
 * @version 1.0
 */
public class Pharmacien extends Utilisateur {
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public Pharmacien() {
        super(); // Appelle le constructeur de la classe parent (Utilisateur)
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public Pharmacien(int id, String nom, String prenom, String login, String password) {
        super(id, nom, prenom, login, password); // Appelle le constructeur de Utilisateur
    }
    
    /**
     * Constructeur sans id (pour création)
     */
    public Pharmacien(String nom, String prenom, String login, String password) {
        super(nom, prenom, login, password);
    }
    
    // ========== IMPLÉMENTATION DE LA MÉTHODE ABSTRAITE ==========
    
    /**
     * Retourne le rôle du pharmacien
     * Cette méthode est OBLIGATOIRE car elle est abstraite dans Utilisateur
     */
    @Override
    public String getRole() {
        return "Pharmacien";
    }
    
    // ========== MÉTHODES SPÉCIFIQUES AU PHARMACIEN ==========
    
    /**
     * Affiche un message indiquant que le pharmacien gère le stock
     * La vraie logique sera dans la couche Service
     */
    public void gererStock() {
        System.out.println(prenom + " " + nom + " gère le stock de médicaments.");
    }
    
    /**
     * Affiche un message pour l'enregistrement d'une vente
     * La vraie logique sera dans VenteService
     */
    public void enregistrerVente() {
        System.out.println(prenom + " " + nom + " enregistre une vente.");
    }
    
    /**
     * Affiche un message pour la consultation des ventes
     */
    public void consulterVentes() {
        System.out.println(prenom + " " + nom + " consulte les ventes.");
    }
    
    /**
     * Affiche un message pour l'annulation d'une vente
     */
    public void annulerVente() {
        System.out.println(prenom + " " + nom + " annule une vente.");
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Pharmacien{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
