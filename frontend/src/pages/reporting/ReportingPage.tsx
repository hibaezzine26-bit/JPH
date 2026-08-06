import React, { useEffect, useMemo, useState } from 'react';
import { AxiosError } from 'axios';
import { useAuth } from '../../context/AuthContext';
import reportingService from '../../services/reportingService';
import type { ReportingDto } from '../../types/reporting';
import Alert from '../../components/common/Alert';
import SearchBar from '../../components/common/SearchBar';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import Input from '../../components/common/Input';
import SectionHeading from '../../components/common/SectionHeading';
import ReportingTable from '../../components/reporting/ReportingTable';
import { getReportingColumnsByRole } from '../../utils/reportingColumns';

const statutOptions = ['', 'EN_COURS', 'ATTENTE_LIVRAISON', 'LIVRE', 'ECARTE', 'ADJUGE', 'LITIGE', 'ANNULE'];
const secteurOptions = ['', 'AMMONIAC', 'SOUFRE', 'EXPORT', 'COMMUN'];
const responsableOptions = ['', 'ATTOUCHI', 'BELYAZID', 'REGUIG', 'EL_HARKI'];
const udmOptions = ['', 'PCE', 'ML', 'MC', 'U', 'KG'];

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

const ReportingPage: React.FC = () => {
  const { user } = useAuth();
  const [reportings, setReportings] = useState<ReportingDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [saveLoading, setSaveLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [editingReporting, setEditingReporting] = useState<ReportingDto | null>(null);
  const [filters, setFilters] = useState({
    search: '',
    statut: '',
    secteur: '',
    responsable: '',
    fournisseur: '',
  });

  const loadReportings = async () => {
    setLoading(true);
    try {
      const params: Record<string, string> = {};
      if (filters.search) params.search = filters.search;
      if (filters.statut) params.statut = filters.statut;
      if (filters.secteur) params.secteur = filters.secteur;
      if (filters.responsable) params.responsable = filters.responsable;
      if (filters.fournisseur) params.fournisseur = filters.fournisseur;
      const reportingsResponse = await reportingService.getAll(params);
      setReportings(reportingsResponse.data);
    } catch (error) {
      setErrorMessage('Impossible de charger les reportings.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadReportings();
  }, []);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    await loadReportings();
  };

  const handleReset = () => {
    setFilters({ search: '', statut: '', secteur: '', responsable: '', fournisseur: '' });
    setTimeout(loadReportings, 0);
  };

  const createNewReporting = (): ReportingDto => ({
    numeroDA: '',
    numeroDossier: '',
    numero: '',
    codeOracle: '',
    codeSAP: '',
    description: '',
    uniteDeMesure: '',
    quantite: NaN,
    secteur: '',
    commande: '',
    fournisseur: '',
    pourcentageLivraison: NaN,
    delaiLivraison: NaN,
    dateNotification: '',
    datePrevisionnelle: '',
    statut: '',
    responsable: '',
    utilisateurId: user?.id,
    commentaire: '',
  });

  const handleCreate = () => {
    setErrorMessage(null);
    setEditingReporting(createNewReporting());
  };

  const handleEdit = async (id: number | undefined) => {
    if (id == null) {
      setErrorMessage('Identifiant du reporting manquant.');
      return;
    }
    setLoading(true);
    try {
      const response = await reportingService.getById(id);
      setEditingReporting(response.data);
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage('Impossible de charger le reporting.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number | undefined) => {
    if (id == null) {
      setErrorMessage('Identifiant du reporting manquant.');
      return;
    }
    if (!window.confirm('Supprimer ce reporting ?')) {
      return;
    }
    setLoading(true);
    try {
      await reportingService.remove(id);
      await loadReportings();
    } catch (error) {
      setErrorMessage('Impossible de supprimer le reporting.');
    } finally {
      setLoading(false);
    }
  };

  const updateEditingField = (field: keyof ReportingDto, value: string | number | null | undefined) => {
    if (!editingReporting) return;
    setEditingReporting({ ...editingReporting, [field]: value } as ReportingDto);
  };

  const handleSave = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!editingReporting) {
      return;
    }

    const isEmptyField = (value: unknown) =>
      value === undefined || value === null || value === '' || (typeof value === 'number' && Number.isNaN(value));

    const requiredFields = [
      { value: editingReporting.numeroDA, label: 'DA' },
      { value: editingReporting.numeroDossier, label: 'Dossier' },
      { value: editingReporting.numero, label: 'N°' },
      { value: editingReporting.codeOracle, label: 'Code Oracle' },
      { value: editingReporting.codeSAP, label: 'Code SAP' },
      { value: editingReporting.secteur, label: 'Secteur' },
      { value: editingReporting.commande, label: 'CMD' },
      { value: editingReporting.fournisseur, label: 'Fournisseur' },
      { value: editingReporting.statut, label: 'Statut livraison' },
      { value: editingReporting.responsable, label: 'Responsable DO' },
      { value: editingReporting.quantite, label: 'Quantité' },
      { value: editingReporting.pourcentageLivraison, label: '% Livraison' },
    ];

    const missing = requiredFields
      .filter((field) => isEmptyField(field.value))
      .map((field) => field.label);

    if (missing.length > 0) {
      setErrorMessage(`Veuillez renseigner les champs obligatoires : ${missing.join(', ')}`);
      return;
    }

    setSaveLoading(true);
    setErrorMessage(null);

    try {
      const reportingId = editingReporting.id;
      const isExisting = reportingId != null;
      const rawPayload: any = {
        ...editingReporting,
        utilisateurId: editingReporting.utilisateurId ?? user?.id,
      };

      const sanitizePayload = (obj: any) => {
        const copy: any = { ...obj };
        Object.keys(copy).forEach((k) => {
          if (copy[k] === '') {
            copy[k] = null;
          }
          if (typeof copy[k] === 'number' && Number.isNaN(copy[k])) {
            copy[k] = null;
          }
        });
        return copy;
      };

      const payload = sanitizePayload(rawPayload);

      if (!payload.utilisateurId) {
        throw new Error('Utilisateur non authentifié');
      }

      if (isExisting) {
        await reportingService.update(reportingId, payload);
      } else {
        await reportingService.create(payload);
      }
      setEditingReporting(null);
      await loadReportings();
    } catch (error) {
      if (error instanceof AxiosError && error.response) {
        const responseData = error.response.data;
        let serverMessage: string | undefined;

        if (responseData?.message) {
          serverMessage = responseData.message;
        } else if (Array.isArray(responseData)) {
          serverMessage = responseData.map((item) => item.message || JSON.stringify(item)).join(', ');
        } else if (responseData?.errors && Array.isArray(responseData.errors)) {
          serverMessage = responseData.errors
            .map((item: any) => `${item.field || ''}: ${item.message || JSON.stringify(item)}`.trim())
            .join(', ');
        } else {
          serverMessage = JSON.stringify(responseData);
        }

        setErrorMessage(
          `Impossible d’enregistrer le reporting : ${error.response.status} ${serverMessage || error.response.statusText}`
        );
      } else {
        setErrorMessage(`Impossible d’enregistrer le reporting : ${error instanceof Error ? error.message : 'Erreur inconnue'}`);
      }
    } finally {
      setSaveLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setEditingReporting(null);
    setErrorMessage(null);
  };

  const statusCounts = useMemo(
    () =>
      reportings.reduce(
        (counts, item) => {
          if (item.statut in counts) {
            counts[item.statut] += 1;
          }
          return counts;
        },
        {
          EN_COURS: 0,
          ATTENTE_LIVRAISON: 0,
          LIVRE: 0,
          ECARTE: 0,
          ADJUGE: 0,
          LITIGE: 0,
          ANNULE: 0,
        } as Record<string, number>
      ),
    [reportings]
  );
  const columns = useMemo(() => getReportingColumnsByRole(user?.role), [user?.role]);

  return (
    <div className="reporting-page">
      <div className="ui-toolbar reporting-page__header">
        <div>
          <p className="ui-toolbar__subtitle">Gestion et suivi des reportings</p>
          <h1>Reportings</h1>
        </div>
        <div className="ui-toolbar__right">
          <Button variant="primary" onClick={handleCreate}>
            Ajouter un reporting
          </Button>
        </div>
      </div>

      <Card>
        <SectionHeading title="Filtrer les reportings" subtitle="Recherche personnalisée" />

        <form id="reporting-filter-form" onSubmit={handleSubmit} className="filter-panel__row reporting-page__filters">
          <div className="ui-form-group">
            <label>Rechercher</label>
            <SearchBar
              value={filters.search}
              onChange={(value: string) => setFilters({ ...filters, search: value })}
              placeholder="Rechercher par DA, commande ou fournisseur"
            />
          </div>
          <div className="ui-form-group">
            <label>Statut</label>
            <select
              value={filters.statut}
              onChange={(event) => setFilters({ ...filters, statut: event.target.value })}
              className="ui-form-control"
            >
              {statutOptions.map((option) => (
                <option key={option} value={option}>
                  {option || 'Tous les statuts'}
                </option>
              ))}
            </select>
          </div>
          <div className="ui-form-group">
            <label>Secteur</label>
            <select
              value={filters.secteur}
              onChange={(event) => setFilters({ ...filters, secteur: event.target.value })}
              className="ui-form-control"
            >
              {secteurOptions.map((option) => (
                <option key={option} value={option}>
                  {option || 'Tous les secteurs'}
                </option>
              ))}
            </select>
          </div>
          <div className="ui-form-group">
            <label>Responsable</label>
            <select
              value={filters.responsable}
              onChange={(event) => setFilters({ ...filters, responsable: event.target.value })}
              className="ui-form-control"
            >
              {responsableOptions.map((option) => (
                <option key={option} value={option}>
                  {option || 'Tous les responsables'}
                </option>
              ))}
            </select>
          </div>
          <div className="ui-form-group">
            <label>Fournisseur</label>
            <input
              value={filters.fournisseur}
              onChange={(event) => setFilters({ ...filters, fournisseur: event.target.value })}
              placeholder="Fournisseur"
              className="ui-form-control"
            />
          </div>
        </form>

        <div className="ui-toolbar reporting-page__actions">
          <Button variant="ghost" onClick={handleReset} type="button">
            Réinitialiser
          </Button>
          <Button variant="primary" type="submit" form="reporting-filter-form">
            Appliquer
          </Button>
        </div>
      </Card>

      {editingReporting && (
        <Card className="reporting-page__section">
          <SectionHeading title="Modifier le reporting" subtitle="Edition rapide" />

          {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}

          <form onSubmit={handleSave} className="ui-form-grid" style={{ gridTemplateColumns: 'repeat(3, minmax(240px, 1fr))' }}>
            <div className="ui-form-group">
              <Input
                label="DA"
                value={editingReporting.numeroDA}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('numeroDA', event.target.value)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="Dossier"
                value={editingReporting.numeroDossier}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('numeroDossier', event.target.value)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="N°"
                value={editingReporting.numero}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('numero', event.target.value)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="Code Oracle"
                value={editingReporting.codeOracle}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('codeOracle', event.target.value)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="Code SAP"
                value={editingReporting.codeSAP}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('codeSAP', event.target.value)}
              />
            </div>
            <div className="ui-form-group" style={{ gridColumn: '1 / -1' }}>
              <label className="ui-form-group__label">Description</label>
              <textarea
                value={editingReporting.description ?? ''}
                onChange={(event) => updateEditingField('description', event.target.value)}
                className="ui-form-control"
                rows={3}
              />
            </div>
            <div className="ui-form-group">
              <label className="ui-form-group__label">UDM</label>
              <select
                value={editingReporting.uniteDeMesure}
                onChange={(event) => updateEditingField('uniteDeMesure', event.target.value)}
                className="ui-form-control"
              >
                {udmOptions.map((option) => (
                  <option key={option} value={option}>
                    {option || 'UDM'}
                  </option>
                ))}
              </select>
            </div>
            <div className="ui-form-group">
              <Input
                label="Quantité"
                type="number"
                value={editingReporting.quantite ?? ''}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('quantite', event.target.value ? parseFloat(event.target.value) : null)}
              />
            </div>
            <div className="ui-form-group">
              <label className="ui-form-group__label">Secteur</label>
              <select
                value={editingReporting.secteur}
                onChange={(event) => updateEditingField('secteur', event.target.value)}
                className="ui-form-control"
              >
                {secteurOptions.map((option) => (
                  <option key={option} value={option}>
                    {option || 'Secteur'}
                  </option>
                ))}
              </select>
            </div>
            <div className="ui-form-group">
              <Input
                label="CMD"
                value={editingReporting.commande}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('commande', event.target.value)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="Fournisseur"
                value={editingReporting.fournisseur}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('fournisseur', event.target.value)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="% Livraison"
                type="number"
                value={editingReporting.pourcentageLivraison ?? ''}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('pourcentageLivraison', event.target.value ? parseInt(event.target.value, 10) : null)}
              />
            </div>
            <div className="ui-form-group">
              <Input
                label="Délai livraison"
                type="number"
                value={editingReporting.delaiLivraison ?? ''}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('delaiLivraison', event.target.value ? parseInt(event.target.value, 10) : null)}
              />
            </div>
            <div className="ui-form-group">
              <label className="ui-form-group__label">Date notification</label>
              <input
                type="date"
                value={editingReporting.dateNotification}
                onChange={(event) => updateEditingField('dateNotification', event.target.value)}
                className="ui-form-control"
              />
            </div>
            <div className="ui-form-group">
              <label className="ui-form-group__label">Date prévisionnelle</label>
              <input
                type="date"
                value={editingReporting.datePrevisionnelle}
                onChange={(event) => updateEditingField('datePrevisionnelle', event.target.value)}
                className="ui-form-control"
              />
            </div>
            <div className="ui-form-group">
              <label className="ui-form-group__label">Statut livraison</label>
              <select
                value={editingReporting.statut}
                onChange={(event) => updateEditingField('statut', event.target.value)}
                className="ui-form-control"
              >
                {statutOptions.map((option) => (
                  <option key={option} value={option}>
                    {option || 'Statut livraison'}
                  </option>
                ))}
              </select>
            </div>
            <div className="ui-form-group">
              <label className="ui-form-group__label">Responsable DO</label>
              <select
                value={editingReporting.responsable}
                onChange={(event) => updateEditingField('responsable', event.target.value)}
                className="ui-form-control"
              >
                {responsableOptions.map((option) => (
                  <option key={option} value={option}>
                    {option || 'Responsable DO'}
                  </option>
                ))}
              </select>
            </div>
            <div className="ui-form-group">
              <Input
                label="Utilisateur ID"
                type="number"
                value={editingReporting.utilisateurId ?? ''}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => updateEditingField('utilisateurId', event.target.value ? parseInt(event.target.value, 10) : null)}
              />
            </div>
            <div className="ui-form-group" style={{ gridColumn: '1 / -1' }}>
              <label className="ui-form-group__label">Commentaire</label>
              <textarea
                value={editingReporting.commentaire ?? ''}
                onChange={(event) => updateEditingField('commentaire', event.target.value)}
                className="ui-form-control"
                rows={3}
              />
            </div>
            <div className="ui-form-actions" style={{ gridColumn: '1 / -1' }}>
              <Button variant="primary" type="submit" disabled={saveLoading}>
                {saveLoading ? 'Enregistrement...' : 'Enregistrer'}
              </Button>
              <Button variant="ghost" type="button" onClick={handleCancelEdit}>
                Annuler
              </Button>
            </div>
          </form>
        </Card>
      )}

      <Card>
        <SectionHeading title="Liste des reportings" subtitle="Tableau moderne" />

        {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}

        <div className="reporting-page__status-row">
          {Object.entries(statusCounts).map(([key, value]) => (
            <Badge
              key={key}
              label={`${statusLabels[key] ?? key}: ${value}`}
              variant={statusVariants[key] ?? 'default'}
            />
          ))}
        </div>

        <ReportingTable
          reportings={reportings}
          columns={columns}
          loading={loading}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </Card>
    </div>
  );
};

export default ReportingPage;
