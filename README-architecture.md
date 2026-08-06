# Architecture du projet JPH

## Vue d'ensemble

Ce projet suit une architecture Spring Boot classique en couches, adaptée à un projet académique de PFA. Elle reste simple, claire et facile à expliquer lors d'une soutenance.

## Architecture backend

Le backend est organisé autour des couches suivantes :

- config : configuration Spring Boot, sécurité et initialisation des données
- controller : contrôleurs REST
- service : logique métier
- repository : interfaces Spring Data JPA
- entity : entités JPA
- dto : objets de transfert de données
- mapper : conversion Entity <-> DTO
- exception : gestion centralisée des erreurs
- security : configuration et composants liés à la sécurité

Structure principale :

```text
src/main/java/com/ocp/jph/
├── config/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── exception/
├── security/
└── JphApplication.java
```

## Architecture frontend

Le frontend utilise React + TypeScript + Vite avec une structure simple et modulable.

Structure principale :

```text
frontend/src/
├── assets/
├── components/
│   ├── common/
│   ├── layout/
│   └── reporting/
├── context/
├── hooks/
├── pages/
├── routes/
├── services/
├── types/
├── constants/
├── utils/
├── styles/
├── App.tsx
└── main.tsx
```

## Responsabilités principales

### Backend
- Les contrôleurs exposent les endpoints REST sans logique métier.
- Les services contiennent la logique métier et les traitements applicatifs.
- Les repositories assurent l'accès aux données via Spring Data JPA.
- Les DTO isolent les données exposées à l'API.
- Les mappers réalisent la transformation entre entités et DTO.

### Frontend
- Les composants sont séparés selon leur usage : commun, layout, reporting.
- Les pages représentent les vues principales de l'application.
- Les services centralisent les appels API.
- Le contexte gère l'authentification.
- Les constantes et utilitaires évitent la duplication.

## Points clés de l'architecture

- Respect de la séparation des responsabilités
- Structure simple et maintenable
- Facile à expliquer en soutenance
- Compatible avec une application académique de type PFA
- Pas de complexité inutile

## Technologies utilisées

- Backend : Spring Boot, Spring Security, Spring Data JPA, MySQL
- Frontend : React, TypeScript, Vite
- Communication : API REST
