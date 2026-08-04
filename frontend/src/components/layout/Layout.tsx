import React, { useEffect, useMemo, useState } from 'react';
import { Outlet } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import Sidebar from './Sidebar';
import Navbar from './Navbar';

const Layout: React.FC = () => {
  const { user, logout } = useAuth();
  const [collapsed, setCollapsed] = useState(false);
  const [theme, setTheme] = useState<'light' | 'dark'>(() => {
    const stored = localStorage.getItem('theme');
    return stored === 'dark' ? 'dark' : 'light';
  });

  const handleLogout = () => {
    logout();
    window.location.href = '/login';
  };

  const toggleTheme = () => {
    setTheme((current) => (current === 'light' ? 'dark' : 'light'));
  };

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }, [theme]);

  const userName = useMemo(() => {
    if (!user) return 'Utilisateur';
    return `${user.prenom} ${user.nom}`;
  }, [user]);

  return (
    <div className={collapsed ? 'app-shell app-shell--collapsed' : 'app-shell'}>
      <Sidebar collapsed={collapsed} onToggle={() => setCollapsed((prev) => !prev)} isConsultant={user?.role === 'CONSULTANT'} onLogout={handleLogout} />
      <div className="app-main">
        <Navbar theme={theme} onToggleTheme={toggleTheme} userName={userName} />
        <main className="app-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
