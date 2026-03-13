import api from './api';

export const movieService = {
  getAll: () => api.get('/movies'),
  search: (title) => api.get(`/movies/search?title=${title}`)
};