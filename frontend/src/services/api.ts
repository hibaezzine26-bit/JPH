import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const auth = localStorage.getItem('auth');
  if (auth) {
    config.headers = config.headers ?? {};
    (config.headers as Record<string, string>)['Authorization'] = `Basic ${auth}`;
  }
  return config;
});

export default api;
