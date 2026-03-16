import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import {
  Paper,
  Grid,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button
} from '@mui/material';
import { movieService } from '../../services/movieService';
import { hallService } from '../../services/hallService';
import './Filters.css';

const Filters = ({ filters, setFilters }) => {
  const [movies, setMovies] = useState([]);
  const [halls, setHalls] = useState([]);

  const [localFilters, setLocalFilters] = useState(() => ({
    date: filters.date || '',
    movieId: filters.movieId || '',
    hallId: filters.hallId || ''
  }));

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [moviesRes, hallsRes] = await Promise.all([
          movieService.getAll(),
          hallService.getAll()
        ]);
        setMovies(moviesRes.data);
        setHalls(hallsRes.data);
      } catch (error) {
        console.error('Ошибка загрузки фильтров:', error);
      }
    };
    fetchData();
  }, []);

  const handleApply = () => {
    setFilters(localFilters);
  };

  const handleReset = () => {
    const resetFilters = {
      date: '',
      movieId: '',
      hallId: ''
    };
    setLocalFilters(resetFilters);
    setFilters(resetFilters);
  };

  return (
    <Paper className="filters-paper" sx={{ p: 3, m: 2 }}>
      <Grid container spacing={3} alignItems="center" sx={{ width: '100%' }}>
        <Grid item xs={12} md={2}>
          <TextField
            fullWidth
            type="date"
            label="Дата"
            value={localFilters.date || ''}
            onChange={(e) => setLocalFilters({ ...localFilters, date: e.target.value })}
            slotProps={{ inputLabel: { shrink: true } }}
            size="medium"
            placeholder="Все даты"
          />
        </Grid>

        <Grid item xs={12} md={5}>
          <FormControl fullWidth size="medium">
            <InputLabel id="movie-select-label" shrink={true}>Фильм</InputLabel>
            <Select
              labelId="movie-select-label"
              value={localFilters.movieId || ''}
              onChange={(e) => setLocalFilters({ ...localFilters, movieId: e.target.value })}
              label="Фильм"
              displayEmpty
              renderValue={(selected) => {
                if (selected === '') {
                  return <span>Все фильмы</span>;
                }
                const movie = movies.find(m => m.movieId === selected);
                return movie?.title || selected;
              }}
            >
              <MenuItem value="">Все фильмы</MenuItem>
              {movies.map((movie) => (
                <MenuItem key={movie.movieId} value={movie.movieId}>
                  {movie.title}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12} md={3}>
          <FormControl fullWidth size="medium">
            <InputLabel id="hall-select-label" shrink={true}>Зал</InputLabel>
            <Select
              labelId="hall-select-label"
              value={localFilters.hallId || ''}
              onChange={(e) => setLocalFilters({ ...localFilters, hallId: e.target.value })}
              label="Зал"
              displayEmpty
              renderValue={(selected) => {
                if (selected === '') {
                  return <span>Все залы</span>;
                }
                const hall = halls.find(h => h.hallId === selected);
                return hall ? `Зал ${hall.hallNumber} (${hall.hallTypeName})` : selected;
              }}
            >
              <MenuItem value="">Все залы</MenuItem>
              {halls.map((hall) => (
                <MenuItem key={hall.hallId} value={hall.hallId}>
                  Зал {hall.hallNumber} ({hall.hallTypeName})
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12} md={2}>
          <Grid container spacing={1}>
            <Grid item xs={12} sm={6} md={12}>
              <Button
                variant="contained"
                onClick={handleApply}
                size="large"
                fullWidth
              >
                Применить
              </Button>
            </Grid>
            <Grid item xs={12} sm={6} md={12}>
              <Button
                variant="outlined"
                onClick={handleReset}
                size="large"
                fullWidth
              >
                Сбросить
              </Button>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Paper>
  );
};

Filters.propTypes = {
  filters: PropTypes.shape({
    date: PropTypes.string,
    movieId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    hallId: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
  }).isRequired,
  setFilters: PropTypes.func.isRequired
};

export default Filters;