import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { useAuth } from '../../context/AuthContext';

interface ProfileForm {
  nom: string;
  prenom: string;
  email: string;
}

interface PasswordForm {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

const ProfilePage: React.FC = () => {
  const navigate = useNavigate();
  const { user, updateUser, logout } = useAuth();
  const [profile, setProfile] = useState<ProfileForm>({ nom: '', prenom: '', email: '' });
  const [password, setPassword] = useState<PasswordForm>({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      setProfile({ nom: user.nom, prenom: user.prenom, email: user.email });
    }
  }, [user]);

  const handleProfileSave = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    setMessage(null);
    setLoading(true);

    try {
      const response = await api.put('/utilisateurs/me', {
        nom: profile.nom,
        prenom: profile.prenom,
        email: profile.email,
        role: user?.role,
      });
      if (response.data) {
        if (response.data.email !== user?.email) {
          setMessage('Profil mis à jour avec succès. Veuillez vous reconnecter avec votre nouvel email.');
          logout();
          navigate('/login');
          return;
        }
        updateUser(response.data);
      }
      setMessage('Profil mis à jour avec succès.');
    } catch (err) {
      setError('Impossible de mettre à jour le profil.');
    } finally {
      setLoading(false);
    }
  };

  const handlePasswordSave = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    setMessage(null);

    if (password.newPassword !== password.confirmPassword) {
      setError('Les nouveaux mots de passe ne correspondent pas.');
      return;
    }

    setLoading(true);
    try {
      await api.put('/utilisateurs/me/mot-de-passe', {
        currentPassword: password.currentPassword,
        newPassword: password.newPassword,
      });
      setMessage('Mot de passe modifié avec succès. Veuillez vous reconnecter.');
      setPassword({ currentPassword: '', newPassword: '', confirmPassword: '' });
      logout();
      navigate('/login');
    } catch (err) {
      setError('Impossible de modifier le mot de passe.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container-fluid">
      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <h2 className="h5 mb-3">Mon profil</h2>
          {message && <div className="alert alert-success">{message}</div>}
          {error && <div className="alert alert-danger">{error}</div>}

          <form onSubmit={handleProfileSave} className="row g-3 mb-4">
            <div className="col-md-6">
              <label className="form-label">Nom</label>
              <input
                type="text"
                className="form-control"
                value={profile.nom}
                onChange={(event) => setProfile({ ...profile, nom: event.target.value })}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Prénom</label>
              <input
                type="text"
                className="form-control"
                value={profile.prenom}
                onChange={(event) => setProfile({ ...profile, prenom: event.target.value })}
              />
            </div>
            <div className="col-md-12">
              <label className="form-label">Email</label>
              <input
                type="email"
                className="form-control"
                value={profile.email}
                onChange={(event) => setProfile({ ...profile, email: event.target.value })}
              />
            </div>
            <div className="col-12 d-flex gap-2">
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Enregistrement...' : 'Enregistrer le profil'}
              </button>
            </div>
          </form>

          <h2 className="h5 mb-3">Changer le mot de passe</h2>
          <form onSubmit={handlePasswordSave} className="row g-3">
            <div className="col-md-4">
              <label className="form-label">Mot de passe actuel</label>
              <input
                type="password"
                className="form-control"
                value={password.currentPassword}
                onChange={(event) => setPassword({ ...password, currentPassword: event.target.value })}
              />
            </div>
            <div className="col-md-4">
              <label className="form-label">Nouveau mot de passe</label>
              <input
                type="password"
                className="form-control"
                value={password.newPassword}
                onChange={(event) => setPassword({ ...password, newPassword: event.target.value })}
              />
            </div>
            <div className="col-md-4">
              <label className="form-label">Confirmer le mot de passe</label>
              <input
                type="password"
                className="form-control"
                value={password.confirmPassword}
                onChange={(event) => setPassword({ ...password, confirmPassword: event.target.value })}
              />
            </div>
            <div className="col-12 d-flex gap-2">
              <button type="submit" className="btn btn-secondary" disabled={loading}>
                {loading ? 'Enregistrement...' : 'Modifier le mot de passe'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
