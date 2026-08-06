import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Alert from '../../components/ui/Alert';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await login(email, password);
      navigate('/');
    } catch (err) {
      setError('Email ou mot de passe incorrect');
    }
  };

  return (
    <div className="ui-page-center">
      <div className="ui-card" style={{ width: '100%', maxWidth: 420 }}>
        <div>
          <h2 className="h4 ui-text-center">Connexion</h2>
          <p className="ui-text-muted ui-text-center" style={{ marginBottom: 24 }}>
            Saisissez votre email et votre mot de passe.
          </p>

          {error && <Alert variant="danger">{error}</Alert>}

          <form onSubmit={handleSubmit}>
            <Input
              id="email"
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="exemple@entreprise.com"
            />
            <Input
              id="password"
              label="Mot de passe"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="••••••••"
            />

            <Button type="submit" variant="primary" className="ui-button--block">
              Se connecter
            </Button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
