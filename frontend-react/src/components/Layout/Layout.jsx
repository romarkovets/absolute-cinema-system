import React from 'react';
import PropTypes from 'prop-types';
import Navbar from '../Navbar/Navbar';
import './Layout.css';

const Layout = ({ children }) => {
  return (
    <div className="layout">
      <Navbar />
      <main className="main-content">
        {children}
      </main>
    </div>
  );
};

Layout.propTypes = {
  children: PropTypes.node.isRequired
};

export default Layout;