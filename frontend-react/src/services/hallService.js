import api from './api';

export const hallService = {
  getAll: () => api.get('/halls')
};