# Documentation API - Authentification JWT

## Endpoint de login

### POST /api/auth/login

Connexion avec email et mot de passe.

### Requête
```json
{
  "email": "admin@example.com",
  "password": "password"
}
```

### Réponse réussie
```json
{
  "token": "<jwt_access_token>",
  "refreshToken": "<jwt_refresh_token>"
}
```

### Utilisation
Ajouter le token dans l'en-tête HTTP :

```http
Authorization: Bearer <jwt_access_token>
```

## Endpoint de refresh

### POST /api/auth/refresh

Rafraîchir le token d'accès avec le refresh token.

### Requête
```json
{
  "refreshToken": "<jwt_refresh_token>"
}
```

### Réponse réussie
```json
{
  "token": "<nouveau_jwt_access_token>"
}
```

## Notes
- Les rôles utilisateur sont intégrés dans le payload JWT.
- Les endpoints protégés utilisent le token Bearer.
