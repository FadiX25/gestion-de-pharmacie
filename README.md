# 💊 Système de Gestion de Pharmacie

Un système complet de gestion de pharmacie développé en Java.
Ce projet utilise des fichiers CSV pour le stockage des données (pas besoin de base de données externe!).

## 📋 Description

Cette application permet de gérer une pharmacie avec deux types d'utilisateurs:

- **Pharmacien**: Gère les ventes, les clients et consulte le stock
- **Gestionnaire**: Gère les commandes, les médicaments et génère des rapports

## 🚀 Démarrage Rapide

### Prérequis

- Java JDK 8 ou supérieur installé
- Un IDE (VS Code, IntelliJ IDEA, Eclipse) ou un terminal

### Installation

1. **Cloner ou télécharger le projet**

2. **Compiler le projet**:
   ```bash
   # Naviguer vers le dossier du projet
   cd "gestion de pharmacie"
   
   # Créer le dossier bin pour les fichiers compilés
   mkdir bin
   
   # Compiler tous les fichiers Java
   javac -d bin -sourcepath src src/Main.java src/models/*.java src/dao/*.java src/services/*.java src/database/*.java
   ```

3. **Exécuter l'application**:
   ```bash
   java -cp bin Main
   ```

## 👤 Comptes de Test

L'application contient des données de test prêtes à l'emploi:

| Type | Login | Mot de passe |
|------|-------|--------------|
| Pharmacien | pdupont | pharma123 |
| Pharmacien | mmartin | pharma123 |
| Gestionnaire | aadmin | admin123 |
| Gestionnaire | lbernard | gestio123 |

## 📁 Structure du Projet

```
gestion de pharmacie/
├── src/
│   ├── Main.java              # Point d'entrée de l'application
│   ├── models/                # Classes métier
│   │   ├── Utilisateur.java   # Classe abstraite de base
│   │   ├── Pharmacien.java    # Représente un pharmacien
│   │   ├── Gestionnaire.java  # Représente un gestionnaire
│   │   ├── Medicament.java    # Représente un médicament
│   │   ├── Client.java        # Représente un client
│   │   ├── Vente.java         # Représente une vente
│   │   ├── Commande.java      # Représente une commande
│   │   └── StockHistorique.java # Historique des mouvements
│   │
│   ├── dao/                   # Accès aux données (CSV)
│   │   ├── PharmacienDAO.java
│   │   ├── GestionnaireDAO.java
│   │   ├── MedicamentDAO.java
│   │   ├── ClientDAO.java
│   │   ├── VenteDAO.java
│   │   ├── CommandeDAO.java
│   │   └── StockHistoriqueDAO.java
│   │
│   ├── services/              # Logique métier
│   │   ├── AuthenticationService.java
│   │   ├── MedicamentService.java
│   │   ├── VenteService.java
│   │   ├── CommandeService.java
│   │   ├── ClientService.java
│   │   └── RapportService.java
│   │
│   └── database/              # Utilitaires CSV
│       └── CSVHelper.java     # Lecture/écriture des fichiers CSV
│
├── data/                      # Fichiers de données CSV
│   ├── pharmaciens.csv
│   ├── gestionnaires.csv
│   ├── medicaments.csv
│   ├── clients.csv
│   ├── ventes.csv
│   ├── commandes.csv
│   └── stock_historique.csv
│
├── docs/                      # Documentation
│   └── context.md             # Contexte du projet
│
└── README.md                  # Ce fichier
```

## 🎯 Fonctionnalités

### Pharmacien
- 🔍 Rechercher des médicaments
- 📋 Voir la liste des médicaments
- 💰 Enregistrer une vente
- 📊 Consulter ses ventes
- ❌ Annuler une vente
- ⚠️ Voir les alertes de stock
- 👥 Gérer les clients

### Gestionnaire
- 📦 Gérer les médicaments (ajouter, modifier)
- 📝 Créer des commandes de réapprovisionnement
- ✅ Valider les livraisons
- ⚠️ Voir les alertes de stock
- 📊 Générer des rapports

## 💾 Stockage des Données

Les données sont stockées dans des fichiers CSV dans le dossier `data/`.
Le format CSV utilise le point-virgule (;) comme séparateur.

**Exemple de fichier pharmaciens.csv:**
```
id;nom;prenom;login;password
1;Dupont;Pierre;pdupont;pharma123
2;Martin;Marie;mmartin;pharma123
```

### Avantages du stockage CSV:
- ✅ Pas besoin d'installer une base de données
- ✅ Fichiers lisibles avec n'importe quel éditeur de texte
- ✅ Facile à sauvegarder et transférer
- ✅ Simple à comprendre pour les débutants

## 🎓 Concepts Java Utilisés

Ce projet illustre plusieurs concepts fondamentaux de Java:

1. **POO (Programmation Orientée Objet)**
   - Classes et objets
   - Héritage (Pharmacien/Gestionnaire extends Utilisateur)
   - Encapsulation (getters/setters)
   - Abstraction (classe abstraite Utilisateur)

2. **Collections**
   - ArrayList pour les listes
   - Parcours avec boucles for-each

3. **Entrées/Sorties**
   - Lecture/écriture de fichiers
   - BufferedReader, PrintWriter
   - Scanner pour l'entrée utilisateur

4. **Gestion des exceptions**
   - try-catch pour les erreurs d'IO
   - Validation des entrées

5. **Architecture en couches**
   - Modèles (données)
   - DAO (accès aux données)
   - Services (logique métier)
   - Main (présentation)

## 🔧 Dépannage

### Le programme ne compile pas
- Vérifiez que Java est installé: `java -version`
- Vérifiez que vous êtes dans le bon dossier
- Assurez-vous que tous les fichiers .java sont présents

### Erreur de fichier non trouvé
- Le dossier `data/` est créé automatiquement au premier lancement
- Les fichiers CSV sont également créés automatiquement

### Problème d'encodage des caractères
- Utilisez un éditeur qui supporte UTF-8
- Sous Windows, vous pouvez utiliser: `chcp 65001` dans le terminal

## 📝 Licence

Projet éducatif - Libre d'utilisation pour l'apprentissage.

## 👨‍💻 Auteur

Développé par un étudiant en première année de Java.

---

*Bon apprentissage! 💪*
