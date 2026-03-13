import React, { useState, useEffect, useCallback } from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  IconButton,
  CircularProgress,
  Alert,
  Box,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from '@mui/material';
import { Edit, Delete, Add } from '@mui/icons-material';
import { sessionService } from '../../services/sessionService';
import Filters from '../../components/Filters/Filters';
import Pagination from '../../components/Pagination/Pagination';
import SessionForm from '../../components/SessionForm/SessionForm';
import { formatDateTime, formatPrice } from '../../utils/formatters';
import './SessionsPage.css';

function SessionsPage() {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(3);
  const [totalPages, setTotalPages] = useState(0);
  const [filters, setFilters] = useState({
    date: '',
    movieId: '',
    hallId: ''
  });
  const [openForm, setOpenForm] = useState(false);
  const [editingSession, setEditingSession] = useState(null);

  const fetchSessions = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await sessionService.getAll(page, pageSize, filters);
      setSessions(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setError('Ошибка загрузки сеансов');
      console.error('Error fetching sessions:', err);
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, filters]);

  useEffect(() => {
    fetchSessions();
  }, [fetchSessions]);

  const handleDelete = async (id) => {
    const confirm = globalThis.confirm('Удалить сеанс?');
    if (!confirm) return;

    try {
      await sessionService.delete(id);
      fetchSessions();
    } catch (err) {
      setError('Ошибка удаления');
      console.error('Error deleting session:', err);
    }
  };

  const handleEdit = (session) => {
    setEditingSession(session);
    setOpenForm(true);
  };

  const handleAdd = () => {
    setEditingSession(null);
    setOpenForm(true);
  };

  const handleFormClose = () => {
    setOpenForm(false);
    fetchSessions();
  };

  const handlePageSizeChange = (newSize) => {
    setPageSize(newSize);
    setPage(0);
  };

  return (
    <div className="sessions-page">
      <Box className="header" sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4">🎬 Управление сеансами</Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={handleAdd}
        >
          Добавить сеанс
        </Button>
      </Box>

      <Filters filters={filters} setFilters={setFilters} />

      {error && <Alert severity="error" sx={{ m: 2 }}>{error}</Alert>}

      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper} sx={{ m: 2, width: 'auto' }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ width: '20%' }}>Фильм</TableCell>
                <TableCell sx={{ width: '30%' }}>Актеры</TableCell>
                <TableCell sx={{ width: '15%' }}>Зал</TableCell>
                <TableCell sx={{ width: '20%' }}>Дата и время</TableCell>
                <TableCell sx={{ width: '10%' }}>Цена</TableCell>
                <TableCell sx={{ width: '5%' }}>Действия</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {sessions.map((session) => (
                <TableRow key={session.sessionId}>
                  <TableCell>{session.movie?.title}</TableCell>
                  <TableCell>
                    {session.movie?.actors?.map((actor) => (
                      <div key={actor.actorId}>🎭 {actor.fullName}</div>
                    ))}
                  </TableCell>
                  <TableCell>Зал {session.hall?.hallNumber} ({session.hall?.hallTypeName})</TableCell>
                  <TableCell>{formatDateTime(session.sessionDate, session.startTime)}</TableCell>
                  <TableCell>{formatPrice(session.price)}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleEdit(session)} color="primary">
                      <Edit />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(session.sessionId)} color="error">
                      <Delete />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: 2, p: 2 }}>
        <Pagination page={page} setPage={setPage} totalPages={totalPages} />

        <FormControl size="small" sx={{ minWidth: 120 }}>
          <InputLabel id="page-size-label">На странице</InputLabel>
          <Select
            labelId="page-size-label"
            value={pageSize}
            label="На странице"
            onChange={(e) => handlePageSizeChange(e.target.value)}
          >
            <MenuItem value={3}>3</MenuItem>
            <MenuItem value={5}>5</MenuItem>
            <MenuItem value={10}>10</MenuItem>
            <MenuItem value={20}>20</MenuItem>
          </Select>
        </FormControl>
      </Box>

      <SessionForm
        open={openForm}
        onClose={handleFormClose}
        session={editingSession}
      />
    </div>
  );
}

export default SessionsPage;