import React from 'react';
import { Moon, Sun } from 'lucide-react';

interface NavbarProps {
  userName: string;
  onToggleTheme: () => void;
  theme: 'light' | 'dark';
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ userName, onToggleTheme, theme, onLogout }) => (
  <header className="app-navbar">
    <div className="app-navbar__actions">
      <button type="button" className="app-navbar__icon-btn" onClick={onToggleTheme}>
        {theme === 'light' ? <Moon size={18} /> : <Sun size={18} />}
      </button>
      <div className="app-navbar__profile">
        <div className="app-navbar__avatar">{userName.charAt(0).toUpperCase()}</div>
        <div className="app-navbar__user">
          <span>Bienvenue</span>
          <strong>{userName}</strong>
        </div>
      </div>
    </div>

    <div className="app-navbar__controls">
      <button type="button" className="app-navbar__action-btn app-navbar__logout-btn" onClick={onLogout}>
        Déconnexion
      </button>
    </div>
  </header>
);

export default Navbar;
