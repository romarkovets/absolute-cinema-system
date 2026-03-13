import React from 'react';
import PropTypes from 'prop-types';
import { Box, Pagination as MuiPagination } from '@mui/material';

const Pagination = ({ page, setPage, totalPages }) => {
  const handleChange = (event, value) => {
    setPage(value - 1);
  };

  if (totalPages <= 1) return null;

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', p: 2 }}>
      <MuiPagination
        count={totalPages}
        page={page + 1}
        onChange={handleChange}
        color="primary"
        size="large"
        showFirstButton
        showLastButton
      />
    </Box>
  );
};

Pagination.propTypes = {
  page: PropTypes.number.isRequired,
  setPage: PropTypes.func.isRequired,
  totalPages: PropTypes.number.isRequired
};

export default Pagination;