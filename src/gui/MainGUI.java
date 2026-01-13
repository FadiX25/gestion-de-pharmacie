package gui;

import database.CSVHelper;

import javax.swing.*;

/**
 * Point d'entrée de l'application avec interface graphique
 * 
 * Cette classe lance l'application en mode graphique (GUI)
 * au lieu du mode console.
 * 
 * @author Étudiant
 * @version 1.0
 */
public class MainGUI {
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        // Initialiser le dossier de données
        CSVHelper.initDataFolder();
        
        // Utiliser le Look and Feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Utiliser le L&F par défaut si échec
            System.out.println("Utilisation du thème par défaut.");
        }
        
        // Lancer l'interface dans le thread EDT (Event Dispatch Thread)
        // C'est une bonne pratique en Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Créer et afficher la fenêtre de login
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
