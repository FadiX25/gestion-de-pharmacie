package services;

import dao.PharmacienDAO;
import dao.GestionnaireDAO;
import models.Pharmacien;
import models.Gestionnaire;
import models.Utilisateur;

/**
 * Classe AuthenticationService - Gère l'authentification des utilisateurs
 * 
 * Cette classe centralise la logique de connexion/déconnexion
 * pour les pharmaciens et les gestionnaires.
 * 
 * @author Étudiant
 * @version 1.0
 */
public class AuthenticationService {
    
    // DAOs pour accéder aux données
    private PharmacienDAO pharmacienDAO;
    private GestionnaireDAO gestionnaireDAO;
    
    // L'utilisateur actuellement connecté
    private Utilisateur utilisateurConnecte;
    
    /**
     * Constructeur
     */
    public AuthenticationService() {
        this.pharmacienDAO = new PharmacienDAO();
        this.gestionnaireDAO = new GestionnaireDAO();
        this.utilisateurConnecte = null;
    }
    
    /**
     * Tente de connecter un utilisateur
     * 
     * @param login    Le login saisi
     * @param password Le mot de passe saisi
     * @return L'utilisateur connecté, ou null si échec
     */
    public Utilisateur seConnecter(String login, String password) {
        // Vérifier les entrées
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("✗ Veuillez saisir un login et un mot de passe.");
            return null;
        }
        
        // D'abord, essayer de trouver un pharmacien
        Pharmacien pharmacien = pharmacienDAO.authentifier(login, password);
        if (pharmacien != null) {
            this.utilisateurConnecte = pharmacien;
            System.out.println("Bienvenue " + pharmacien.getPrenom() + " " + pharmacien.getNom() + " (Pharmacien)");
            return pharmacien;
        }
        
        // Sinon, essayer de trouver un gestionnaire
        Gestionnaire gestionnaire = gestionnaireDAO.authentifier(login, password);
        if (gestionnaire != null) {
            this.utilisateurConnecte = gestionnaire;
            System.out.println("Bienvenue " + gestionnaire.getPrenom() + " " + gestionnaire.getNom() + " (Gestionnaire)");
            return gestionnaire;
        }
        
        // Aucun utilisateur trouvé
        System.out.println("✗ Login ou mot de passe incorrect.");
        return null;
    }
    
    /**
     * Déconnecte l'utilisateur actuel
     */
    public void seDeconnecter() {
        if (utilisateurConnecte != null) {
            System.out.println("Au revoir " + utilisateurConnecte.getPrenom() + " !");
            utilisateurConnecte.seDeconnecter();
            utilisateurConnecte = null;
        } else {
            System.out.println("Aucun utilisateur n'est connecté.");
        }
    }
    
    /**
     * Vérifie si un utilisateur est connecté
     * 
     * @return true si quelqu'un est connecté
     */
    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }
    
    /**
     * Retourne l'utilisateur actuellement connecté
     * 
     * @return L'utilisateur connecté, ou null
     */
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un pharmacien
     */
    public boolean estPharmacien() {
        return utilisateurConnecte != null && utilisateurConnecte instanceof Pharmacien;
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un gestionnaire
     */
    public boolean estGestionnaire() {
        return utilisateurConnecte != null && utilisateurConnecte instanceof Gestionnaire;
    }
    
    /**
     * Retourne le pharmacien connecté (si c'en est un)
     */
    public Pharmacien getPharmacienConnecte() {
        if (estPharmacien()) {
            return (Pharmacien) utilisateurConnecte;
        }
        return null;
    }
    
    /**
     * Retourne le gestionnaire connecté (si c'en est un)
     */
    public Gestionnaire getGestionnaireConnecte() {
        if (estGestionnaire()) {
            return (Gestionnaire) utilisateurConnecte;
        }
        return null;
    }
    
    /**
     * Affiche le profil de l'utilisateur connecté
     */
    public void afficherProfil() {
        if (utilisateurConnecte != null) {
            utilisateurConnecte.consulterProfil();
        } else {
            System.out.println("Aucun utilisateur n'est connecté.");
        }
    }
}
