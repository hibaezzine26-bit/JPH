# Présentation de l'architecture du projet PFA

## Titre
Architecture logicielle du système de gestion des pièces de rechange (PDR)

## Objectif
Présenter une architecture simple, claire, professionnelle et adaptée à un projet académique de fin d'étude.

## Choix architectural
Le projet suit une architecture Spring Boot classique en couches, combinée à une architecture React simple pour le frontend.

Cette approche a été choisie pour :
- garder une structure facile à comprendre,
- favoriser la maintenabilité,
- respecter les bonnes pratiques de développement,
- rester adaptée à un projet de type PFA.

## Architecture backend
Le backend est organisé selon les couches suivantes :
- Controller : exposition des endpoints REST
- Service : logique métier
- Repository : accès aux données
- Entity : représentation des tables MySQL
- DTO : objets de transfert de données
- Mapper : transformation entre entités et DTO
- Security / Config / Exception : sécurité, configuration et gestion des erreurs

## Architecture frontend
Le frontend est organisé selon une logique simple :
- Pages : interfaces principales de l'application
- Components : composants réutilisables
- Services : appels API REST
- Context : gestion de l'authentification
- Types / Utils / Constants : organisation et réutilisation du code

## Avantages de cette architecture
- Structure claire et logique
- Séparation des responsabilités
- Facilité de maintenance
- Adaptée à une soutenance universitaire
- Évolutive sans complexité inutile

## Conclusion
Cette architecture permet de construire une application robuste, lisible et professionnelle, tout en restant simple et compréhensible pour un projet académique.
