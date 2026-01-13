package gui;

import models.*;
import services.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Interface graphique pour le Pharmacien
 * 
 * Permet au pharmacien de:
 * - Voir les médicaments
 * - Enregistrer des ventes
 * - Consulter ses ventes
 * - Voir les alertes de stock
 * 
 * @author Étudiant
 * @version 1.0
 */
public class PharmacienFrame extends JFrame {
    
    // Services
    private AuthenticationService authService;
    private MedicamentService medicamentService;
    private VenteService venteService;
    private ClientService clientService;
    
    // Composants
    private JTabbedPane tabbedPane;
    private JTable medicamentsTable;
    private JTable ventesTable;
    private JLabel welcomeLabel;
    
    /**
     * Constructeur
     */
    public PharmacienFrame(AuthenticationService authService) {
        this.authService = authService;
        this.medicamentService = new MedicamentService();
        this.venteService = new VenteService();
        this.clientService = new ClientService();
        
        // Configuration de la fenêtre
        setTitle("💊 Pharmacie - Espace Pharmacien");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        creerContenu();
    }
    
    /**
     * Crée le contenu de la fenêtre
     */
    private void creerContenu() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // ===== Barre du haut =====
        JPanel topBar = creerBarreHaut();
        
        // ===== Onglets =====
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Onglet Médicaments
        JPanel medicamentsPanel = creerPanelMedicaments();
        tabbedPane.addTab("📦 Médicaments", medicamentsPanel);
        
        // Onglet Nouvelle Vente
        JPanel ventePanel = creerPanelNouvelleVente();
        tabbedPane.addTab("🛒 Nouvelle Vente", ventePanel);
        
        // Onglet Mes Ventes
        JPanel mesVentesPanel = creerPanelMesVentes();
        tabbedPane.addTab("📋 Mes Ventes", mesVentesPanel);
        
        // Onglet Alertes Stock
        JPanel alertesPanel = creerPanelAlertes();
        tabbedPane.addTab("⚠️ Alertes Stock", alertesPanel);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Crée la barre du haut avec le nom de l'utilisateur et déconnexion
     */
    private JPanel creerBarreHaut() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Nom de l'utilisateur
        Pharmacien p = authService.getPharmacienConnecte();
        welcomeLabel = new JLabel("Bienvenue, " + p.getPrenom() + " " + p.getNom());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        
        // Bouton déconnexion
        JButton logoutBtn = new JButton("Déconnexion");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
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
     * Crée le panel pour afficher les médicaments
     */
    private JPanel creerPanelMedicaments() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Rechercher: ");
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("🔍 Rechercher");
        JButton refreshBtn = new JButton("🔄 Actualiser");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        
        // Tableau des médicaments
        String[] colonnes = {"ID", "Nom", "Dosage", "Stock", "Prix (€)"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        medicamentsTable = new JTable(model);
        medicamentsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        medicamentsTable.setRowHeight(25);
        medicamentsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(medicamentsTable);
        
        // Charger les données
        chargerMedicaments(model);
        
        // Actions
        searchBtn.addActionListener(e -> {
            String recherche = searchField.getText().trim();
            rechercherMedicaments(model, recherche);
        });
        
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            chargerMedicaments(model);
        });
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    rechercherMedicaments(model, searchField.getText().trim());
                }
            }
        });
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Charge les médicaments dans le tableau
     */
    private void chargerMedicaments(DefaultTableModel model) {
        model.setRowCount(0);
        List<Medicament> medicaments = medicamentService.getTousMedicaments();
        
        for (Medicament med : medicaments) {
            model.addRow(new Object[]{
                med.getId(),
                med.getNom(),
                med.getDosage(),
                med.getStock(),
                String.format("%.2f", med.getPrixUnitaire())
            });
        }
    }
    
    /**
     * Recherche des médicaments par nom
     */
    private void rechercherMedicaments(DefaultTableModel model, String recherche) {
        model.setRowCount(0);
        List<Medicament> medicaments;
        
        if (recherche.isEmpty()) {
            medicaments = medicamentService.getTousMedicaments();
        } else {
            medicaments = medicamentService.rechercherMedicament(recherche);
        }
        
        for (Medicament med : medicaments) {
            model.addRow(new Object[]{
                med.getId(),
                med.getNom(),
                med.getDosage(),
                med.getStock(),
                String.format("%.2f", med.getPrixUnitaire())
            });
        }
    }
    
    /**
     * Crée le panel pour enregistrer une nouvelle vente
     */
    private JPanel creerPanelNouvelleVente() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titre = new JLabel("Enregistrer une nouvelle vente");
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
        JLabel infoMedLabel = new JLabel(" ");
        infoMedLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(infoMedLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // ID Client
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("ID Client (0 = anonyme):"), gbc);
        gbc.gridx = 1;
        JTextField idClientField = new JTextField(15);
        idClientField.setText("0");
        panel.add(idClientField, gbc);
        
        // Quantité
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        panel.add(quantiteSpinner, gbc);
        
        // Bouton vendre
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JButton vendreBtn = new JButton("💰 Enregistrer la vente");
        vendreBtn.setFont(new Font("Arial", Font.BOLD, 14));
        vendreBtn.setBackground(new Color(46, 204, 113));
        vendreBtn.setForeground(Color.WHITE);
        panel.add(vendreBtn, gbc);
        
        // Message résultat
        gbc.gridy = 6;
        JLabel resultLabel = new JLabel(" ");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultLabel, gbc);
        
        // Actions
        idMedField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int id = Integer.parseInt(idMedField.getText().trim());
                    Medicament med = medicamentService.getMedicament(id);
                    if (med != null) {
                        infoMedLabel.setText(med.getNom() + " - " + med.getDosage() + " - Stock: " + med.getStock() + " - Prix: " + med.getPrixUnitaire() + "€");
                        infoMedLabel.setForeground(new Color(46, 204, 113));
                    } else {
                        infoMedLabel.setText("Médicament non trouvé");
                        infoMedLabel.setForeground(Color.RED);
                    }
                } catch (NumberFormatException ex) {
                    infoMedLabel.setText(" ");
                }
            }
        });
        
        vendreBtn.addActionListener(e -> {
            try {
                int idMed = Integer.parseInt(idMedField.getText().trim());
                int idClient = Integer.parseInt(idClientField.getText().trim());
                int quantite = (Integer) quantiteSpinner.getValue();
                int idPharmacien = authService.getPharmacienConnecte().getId();
                
                if (idClient == 0) idClient = 1; // Client anonyme par défaut
                
                boolean succes = venteService.enregistrerVente(idPharmacien, idClient, idMed, quantite);
                
                if (succes) {
                    resultLabel.setText("✓ Vente enregistrée avec succès!");
                    resultLabel.setForeground(new Color(46, 204, 113));
                    // Reset form
                    idMedField.setText("");
                    idClientField.setText("0");
                    quantiteSpinner.setValue(1);
                    infoMedLabel.setText(" ");
                } else {
                    resultLabel.setText("✗ Erreur lors de la vente");
                    resultLabel.setForeground(Color.RED);
                }
            } catch (NumberFormatException ex) {
                resultLabel.setText("✗ Veuillez entrer des valeurs valides");
                resultLabel.setForeground(Color.RED);
            }
        });
        
        return panel;
    }
    
    /**
     * Crée le panel pour voir les ventes du pharmacien
     */
    private JPanel creerPanelMesVentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Bouton actualiser
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("🔄 Actualiser");
        topPanel.add(refreshBtn);
        
        // Tableau des ventes
        String[] colonnes = {"ID", "Date", "ID Médicament", "Quantité", "Montant (€)"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ventesTable = new JTable(model);
        ventesTable.setFont(new Font("Arial", Font.PLAIN, 13));
        ventesTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(ventesTable);
        
        // Total
        JLabel totalLabel = new JLabel("Total: 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Charger les ventes
        chargerVentes(model, totalLabel);
        
        refreshBtn.addActionListener(e -> chargerVentes(model, totalLabel));
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Charge les ventes du pharmacien
     */
    private void chargerVentes(DefaultTableModel model, JLabel totalLabel) {
        model.setRowCount(0);
        int idPharmacien = authService.getPharmacienConnecte().getId();
        List<Vente> ventes = venteService.getVentesPharmacien(idPharmacien);
        
        double total = 0;
        for (Vente v : ventes) {
            model.addRow(new Object[]{
                v.getId(),
                v.getDateVente().toString().substring(0, 16),
                v.getIdMedicament(),
                v.getQuantite(),
                String.format("%.2f", v.getMontantTotal())
            });
            total += v.getMontantTotal();
        }
        
        totalLabel.setText("Total: " + String.format("%.2f", total) + " € (" + ventes.size() + " ventes)");
    }
    
    /**
     * Crée le panel des alertes de stock
     */
    private JPanel creerPanelAlertes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Titre
        JLabel titre = new JLabel("⚠️ Médicaments en stock critique (< 10 unités)");
        titre.setFont(new Font("Arial", Font.BOLD, 16));
        titre.setForeground(new Color(231, 76, 60));
        
        // Bouton actualiser
        JButton refreshBtn = new JButton("🔄 Actualiser");
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titre, BorderLayout.WEST);
        topPanel.add(refreshBtn, BorderLayout.EAST);
        
        // Tableau
        String[] colonnes = {"ID", "Nom", "Dosage", "Stock"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);
        JTable alertesTable = new JTable(model);
        alertesTable.setFont(new Font("Arial", Font.PLAIN, 13));
        alertesTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(alertesTable);
        
        // Charger les alertes
        chargerAlertes(model);
        
        refreshBtn.addActionListener(e -> chargerAlertes(model));
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Charge les médicaments en stock critique
     */
    private void chargerAlertes(DefaultTableModel model) {
        model.setRowCount(0);
        List<Medicament> alertes = medicamentService.getMedicamentsStockCritique();
        
        for (Medicament med : alertes) {
            model.addRow(new Object[]{
                med.getId(),
                med.getNom(),
                med.getDosage(),
                med.getStock()
            });
        }
    }
}
