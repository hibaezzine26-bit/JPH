import React from 'react';

interface BadgeProps {
  label: string;
  variant?: 'default' | 'success' | 'warning' | 'danger' | 'info';
  className?: string;
}

const Badge: React.FC<BadgeProps> = ({ label, variant = 'default', className = '' }) => (
  <span className={`ui-badge ui-badge--${variant} ${className}`}>{label}</span>
);

export default Badge;
