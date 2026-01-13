package services;

import dao.ClientDAO;
import models.Client;

import java.util.List;

/**
 * Classe ClientService - Logique métier pour la gestion des clients
 * 
 * @author Étudiant
 * @version 1.0
 */
public class ClientService {
    
    private ClientDAO clientDAO;
    
    public ClientService() {
        this.clientDAO = new ClientDAO();
    }
    
    /**
     * Ajoute un nouveau client
     */
    public boolean ajouterClient(String nom, String prenom, String email, String adresse) {
        // Validation
        if (nom == null || nom.isEmpty() || prenom == null || prenom.isEmpty()) {
            System.out.println("✗ Le nom et le prénom sont obligatoires.");
            return false;
        }
        
        Client client = new Client(nom, prenom, email, adresse);
        return clientDAO.ajouter(client);
    }
    
    /**
     * Recherche un client par son nom
     */
    public List<Client> rechercherClient(String nom) {
        return clientDAO.rechercherParNom(nom);
    }
    
    /**
     * Récupère un client par son ID
     */
    public Client getClient(int id) {
        return clientDAO.trouverParId(id);
    }
    
    /**
     * Récupère tous les clients
     */
    public List<Client> getTousClients() {
        return clientDAO.trouverTous();
    }
    
    /**
     * Met à jour un client
     */
    public boolean mettreAJourClient(Client client) {
        return clientDAO.mettreAJour(client);
    }
    
    /**
     * Supprime un client
     */
    public boolean supprimerClient(int id) {
        return clientDAO.supprimer(id);
    }
    
    /**
     * Affiche tous les clients
     */
    public void afficherTousClients() {
        List<Client> clients = getTousClients();
        
        System.out.println("\n========== LISTE DES CLIENTS ==========");
        System.out.println("----------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-25s%n", "ID", "Nom", "Prénom", "Email");
        System.out.println("----------------------------------------");
        
        for (Client client : clients) {
            System.out.printf("%-5d %-15s %-15s %-25s%n", 
                client.getId(), 
                client.getNom(),
                client.getPrenom(),
                client.getEmail() != null ? client.getEmail() : "-"
            );
        }
        
        System.out.println("----------------------------------------");
        System.out.println("Total: " + clients.size() + " clients");
        System.out.println("========================================\n");
    }
}
