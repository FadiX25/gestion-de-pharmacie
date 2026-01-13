package dao;

import database.CSVHelper;
import models.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe ClientDAO - Gère les opérations CRUD pour les Clients
 * 
 * Version CSV
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class ClientDAO {
    
    private static final String FICHIER = "clients.csv";
    private static final String[] ENTETES = {"id", "nom", "prenom", "email", "adresse"};
    
    public ClientDAO() {
        CSVHelper.creerFichierSiAbsent(FICHIER, ENTETES);
    }
    
    // ========== CREATE ==========
    
    public boolean ajouter(Client client) {
        int nouvelId = CSVHelper.getProchainId(FICHIER);
        client.setId(nouvelId);
        
        String[] donnees = {
            String.valueOf(client.getId()),
            client.getNom(),
            client.getPrenom(),
            client.getEmail() != null ? client.getEmail() : "",
            client.getAdresse() != null ? client.getAdresse() : ""
        };
        
        boolean succes = CSVHelper.ajouterLigne(FICHIER, donnees);
        
        if (succes) {
            System.out.println("✓ Client ajouté avec succès! ID: " + client.getId());
        }
        
        return succes;
    }
    
    // ========== READ ==========
    
    public Client trouverParId(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                return extraireClient(ligne);
            }
        }
        
        return null;
    }
    
    /**
     * Cherche des clients par nom ou prénom
     */
    public List<Client> rechercherParNom(String nom) {
        List<Client> resultats = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            if (ligne[1].toLowerCase().contains(nom.toLowerCase()) ||
                ligne[2].toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(extraireClient(ligne));
            }
        }
        
        return resultats;
    }
    
    public List<Client> trouverTous() {
        List<Client> clients = new ArrayList<>();
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        
        for (String[] ligne : lignes) {
            clients.add(extraireClient(ligne));
        }
        
        return clients;
    }
    
    // ========== UPDATE ==========
    
    public boolean mettreAJour(Client client) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == client.getId()) {
                nouvellesLignes.add(new String[]{
                    String.valueOf(client.getId()),
                    client.getNom(),
                    client.getPrenom(),
                    client.getEmail() != null ? client.getEmail() : "",
                    client.getAdresse() != null ? client.getAdresse() : ""
                });
                trouve = true;
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Client mis à jour avec succès!");
        }
        
        return trouve;
    }
    
    // ========== DELETE ==========
    
    public boolean supprimer(int id) {
        List<String[]> lignes = CSVHelper.lireFichier(FICHIER);
        List<String[]> nouvellesLignes = new ArrayList<>();
        boolean trouve = false;
        
        for (String[] ligne : lignes) {
            if (Integer.parseInt(ligne[0]) == id) {
                trouve = true;
            } else {
                nouvellesLignes.add(ligne);
            }
        }
        
        if (trouve) {
            CSVHelper.ecrireFichier(FICHIER, ENTETES, nouvellesLignes);
            System.out.println("✓ Client supprimé avec succès!");
        }
        
        return trouve;
    }
    
    // ========== MÉTHODE UTILITAIRE ==========
    
    private Client extraireClient(String[] ligne) {
        Client client = new Client();
        client.setId(Integer.parseInt(ligne[0]));
        client.setNom(ligne[1]);
        client.setPrenom(ligne[2]);
        client.setEmail(ligne.length > 3 ? ligne[3] : "");
        client.setAdresse(ligne.length > 4 ? ligne[4] : "");
        return client;
    }
}
