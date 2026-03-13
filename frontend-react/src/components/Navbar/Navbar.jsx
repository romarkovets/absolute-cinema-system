import React from 'react';
import { AppBar, Toolbar, Typography, Box } from '@mui/material';
import { TheaterComedy } from '@mui/icons-material';
import './Navbar.css';

const Navbar = () => {
  return (
    <AppBar position="static" sx={{ backgroundColor: '#1976d2' }}>
      <Toolbar>
        <TheaterComedy sx={{ mr: 2, fontSize: 30 }} />
        <Typography variant="h5" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
          Кинотеатр «АБСОЛЮТ»
        </Typography>
        <Box>
          <Typography variant="subtitle1" sx={{ opacity: 0.9 }}>
            Админ-панель
          </Typography>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;