import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  className?: string;
}

const Button: React.FC<ButtonProps> = ({ variant = 'primary', className = '', children, ...props }) => (
  <button className={`ui-button ui-button--${variant} ${className}`} {...props}>
    {children}
  </button>
);

export default Button;
