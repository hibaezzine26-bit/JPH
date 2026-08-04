import React, { useEffect, useState } from 'react';
import api from '../../services/api';

interface HistoriqueDto {
  id: number;
  action: string;
  ancienneValeur: string;
  nouvelleValeur: string;
  dateAction: string;
  reportingId: number;
}

const HistoriquePage: React.FC = () => {
  const [historiques, setHistoriques] = useState<HistoriqueDto[]>([]);
  const [loading, setLoading] = useState(false);

  const loadHistoriques = async () => {
    setLoading(true);
    try {
      const response = await api.get<HistoriqueDto[]>('/historiques');
      setHistoriques(response.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHistoriques();
  }, []);

  return (
    <div className="container-fluid">
      <div className="card shadow-sm mb-4">
        <div className="card-body d-flex flex-column flex-md-row justify-content-between gap-3 align-items-start">
          <div>
            <p className="text-uppercase text-muted small mb-1">Historique</p>
            <h1 className="h4 mb-0">Suivi des actions</h1>
          </div>
          <span className="badge bg-secondary py-2 px-3">{historiques.length} événements enregistrés</span>
        </div>
      </div>

      <div className="card shadow-sm">
        <div className="card-body table-responsive">
          <table className="table table-striped mb-0">
            <thead className="table-light">
              <tr>
                <th>Action</th>
                <th>Ancienne valeur</th>
                <th>Nouvelle valeur</th>
                <th>Reporting</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={5} className="text-center text-muted py-4">
                    Chargement...
                  </td>
                </tr>
              ) : historiques.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center text-muted py-4">
                    Aucun historique trouvé.
                  </td>
                </tr>
              ) : (
                historiques.map((item) => (
                  <tr key={item.id}>
                    <td>{item.action}</td>
                    <td>{item.ancienneValeur || '—'}</td>
                    <td>{item.nouvelleValeur || '—'}</td>
                    <td>{item.reportingId}</td>
                    <td>{new Date(item.dateAction).toLocaleString()}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default HistoriquePage;
