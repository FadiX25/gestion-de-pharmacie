package services;

import dao.CommandeDAO;
import dao.MedicamentDAO;
import models.Commande;
import models.Medicament;

import java.util.List;

/**
 * Classe CommandeService - Logique métier pour la gestion des commandes
 * 
 * Cette classe gère:
 * - La création de commandes de réapprovisionnement
 * - Le suivi des commandes
 * - La réception des commandes (mise à jour du stock)
 * 
 * @author Étudiant
 * @version 1.0
 */
public class CommandeService {
    
    private CommandeDAO commandeDAO;
    private MedicamentDAO medicamentDAO;
    private MedicamentService medicamentService;
    
    public CommandeService() {
        this.commandeDAO = new CommandeDAO();
        this.medicamentDAO = new MedicamentDAO();
        this.medicamentService = new MedicamentService();
    }
    
    /**
     * Crée une nouvelle commande de réapprovisionnement
     */
    public boolean creerCommande(int idGestionnaire, int idMedicament, int quantite) {
        // Validation
        if (quantite <= 0) {
            System.out.println("✗ La quantité doit être positive.");
            return false;
        }
        
        // Vérifier que le médicament existe
        Medicament medicament = medicamentDAO.trouverParId(idMedicament);
        if (medicament == null) {
            System.out.println("✗ Médicament non trouvé.");
            return false;
        }
        
        // Créer la commande
        Commande commande = new Commande(idGestionnaire, idMedicament, quantite);
        
        boolean succes = commandeDAO.ajouter(commande);
        
        if (succes) {
            System.out.println("\n========== COMMANDE CRÉÉE ==========");
            System.out.println("Commande #" + commande.getId());
            System.out.println("Médicament: " + medicament.getNom());
            System.out.println("Quantité commandée: " + quantite);
            System.out.println("Statut: EN ATTENTE");
            System.out.println("=====================================\n");
        }
        
        return succes;
    }
    
    /**
     * Marque une commande comme livrée et met à jour le stock
     */
    public boolean recevoirCommande(int idCommande) {
        // Récupérer la commande
        Commande commande = commandeDAO.trouverParId(idCommande);
        if (commande == null) {
            System.out.println("✗ Commande non trouvée.");
            return false;
        }
        
        // Vérifier que la commande est en attente
        if (!commande.estEnAttente()) {
            System.out.println("✗ Cette commande n'est plus en attente.");
            return false;
        }
        
        // Marquer comme livrée
        boolean statutMisAJour = commandeDAO.marquerLivree(idCommande);
        
        if (!statutMisAJour) {
            System.out.println("✗ Erreur lors de la mise à jour du statut.");
            return false;
        }
        
        // Augmenter le stock
        boolean stockMisAJour = medicamentService.augmenterStock(
            commande.getIdMedicament(), 
            commande.getQuantite()
        );
        
        if (stockMisAJour) {
            Medicament med = medicamentDAO.trouverParId(commande.getIdMedicament());
            System.out.println("\n========== COMMANDE REÇUE ==========");
            System.out.println("Commande #" + idCommande + " reçue!");
            System.out.println("Médicament: " + (med != null ? med.getNom() : "Inconnu"));
            System.out.println("Quantité ajoutée au stock: " + commande.getQuantite());
            System.out.println("=====================================\n");
        }
        
        return stockMisAJour;
    }
    
    /**
     * Annule une commande
     */
    public boolean annulerCommande(int idCommande) {
        Commande commande = commandeDAO.trouverParId(idCommande);
        if (commande == null) {
            System.out.println("✗ Commande non trouvée.");
            return false;
        }
        
        if (!commande.estEnAttente()) {
            System.out.println("✗ Seules les commandes en attente peuvent être annulées.");
            return false;
        }
        
        return commandeDAO.annuler(idCommande);
    }
    
    /**
     * Récupère une commande par son ID
     */
    public Commande getCommande(int idCommande) {
        return commandeDAO.trouverParId(idCommande);
    }
    
    /**
     * Récupère toutes les commandes
     */
    public List<Commande> getToutesCommandes() {
        return commandeDAO.trouverTous();
    }
    
    /**
     * Récupère les commandes en attente
     */
    public List<Commande> getCommandesEnAttente() {
        return commandeDAO.trouverCommandesEnAttente();
    }
    
    /**
     * Affiche toutes les commandes
     */
    public void afficherToutesCommandes() {
        List<Commande> commandes = getToutesCommandes();
        
        System.out.println("\n========== LISTE DES COMMANDES ==========");
        System.out.println("------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %-12s %-15s%n", "ID", "Médicament", "Quantité", "Statut", "Date");
        System.out.println("------------------------------------------");
        
        for (Commande cmd : commandes) {
            Medicament med = medicamentDAO.trouverParId(cmd.getIdMedicament());
            String nomMed = med != null ? med.getNom() : "Inconnu";
            
            System.out.printf("%-5d %-20s %-10d %-12s %-15s%n", 
                cmd.getId(), 
                nomMed,
                cmd.getQuantite(),
                cmd.getStatut(),
                cmd.getDateCommande().toString().substring(0, 10)
            );
        }
        
        System.out.println("------------------------------------------");
        System.out.println("Total: " + commandes.size() + " commandes");
        System.out.println("==========================================\n");
    }
    
    /**
     * Affiche les commandes en attente
     */
    public void afficherCommandesEnAttente() {
        List<Commande> commandes = getCommandesEnAttente();
        
        System.out.println("\n========== COMMANDES EN ATTENTE ==========");
        System.out.println("-------------------------------------------");
        
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande en attente.");
        } else {
            System.out.printf("%-5s %-25s %-10s %-15s%n", "ID", "Médicament", "Quantité", "Date");
            System.out.println("-------------------------------------------");
            
            for (Commande cmd : commandes) {
                Medicament med = medicamentDAO.trouverParId(cmd.getIdMedicament());
                String nomMed = med != null ? med.getNom() : "Inconnu";
                
                System.out.printf("%-5d %-25s %-10d %-15s%n", 
                    cmd.getId(), 
                    nomMed,
                    cmd.getQuantite(),
                    cmd.getDateCommande().toString().substring(0, 10)
                );
            }
        }
        
        System.out.println("-------------------------------------------");
        System.out.println("Total en attente: " + commandes.size());
        System.out.println("===========================================\n");
    }
}
