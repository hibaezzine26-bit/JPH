# Schéma backend détaillé

## Architecture Spring Boot en couches

```text
Client / Navigateur
        |
        v
Controller REST
  ┌───────────────────────────────┐
  │ ReportingController            │
  │ HistoriqueController          │
  │ UtilisateurController        │
  │ ImportExcelController        │
  └──────────────┬────────────────┘
                 |
                 v
           Service Layer
  ┌───────────────────────────────┐
  │ ReportingService              │
  │ HistoriqueService            │
  │ UtilisateurService          │
  │ ImportExcelService          │
  └──────────────┬────────────────┘
                 |
                 v
         Repository Layer
  ┌───────────────────────────────┐
  │ ReportingRepository           │
  │ HistoriqueRepository         │
  │ UtilisateurRepository       │
  │ ImportExcelRepository       │
  └──────────────┬────────────────┘
                 |
                 v
         Base de données MySQL


## Composants principaux

### 1. Controller
Responsable de l'exposition des endpoints REST.
- reçoit les requêtes HTTP
- délègue au service
- retourne les réponses HTTP

### 2. Service
Contient la logique métier.
- traitement des données
- validations métier
- orchestration des opérations
- appel aux repositories

### 3. Repository
Accès aux données via Spring Data JPA.
- sauvegarde
- recherche
- mise à jour
- suppression

### 4. Entity
Représente les tables de la base de données.
- Mapping JPA
- relations entre objets

### 5. DTO
Objet de transfert de données utilisé pour l'API.
- protège l'entité
- facilite l'échange entre couches

### 6. Mapper
Transformation Entity <-> DTO.
- améliore la lisibilité
- sépare la couche API et la couche domaine

### 7. Security / Config / Exception
- SecurityConfig : configuration de sécurité
- UserDataInitializer : création des utilisateurs par défaut
- GlobalExceptionHandler : gestion centralisée des erreurs
