import api from './api';
import type { User } from '../types/user';

const authService = {
  login: async (authHeader: string) => {
    const response = await api.get<User>('/utilisateurs/me', {
      headers: {
        Authorization: `Basic ${authHeader}`,
      },
    });
    localStorage.setItem('auth', authHeader);
    return response;
  },
  logout: () => {
    localStorage.removeItem('auth');
  },
  getCurrentUser: () => api.get<User>('/utilisateurs/me'),
};

export default authService;
