# 📘 Documentation Technique et Opérationnelle - Projet JPH (OCP Group)

**Application Web de Gestion, Suivi, Reporting et Traçabilité des Pièces de Rechange (PDR)**  
*Groupe OCP — Direction des Opérations / Service JPH I/J*

---

## 📋 Table des Matières

1. [Présentation Générale & Contexte Métier](#1-présentation-générale--contexte-métier)
2. [Architecture Technique & Stack Technologique](#2-architecture-technique--stack-technologique)
3. [Structure du Code Source](#3-structure-du-code-source)
4. [Dictionnaire de Données (Base de données `pdr_reporting_db`)](#4-dictionnaire-de-données-base-de-données-pdr_reporting_db)
5. [Spécification des API REST (Endpoints HTTP)](#5-spécification-des-api-rest-endpoints-http)
6. [Sécurité & Contrôle d'Accès (JWT & RBAC)](#6-sécurité--contrôle-daccès-jwt--rbac)
7. [Module d'Importation & Exportation Excel (Apache POI)](#7-module-dimportation--exportation-excel-apache-poi)
8. [Tests Automatisés & Assurance Qualité (JUnit 5 & Mockito)](#8-tests-automatisés--assurance-qualité-junit-5--mockito)
9. [Guide d'Installation, Configuration et Démarrage](#9-guide-dinstallation-configuration-et-démarrage)

---

## 1. 📌 Présentation Générale & Contexte Métier

Le projet **JPH** a été développé pour le **Groupe OCP** afin d'automatiser et de digitaliser le processus de gestion et de suivi opérationnel des **Pièces de Rechange (PDR)** et des **Demandes d'Achat (DA)**.

### Problématique Initiale
Auparavant, le suivi des approvisionnements reposait sur la manipulation manuelle de volumineux fichiers Excel dispersés entre différents intervenants. Cette approche entraînait :
- Une perte de centralisation des données.
- L'absence d'historique et de traçabilité des modifications.
- Un risque élevé d'erreurs de saisie.
- Des difficultés pour obtenir des indicateurs synthétiques (KPI) en temps réel.

### Solution Apportée par JPH
L'application JPH centralise les données sur une plateforme web sécurisée offrant :
- **Un suivi en temps réel** des statuts de livraison, pourcentages d'avancement et délais contractuels.
- **Un contrôle d'accès strict (RBAC)** différenciant les privilèges des Administrateurs et des Consultants.
- **L'historisation automatique** de chaque modification apportée à un dossier PDR.
- **L'importation/exportation fluide** de fichiers Excel (.xlsx) via la bibliothèque Apache POI.
- **Un Tableau de bord interactif** doté de cartes KPI et de graphiques statistiques.

---

## 2. 🛠️ Architecture Technique & Stack Technologique

L'application adopte une architecture logicielle découplée **SPA (Single Page Application) / API REST** :

```text
+-------------------------------------------------------+
|                Couche Présentation                    |
|             React 18 + TypeScript + Vite              |
+-------------------------------------------------------+
                           |
                     HTTP / JSON (REST)
                     Bearer JWT Token
                           v
+-------------------------------------------------------+
|              Couche Serveur & Sécurité                |
|       Spring Boot 3 + Spring Security 6 + JWT         |
+-------------------------------------------------------+
                           |
                  Spring Data JPA / ORM
                           v
+-------------------------------------------------------+
|                Couche Persistance                     |
|                 SGBD MySQL 8.0                        |
+-------------------------------------------------------+
```

### Stack Technologique détaillée :

| Composant | Technologie / Librairie | Rôle & Description |
| :--- | :--- | :--- |
| **Backend** | **Java 21 (LTS)** | Langage de programmation principal |
| | **Spring Boot 3.4 / 4.x** | Framework applicatif backend découplé |
| | **Spring Security** | Gestion de la sécurité et des autorisations |
| | **JSON Web Token (jjwt 0.12.6)** | Authentification sans état (Stateless) |
| | **Spring Data JPA / Hibernate** | Mappage objet-relationnel (ORM) et requêtes MySQL |
| | **Apache POI (5.2.3)** | Parsing et génération de fichiers Excel (.xlsx) |
| | **Lombok** | Génération automatique des Getters/Setters et constructeurs |
| **Frontend** | **React 18** | Bibliothèque d'interface utilisateur réactive |
| | **TypeScript** | Typage statique et sécurisation du code client |
| | **Vite** | Toolchain et serveur de développement ultra-rapide |
| | **React Router 7** | Routage dynamique et protection des routes |
| | **Axios** | Client HTTP pour l'interaction avec l'API REST |
| | **Recharts** | Visualisation graphique des données statistiques (PieChart, BarChart) |
| | **Lucide React** | Bibliothèque d'icônes vectorielles |
| **SGBD** | **MySQL 8.0** | Base de données relationnelle (`pdr_reporting_db`) |

---

## 3. 📂 Structure du Code Source

```text
JPH/
├── pom.xml                                           # Descripteur de dépendances Maven Backend
├── src/main/resources/
│   └── application.properties                        # Configuration MySQL & Hibernate
├── src/main/java/com/ocp/jph/
│   ├── JphApplication.java                           # Classe principale de démarrage Spring Boot
│   ├── config/
│   │   └── SecurityConfig.java                       # Configuration Spring Security & CORS
│   ├── security/
│   │   ├── JwtAuthenticationFilter.java              # Filtre d'interception des requêtes HTTP
│   │   ├── JwtService.java                           # Utilitaire de génération/validation JWT
│   │   └── CustomUserDetailsService.java              # Chargement des utilisateurs par email
│   ├── controller/
│   │   ├── AuthenticationController.java             # Endpoints d'authentification
│   │   ├── ReportingController.java                  # Endpoints CRUD, filtres, import/export PDR
│   │   ├── HistoriqueController.java                 # Endpoints de consultation des audits
│   │   ├── ImportExcelController.java                # Endpoints de gestion du journal d'import
│   │   └── UtilisateurController.java                # Endpoints de gestion des utilisateurs
│   ├── service/
│   │   ├── ReportingService.java                     # Logique métier PDR & parsing Apache POI
│   │   ├── UtilisateurService.java                   # Gestion des comptes & chiffrement BCrypt
│   │   ├── HistoriqueService.java                    # Logique de traçabilité
│   │   └── ImportExcelService.java                   # Journalisation des imports Excel
│   ├── repository/
│   │   ├── ReportingRepository.java                  # Interface JPA pour Reporting
│   │   ├── UtilisateurRepository.java                # Interface JPA pour Utilisateur
│   │   ├── HistoriqueRepository.java                 # Interface JPA pour Historique
│   │   └── ImportExcelRepository.java                # Interface JPA pour ImportExcel
│   ├── entity/                                       # Entités JPA & Énumérations
│   │   ├── Reporting.java, Utilisateur.java, Historique.java, ImportExcel.java
│   │   └── Role.java, Statut.java, Secteur.java, Responsable.java, Udm.java
│   └── dto/ & mapper/                                # Objets de transfert & Mappers bidirectionnels
├── src/test/java/com/ocp/jph/                       # Suite de tests unitaires (JUnit 5 & Mockito)
│   ├── security/JwtServiceTest.java
│   └── service/ReportingServiceTest.java, ImportExcelServiceTest.java
└── frontend/                                         # Application Client React TypeScript
    ├── package.json
    ├── vite.config.ts
    └── src/
        ├── App.tsx, main.tsx, index.css              # Entrée principale & styles globaux
        ├── pages/                                    # Vues (Dashboard, Reporting, Import, Historique, Auth)
        ├── components/                               # Composants UI (ReportingTable, StatCard, Badge, Modal)
        ├── services/                                 # Services API Axios (`api.ts`, `reportingService.ts`)
        ├── context/                                  # Gestionnaire d'état AuthContext
        ├── types/                                    # Interfaces TypeScript (`reporting.ts`, `user.ts`)
        └── utils/                                    # Colonnes dynamiques par rôle & helpers
```

---

## 4. 🗄️ Dictionnaire de Données (Base de données `pdr_reporting_db`)

### Table : `utilisateur`
Stocke les identifiants et les privilèges d'accès des utilisateurs.
- `id` (`BIGINT`, PK, Auto-increment) : Identifiant unique.
- `nom` (`VARCHAR(255)`) : Nom de l'utilisateur.
- `prenom` (`VARCHAR(255)`) : Prénom de l'utilisateur.
- `email` (`VARCHAR(255)`, Unique, Not Null) : Adresse de connexion.
- `mot_de_passe` (`VARCHAR(255)`, Not Null) : Empreinte hachée avec BCrypt.
- `role` (`VARCHAR(50)`, Enum) : `ADMINISTRATEUR` ou `CONSULTANT`.

### Table : `reporting`
Stocke les dossiers de suivi des pièces de rechange (PDR).
- `id` (`BIGINT`, PK, Auto-increment) : Identifiant unique du dossier.
- `numero_da` (`VARCHAR(255)`) : Numéro de la Demande d'Achat (DA).
- `numero_dossier` (`VARCHAR(255)`) : Numéro de référence dossier.
- `numero` (`VARCHAR(255)`) : Numéro d'ordre.
- `code_oracle` (`VARCHAR(255)`) : Référence article Oracle.
- `code_sap` (`VARCHAR(255)`) : Référence article SAP.
- `description` (`VARCHAR(2000)`) : Description technique de la pièce.
- `unite_de_mesure` (`VARCHAR(50)`, Enum) : `PCE`, `ML`, `MC`, `U`, `KG`.
- `quantite` (`DOUBLE`) : Quantité de pièces retenue.
- `secteur` (`VARCHAR(50)`, Enum) : `AMMONIAC`, `SOUFRE`, `EXPORT`, `COMMUN`.
- `commande` (`VARCHAR(255)`) : Numéro de Bon de Commande (CMD).
- `fournisseur` (`VARCHAR(255)`) : Nom de l'équipementier / fournisseur.
- `pourcentage_livraison` (`INT`) : Avancement physique de livraison (0 à 100%).
- `delai_livraison` (`INT`) : Délai contractuel de livraison (jours).
- `date_notification` (`DATE`) : Date d'envoi de la notification.
- `date_previsionnelle` (`DATE`) : Date estimée de réception sur site.
- `statut` (`VARCHAR(50)`, Enum) : `EN_COURS`, `ATTENTE_LIVRAISON`, `LIVRE`, `ECARTE`, `ADJUGE`, `LITIGE`, `ANNULE`.
- `responsable` (`VARCHAR(50)`, Enum) : `ATTOUCHI`, `BELYAZID`, `REGUIG`, `EL_HARKI`.
- `utilisateur_id` (`BIGINT`, FK) : Utilisateur créateur/propriétaire.
- `commentaire` (`VARCHAR(2000)`) : Observations métier.
- `date_creation` (`DATETIME`) : Horodatage de création.
- `date_modification` (`DATETIME`) : Horodatage de mise à jour.

### Table : `historique`
Garde la trace immuable de chaque modification effectuée.
- `id` (`BIGINT`, PK, Auto-increment) : Identifiant de la trace.
- `reporting_id` (`BIGINT`, FK) : Dossier reporting concerné.
- `champ_modifie` (`VARCHAR(255)`) : Champ altéré.
- `ancienne_valeur` (`VARCHAR(2000)`) : Valeur d'origine.
- `nouvelle_valeur` (`VARCHAR(2000)`) : Nouvelle valeur appliquée.
- `date_action` (`DATETIME`) : Horodatage de l'action.
- `modifie_par` (`VARCHAR(255)`) : Email de l'auteur de la modification.

### Table : `import_excel`
Conserve le journal des téléversements de fichiers Excel.
- `id` (`BIGINT`, PK, Auto-increment) : Identifiant unique d'import.
- `nom_fichier` (`VARCHAR(255)`) : Nom du fichier `.xlsx`.
- `date_import` (`DATETIME`) : Horodatage de l'importation.
- `nombre_lignes` (`INT`) : Nombre de lignes insérées avec succès.
- `statut_import` (`VARCHAR(50)`) : Statut du résultat (`SUCCES`, `ERREUR`).

---

## 5. 🔌 Spécification des API REST (Endpoints HTTP)

### Authentification (`/api/auth`)
- `POST /api/auth/login` : Authentification utilisateur. Renvoie un jeton JWT et le profil.

### Utilisateurs (`/api/utilisateurs`)
- `POST /api/utilisateurs/register` : Inscription d'un utilisateur.
- `GET /api/utilisateurs/me` : Récupération du profil de l'utilisateur connecté.
- `PUT /api/utilisateurs/me` : Mise à jour du profil connecté.
- `PUT /api/utilisateurs/me/mot-de-passe` : Changement du mot de passe.

### Reportings (`/api/reportings`)
- `GET /api/reportings` : Liste des reportings avec filtres (`search`, `statut`, `secteur`, `responsable`, `fournisseur`, `commande`, `sort`).
- `GET /api/reportings/{id}` : Détails d'un reporting spécifique.
- `POST /api/reportings` : Création d'un dossier PDR (*ADMINISTRATEUR*).
- `PUT /api/reportings/{id}` : Modification d'un dossier PDR (*ADMINISTRATEUR*).
- `DELETE /api/reportings/{id}` : Suppression d'un dossier PDR (*ADMINISTRATEUR*).
- `POST /api/reportings/import` : Téléversement d'un fichier Excel `.xlsx` (*ADMINISTRATEUR*).
- `GET /api/reportings/export` : Exportation des données sous format Excel (`.xlsx`) ou CSV.

### Historiques (`/api/historiques`)
- `GET /api/historiques` : Consultation de la liste des audits de modification (*ADMINISTRATEUR*).

---

## 6. 🔒 Sécurité & Contrôle d'Accès (JWT & RBAC)

La sécurité repose sur l'architecture sans état **Spring Security + JWT** :

1. **Génération du Jeton** : Lors d'une connexion réussie (`/api/auth/login`), un jeton signed JWT (HMAC-SHA256) est généré contenant les claims : `sub` (email) et `role` (`ROLE_ADMINISTRATEUR` ou `ROLE_CONSULTANT`).
2. **Interception HTTP** : Le filtre [JwtAuthenticationFilter.java](file:///c:/Users/HP/Desktop/JPH/src/main/java/com/ocp/jph/security/JwtAuthenticationFilter.java) intercepte chaque requête, extrait le jeton dans l'en-tête `Authorization: Bearer <token>`, valide la signature et initialise le `SecurityContextHolder`.
3. **Contrôle par Rôle (RBAC)** :
   - **ADMINISTRATEUR** : Possède les droits complets CRUD (Création, Lecture, Modification, Suppression, Importation Excel et consultation de l'Historique).
   - **CONSULTANT** : Possède des droits d'accès strictement en **Lecture Seule** (Consultation, Recherche, Filtrage et Exportation Excel/CSV). Les fonctionnalités d'édition, création, suppression et import lui sont masquées et bloquées au niveau HTTP (403 Forbidden).

---

## 7. 📊 Module d'Importation & Exportation Excel (Apache POI)

- **Importation (`ImportExcelService` / `ReportingService`)** :
  Le fichier `.xlsx` téléversé est analysé par la bibliothèque **Apache POI**. Les en-têtes sont validées par normalisation unicode (vérification des colonnes obligatoires : *DA, Dossier, N°, code Oracle, Code SAP, Description, UDM, Q retenue, Secteur, Fournisseur, CMD, %Livraison, Délai livraison, Date notification, Date Prévisionnelle livraison, Commentaire, Statut Livraison, Responsable Dossier*). Les données sont converties et enregistrées en base de données avec gestion fine des erreurs par ligne.
- **Exportation (`exportToExcel`)** :
  Génère dynamiquement un classeur `XSSFWorkbook` stylisé (en-têtes en gras, colonnes auto-ajustées) contenant le jeu de données filtré et le retourne sous forme de flux binaire téléchargeable (`byte[]`).

---

## 8. 🧪 Tests Automatisés & Assurance Qualité (JUnit 5 & Mockito)

L'application intègre une suite de tests automatisés couvrant les services critiques :

| Classe de Test | Nombre de Tests | Résultat |
| :--- | :---: | :---: |
| `JwtServiceTest` | 1 | **100% SUCCÈS** |
| `ImportExcelServiceTest` | 4 | **100% SUCCÈS** |
| `ReportingServiceTest` | 5 | **100% SUCCÈS** |
| **TOTAL** | **10** | **`BUILD SUCCESS`** |

Exécution des tests via Maven :
```bash
.\mvnw.cmd test
```

---

## 9. 🚀 Guide d'Installation, Configuration et Démarrage

### Prérequis
- Java JDK 21 ou supérieur.
- Node.js 18+ & npm.
- Base de données MySQL 8.0 active.

### 1. Configuration de la base de données MySQL
Créer la base de données dans MySQL :
```sql
CREATE DATABASE pdr_reporting_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Dans `src/main/resources/application.properties`, adaptez les identifiants si nécessaire :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pdr_reporting_db?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=
```

### 2. Démarrage du Backend (Spring Boot)
À la racine du projet :
```bash
.\mvnw.cmd spring-boot:run
```
Le serveur backend démarre sur `http://localhost:8080`.

### 3. Démarrage du Frontend (React + Vite)
Dans le dossier `frontend` :
```bash
cd frontend
npm install
npm run dev
```
L'application client démarre sur `http://localhost:5173`.
