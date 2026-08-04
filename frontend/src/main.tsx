import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import { AuthProvider } from './context/AuthContext.tsx';
import Layout from './components/layout/Layout.tsx';
import ProtectedRoute from './components/ProtectedRoute.tsx';
import LoginPage from './pages/auth/LoginPage.tsx';
import Dashboard from './pages/dashboard/Dashboard.tsx';
import ImportPage from './pages/import/ImportPage.tsx';
import ReportingPage from './pages/reporting/ReportingPage.tsx';
import HistoriquePage from './pages/historique/HistoriquePage.tsx';
import ProfilePage from './pages/auth/ProfilePage.tsx';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            <Route index element={<ProtectedRoute allowedRoles={['ADMINISTRATEUR', 'CONSULTANT']}><Dashboard /></ProtectedRoute>} />
            <Route path="import" element={<ProtectedRoute allowedRoles={['ADMINISTRATEUR']}><ImportPage /></ProtectedRoute>} />
            <Route path="reporting" element={<ProtectedRoute allowedRoles={['ADMINISTRATEUR']}><ReportingPage /></ProtectedRoute>} />
            <Route path="historique" element={<ProtectedRoute allowedRoles={['ADMINISTRATEUR']}><HistoriquePage /></ProtectedRoute>} />
            <Route path="profile" element={<ProtectedRoute allowedRoles={['ADMINISTRATEUR']}><ProfilePage /></ProtectedRoute>} />
          </Route>
        </Routes>
      </AuthProvider>
    </Router>
  </StrictMode>,
);
