import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, mdp: string) => Promise<void>;
  logout: () => void;
  updateUser: (user: User) => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchMe = async () => {
    try {
      const response = await api.get('/utilisateurs/me');
      setUser(response.data);
    } catch (error) {
      localStorage.removeItem('auth');
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const auth = localStorage.getItem('auth');
    if (auth) {
      fetchMe();
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email: string, mdp: string) => {
    const authHeader = btoa(`${email}:${mdp}`);
    localStorage.setItem('auth', authHeader);
    try {
      const response = await api.get('/utilisateurs/me');
      setUser(response.data);
    } catch (error) {
      localStorage.removeItem('auth');
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('auth');
    setUser(null);
  };

  const updateUser = (user: User) => {
    setUser(user);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, updateUser, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
