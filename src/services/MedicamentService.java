package services;

import dao.MedicamentDAO;
import dao.StockHistoriqueDAO;
import models.Medicament;
import models.StockHistorique;

import java.util.List;

/**
 * Classe MedicamentService - Logique métier pour la gestion des médicaments
 * 
 * Cette classe gère:
 * - L'ajout et la modification de médicaments
 * - La gestion du stock (augmentation/diminution)
 * - La détection des stocks critiques
 * - L'historique des mouvements
 * 
 * @author Étudiant
 * @version 1.0
 */
public class MedicamentService {
    
    private MedicamentDAO medicamentDAO;
    private StockHistoriqueDAO stockHistoriqueDAO;
    
    public MedicamentService() {
        this.medicamentDAO = new MedicamentDAO();
        this.stockHistoriqueDAO = new StockHistoriqueDAO();
    }
    
    // ========== GESTION DU CATALOGUE ==========
    
    /**
     * Ajoute un nouveau médicament au catalogue
     */
    public boolean ajouterMedicament(String nom, String dosage, int stockInitial, double prixUnitaire) {
        // Validation des entrées
        if (nom == null || nom.isEmpty()) {
            System.out.println("✗ Le nom du médicament est obligatoire.");
            return false;
        }
        
        if (prixUnitaire <= 0) {
            System.out.println("✗ Le prix doit être positif.");
            return false;
        }
        
        if (stockInitial < 0) {
            System.out.println("✗ Le stock ne peut pas être négatif.");
            return false;
        }
        
        // Créer le médicament
        Medicament medicament = new Medicament(nom, dosage, stockInitial, prixUnitaire);
        
        // L'ajouter à la base de données
        boolean succes = medicamentDAO.ajouter(medicament);
        
        // Si succès et stock initial > 0, enregistrer dans l'historique
        if (succes && stockInitial > 0) {
            stockHistoriqueDAO.enregistrerReapprovisionnement(medicament.getId(), stockInitial);
        }
        
        return succes;
    }
    
    /**
     * Recherche des médicaments par nom
     */
    public List<Medicament> rechercherMedicament(String nom) {
        return medicamentDAO.rechercherParNom(nom);
    }
    
    /**
     * Récupère un médicament par son ID
     */
    public Medicament getMedicament(int id) {
        return medicamentDAO.trouverParId(id);
    }
    
    /**
     * Récupère tous les médicaments
     */
    public List<Medicament> getTousMedicaments() {
        return medicamentDAO.trouverTous();
    }
    
    /**
     * Met à jour les informations d'un médicament
     */
    public boolean mettreAJourMedicament(Medicament medicament) {
        return medicamentDAO.mettreAJour(medicament);
    }
    
    // ========== GESTION DU STOCK ==========
    
    /**
     * Vérifie si le stock est suffisant pour une quantité demandée
     */
    public boolean verifierStock(int idMedicament, int quantiteDemandee) {
        Medicament med = medicamentDAO.trouverParId(idMedicament);
        if (med == null) {
            return false;
        }
        return med.verifierStock(quantiteDemandee);
    }
    
    /**
     * Diminue le stock (pour une vente)
     * Enregistre le mouvement dans l'historique
     */
    public boolean diminuerStock(int idMedicament, int quantite) {
        // Vérifier que le stock est suffisant
        if (!verifierStock(idMedicament, quantite)) {
            System.out.println("✗ Stock insuffisant pour ce médicament.");
            return false;
        }
        
        // Diminuer le stock
        boolean succes = medicamentDAO.diminuerStock(idMedicament, quantite);
        
        // Enregistrer dans l'historique
        if (succes) {
            stockHistoriqueDAO.enregistrerVente(idMedicament, quantite);
            
            // Vérifier si le stock est maintenant critique
            Medicament med = medicamentDAO.trouverParId(idMedicament);
            if (med != null && med.estStockCritique()) {
                System.out.println("⚠️ ATTENTION: Le stock de " + med.getNom() + " est critique! (" + med.getStock() + " unités)");
            }
        }
        
        return succes;
    }
    
    /**
     * Augmente le stock (pour un réapprovisionnement)
     * Enregistre le mouvement dans l'historique
     */
    public boolean augmenterStock(int idMedicament, int quantite) {
        if (quantite <= 0) {
            System.out.println("✗ La quantité doit être positive.");
            return false;
        }
        
        boolean succes = medicamentDAO.augmenterStock(idMedicament, quantite);
        
        if (succes) {
            stockHistoriqueDAO.enregistrerReapprovisionnement(idMedicament, quantite);
        }
        
        return succes;
    }
    
    /**
     * Récupère les médicaments en stock critique
     */
    public List<Medicament> getMedicamentsStockCritique() {
        return medicamentDAO.trouverStockCritique();
    }
    
    /**
     * Affiche les médicaments en stock critique
     */
    public void afficherStockCritique() {
        List<Medicament> critiques = getMedicamentsStockCritique();
        
        if (critiques.isEmpty()) {
            System.out.println("✓ Aucun médicament en stock critique.");
            return;
        }
        
        System.out.println("\n⚠️ ========== MÉDICAMENTS EN STOCK CRITIQUE ========== ⚠️");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-5s %-25s %-10s %-10s%n", "ID", "Nom", "Dosage", "Stock");
        System.out.println("-------------------------------------------------------");
        
        for (Medicament med : critiques) {
            System.out.printf("%-5d %-25s %-10s %-10d%n", 
                med.getId(), 
                med.getNom(), 
                med.getDosage(), 
                med.getStock()
            );
        }
        
        System.out.println("-------------------------------------------------------");
        System.out.println("Nombre de médicaments en stock critique: " + critiques.size());
        System.out.println("=======================================================\n");
    }
    
    // ========== HISTORIQUE ==========
    
    /**
     * Récupère l'historique des mouvements d'un médicament
     */
    public List<StockHistorique> getHistoriqueMedicament(int idMedicament) {
        return stockHistoriqueDAO.trouverParMedicament(idMedicament);
    }
    
    /**
     * Affiche l'historique d'un médicament
     */
    public void afficherHistoriqueMedicament(int idMedicament) {
        Medicament med = medicamentDAO.trouverParId(idMedicament);
        if (med == null) {
            System.out.println("✗ Médicament non trouvé.");
            return;
        }
        
        List<StockHistorique> historiques = getHistoriqueMedicament(idMedicament);
        
        System.out.println("\n========== HISTORIQUE: " + med.getNom() + " ==========");
        System.out.println("Stock actuel: " + med.getStock() + " unités");
        System.out.println("----------------------------------------------------");
        
        if (historiques.isEmpty()) {
            System.out.println("Aucun mouvement enregistré.");
        } else {
            for (StockHistorique h : historiques) {
                String signe = h.getQuantite() >= 0 ? "+" : "";
                System.out.printf("%s: %s%d | %s%n", 
                    h.getDateOperation(), 
                    signe,
                    h.getQuantite(),
                    h.getTypeOperation()
                );
            }
        }
        
        System.out.println("====================================================\n");
    }
    
    // ========== AFFICHAGE ==========
    
    /**
     * Affiche tous les médicaments
     */
    public void afficherTousMedicaments() {
        List<Medicament> medicaments = getTousMedicaments();
        
        System.out.println("\n========== CATALOGUE DES MÉDICAMENTS ==========");
        System.out.println("------------------------------------------------");
        System.out.printf("%-5s %-25s %-10s %-8s %-10s%n", "ID", "Nom", "Dosage", "Stock", "Prix");
        System.out.println("------------------------------------------------");
        
        for (Medicament med : medicaments) {
            String stockAlert = med.estStockCritique() ? " ⚠️" : "";
            System.out.printf("%-5d %-25s %-10s %-8d %.2f DT%s%n", 
                med.getId(), 
                med.getNom(), 
                med.getDosage(), 
                med.getStock(), 
                med.getPrixUnitaire(),
                stockAlert
            );
        }
        
        System.out.println("------------------------------------------------");
        System.out.println("Total: " + medicaments.size() + " médicaments");
        System.out.println("================================================\n");
    }
}
