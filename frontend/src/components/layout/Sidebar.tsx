import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  LayoutDashboard,
  FileSpreadsheet,
  History,
  BarChart3,
  User,
  LogOut,
  ChevronLeft,
  ChevronRight,
} from 'lucide-react';
import ocpLogo from '../../assets/ocp-logo.png';

interface MenuItem {
  label: string;
  path?: string;
  icon: React.ComponentType<{ size?: number; className?: string }>;
  action?: () => void;
}

interface SidebarProps {
  collapsed: boolean;
  onToggle: () => void;
  isConsultant: boolean;
  onLogout: () => void;
}

const Sidebar: React.FC<SidebarProps> = ({ collapsed, onToggle, isConsultant, onLogout }) => {
  const location = useLocation();

  const menuItems: MenuItem[] = [
    { label: 'Dashboard', path: '/', icon: LayoutDashboard },
    { label: 'Reporting', path: '/reporting', icon: BarChart3 },
    { label: 'Import Excel', path: '/import', icon: FileSpreadsheet },
    { label: 'Historique', path: '/historique', icon: History },
    { label: 'Profil', path: '/profile', icon: User },
  ];

  const visibleItems = isConsultant ? menuItems.filter((item) => item.path !== '/import' && item.path !== '/reporting' && item.path !== '/historique') : menuItems;

  return (
    <aside className={collapsed ? 'app-sidebar app-sidebar--collapsed' : 'app-sidebar'}>
      <div className="app-sidebar__brand">
        <div className="app-sidebar__logo">
          <img src={ocpLogo} alt="OCP" />
        </div>
        {!collapsed && (
          <div className="app-sidebar__brand-text">
            <span>OCP</span>
            <strong>JPH</strong>
          </div>
        )}
      </div>

      <nav className="app-sidebar__nav">
        {visibleItems.map((item) => {
          const active = item.path === location.pathname;
          const Icon = item.icon;
          return (
            <Link
              key={item.label}
              to={item.path ?? '#'}
              className={`app-sidebar__item ${active ? 'active' : ''}`}
            >
              <Icon size={18} className="app-sidebar__item-icon" />
              {!collapsed && <span>{item.label}</span>}
            </Link>
          );
        })}
      </nav>

      <div className="app-sidebar__footer">
        <button type="button" className="app-sidebar__logout" onClick={onLogout}>
          <LogOut size={18} className="app-sidebar__item-icon" />
          {!collapsed && <span>Déconnexion</span>}
        </button>

        <button type="button" className="app-sidebar__toggle" onClick={onToggle}>
          {collapsed ? <ChevronRight size={18} /> : <ChevronLeft size={18} />}
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
