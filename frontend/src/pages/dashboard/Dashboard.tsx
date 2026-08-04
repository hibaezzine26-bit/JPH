import React, { useEffect, useState } from 'react';
import api from '../../services/api';

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

const sortOptions = [
  { value: '', label: 'Trier par défaut' },
  { value: 'numero,asc', label: 'N° croissant' },
  { value: 'numero,desc', label: 'N° décroissant' },
];

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

      // demander explicitement le format Excel (.xlsx)
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

  return (
    <div className="container-fluid">
      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3 mb-3">
            <div>
              <h1 className="h4 mb-1">Consultant - Reportings</h1>
            </div>
            <div className="d-flex gap-2 flex-wrap">
              <button type="button" className="btn btn-primary" onClick={handleExport}>
                Exporter
              </button>
            </div>
          </div>

          <div className="row g-3 mb-3">
            <div className="col-md-3">
              <input
                type="search"
                className="form-control"
                placeholder="Rechercher..."
                value={filters.search}
                onChange={(event) => setFilters({ ...filters, search: event.target.value })}
              />
            </div>
            <div className="col-md-2">
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
            <div className="col-md-2">
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
            <div className="col-md-2">
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
            <div className="col-md-2">
              <input
                type="text"
                className="form-control"
                placeholder="Fournisseur"
                value={filters.fournisseur}
                onChange={(event) => setFilters({ ...filters, fournisseur: event.target.value })}
              />
            </div>
            <div className="col-md-1">
              <select
                className="form-select"
                value={filters.sort}
                onChange={(event) => setFilters({ ...filters, sort: event.target.value })}
              >
                {sortOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="d-flex justify-content-between mb-3">
            <div className="d-flex gap-2 flex-wrap">
              <button type="button" className="btn btn-outline-secondary" onClick={handleReset}>
                Réinitialiser
              </button>
            </div>
            <span className="text-muted">{reportings.length} reportings affichés</span>
          </div>

          {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

          <div className="table-responsive">
            <table className="table table-striped align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th>N°</th>
                  <th>Code Oracle</th>
                  <th>Code SAP</th>
                  <th>Description</th>
                  <th>UDM</th>
                  <th>Quantité</th>
                  <th>Secteur</th>
                  <th>CMD</th>
                  <th>Fournisseur</th>
                  <th>% Livraison</th>
                  <th>Délai</th>
                  <th>Statut</th>
                  <th>Responsable</th>
                  <th>Commentaire</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan={14} className="text-center py-4 text-muted">
                      Chargement...
                    </td>
                  </tr>
                ) : reportings.length === 0 ? (
                  <tr>
                    <td colSpan={16} className="text-center py-4 text-muted">
                      Aucun reporting trouvé.
                    </td>
                  </tr>
                ) : (
                  reportings.map((item) => (
                    <tr key={item.id}>
                      <td>{item.numero}</td>
                      <td>{item.codeOracle}</td>
                      <td>{item.codeSAP}</td>
                      <td>{item.description}</td>
                      <td>{item.uniteDeMesure}</td>
                      <td>{item.quantite ?? ''}</td>
                      <td>{item.secteur}</td>
                      <td>{item.commande}</td>
                      <td>{item.fournisseur}</td>
                      <td>{item.pourcentageLivraison ?? ''}%</td>
                      <td>{item.delaiLivraison ?? ''}</td>
                      <td>{item.statut}</td>
                      <td>{item.responsable}</td>
                      <td>{item.commentaire}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
