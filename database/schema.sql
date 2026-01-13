-- ============================================================
-- Script SQL pour la création de la base de données Pharmacie
-- ============================================================
-- Ce script crée toutes les tables nécessaires pour le système
-- de gestion de pharmacie.
-- 
-- Pour exécuter ce script :
-- 1. Ouvrez MySQL Workbench ou phpMyAdmin
-- 2. Créez une nouvelle connexion ou utilisez une existante
-- 3. Copiez et collez ce script
-- 4. Exécutez-le
-- ============================================================

-- Supprimer la base de données si elle existe (pour repartir de zéro)
DROP DATABASE IF EXISTS pharmacie_db;

-- Créer la base de données
CREATE DATABASE pharmacie_db;

-- Utiliser cette base de données
USE pharmacie_db;

-- ============================================================
-- TABLE: Pharmacien
-- Stocke les informations des pharmaciens
-- ============================================================
CREATE TABLE Pharmacien (
    id_pharmacien INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE: Gestionnaire
-- Stocke les informations des gestionnaires/managers
-- ============================================================
CREATE TABLE Gestionnaire (
    id_gestionnaire INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE: Client
-- Stocke les informations des clients de la pharmacie
-- ============================================================
CREATE TABLE Client (
    id_client INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    adresse VARCHAR(200),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE: Medicament
-- Stocke le catalogue des médicaments
-- ============================================================
CREATE TABLE Medicament (
    id_medicament INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    dosage VARCHAR(50),
    stock INT DEFAULT 0,
    prix_unitaire DECIMAL(10, 2) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Contrainte: le stock ne peut pas être négatif
    CONSTRAINT check_stock CHECK (stock >= 0)
);

-- ============================================================
-- TABLE: Vente
-- Enregistre toutes les ventes effectuées
-- ============================================================
CREATE TABLE Vente (
    id_vente INT PRIMARY KEY AUTO_INCREMENT,
    id_pharmacien INT NOT NULL,
    id_client INT,
    id_medicament INT NOT NULL,
    quantite INT NOT NULL,
    montant_total DECIMAL(10, 2) NOT NULL,
    date_vente TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Clés étrangères (liens vers les autres tables)
    FOREIGN KEY (id_pharmacien) REFERENCES Pharmacien(id_pharmacien),
    FOREIGN KEY (id_client) REFERENCES Client(id_client),
    FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament),
    
    -- Contrainte: la quantité doit être positive
    CONSTRAINT check_quantite_vente CHECK (quantite > 0)
);

-- ============================================================
-- TABLE: Commande
-- Enregistre les commandes de réapprovisionnement
-- ============================================================
CREATE TABLE Commande (
    id_commande INT PRIMARY KEY AUTO_INCREMENT,
    id_gestionnaire INT NOT NULL,
    id_medicament INT NOT NULL,
    quantite INT NOT NULL,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Clés étrangères
    FOREIGN KEY (id_gestionnaire) REFERENCES Gestionnaire(id_gestionnaire),
    FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament),
    
    -- Contraintes
    CONSTRAINT check_quantite_commande CHECK (quantite > 0),
    CONSTRAINT check_statut CHECK (statut IN ('EN_ATTENTE', 'LIVREE', 'ANNULEE'))
);

-- ============================================================
-- TABLE: StockHistorique
-- Garde l'historique de tous les mouvements de stock
-- ============================================================
CREATE TABLE StockHistorique (
    id_historique INT PRIMARY KEY AUTO_INCREMENT,
    id_medicament INT NOT NULL,
    quantite INT NOT NULL,
    type_operation VARCHAR(30) NOT NULL,
    date_operation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Clé étrangère
    FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament),
    
    -- Contrainte sur le type d'opération
    CONSTRAINT check_type_operation CHECK (type_operation IN ('VENTE', 'REAPPROVISIONNEMENT', 'AJUSTEMENT'))
);

-- ============================================================
-- DONNÉES DE TEST
-- Insérer quelques données pour tester l'application
-- ============================================================

-- Insérer des pharmaciens (mot de passe: "password123")
INSERT INTO Pharmacien (nom, prenom, login, password) VALUES
('Dupont', 'Marie', 'mdupont', 'password123'),
('Martin', 'Pierre', 'pmartin', 'password123');

-- Insérer des gestionnaires
INSERT INTO Gestionnaire (nom, prenom, login, password) VALUES
('Bernard', 'Sophie', 'sbernard', 'admin123'),
('Dubois', 'Jean', 'jdubois', 'admin123');

-- Insérer des clients
INSERT INTO Client (nom, prenom, email, adresse) VALUES
('Lefebvre', 'Paul', 'paul.lefebvre@email.com', '10 rue de Paris, 75001 Paris'),
('Moreau', 'Julie', 'julie.moreau@email.com', '25 avenue des Champs, 75008 Paris'),
('Petit', 'Lucas', 'lucas.petit@email.com', '5 place de la République, 69001 Lyon');

-- Insérer des médicaments
INSERT INTO Medicament (nom, dosage, stock, prix_unitaire) VALUES
('Paracétamol', '500mg', 100, 2.50),
('Ibuprofène', '400mg', 75, 3.80),
('Amoxicilline', '250mg', 50, 8.50),
('Doliprane', '1000mg', 150, 3.20),
('Aspirine', '500mg', 5, 2.00),  -- Stock critique pour tester
('Ventoline', '100mcg', 30, 12.50),
('Oméprazole', '20mg', 40, 6.75);

-- Insérer quelques ventes de test
INSERT INTO Vente (id_pharmacien, id_client, id_medicament, quantite, montant_total) VALUES
(1, 1, 1, 2, 5.00),   -- Marie vend 2 Paracétamol à Paul
(1, 2, 2, 1, 3.80),   -- Marie vend 1 Ibuprofène à Julie
(2, 3, 4, 3, 9.60);   -- Pierre vend 3 Doliprane à Lucas

-- Insérer l'historique correspondant aux ventes
INSERT INTO StockHistorique (id_medicament, quantite, type_operation) VALUES
(1, -2, 'VENTE'),
(2, -1, 'VENTE'),
(4, -3, 'VENTE');

-- Insérer une commande de test
INSERT INTO Commande (id_gestionnaire, id_medicament, quantite, statut) VALUES
(1, 5, 100, 'EN_ATTENTE');  -- Sophie commande 100 Aspirine (stock critique)

-- ============================================================
-- REQUÊTES UTILES (pour tester)
-- ============================================================

-- Voir tous les médicaments avec leur stock
-- SELECT * FROM Medicament;

-- Voir les médicaments en stock critique (moins de 10)
-- SELECT * FROM Medicament WHERE stock < 10;

-- Voir toutes les ventes avec les noms
-- SELECT v.id_vente, p.prenom AS pharmacien, c.prenom AS client, 
--        m.nom AS medicament, v.quantite, v.montant_total, v.date_vente
-- FROM Vente v
-- JOIN Pharmacien p ON v.id_pharmacien = p.id_pharmacien
-- JOIN Client c ON v.id_client = c.id_client
-- JOIN Medicament m ON v.id_medicament = m.id_medicament;

-- ============================================================
-- FIN DU SCRIPT
-- ============================================================

SELECT 'Base de données créée avec succès!' AS Message;
