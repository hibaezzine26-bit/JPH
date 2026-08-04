import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const auth = localStorage.getItem('auth');
  if (auth) {
    if (!config.headers) {
      config.headers = {} as any;
    }
    (config.headers as Record<string, string>)['Authorization'] = `Basic ${auth}`;
  }
  return config;
});

export default api;
