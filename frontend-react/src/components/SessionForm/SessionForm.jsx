import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert
} from '@mui/material';
import { sessionService } from '../../services/sessionService';
import { movieService } from '../../services/movieService';
import { hallService } from '../../services/hallService';
import './SessionForm.css';

const SessionForm = ({ open, onClose, session }) => {
  const [formData, setFormData] = useState({
    movieId: '',
    hallId: '',
    sessionDate: '',
    startTime: '',
    price: ''
  });
  const [movies, setMovies] = useState([]);
  const [halls, setHalls] = useState([]);
  const [error, setError] = useState('');

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
        console.error('Ошибка загрузки данных для фильтров:', error);
        setError('Ошибка загрузки данных');
      }
    };
    fetchData();
  }, []);

  useEffect(() => {
    if (session) {
      setFormData({
        movieId: session.movie?.movieId || '',
        hallId: session.hall?.hallId || '',
        sessionDate: session.sessionDate ?
          `${session.sessionDate[0]}-${String(session.sessionDate[1]).padStart(2, '0')}-${String(session.sessionDate[2]).padStart(2, '0')}` : '',
        startTime: session.startTime ?
          `${String(session.startTime[0]).padStart(2, '0')}:${String(session.startTime[1]).padStart(2, '0')}` : '',
        price: session.price || ''
      });
    } else {
      setFormData({
        movieId: '',
        hallId: '',
        sessionDate: '',
        startTime: '',
        price: ''
      });
    }
  }, [session]);

  const handleSubmit = async () => {
    setError('');

    if (!formData.movieId || !formData.hallId || !formData.sessionDate || !formData.startTime || !formData.price) {
      setError('Заполните все поля');
      return;
    }

    try {
      const sessionData = {
        movie: { movieId: Number.parseInt(formData.movieId, 10) },
        hall: { hallId: Number.parseInt(formData.hallId, 10) },
        sessionDate: formData.sessionDate,
        startTime: `${formData.startTime}:00`,
        price: Number.parseFloat(formData.price)
      };

      if (session) {
        await sessionService.update(session.sessionId, sessionData);
      } else {
        await sessionService.create(sessionData);
      }
      onClose();
    } catch (err) {
      const errorMessage = err.response?.data?.message || 'Ошибка сохранения';
      setError(errorMessage);
      console.error('Ошибка при сохранении сеанса:', err);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        {session ? 'Редактирование сеанса' : 'Новый сеанс'}
      </DialogTitle>
      <DialogContent>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

        <FormControl fullWidth margin="normal">
          <InputLabel id="movie-select-label">Фильм</InputLabel>
          <Select
            labelId="movie-select-label"
            value={formData.movieId}
            onChange={(e) => setFormData({ ...formData, movieId: e.target.value })}
            label="Фильм"
          >
            {movies.map((movie) => (
              <MenuItem key={movie.movieId} value={movie.movieId}>
                {movie.title}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl fullWidth margin="normal">
          <InputLabel id="hall-select-label">Зал</InputLabel>
          <Select
            labelId="hall-select-label"
            value={formData.hallId}
            onChange={(e) => setFormData({ ...formData, hallId: e.target.value })}
            label="Зал"
          >
            {halls.map((hall) => (
              <MenuItem key={hall.hallId} value={hall.hallId}>
                Зал {hall.hallNumber} ({hall.hallTypeName})
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <TextField
          fullWidth
          margin="normal"
          type="date"
          label="Дата"
          value={formData.sessionDate}
          onChange={(e) => setFormData({ ...formData, sessionDate: e.target.value })}
          slotProps={{ inputLabel: { shrink: true } }}
        />

        <TextField
          fullWidth
          margin="normal"
          type="time"
          label="Время"
          value={formData.startTime}
          onChange={(e) => setFormData({ ...formData, startTime: e.target.value })}
          slotProps={{ inputLabel: { shrink: true } }}
        />

        <TextField
          fullWidth
          margin="normal"
          type="number"
          label="Цена"
          value={formData.price}
          onChange={(e) => setFormData({ ...formData, price: e.target.value })}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Отмена</Button>
        <Button onClick={handleSubmit} variant="contained" color="primary">
          {session ? 'Сохранить' : 'Создать'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

SessionForm.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  session: PropTypes.shape({
    sessionId: PropTypes.number,
    movie: PropTypes.shape({
      movieId: PropTypes.number
    }),
    hall: PropTypes.shape({
      hallId: PropTypes.number
    }),
    sessionDate: PropTypes.arrayOf(PropTypes.number),
    startTime: PropTypes.arrayOf(PropTypes.number),
    price: PropTypes.number
  })
};

export default SessionForm;