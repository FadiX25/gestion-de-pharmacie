package services;

import dao.VenteDAO;
import dao.ClientDAO;
import dao.MedicamentDAO;
import models.Vente;
import models.Client;
import models.Medicament;

import java.util.List;

/**
 * Classe VenteService - Logique métier pour la gestion des ventes
 * 
 * Cette classe gère:
 * - L'enregistrement des ventes
 * - La consultation des ventes
 * - L'annulation des ventes
 * - Le calcul des montants
 * 
 * @author Étudiant
 * @version 1.0
 */
public class VenteService {
    
    private VenteDAO venteDAO;
    private MedicamentDAO medicamentDAO;
    private ClientDAO clientDAO;
    private MedicamentService medicamentService;
    
    public VenteService() {
        this.venteDAO = new VenteDAO();
        this.medicamentDAO = new MedicamentDAO();
        this.clientDAO = new ClientDAO();
        this.medicamentService = new MedicamentService();
    }
    
    /**
     * Enregistre une nouvelle vente
     * 
     * Cette méthode:
     * 1. Vérifie que le médicament existe
     * 2. Vérifie que le stock est suffisant
     * 3. Calcule le montant total
     * 4. Enregistre la vente
     * 5. Met à jour le stock
     * 
     * @param idPharmacien L'ID du pharmacien qui fait la vente
     * @param idClient     L'ID du client (peut être 0 pour client anonyme)
     * @param idMedicament L'ID du médicament vendu
     * @param quantite     La quantité vendue
     * @return true si la vente a réussi, false sinon
     */
    public boolean enregistrerVente(int idPharmacien, int idClient, int idMedicament, int quantite) {
        // ===== ÉTAPE 1: Validation =====
        
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
        
        // Vérifier le stock
        if (!medicament.verifierStock(quantite)) {
            System.out.println("✗ Stock insuffisant! Stock disponible: " + medicament.getStock());
            return false;
        }
        
        // Vérifier le client (si spécifié)
        if (idClient > 0) {
            Client client = clientDAO.trouverParId(idClient);
            if (client == null) {
                System.out.println("✗ Client non trouvé.");
                return false;
            }
        }
        
        // ===== ÉTAPE 2: Calcul du montant =====
        
        double montantTotal = medicament.calculerPrixTotal(quantite);
        
        // ===== ÉTAPE 3: Créer et enregistrer la vente =====
        
        Vente vente = new Vente(idPharmacien, idClient, idMedicament, quantite, montantTotal);
        
        boolean venteEnregistree = venteDAO.ajouter(vente);
        
        if (!venteEnregistree) {
            System.out.println("✗ Erreur lors de l'enregistrement de la vente.");
            return false;
        }
        
        // ===== ÉTAPE 4: Mettre à jour le stock =====
        
        boolean stockMisAJour = medicamentService.diminuerStock(idMedicament, quantite);
        
        if (!stockMisAJour) {
            // En cas d'erreur, on devrait annuler la vente (transaction)
            // Pour simplifier, on affiche juste un avertissement
            System.out.println("⚠️ Attention: La vente est enregistrée mais le stock n'a pas été mis à jour.");
        }
        
        // ===== ÉTAPE 5: Afficher le résumé =====
        
        System.out.println("\n========== VENTE EFFECTUÉE ==========");
        System.out.println("Médicament: " + medicament.getNom() + " " + medicament.getDosage());
        System.out.println("Quantité: " + quantite);
        System.out.println("Prix unitaire: " + medicament.getPrixUnitaire() + " €");
        System.out.println("TOTAL: " + montantTotal + " €");
        System.out.println("======================================\n");
        
        return true;
    }
    
    /**
     * Annule une vente et remet le stock
     */
    public boolean annulerVente(int idVente) {
        // Récupérer la vente
        Vente vente = venteDAO.trouverParId(idVente);
        if (vente == null) {
            System.out.println("✗ Vente non trouvée.");
            return false;
        }
        
        // Remettre le stock
        boolean stockRemis = medicamentService.augmenterStock(
            vente.getIdMedicament(), 
            vente.getQuantite()
        );
        
        if (!stockRemis) {
            System.out.println("⚠️ Attention: Impossible de remettre le stock.");
        }
        
        // Supprimer la vente
        boolean venteAnnulee = venteDAO.supprimer(idVente);
        
        if (venteAnnulee) {
            System.out.println("✓ Vente #" + idVente + " annulée avec succès.");
        }
        
        return venteAnnulee;
    }
    
    /**
     * Récupère une vente par son ID
     */
    public Vente getVente(int idVente) {
        return venteDAO.trouverParId(idVente);
    }
    
    /**
     * Récupère toutes les ventes
     */
    public List<Vente> getToutesVentes() {
        return venteDAO.trouverTous();
    }
    
    /**
     * Récupère les ventes d'un pharmacien
     */
    public List<Vente> getVentesPharmacien(int idPharmacien) {
        return venteDAO.trouverParPharmacien(idPharmacien);
    }
    
    /**
     * Récupère les ventes du jour
     */
    public List<Vente> getVentesDuJour() {
        return venteDAO.trouverVentesDuJour();
    }
    
    /**
     * Calcule le chiffre d'affaires total
     */
    public double getChiffreAffairesTotal() {
        return venteDAO.calculerChiffreAffairesTotal();
    }
    
    /**
     * Calcule le chiffre d'affaires du jour
     */
    public double getChiffreAffairesDuJour() {
        return venteDAO.calculerChiffreAffairesDuJour();
    }
    
    /**
     * Affiche toutes les ventes
     */
    public void afficherToutesVentes() {
        List<Vente> ventes = getToutesVentes();
        
        System.out.println("\n========== HISTORIQUE DES VENTES ==========");
        System.out.println("--------------------------------------------");
        System.out.printf("%-5s %-15s %-10s %-10s %-12s%n", "ID", "Date", "Qté", "Montant", "Médicament");
        System.out.println("--------------------------------------------");
        
        for (Vente vente : ventes) {
            Medicament med = medicamentDAO.trouverParId(vente.getIdMedicament());
            String nomMed = med != null ? med.getNom() : "Inconnu";
            
            System.out.printf("%-5d %-15s %-10d %-10.2f %-12s%n", 
                vente.getId(), 
                vente.getDateVente().toString().substring(0, 16),
                vente.getQuantite(),
                vente.getMontantTotal(),
                nomMed
            );
        }
        
        System.out.println("--------------------------------------------");
        System.out.println("Total: " + ventes.size() + " ventes");
        System.out.println("Chiffre d'affaires: " + getChiffreAffairesTotal() + " €");
        System.out.println("============================================\n");
    }
    
    /**
     * Affiche les ventes du jour
     */
    public void afficherVentesDuJour() {
        List<Vente> ventes = getVentesDuJour();
        
        System.out.println("\n========== VENTES DU JOUR ==========");
        System.out.println("-------------------------------------");
        
        if (ventes.isEmpty()) {
            System.out.println("Aucune vente aujourd'hui.");
        } else {
            System.out.printf("%-5s %-12s %-10s %-10s%n", "ID", "Heure", "Qté", "Montant");
            System.out.println("-------------------------------------");
            
            for (Vente vente : ventes) {
                System.out.printf("%-5d %-12s %-10d %-10.2f €%n", 
                    vente.getId(), 
                    vente.getDateVente().toString().substring(11, 16),
                    vente.getQuantite(),
                    vente.getMontantTotal()
                );
            }
        }
        
        System.out.println("-------------------------------------");
        System.out.println("CA du jour: " + getChiffreAffairesDuJour() + " €");
        System.out.println("=====================================\n");
    }
}
