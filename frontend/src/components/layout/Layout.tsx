import React, { useEffect, useState } from 'react';
import { Link, useNavigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  LayoutDashboard,
  FileSpreadsheet,
  History,
  BarChart3,
  LogOut,
  User,
  Moon,
  Sun,
} from 'lucide-react';

const logoUrl = new URL('../../assets/ocp-logo.png', import.meta.url).href;

const Layout: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [theme, setTheme] = useState<'light' | 'dark'>(() => {
    const stored = localStorage.getItem('theme');
    return stored === 'dark' ? 'dark' : 'light';
  });

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const toggleTheme = () => {
    setTheme((current) => (current === 'light' ? 'dark' : 'light'));
  };

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }, [theme]);

  const isConsultant = user?.role === 'CONSULTANT';

  const menuItems = [
    ...(isConsultant
      ? []
      : [
          { icon: LayoutDashboard, label: 'Tableau de bord', path: '/' },
          { icon: FileSpreadsheet, label: 'Import Excel', path: '/import' },
          { icon: BarChart3, label: 'Reporting', path: '/reporting' },
          { icon: History, label: 'Historique', path: '/historique' },
          { icon: User, label: 'Profil', path: '/profile' },
        ]),
  ];


  return (
    <div className="min-vh-100 bg-light text-dark">
      <header className="topbar topbar--clean">
        <div className="container d-flex align-items-center justify-content-between py-2">
          <div className="d-flex align-items-center gap-3">
            <a href="/" className="d-flex align-items-center text-decoration-none">
              <div className="logo-frame d-flex align-items-center justify-content-center">
                <img src={logoUrl} alt="OCP logo" className="logo-img" />
              </div>
              <div className="ms-2 d-none d-md-block">
                <div className="text-uppercase small text-muted mb-0">OCP</div>
                <div className="fw-bold">JPH</div>
              </div>
            </a>
          </div>

          <nav className="topbar-nav d-none d-lg-flex align-items-center">
            {menuItems.map((item) => {
              const active = location.pathname === item.path;
              return (
                <Link key={item.path} to={item.path} className={`topbar-link ${active ? 'active' : ''}`}>
                  <item.icon size={16} className="me-2" />
                  {item.label}
                  <span className="chev ms-2">?</span>
                </Link>
              );
            })}
          </nav>

          <div className="d-flex align-items-center gap-3">
            <button type="button" className="btn btn-light btn-sm d-flex align-items-center" onClick={toggleTheme}>
              {theme === 'light' ? <Moon size={16} /> : <Sun size={16} />}
              <span className="ms-2">{theme === 'light' ? 'Sombre' : 'Clair'}</span>
            </button>
            <button type="button" className="btn btn-light btn-sm d-flex align-items-center" onClick={handleLogout}>
              <LogOut size={16} />
              <span className="ms-2">Déconnexion</span>
            </button>
          </div>
        </div>
      </header>

      <main className="container-fluid flex-grow-1 p-4">
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;
