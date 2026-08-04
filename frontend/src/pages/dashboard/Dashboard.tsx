import React, { useEffect, useMemo, useState } from 'react';
import api from '../../services/api';
import SearchBar from '../../components/ui/SearchBar';
import StatCard from '../../components/ui/StatCard';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';
import Loading from '../../components/ui/Loading';
import Badge from '../../components/ui/Badge';

interface ReportingDto {
  id?: number;
  numeroDA: string;
  numeroDossier: string;
  numero: string;
  codeOracle: string;
  codeSAP: string;
  description?: string;
  uniteDeMesure: string;
  quantite: number;
  secteur: string;
  commande: string;
  fournisseur: string;
  pourcentageLivraison: number;
  delaiLivraison: number;
  dateNotification: string;
  datePrevisionnelle: string;
  statut: string;
  responsable: string;
  commentaire?: string;
  dateCreation?: string;
  dateModification?: string;
}

const statutOptions = ['', 'EN_COURS', 'ATTENTE_LIVRAISON', 'LIVRE', 'ECARTE', 'ADJUGE', 'LITIGE', 'ANNULE'];
const secteurOptions = ['', 'AMMONIAC', 'SOUFRE', 'EXPORT', 'COMMUN'];
const responsableOptions = ['', 'ATTOUCHI', 'BELYAZID', 'REGUIG', 'EL_HARKI'];

const statusLabels: Record<string, string> = {
  EN_COURS: 'En cours',
  ATTENTE_LIVRAISON: 'Attente',
  LIVRE: 'Livré',
  ECARTE: 'Écarté',
  ADJUGE: 'Adjugé',
  LITIGE: 'Litige',
  ANNULE: 'Annulé',
};

const statusVariants: Record<string, 'success' | 'warning' | 'danger' | 'info' | 'default'> = {
  EN_COURS: 'info',
  ATTENTE_LIVRAISON: 'warning',
  LIVRE: 'success',
  ECARTE: 'danger',
  ADJUGE: 'info',
  LITIGE: 'danger',
  ANNULE: 'danger',
};

const statusCards = ['EN_COURS', 'LIVRE', 'ATTENTE_LIVRAISON', 'ECARTE', 'ADJUGE', 'LITIGE', 'ANNULE'];

const Dashboard: React.FC = () => {
  const [reportings, setReportings] = useState<ReportingDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [filters, setFilters] = useState({
    search: '',
    statut: '',
    secteur: '',
    responsable: '',
    fournisseur: '',
    sort: '',
  });

  const loadReportings = async () => {
    setLoading(true);
    setErrorMessage(null);
    try {
      const params: Record<string, string> = {};
      if (filters.search) params.search = filters.search;
      if (filters.statut) params.statut = filters.statut;
      if (filters.secteur) params.secteur = filters.secteur;
      if (filters.responsable) params.responsable = filters.responsable;
      if (filters.fournisseur) params.fournisseur = filters.fournisseur;
      if (filters.sort) params.sort = filters.sort;
      const response = await api.get<ReportingDto[]>('/reportings', { params });
      setReportings(response.data);
    } catch (error) {
      setErrorMessage('Impossible de charger les reportings.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadReportings();
  }, [filters]);

  const handleReset = () => {
    setFilters({ search: '', statut: '', secteur: '', responsable: '', fournisseur: '', sort: '' });
  };

  const handleExport = async () => {
    try {
      const params: Record<string, string> = {};
      if (filters.search) params.search = filters.search;
      if (filters.statut) params.statut = filters.statut;
      if (filters.secteur) params.secteur = filters.secteur;
      if (filters.responsable) params.responsable = filters.responsable;
      if (filters.fournisseur) params.fournisseur = filters.fournisseur;
      if (filters.sort) params.sort = filters.sort;
      params.format = 'xlsx';

      const response = await api.get('/reportings/export', {
        params,
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'reportings.xlsx');
      document.body.appendChild(link);
      link.click();
      link.parentNode?.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error: any) {
      const status = error?.response?.status;
      if (status === 401) {
        setErrorMessage('Authentification requise pour l’export. Veuillez vous reconnecter.');
      } else if (status === 403) {
        setErrorMessage('Vous n’êtes pas autorisé(e) à exporter les reportings.');
      } else {
        setErrorMessage('Impossible de télécharger l’export.');
      }
      console.error('Export error', error);
    }
  };

  const statusCounts = useMemo(() => {
    const counts: Record<string, number> = {
      EN_COURS: 0,
      ATTENTE_LIVRAISON: 0,
      LIVRE: 0,
      ECARTE: 0,
      ADJUGE: 0,
      LITIGE: 0,
      ANNULE: 0,
    };
    reportings.forEach((report) => {
      if (report.statut in counts) {
        counts[report.statut] += 1;
      }
    });
    return counts;
  }, [reportings]);

  const totalCount = reportings.length;
  const chartData = statusCards.map((key) => ({
    key,
    label: statusLabels[key],
    count: statusCounts[key],
    ratio: totalCount ? Math.round((statusCounts[key] / totalCount) * 100) : 0,
  }));

  return (
    <div className="ui-section">
      <div className="ui-toolbar">
        <div className="ui-toolbar__title">
          <h1>Tableau de bord</h1>
          <p className="ui-toolbar__subtitle">Vue synthétique et actions rapides sur les reportings.</p>
        </div>
        <div className="ui-toolbar__right">
          <Button variant="ghost" className="ui-button--small" onClick={handleReset}>
            Réinitialiser
          </Button>
          <Button variant="primary" className="ui-button--small" onClick={handleExport}>
            Exporter
          </Button>
        </div>
      </div>

      <div className="ui-section ui-section--grid">
        <StatCard title="Total reportings" value={totalCount} subtitle="Tous statuts confondus" />
        <StatCard title="En cours" value={statusCounts.EN_COURS} subtitle="Livraison en cours" />
        <StatCard title="Attente" value={statusCounts.ATTENTE_LIVRAISON} subtitle="Prêt à livrer" />
        <StatCard title="Livré" value={statusCounts.LIVRE} subtitle="Reportings terminés" />
        <StatCard title="Écarté" value={statusCounts.ECARTE} subtitle="Refusés / stoppés" />
        <StatCard title="Adjugé" value={statusCounts.ADJUGE} subtitle="Déjà attribués" />
        <StatCard title="Litige" value={statusCounts.LITIGE} subtitle="Interventions requises" />
        <StatCard title="Annulé" value={statusCounts.ANNULE} subtitle="Annulés" />
      </div>

      <Card>
        <div className="ui-toolbar">
          <div className="ui-toolbar__left">
            <SearchBar value={filters.search} onChange={(value) => setFilters({ ...filters, search: value })} />
          </div>
          <div className="ui-toolbar__right">
            <Badge label={`${totalCount} reportings`} variant="info" />
          </div>
        </div>

        <div className="filter-panel__row mb-4">
          <div>
            <select
              className="form-select"
              value={filters.statut}
              onChange={(event) => setFilters({ ...filters, statut: event.target.value })}
            >
              {statutOptions.map((option) => (
                <option key={option} value={option}>
                  {option || 'Tous les statuts'}
                </option>
              ))}
            </select>
          </div>
          <div>
            <select
              className="form-select"
              value={filters.secteur}
              onChange={(event) => setFilters({ ...filters, secteur: event.target.value })}
            >
              {secteurOptions.map((option) => (
                <option key={option} value={option}>
                  {option || 'Tous les secteurs'}
                </option>
              ))}
            </select>
          </div>
          <div>
            <select
              className="form-select"
              value={filters.responsable}
              onChange={(event) => setFilters({ ...filters, responsable: event.target.value })}
            >
              {responsableOptions.map((option) => (
                <option key={option} value={option}>
                  {option || 'Tous les responsables'}
                </option>
              ))}
            </select>
          </div>
          <div>
            <input
              type="text"
              className="form-control"
              placeholder="Fournisseur"
              value={filters.fournisseur}
              onChange={(event) => setFilters({ ...filters, fournisseur: event.target.value })}
            />
          </div>
        </div>

        {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

        <div className="ui-section">
          <Card>
            <div className="section-heading">
              <p className="section-heading__subtitle">Analyse statuts</p>
              <h2 className="section-heading__title">Répartition des statuts</h2>
            </div>
            <div className="chart-bars">
              {chartData.map((item) => (
                <div key={item.key} className="chart-bars__item">
                  <span className="chart-bars__label">{item.label}</span>
                  <div className="chart-bars__track">
                    <div className="chart-bars__fill" style={{ width: `${item.ratio}%` }} />
                  </div>
                  <span className="chart-bars__value">{item.count}</span>
                </div>
              ))}
            </div>
          </Card>

          <Card>
            <div className="section-heading">
              <p className="section-heading__subtitle">Derniers reportings</p>
              <h2 className="section-heading__title">Vue rapide</h2>
            </div>
            {loading ? (
              <Loading />
            ) : reportings.length === 0 ? (
              <div className="text-muted">Aucun reporting trouvé.</div>
            ) : (
              <div className="app-table-responsive">
                <table className="app-table">
                  <thead>
                    <tr>
                      <th>DA</th>
                      <th>Commande</th>
                      <th>Fournisseur</th>
                      <th>Statut</th>
                      <th>Responsable</th>
                    </tr>
                  </thead>
                  <tbody>
                    {reportings.slice(0, 6).map((item) => (
                      <tr key={item.id}>
                        <td>{item.numeroDA}</td>
                        <td>{item.commande}</td>
                        <td>{item.fournisseur}</td>
                        <td>
                          <Badge label={statusLabels[item.statut] ?? item.statut} variant={statusVariants[item.statut] ?? 'default'} />
                        </td>
                        <td>{item.responsable}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </Card>
        </div>
      </Card>
    </div>
  );
};

export default Dashboard;
