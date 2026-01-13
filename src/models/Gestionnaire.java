package models;

/**
 * Classe Gestionnaire - Représente un gestionnaire/manager dans le système
 * 
 * Le gestionnaire peut :
 * - Faire tout ce qu'un pharmacien peut faire
 * - Gérer les commandes de réapprovisionnement
 * - Générer des rapports
 * - Envoyer des notifications
 * - Consulter le stock critique
 * - Ajouter des médicaments
 * 
 * Cette classe HÉRITE de Utilisateur
 * 
 * @author Étudiant
 * @version 1.0
 */
public class Gestionnaire extends Utilisateur {
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public Gestionnaire() {
        super();
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public Gestionnaire(int id, String nom, String prenom, String login, String password) {
        super(id, nom, prenom, login, password);
    }
    
    /**
     * Constructeur sans id (pour création)
     */
    public Gestionnaire(String nom, String prenom, String login, String password) {
        super(nom, prenom, login, password);
    }
    
    // ========== IMPLÉMENTATION DE LA MÉTHODE ABSTRAITE ==========
    
    @Override
    public String getRole() {
        return "Gestionnaire";
    }
    
    // ========== MÉTHODES SPÉCIFIQUES AU GESTIONNAIRE ==========
    
    /**
     * Gérer les commandes de réapprovisionnement
     */
    public void gererCommandes() {
        System.out.println(prenom + " " + nom + " gère les commandes de réapprovisionnement.");
    }
    
    /**
     * Générer des rapports statistiques
     */
    public void genererRapports() {
        System.out.println(prenom + " " + nom + " génère les rapports.");
    }
    
    /**
     * Envoyer des notifications (ex: stock faible)
     */
    public void envoyerNotification(String message) {
        System.out.println("NOTIFICATION de " + prenom + " " + nom + ": " + message);
    }
    
    /**
     * Ajouter un nouveau médicament au catalogue
     */
    public void ajouterMedicament() {
        System.out.println(prenom + " " + nom + " ajoute un médicament au catalogue.");
    }
    
    /**
     * Consulter les médicaments en stock critique
     */
    public void consulterStockCritique() {
        System.out.println(prenom + " " + nom + " consulte le stock critique.");
    }
    
    /**
     * Consulter l'historique des opérations
     */
    public void consulterHistorique() {
        System.out.println(prenom + " " + nom + " consulte l'historique.");
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Gestionnaire{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
