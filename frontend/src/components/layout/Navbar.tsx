import React from 'react';
import { Moon, Sun, Bell, Search } from 'lucide-react';

interface NavbarProps {
  userName: string;
  onToggleTheme: () => void;
  theme: 'light' | 'dark';
}

const Navbar: React.FC<NavbarProps> = ({ userName, onToggleTheme, theme }) => (
  <header className="app-navbar">
    <div className="app-navbar__search">
      <Search size={18} className="app-navbar__search-icon" />
      <input type="search" placeholder="Rechercher..." aria-label="Recherche" />
    </div>

    <div className="app-navbar__actions">
      <button type="button" className="app-navbar__icon-btn" onClick={onToggleTheme}>
        {theme === 'light' ? <Moon size={18} /> : <Sun size={18} />}
      </button>
      <button type="button" className="app-navbar__icon-btn">
        <Bell size={18} />
      </button>
      <div className="app-navbar__profile">
        <div className="app-navbar__avatar">{userName.charAt(0).toUpperCase()}</div>
        <div className="app-navbar__user">
          <span>Bienvenue</span>
          <strong>{userName}</strong>
        </div>
      </div>
    </div>
  </header>
);

export default Navbar;
