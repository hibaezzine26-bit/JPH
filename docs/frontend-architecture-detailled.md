# Schéma frontend détaillé

## Architecture React + TypeScript

```text
Utilisateur
   |
   v
Pages
├── LoginPage
├── Dashboard
├── ReportingPage
├── ImportPage
├── HistoriquePage
└── ProfilePage
   |
   v
Components
├── common/
│   ├── Button
│   ├── Input
│   ├── Modal
│   ├── Card
│   ├── Loader
│   ├── Table
│   └── Pagination
├── layout/
│   ├── Navbar
│   ├── Sidebar
│   └── Layout
└── reporting/
    └── ReportingTable
   |
   v
Context / Hooks
├── AuthContext
├── useAuth
└── hooks personnalisés
   |
   v
Services API
├── authService
├── reportingService
├── historiqueService
├── userService
└── api
   |
   v
Backend Spring Boot
```

## Rôle des dossiers

### pages
Représente les vues principales de l'application.
- LoginPage
- Dashboard
- ReportingPage
- ImportPage
- HistoriquePage
- ProfilePage

### components/common
Composants réutilisables et génériques.
- Button
- Input
- Modal
- Card
- Loader
- Table
- Pagination

### components/layout
Composants de structure de l'interface.
- Navbar
- Sidebar
- Layout

### components/reporting
Composants spécifiques au reporting.
- ReportingTable

### services
Centralise les appels HTTP vers l'API REST.

### context
Gère l'authentification et les données globales de l'application.

### types
Définit les interfaces et modèles TypeScript.

### constants / utils / styles
Permettent d'éviter la duplication et d'améliorer la lisibilité du code.
