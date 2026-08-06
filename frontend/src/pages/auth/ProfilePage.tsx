import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import userService from '../../services/userService';
import type { ProfileUpdatePayload } from '../../types/user';
import Alert from '../../components/ui/Alert';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';

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
      const payload: ProfileUpdatePayload = {
        nom: profile.nom,
        prenom: profile.prenom,
        email: profile.email,
        role: user?.role,
      };
      const response = await userService.updateProfile(payload);
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
      await userService.updatePassword({
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
    <div className="ui-page">
      <div className="ui-card">
        <div>
          <h2 className="h5" style={{ marginBottom: 16 }}>
            Mon profil
          </h2>
          {message && <Alert variant="success">{message}</Alert>}
          {error && <Alert variant="danger">{error}</Alert>}

          <form onSubmit={handleProfileSave} className="ui-grid ui-grid--2" style={{ gap: 16, marginBottom: 24 }}>
            <Input
              label="Nom"
              type="text"
              value={profile.nom}
              onChange={(event) => setProfile({ ...profile, nom: event.target.value })}
            />
            <Input
              label="Prénom"
              type="text"
              value={profile.prenom}
              onChange={(event) => setProfile({ ...profile, prenom: event.target.value })}
            />
            <Input
              label="Email"
              type="email"
              value={profile.email}
              onChange={(event) => setProfile({ ...profile, email: event.target.value })}
            />
            <div className="ui-form-actions" style={{ gridColumn: '1 / -1' }}>
              <Button type="submit" variant="primary" disabled={loading}>
                {loading ? 'Enregistrement...' : 'Enregistrer le profil'}
              </Button>
            </div>
          </form>

          <h2 className="h5" style={{ marginBottom: 16 }}>
            Changer le mot de passe
          </h2>
          <form onSubmit={handlePasswordSave} className="ui-grid ui-grid--3" style={{ gap: 16 }}>
            <Input
              label="Mot de passe actuel"
              type="password"
              value={password.currentPassword}
              onChange={(event) => setPassword({ ...password, currentPassword: event.target.value })}
            />
            <Input
              label="Nouveau mot de passe"
              type="password"
              value={password.newPassword}
              onChange={(event) => setPassword({ ...password, newPassword: event.target.value })}
            />
            <Input
              label="Confirmer le mot de passe"
              type="password"
              value={password.confirmPassword}
              onChange={(event) => setPassword({ ...password, confirmPassword: event.target.value })}
            />
            <div className="ui-form-actions" style={{ gridColumn: '1 / -1' }}>
              <Button type="submit" variant="secondary" disabled={loading}>
                {loading ? 'Enregistrement...' : 'Modifier le mot de passe'}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
