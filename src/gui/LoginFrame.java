package gui;

import services.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Fenêtre de connexion - Interface graphique
 * 
 * Cette classe affiche une fenêtre de login simple avec:
 * - Un champ pour le login
 * - Un champ pour le mot de passe
 * - Un bouton de connexion
 * 
 * @author Étudiant
 * @version 1.0
 */
public class LoginFrame extends JFrame {
    
    // Composants de l'interface
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JLabel messageLabel;
    
    // Service d'authentification
    private AuthenticationService authService;
    
    /**
     * Constructeur - Crée la fenêtre de connexion
     */
    public LoginFrame() {
        this.authService = new AuthenticationService();
        
        // Configuration de la fenêtre
        setTitle("Pharmacie - Connexion");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre
        setResizable(false);
        
        // Créer le contenu
        creerContenu();
    }
    
    /**
     * Crée tous les composants de l'interface
     */
    private void creerContenu() {
        // Panel principal avec un peu de marge
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255)); // Bleu très clair
        
        // ===== Titre =====
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 248, 255));
        JLabel titleLabel = new JLabel("Système de Gestion de Pharmacie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        
        // ===== Formulaire =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Label Login
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel loginLabel = new JLabel("Login :");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(loginLabel, gbc);
        
        // Champ Login
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        loginField = new JTextField(20);
        loginField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(loginField, gbc);
        
        // Label Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);
        
        // Champ Mot de passe
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);
        
        // Message d'erreur/info
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(messageLabel, gbc);
        
        // ===== Boutons =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        connectButton = new JButton("Se connecter");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(new Color(41, 128, 185));
        connectButton.setForeground(Color.WHITE);
        connectButton.setOpaque(true);
        connectButton.setBorderPainted(false);
        connectButton.setFocusPainted(false);
        connectButton.setPreferredSize(new Dimension(150, 40));
        connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonPanel.add(connectButton);
        
        // ===== Info comptes de test =====
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Comptes de test"));
        
        JLabel info1 = new JLabel("Pharmacien: ybali / pharma123");
        info1.setFont(new Font("Arial", Font.PLAIN, 11));
        info1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel info2 = new JLabel("Gestionnaire: ybenammar / admin123");
        info2.setFont(new Font("Arial", Font.PLAIN, 11));
        info2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(info1);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(info2);
        infoPanel.add(Box.createVerticalStrut(5));
        
        // ===== Assembler le tout =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // ===== Actions =====
        
        // Clic sur le bouton connexion
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seConnecter();
            }
        });
        
        // Appuyer sur Entrée dans le champ password
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    seConnecter();
                }
            }
        });
        
        // Appuyer sur Entrée dans le champ login
        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
    }
    
    /**
     * Tente de connecter l'utilisateur
     */
    private void seConnecter() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (login.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }
        
        // Tentative de connexion
        authService.seConnecter(login, password);
        
        if (authService.estConnecte()) {
            // Connexion réussie - ouvrir la bonne fenêtre
            if (authService.estPharmacien()) {
                PharmacienFrame pharmacienFrame = new PharmacienFrame(authService);
                pharmacienFrame.setVisible(true);
            } else if (authService.estGestionnaire()) {
                GestionnaireFrame gestionnaireFrame = new GestionnaireFrame(authService);
                gestionnaireFrame.setVisible(true);
            }
            // Fermer la fenêtre de login
            this.dispose();
        } else {
            // Échec de connexion
            messageLabel.setText("Login ou mot de passe incorrect.");
            passwordField.setText("");
        }
    }
}
