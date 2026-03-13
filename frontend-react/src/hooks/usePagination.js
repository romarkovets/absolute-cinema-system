import { useState } from 'react';

export const usePagination = (initialPage = 0, initialSize = 10) => {
  const [page, setPage] = useState(initialPage);
  const [pageSize, setPageSize] = useState(initialSize);
  const [totalPages, setTotalPages] = useState(0);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handlePageSizeChange = (newSize) => {
    setPageSize(newSize);
    setPage(0);
  };

  return {
    page,
    pageSize,
    totalPages,
    setTotalPages,
    handlePageChange,
    handlePageSizeChange
  };
};