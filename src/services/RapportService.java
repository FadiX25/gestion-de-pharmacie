package services;

import models.Medicament;
import models.Vente;

import java.util.List;

/**
 * Classe RapportService - Génère des rapports et statistiques
 * 
 * Cette classe est utilisée par les gestionnaires pour avoir
 * une vue d'ensemble de l'activité de la pharmacie.
 * 
 * @author Étudiant
 * @version 1.0
 */
public class RapportService {
    
    private MedicamentService medicamentService;
    private VenteService venteService;
    private CommandeService commandeService;
    
    public RapportService() {
        this.medicamentService = new MedicamentService();
        this.venteService = new VenteService();
        this.commandeService = new CommandeService();
    }
    
    /**
     * Génère un rapport complet de la pharmacie
     */
    public void genererRapportComplet() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           RAPPORT COMPLET DE LA PHARMACIE                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        
        // Section: Chiffre d'affaires
        System.out.println("║                                                              ║");
        System.out.println("║  📊 CHIFFRE D'AFFAIRES                                       ║");
        System.out.printf("║     • CA Total:      %10.2f €                            ║%n", venteService.getChiffreAffairesTotal());
        System.out.printf("║     • CA du jour:    %10.2f €                            ║%n", venteService.getChiffreAffairesDuJour());
        
        // Section: Ventes
        System.out.println("║                                                              ║");
        System.out.println("║  🛒 VENTES                                                   ║");
        List<Vente> ventesJour = venteService.getVentesDuJour();
        List<Vente> toutesVentes = venteService.getToutesVentes();
        System.out.printf("║     • Ventes du jour:    %5d                               ║%n", ventesJour.size());
        System.out.printf("║     • Total des ventes:  %5d                               ║%n", toutesVentes.size());
        
        // Section: Stock
        System.out.println("║                                                              ║");
        System.out.println("║  📦 STOCK                                                    ║");
        List<Medicament> stockCritique = medicamentService.getMedicamentsStockCritique();
        List<Medicament> tousMedicaments = medicamentService.getTousMedicaments();
        System.out.printf("║     • Médicaments en catalogue:  %5d                       ║%n", tousMedicaments.size());
        System.out.printf("║     • Stock critique:            %5d  ⚠️                    ║%n", stockCritique.size());
        
        // Section: Commandes
        System.out.println("║                                                              ║");
        System.out.println("║  📋 COMMANDES                                                ║");
        System.out.printf("║     • Commandes en attente:  %5d                           ║%n", commandeService.getCommandesEnAttente().size());
        System.out.printf("║     • Total commandes:       %5d                           ║%n", commandeService.getToutesCommandes().size());
        
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
    
    /**
     * Génère un rapport sur les stocks critiques
     */
    public void genererRapportStockCritique() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           RAPPORT: STOCKS CRITIQUES                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        List<Medicament> stockCritique = medicamentService.getMedicamentsStockCritique();
        
        if (stockCritique.isEmpty()) {
            System.out.println("║  ✓ Aucun médicament en stock critique!                     ║");
        } else {
            System.out.println("║  ⚠️ ATTENTION: Les médicaments suivants doivent être       ║");
            System.out.println("║  commandés rapidement:                                     ║");
            System.out.println("║                                                            ║");
            System.out.printf("║  %-25s %-10s %-10s      ║%n", "Nom", "Dosage", "Stock");
            System.out.println("║  ------------------------------------------------          ║");
            
            for (Medicament med : stockCritique) {
                System.out.printf("║  %-25s %-10s %-10d      ║%n", 
                    med.getNom(), 
                    med.getDosage(), 
                    med.getStock()
                );
            }
        }
        
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Génère un rapport des ventes du jour
     */
    public void genererRapportVentesDuJour() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           RAPPORT: VENTES DU JOUR                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        List<Vente> ventes = venteService.getVentesDuJour();
        double ca = venteService.getChiffreAffairesDuJour();
        
        System.out.printf("║  Nombre de ventes:     %5d                                ║%n", ventes.size());
        System.out.printf("║  Chiffre d'affaires:   %10.2f €                         ║%n", ca);
        
        if (!ventes.isEmpty()) {
            double moyenneParVente = ca / ventes.size();
            System.out.printf("║  Moyenne par vente:    %10.2f €                         ║%n", moyenneParVente);
        }
        
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Envoie une notification de stock critique
     */
    public void envoyerNotificationStockCritique() {
        List<Medicament> stockCritique = medicamentService.getMedicamentsStockCritique();
        
        if (!stockCritique.isEmpty()) {
            System.out.println("\n🔔 ===== NOTIFICATION: STOCK CRITIQUE ===== 🔔");
            System.out.println("Les médicaments suivants sont en stock critique:");
            
            for (Medicament med : stockCritique) {
                System.out.println("  ⚠️ " + med.getNom() + " (" + med.getDosage() + ") - " + med.getStock() + " unités restantes");
            }
            
            System.out.println("Veuillez passer une commande rapidement.");
            System.out.println("===========================================\n");
        }
    }
}
