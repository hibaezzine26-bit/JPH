import React from 'react';

interface StatCardProps {
  title: string;
  value: number;
  subtitle?: string;
  className?: string;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, subtitle, className = '' }) => (
  <div className={`stat-card ${className}`}>
    <div className="stat-card__title">{title}</div>
    <div className="stat-card__value">{value}</div>
    {subtitle && <div className="stat-card__subtitle">{subtitle}</div>}
  </div>
);

export default StatCard;
