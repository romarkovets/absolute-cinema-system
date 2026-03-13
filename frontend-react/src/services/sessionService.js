    import api from './api';

    export const sessionService = {
      getAll: (page = 0, size = 10, filters = {}) => {
        const params = {
          page,
          size,
          ...(filters.date && { date: filters.date }),
          ...(filters.movieId && { movieId: filters.movieId }),
          ...(filters.hallId && { hallId: filters.hallId })
        };
        return api.get('/sessions', { params });
      },

      getById: (id) => api.get(`/sessions/${id}`),

      create: (data) => api.post('/sessions', data),

      update: (id, data) => api.put(`/sessions/${id}`, data),

      delete: (id) => api.delete(`/sessions/${id}`)
    };