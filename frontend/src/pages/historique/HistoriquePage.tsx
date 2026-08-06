import React, { useEffect, useState } from 'react';
import historiqueService from '../../services/historiqueService';

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
      const response = await historiqueService.getAll();
      setHistoriques(response.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHistoriques();
  }, []);

  return (
    <div className="ui-page">
      <div className="ui-card" style={{ marginBottom: 24 }}>
        <div className="ui-toolbar" style={{ alignItems: 'flex-start', gap: 12, flexWrap: 'wrap' }}>
          <div>
            <p className="ui-form-group__label" style={{ textTransform: 'uppercase', letterSpacing: '0.12em', marginBottom: 4, color: 'var(--text-secondary)', fontSize: '0.8rem' }}>
              Historique
            </p>
            <h1 className="h4" style={{ margin: 0 }}>
              Suivi des actions
            </h1>
          </div>
          <span className="ui-badge ui-badge--default">{historiques.length} événements enregistrés</span>
        </div>
      </div>

      <div className="ui-card">
        <div className="app-table-responsive">
          <table className="app-table app-table--striped">
            <thead className="app-table__head">
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
                  <td colSpan={5} className="ui-text-center ui-text-muted" style={{ padding: '1rem 0' }}>
                    Chargement...
                  </td>
                </tr>
              ) : historiques.length === 0 ? (
                <tr>
                  <td colSpan={5} className="ui-text-center ui-text-muted" style={{ padding: '1rem 0' }}>
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
