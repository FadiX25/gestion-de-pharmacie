import database.CSVHelper;
import models.*;
import services.*;

import java.util.List;
import java.util.Scanner;

/**
 * Classe Main - Point d'entrée de l'application
 * 
 * Cette classe contient le menu principal et gère l'interaction
 * avec l'utilisateur via la console.
 * 
 * Version CSV - Utilise des fichiers CSV au lieu de MySQL
 * 
 * @author Étudiant
 * @version 2.0 (CSV)
 */
public class Main {
    
    // Scanner pour lire les entrées utilisateur
    private static Scanner scanner = new Scanner(System.in);
    
    // Services
    private static AuthenticationService authService = new AuthenticationService();
    private static MedicamentService medicamentService = new MedicamentService();
    private static VenteService venteService = new VenteService();
    private static CommandeService commandeService = new CommandeService();
    private static ClientService clientService = new ClientService();
    private static RapportService rapportService = new RapportService();
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║     💊 SYSTÈME DE GESTION DE PHARMACIE 💊                    ║");
        System.out.println("║                                                              ║");
        System.out.println("║          Bienvenue dans votre application                    ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
        
        // Initialiser le dossier de données CSV
        CSVHelper.initDataFolder();
        System.out.println("✓ Système de données CSV initialisé.");
        System.out.println("  Les données sont stockées dans le dossier 'data/'");
        System.out.println();
        
        // Afficher les utilisateurs de test
        afficherInfosConnexion();
        
        // Boucle principale
        boolean continuer = true;
        while (continuer) {
            if (!authService.estConnecte()) {
                // Menu de connexion
                continuer = menuConnexion();
            } else {
                // Menu principal selon le rôle
                if (authService.estPharmacien()) {
                    menuPharmacien();
                } else if (authService.estGestionnaire()) {
                    menuGestionnaire();
                }
            }
        }
        
        // Fermer les ressources
        scanner.close();
        
        System.out.println("\nMerci d'avoir utilisé notre application. À bientôt!");
    }
    
    /**
     * Affiche les informations de connexion pour les tests
     */
    private static void afficherInfosConnexion() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              COMPTES DE TEST DISPONIBLES                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  PHARMACIEN:                                                 ║");
        System.out.println("║    Login: pdupont    |    Mot de passe: pharma123           ║");
        System.out.println("║                                                              ║");
        System.out.println("║  GESTIONNAIRE:                                               ║");
        System.out.println("║    Login: aadmin     |    Mot de passe: admin123            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    // ========== MENU DE CONNEXION ==========
    
    /**
     * Affiche le menu de connexion
     * @return false si l'utilisateur veut quitter
     */
    private static boolean menuConnexion() {
        System.out.println("\n========== CONNEXION ==========");
        System.out.println("1. Se connecter");
        System.out.println("0. Quitter");
        System.out.println("================================");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                System.out.print("\nLogin: ");
                String login = scanner.nextLine();
                System.out.print("Mot de passe: ");
                String password = scanner.nextLine();
                
                authService.seConnecter(login, password);
                break;
                
            case 0:
                return false;
                
            default:
                System.out.println("❌ Choix invalide!");
        }
        
        return true;
    }
    
    // ========== MENU PHARMACIEN ==========
    
    /**
     * Affiche le menu du pharmacien
     */
    private static void menuPharmacien() {
        System.out.println("\n========== MENU PHARMACIEN ==========");
        System.out.println("1. Rechercher un médicament");
        System.out.println("2. Voir tous les médicaments");
        System.out.println("3. Enregistrer une vente");
        System.out.println("4. Consulter mes ventes");
        System.out.println("5. Annuler une vente");
        System.out.println("6. Voir les alertes de stock");
        System.out.println("7. Gérer les clients");
        System.out.println("8. Mon profil");
        System.out.println("0. Se déconnecter");
        System.out.println("======================================");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                rechercherMedicament();
                break;
            case 2:
                afficherTousMedicaments();
                break;
            case 3:
                enregistrerVente();
                break;
            case 4:
                consulterMesVentes();
                break;
            case 5:
                annulerVente();
                break;
            case 6:
                afficherAlertesStock();
                break;
            case 7:
                menuClients();
                break;
            case 8:
                afficherProfil();
                break;
            case 0:
                authService.seDeconnecter();
                break;
            default:
                System.out.println("❌ Choix invalide!");
        }
    }
    
    // ========== MENU GESTIONNAIRE ==========
    
    /**
     * Affiche le menu du gestionnaire
     */
    private static void menuGestionnaire() {
        System.out.println("\n========== MENU GESTIONNAIRE ==========");
        System.out.println("1. Voir tous les médicaments");
        System.out.println("2. Ajouter un médicament");
        System.out.println("3. Modifier un médicament");
        System.out.println("4. Créer une commande de réapprovisionnement");
        System.out.println("5. Voir les commandes en attente");
        System.out.println("6. Valider une livraison");
        System.out.println("7. Voir les alertes de stock");
        System.out.println("8. Générer un rapport");
        System.out.println("9. Mon profil");
        System.out.println("0. Se déconnecter");
        System.out.println("========================================");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                afficherTousMedicaments();
                break;
            case 2:
                ajouterMedicament();
                break;
            case 3:
                modifierMedicament();
                break;
            case 4:
                creerCommande();
                break;
            case 5:
                voirCommandesEnAttente();
                break;
            case 6:
                validerLivraison();
                break;
            case 7:
                afficherAlertesStock();
                break;
            case 8:
                menuRapports();
                break;
            case 9:
                afficherProfil();
                break;
            case 0:
                authService.seDeconnecter();
                break;
            default:
                System.out.println("❌ Choix invalide!");
        }
    }
    
    // ========== FONCTIONS MÉDICAMENTS ==========
    
    private static void rechercherMedicament() {
        System.out.print("\nNom du médicament à rechercher: ");
        String nom = scanner.nextLine();
        
        List<Medicament> resultats = medicamentService.rechercherMedicament(nom);
        
        if (resultats.isEmpty()) {
            System.out.println("\nAucun médicament trouvé.");
        } else {
            System.out.println("\n========== RÉSULTATS ==========");
            for (Medicament med : resultats) {
                System.out.println(med);
                System.out.println("--------------------------------");
            }
        }
    }
    
    private static void afficherTousMedicaments() {
        List<Medicament> medicaments = medicamentService.getTousMedicaments();
        
        if (medicaments.isEmpty()) {
            System.out.println("\nAucun médicament en stock.");
        } else {
            System.out.println("\n========== LISTE DES MÉDICAMENTS ==========");
            for (Medicament med : medicaments) {
                System.out.println(med);
                System.out.println("--------------------------------------------");
            }
            System.out.println("Total: " + medicaments.size() + " médicament(s)");
        }
    }
    
    private static void afficherAlertesStock() {
        List<Medicament> alertes = medicamentService.getMedicamentsStockCritique();
        
        if (alertes.isEmpty()) {
            System.out.println("\n✓ Aucune alerte de stock. Tout va bien!");
        } else {
            System.out.println("\n⚠️ ========== ALERTES STOCK ========== ⚠️");
            for (Medicament med : alertes) {
                System.out.println("🔴 " + med.getNom() + " - Stock: " + med.getStock() + " unités");
            }
            System.out.println("==========================================");
            System.out.println("Total: " + alertes.size() + " médicament(s) en alerte");
        }
    }
    
    private static void ajouterMedicament() {
        System.out.println("\n========== AJOUTER UN MÉDICAMENT ==========");
        
        System.out.print("Nom du médicament: ");
        String nom = scanner.nextLine();
        
        System.out.print("Dosage (ex: 500mg): ");
        String dosage = scanner.nextLine();
        
        System.out.print("Stock initial: ");
        int stock = lireEntier();
        
        System.out.print("Prix unitaire (€): ");
        double prix = lireDouble();
        
        medicamentService.ajouterMedicament(nom, dosage, stock, prix);
    }
    
    private static void modifierMedicament() {
        System.out.println("\n========== MODIFIER UN MÉDICAMENT ==========");
        
        System.out.print("ID du médicament à modifier: ");
        int id = lireEntier();
        
        Medicament med = medicamentService.getMedicament(id);
        if (med == null) {
            System.out.println("❌ Médicament non trouvé!");
            return;
        }
        
        System.out.println("\nMédicament actuel: " + med);
        System.out.println("\n(Appuyez sur Entrée pour garder la valeur actuelle)");
        
        System.out.print("Nouveau nom [" + med.getNom() + "]: ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) med.setNom(nom);
        
        System.out.print("Nouveau dosage [" + med.getDosage() + "]: ");
        String dosage = scanner.nextLine();
        if (!dosage.isEmpty()) med.setDosage(dosage);
        
        System.out.print("Nouveau stock [" + med.getStock() + "]: ");
        String stockStr = scanner.nextLine();
        if (!stockStr.isEmpty()) {
            try {
                med.setStock(Integer.parseInt(stockStr));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Stock invalide, valeur inchangée.");
            }
        }
        
        System.out.print("Nouveau prix [" + med.getPrixUnitaire() + "]: ");
        String prixStr = scanner.nextLine();
        if (!prixStr.isEmpty()) {
            try {
                med.setPrixUnitaire(Double.parseDouble(prixStr));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Prix invalide, valeur inchangée.");
            }
        }
        
        medicamentService.mettreAJourMedicament(med);
    }
    
    // ========== FONCTIONS VENTES ==========
    
    private static void enregistrerVente() {
        System.out.println("\n========== NOUVELLE VENTE ==========");
        
        System.out.print("ID du médicament: ");
        int idMed = lireEntier();
        
        Medicament med = medicamentService.getMedicament(idMed);
        if (med == null) {
            System.out.println("❌ Médicament non trouvé!");
            return;
        }
        
        System.out.println("Médicament: " + med.getNom() + " - Stock: " + med.getStock() + " - Prix: " + med.getPrixUnitaire() + "€");
        
        System.out.print("ID du client (0 si anonyme): ");
        int idClient = lireEntier();
        
        System.out.print("Quantité: ");
        int quantite = lireEntier();
        
        if (quantite <= 0) {
            System.out.println("❌ Quantité invalide!");
            return;
        }
        
        // Récupérer l'ID du pharmacien connecté
        int idPharmacien = authService.getPharmacienConnecte().getId();
        
        venteService.enregistrerVente(idPharmacien, idClient == 0 ? 1 : idClient, idMed, quantite);
    }
    
    private static void consulterMesVentes() {
        int idPharmacien = authService.getPharmacienConnecte().getId();
        List<Vente> ventes = venteService.getVentesPharmacien(idPharmacien);
        
        if (ventes.isEmpty()) {
            System.out.println("\nVous n'avez pas encore effectué de ventes.");
        } else {
            System.out.println("\n========== MES VENTES ==========");
            double total = 0;
            for (Vente v : ventes) {
                System.out.println(v);
                System.out.println("--------------------------------");
                total += v.getMontantTotal();
            }
            System.out.println("Total: " + ventes.size() + " vente(s)");
            System.out.println("Montant total: " + String.format("%.2f", total) + " €");
        }
    }
    
    private static void annulerVente() {
        System.out.print("\nID de la vente à annuler: ");
        int idVente = lireEntier();
        
        venteService.annulerVente(idVente);
    }
    
    // ========== FONCTIONS CLIENTS ==========
    
    private static void menuClients() {
        System.out.println("\n========== GESTION CLIENTS ==========");
        System.out.println("1. Voir tous les clients");
        System.out.println("2. Rechercher un client");
        System.out.println("3. Ajouter un client");
        System.out.println("0. Retour");
        System.out.println("======================================");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                List<Client> clients = clientService.getTousClients();
                if (clients.isEmpty()) {
                    System.out.println("\nAucun client enregistré.");
                } else {
                    System.out.println("\n========== LISTE DES CLIENTS ==========");
                    for (Client c : clients) {
                        System.out.println(c);
                        System.out.println("----------------------------------------");
                    }
                }
                break;
                
            case 2:
                System.out.print("\nNom à rechercher: ");
                String nom = scanner.nextLine();
                List<Client> resultats = clientService.rechercherClient(nom);
                if (resultats.isEmpty()) {
                    System.out.println("Aucun client trouvé.");
                } else {
                    for (Client c : resultats) {
                        System.out.println(c);
                    }
                }
                break;
                
            case 3:
                System.out.println("\n========== NOUVEAU CLIENT ==========");
                System.out.print("Nom: ");
                String nomC = scanner.nextLine();
                System.out.print("Prénom: ");
                String prenomC = scanner.nextLine();
                System.out.print("Email (optionnel): ");
                String email = scanner.nextLine();
                System.out.print("Adresse (optionnel): ");
                String adresse = scanner.nextLine();
                
                clientService.ajouterClient(nomC, prenomC, email.isEmpty() ? null : email, adresse.isEmpty() ? null : adresse);
                break;
                
            case 0:
                break;
                
            default:
                System.out.println("❌ Choix invalide!");
        }
    }
    
    // ========== FONCTIONS COMMANDES ==========
    
    private static void creerCommande() {
        System.out.println("\n========== NOUVELLE COMMANDE ==========");
        
        System.out.print("ID du médicament: ");
        int idMed = lireEntier();
        
        Medicament med = medicamentService.getMedicament(idMed);
        if (med == null) {
            System.out.println("❌ Médicament non trouvé!");
            return;
        }
        
        System.out.println("Médicament: " + med.getNom() + " - Stock actuel: " + med.getStock());
        
        System.out.print("Quantité à commander: ");
        int quantite = lireEntier();
        
        if (quantite <= 0) {
            System.out.println("❌ Quantité invalide!");
            return;
        }
        
        int idGestionnaire = authService.getGestionnaireConnecte().getId();
        commandeService.creerCommande(idGestionnaire, idMed, quantite);
    }
    
    private static void voirCommandesEnAttente() {
        List<Commande> commandes = commandeService.getCommandesEnAttente();
        
        if (commandes.isEmpty()) {
            System.out.println("\nAucune commande en attente.");
        } else {
            System.out.println("\n========== COMMANDES EN ATTENTE ==========");
            for (Commande c : commandes) {
                System.out.println(c);
                System.out.println("-------------------------------------------");
            }
            System.out.println("Total: " + commandes.size() + " commande(s) en attente");
        }
    }
    
    private static void validerLivraison() {
        System.out.print("\nID de la commande livrée: ");
        int idCommande = lireEntier();
        
        commandeService.recevoirCommande(idCommande);
    }
    
    // ========== FONCTIONS RAPPORTS ==========
    
    private static void menuRapports() {
        System.out.println("\n========== RAPPORTS ==========");
        System.out.println("1. Rapport du jour");
        System.out.println("2. Rapport complet");
        System.out.println("0. Retour");
        System.out.println("===============================");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                venteService.afficherVentesDuJour();
                break;
            case 2:
                rapportService.genererRapportComplet();
                break;
            case 0:
                break;
            default:
                System.out.println("❌ Choix invalide!");
        }
    }
    
    // ========== FONCTIONS PROFIL ==========
    
    private static void afficherProfil() {
        if (authService.estPharmacien()) {
            Pharmacien p = authService.getPharmacienConnecte();
            System.out.println("\n========== MON PROFIL ==========");
            System.out.println("Nom: " + p.getNom());
            System.out.println("Prénom: " + p.getPrenom());
            System.out.println("Login: " + p.getLogin());
            System.out.println("Rôle: " + p.getRole());
            System.out.println("=================================");
        } else if (authService.estGestionnaire()) {
            Gestionnaire g = authService.getGestionnaireConnecte();
            System.out.println("\n========== MON PROFIL ==========");
            System.out.println("Nom: " + g.getNom());
            System.out.println("Prénom: " + g.getPrenom());
            System.out.println("Login: " + g.getLogin());
            System.out.println("Rôle: " + g.getRole());
            System.out.println("=================================");
        }
    }
    
    // ========== FONCTIONS UTILITAIRES ==========
    
    /**
     * Lit un entier au clavier avec gestion des erreurs
     */
    private static int lireEntier() {
        while (true) {
            try {
                String ligne = scanner.nextLine();
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                System.out.print("❌ Veuillez entrer un nombre valide: ");
            }
        }
    }
    
    /**
     * Lit un nombre décimal au clavier avec gestion des erreurs
     */
    private static double lireDouble() {
        while (true) {
            try {
                String ligne = scanner.nextLine();
                return Double.parseDouble(ligne.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("❌ Veuillez entrer un nombre valide: ");
            }
        }
    }
}
