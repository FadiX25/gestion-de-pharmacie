package models;

/**
 * Classe Client - Représente un client de la pharmacie
 * 
 * Cette classe stocke les informations des clients :
 * - Identifiant unique
 * - Nom et prénom
 * - Email pour les notifications
 * - Adresse pour les livraisons
 * 
 * @author Étudiant
 * @version 1.0
 */
public class Client {
    
    // ========== ATTRIBUTS ==========
    
    private int id;         // Identifiant unique du client
    private String nom;     // Nom de famille
    private String prenom;  // Prénom
    private String email;   // Adresse email
    private String adresse; // Adresse postale
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut
     */
    public Client() {
    }
    
    /**
     * Constructeur avec tous les paramètres
     */
    public Client(int id, String nom, String prenom, String email, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
    }
    
    /**
     * Constructeur sans id (pour création)
     */
    public Client(String nom, String prenom, String email, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
    }
    
    // ========== MÉTHODES ==========
    
    /**
     * Retourne le nom complet du client
     * 
     * @return Le prénom suivi du nom
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    /**
     * Affiche les informations du client
     */
    public void afficherInfo() {
        System.out.println("========== CLIENT ==========");
        System.out.println("ID: " + id);
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
        System.out.println("Email: " + email);
        System.out.println("Adresse: " + adresse);
        System.out.println("============================");
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
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
