package gui;

import models.*;
import services.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Interface graphique pour le Gestionnaire
 * 
 * Permet au gestionnaire de:
 * - Gérer les médicaments (ajouter, modifier)
 * - Créer et gérer les commandes
 * - Voir les alertes de stock
 * - Générer des rapports
 * 
 * @author Étudiant
 * @version 1.0
 */
public class GestionnaireFrame extends JFrame {
    
    // Services
    private AuthenticationService authService;
    private MedicamentService medicamentService;
    private CommandeService commandeService;
    private VenteService venteService;
    
    // Composants
    private JTabbedPane tabbedPane;
    
    /**
     * Constructeur
     */
    public GestionnaireFrame(AuthenticationService authService) {
        this.authService = authService;
        this.medicamentService = new MedicamentService();
        this.commandeService = new CommandeService();
        this.venteService = new VenteService();
        
        // Configuration de la fenêtre
        setTitle("💊 Pharmacie - Espace Gestionnaire");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        creerContenu();
    }
    
    /**
     * Crée le contenu de la fenêtre
     */
    private void creerContenu() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Barre du haut
        JPanel topBar = creerBarreHaut();
        
        // Onglets
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Onglet Médicaments
        JPanel medicamentsPanel = creerPanelMedicaments();
        tabbedPane.addTab("📦 Médicaments", medicamentsPanel);
        
        // Onglet Ajouter Médicament
        JPanel ajouterPanel = creerPanelAjouterMedicament();
        tabbedPane.addTab("➕ Ajouter Médicament", ajouterPanel);
        
        // Onglet Commandes
        JPanel commandesPanel = creerPanelCommandes();
        tabbedPane.addTab("📋 Commandes", commandesPanel);
        
        // Onglet Nouvelle Commande
        JPanel nouvelleCommandePanel = creerPanelNouvelleCommande();
        tabbedPane.addTab("🛒 Nouvelle Commande", nouvelleCommandePanel);
        
        // Onglet Rapport
        JPanel rapportPanel = creerPanelRapport();
        tabbedPane.addTab("📊 Rapport", rapportPanel);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Crée la barre du haut
     */
    private JPanel creerBarreHaut() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(142, 68, 173)); // Violet pour gestionnaire
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        Gestionnaire g = authService.getGestionnaireConnecte();
        JLabel welcomeLabel = new JLabel("Bienvenue, " + g.getPrenom() + " " + g.getNom() + " (Gestionnaire)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutBtn.addActionListener(e -> {
            authService.seDeconnecter();
            new LoginFrame().setVisible(true);
            dispose();
        });
        
        panel.add(welcomeLabel, BorderLayout.WEST);
        panel.add(logoutBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Panel pour voir les médicaments
     */
    private JPanel creerPanelMedicaments() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Barre du haut
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("🔄 Actualiser");
        JButton alertesBtn = new JButton("⚠️ Voir alertes stock");
        topPanel.add(refreshBtn);
        topPanel.add(alertesBtn);
        
        // Tableau
        String[] colonnes = {"ID", "Nom", "Dosage", "Stock", "Prix (€)", "État"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        chargerMedicaments(model, false);
        
        refreshBtn.addActionListener(e -> chargerMedicaments(model, false));
        alertesBtn.addActionListener(e -> chargerMedicaments(model, true));
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Charge les médicaments
     */
    private void chargerMedicaments(DefaultTableModel model, boolean alertesOnly) {
        model.setRowCount(0);
        List<Medicament> medicaments;
        
        if (alertesOnly) {
            medicaments = medicamentService.getMedicamentsStockCritique();
        } else {
            medicaments = medicamentService.getTousMedicaments();
        }
        
        for (Medicament med : medicaments) {
            String etat = med.getStock() < 10 ? "⚠️ CRITIQUE" : "✓ OK";
            model.addRow(new Object[]{
                med.getId(),
                med.getNom(),
                med.getDosage(),
                med.getStock(),
                String.format("%.2f", med.getPrixUnitaire()),
                etat
            });
        }
    }
    
    /**
     * Panel pour ajouter un médicament
     */
    private JPanel creerPanelAjouterMedicament() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titre = new JLabel("Ajouter un nouveau médicament");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titre, gbc);
        
        gbc.gridwidth = 1;
        
        // Nom
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        JTextField nomField = new JTextField(20);
        panel.add(nomField, gbc);
        
        // Dosage
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 1;
        JTextField dosageField = new JTextField(20);
        panel.add(dosageField, gbc);
        
        // Stock initial
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Stock initial:"), gbc);
        gbc.gridx = 1;
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        panel.add(stockSpinner, gbc);
        
        // Prix unitaire
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Prix unitaire (€):"), gbc);
        gbc.gridx = 1;
        JSpinner prixSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.5));
        panel.add(prixSpinner, gbc);
        
        // Bouton ajouter
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JButton ajouterBtn = new JButton("Ajouter le medicament");
        ajouterBtn.setFont(new Font("Arial", Font.BOLD, 14));
        ajouterBtn.setBackground(new Color(39, 174, 96));
        ajouterBtn.setForeground(Color.WHITE);
        ajouterBtn.setOpaque(true);
        ajouterBtn.setBorderPainted(false);
        ajouterBtn.setFocusPainted(false);
        panel.add(ajouterBtn, gbc);
        
        // Message
        gbc.gridy = 6;
        JLabel resultLabel = new JLabel(" ");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultLabel, gbc);
        
        ajouterBtn.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String dosage = dosageField.getText().trim();
            int stock = (Integer) stockSpinner.getValue();
            double prix = (Double) prixSpinner.getValue();
            
            if (nom.isEmpty()) {
                resultLabel.setText("✗ Le nom est obligatoire");
                resultLabel.setForeground(Color.RED);
                return;
            }
            
            boolean succes = medicamentService.ajouterMedicament(nom, dosage, stock, prix);
            
            if (succes) {
                resultLabel.setText("✓ Médicament ajouté avec succès!");
                resultLabel.setForeground(new Color(46, 204, 113));
                nomField.setText("");
                dosageField.setText("");
                stockSpinner.setValue(0);
                prixSpinner.setValue(0.0);
            } else {
                resultLabel.setText("✗ Erreur lors de l'ajout");
                resultLabel.setForeground(Color.RED);
            }
        });
        
        return panel;
    }
    
    /**
     * Panel pour voir les commandes
     */
    private JPanel creerPanelCommandes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Barre du haut
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("🔄 Actualiser");
        JButton enAttenteBtn = new JButton("📋 En attente seulement");
        topPanel.add(refreshBtn);
        topPanel.add(enAttenteBtn);
        
        // Tableau
        String[] colonnes = {"ID", "ID Médicament", "Quantité", "Statut", "Date"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Panel de validation
        JPanel validationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("ID Commande:");
        JTextField idField = new JTextField(5);
        JButton validerBtn = new JButton("Valider livraison");
        validerBtn.setBackground(new Color(39, 174, 96));
        validerBtn.setForeground(Color.WHITE);
        validerBtn.setOpaque(true);
        validerBtn.setBorderPainted(false);
        validerBtn.setFocusPainted(false);
        
        validationPanel.add(idLabel);
        validationPanel.add(idField);
        validationPanel.add(validerBtn);
        
        chargerCommandes(model, false);
        
        refreshBtn.addActionListener(e -> chargerCommandes(model, false));
        enAttenteBtn.addActionListener(e -> chargerCommandes(model, true));
        
        validerBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                commandeService.recevoirCommande(id);
                chargerCommandes(model, false);
                idField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID invalide");
            }
        });
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(validationPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Charge les commandes
     */
    private void chargerCommandes(DefaultTableModel model, boolean enAttenteOnly) {
        model.setRowCount(0);
        List<Commande> commandes;
        
        if (enAttenteOnly) {
            commandes = commandeService.getCommandesEnAttente();
        } else {
            commandes = commandeService.getToutesCommandes();
        }
        
        for (Commande c : commandes) {
            model.addRow(new Object[]{
                c.getId(),
                c.getIdMedicament(),
                c.getQuantite(),
                c.getStatut(),
                c.getDateCommande().toString().substring(0, 16)
            });
        }
    }
    
    /**
     * Panel pour créer une commande
     */
    private JPanel creerPanelNouvelleCommande() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titre = new JLabel("Créer une commande de réapprovisionnement");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titre, gbc);
        
        gbc.gridwidth = 1;
        
        // ID Médicament
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Médicament:"), gbc);
        gbc.gridx = 1;
        JTextField idMedField = new JTextField(15);
        panel.add(idMedField, gbc);
        
        // Info médicament
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel(" ");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(infoLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Quantité
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Quantité à commander:"), gbc);
        gbc.gridx = 1;
        JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 10));
        panel.add(quantiteSpinner, gbc);
        
        // Bouton commander
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton commanderBtn = new JButton("Creer la commande");
        commanderBtn.setFont(new Font("Arial", Font.BOLD, 14));
        commanderBtn.setBackground(new Color(41, 128, 185));
        commanderBtn.setForeground(Color.WHITE);
        commanderBtn.setOpaque(true);
        commanderBtn.setBorderPainted(false);
        commanderBtn.setFocusPainted(false);
        panel.add(commanderBtn, gbc);
        
        // Message
        gbc.gridy = 5;
        JLabel resultLabel = new JLabel(" ");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultLabel, gbc);
        
        idMedField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int id = Integer.parseInt(idMedField.getText().trim());
                    Medicament med = medicamentService.getMedicament(id);
                    if (med != null) {
                        infoLabel.setText(med.getNom() + " - Stock actuel: " + med.getStock());
                        infoLabel.setForeground(med.getStock() < 10 ? Color.RED : new Color(46, 204, 113));
                    } else {
                        infoLabel.setText("Médicament non trouvé");
                        infoLabel.setForeground(Color.RED);
                    }
                } catch (NumberFormatException ex) {
                    infoLabel.setText(" ");
                }
            }
        });
        
        commanderBtn.addActionListener(e -> {
            try {
                int idMed = Integer.parseInt(idMedField.getText().trim());
                int quantite = (Integer) quantiteSpinner.getValue();
                int idGestionnaire = authService.getGestionnaireConnecte().getId();
                
                boolean succes = commandeService.creerCommande(idGestionnaire, idMed, quantite);
                
                if (succes) {
                    resultLabel.setText("✓ Commande créée avec succès!");
                    resultLabel.setForeground(new Color(46, 204, 113));
                    idMedField.setText("");
                    infoLabel.setText(" ");
                    quantiteSpinner.setValue(10);
                } else {
                    resultLabel.setText("✗ Erreur lors de la création");
                    resultLabel.setForeground(Color.RED);
                }
            } catch (NumberFormatException ex) {
                resultLabel.setText("✗ ID invalide");
                resultLabel.setForeground(Color.RED);
            }
        });
        
        return panel;
    }
    
    /**
     * Panel pour le rapport
     */
    private JPanel creerPanelRapport() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Zone de texte pour le rapport
        JTextArea rapportArea = new JTextArea();
        rapportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rapportArea.setEditable(false);
        rapportArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(rapportArea);
        
        // Bouton générer
        JButton genererBtn = new JButton("Generer le rapport");
        genererBtn.setFont(new Font("Arial", Font.BOLD, 14));
        genererBtn.setBackground(new Color(142, 68, 173));
        genererBtn.setForeground(Color.WHITE);
        genererBtn.setOpaque(true);
        genererBtn.setBorderPainted(false);
        genererBtn.setFocusPainted(false);
        
        genererBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════════════\n");
            sb.append("              RAPPORT COMPLET DE LA PHARMACIE\n");
            sb.append("═══════════════════════════════════════════════════════════\n\n");
            
            // Chiffre d'affaires
            sb.append("📊 CHIFFRE D'AFFAIRES\n");
            sb.append("─────────────────────────────────────────────────────────\n");
            sb.append(String.format("   • CA Total:      %10.2f €\n", venteService.getChiffreAffairesTotal()));
            sb.append(String.format("   • CA du jour:    %10.2f €\n", venteService.getChiffreAffairesDuJour()));
            sb.append("\n");
            
            // Ventes
            sb.append("🛒 VENTES\n");
            sb.append("─────────────────────────────────────────────────────────\n");
            sb.append(String.format("   • Ventes du jour:     %5d\n", venteService.getVentesDuJour().size()));
            sb.append(String.format("   • Total des ventes:   %5d\n", venteService.getToutesVentes().size()));
            sb.append("\n");
            
            // Stock
            sb.append("📦 STOCK\n");
            sb.append("─────────────────────────────────────────────────────────\n");
            sb.append(String.format("   • Médicaments en catalogue:  %5d\n", medicamentService.getTousMedicaments().size()));
            sb.append(String.format("   • Stock critique:            %5d ⚠️\n", medicamentService.getMedicamentsStockCritique().size()));
            sb.append("\n");
            
            // Commandes
            sb.append("📋 COMMANDES\n");
            sb.append("─────────────────────────────────────────────────────────\n");
            sb.append(String.format("   • Commandes en attente:  %5d\n", commandeService.getCommandesEnAttente().size()));
            sb.append(String.format("   • Total commandes:       %5d\n", commandeService.getToutesCommandes().size()));
            sb.append("\n");
            
            sb.append("═══════════════════════════════════════════════════════════\n");
            sb.append("                    Rapport généré avec succès\n");
            sb.append("═══════════════════════════════════════════════════════════\n");
            
            rapportArea.setText(sb.toString());
        });
        
        panel.add(genererBtn, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}
