package models;

/**
 * Classe abstraite Utilisateur - Classe de base pour tous les utilisateurs
 * 
 * Cette classe représente un utilisateur générique du système.
 * Elle est abstraite car on ne crée jamais un "Utilisateur" directement,
 * mais toujours un Pharmacien ou un Gestionnaire.
 * 
 * @author Étudiant
 * @version 1.0
 */
public abstract class Utilisateur {
    
    // ========== ATTRIBUTS ==========
    // Ces attributs sont "protected" pour que les classes enfants puissent y accéder
    
    protected int id;           // Identifiant unique de l'utilisateur
    protected String nom;       // Nom de famille
    protected String prenom;    // Prénom
    protected String login;     // Nom d'utilisateur pour la connexion
    protected String password;  // Mot de passe (pwd dans le diagramme)
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut (vide)
     * Nécessaire pour certaines opérations
     */
    public Utilisateur() {
    }
    
    /**
     * Constructeur avec tous les paramètres
     * 
     * @param id       L'identifiant unique
     * @param nom      Le nom de famille
     * @param prenom   Le prénom
     * @param login    Le nom d'utilisateur
     * @param password Le mot de passe
     */
    public Utilisateur(int id, String nom, String prenom, String login, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
    }
    
    /**
     * Constructeur sans id (pour création d'un nouvel utilisateur)
     * L'id sera généré automatiquement par la base de données
     */
    public Utilisateur(String nom, String prenom, String login, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
    }
    
    // ========== MÉTHODES ABSTRAITES ==========
    // Ces méthodes DOIVENT être implémentées par les classes enfants
    
    /**
     * Retourne le rôle de l'utilisateur
     * Chaque type d'utilisateur doit définir son propre rôle
     */
    public abstract String getRole();
    
    // ========== MÉTHODES CONCRÈTES ==========
    
    /**
     * Méthode pour se connecter
     * Vérifie si le login et mot de passe correspondent
     * 
     * @param loginSaisi    Le login entré par l'utilisateur
     * @param passwordSaisi Le mot de passe entré
     * @return true si les identifiants sont corrects, false sinon
     */
    public boolean seConnecter(String loginSaisi, String passwordSaisi) {
        // On compare le login et le mot de passe
        return this.login.equals(loginSaisi) && this.password.equals(passwordSaisi);
    }
    
    /**
     * Méthode pour se déconnecter
     * Affiche un message de déconnexion
     */
    public void seDeconnecter() {
        System.out.println(prenom + " " + nom + " s'est déconnecté.");
    }
    
    /**
     * Affiche les informations du profil
     */
    public void consulterProfil() {
        System.out.println("========== PROFIL ==========");
        System.out.println("ID: " + id);
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
        System.out.println("Login: " + login);
        System.out.println("Rôle: " + getRole());
        System.out.println("=============================");
    }
    
    // ========== GETTERS ET SETTERS ==========
    // Ces méthodes permettent d'accéder aux attributs privés de manière contrôlée
    
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
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // ========== MÉTHODE toString ==========
    
    /**
     * Retourne une représentation textuelle de l'utilisateur
     * Utile pour le débogage et l'affichage
     */
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
