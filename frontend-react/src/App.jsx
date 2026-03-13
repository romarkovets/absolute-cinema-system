import React from 'react';
import Layout from './components/Layout/Layout';
import SessionsPage from './pages/SessionsPage/SessionsPage';
import './App.css';

function App() {
  return (
    <div className="App">
      <Layout>
        <SessionsPage />
      </Layout>
    </div>
  );
}

export default App;