import React, { useState } from 'react';
import reportingService from '../../services/reportingService';
import Alert from '../../components/ui/Alert';
import Button from '../../components/ui/Button';

const ImportPage: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage('');
    setError('');
    setSelectedFile(event.target.files?.[0] ?? null);
  };

  const handleUpload = async (event: React.FormEvent) => {
    event.preventDefault();
    setMessage('');
    setError('');

    if (!selectedFile) {
      setError('Choisissez un fichier Excel à importer.');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    setUploading(true);
    try {
      const response = await reportingService.importFile(formData);
      setMessage(response.data || 'Fichier importé avec succès.');
      setSelectedFile(null);
    } catch (uploadError: any) {
      const serverData = uploadError?.response?.data;
      const serverMessage = formatServerError(serverData);
      setError(
        serverMessage
          ? `Erreur serveur : ${serverMessage}`
          : 'Impossible d’importer le fichier. Vérifiez le format et réessayez.'
      );
    } finally {
      setUploading(false);
    }
  };

  const formatServerError = (data: any): string | null => {
    if (!data) {
      return null;
    }
    if (typeof data === 'string') {
      return data;
    }
    if (typeof data === 'object') {
      if ('message' in data && typeof data.message === 'string') {
        return data.message;
      }
      if ('errors' in data) {
        const errors = data.errors;
        if (Array.isArray(errors)) {
          return errors.map((errorItem) => {
            if (typeof errorItem === 'string') return errorItem;
            if (errorItem?.message) return errorItem.message;
            return JSON.stringify(errorItem);
          }).join(' / ');
        }
        return JSON.stringify(errors);
      }
      return JSON.stringify(data);
    }
    return String(data);
  };

  return (
    <div className="ui-page">
      <div className="ui-card" style={{ maxWidth: 720, margin: '0 auto' }}>
        <div>
          <h1 className="h4" style={{ marginBottom: 16 }}>
            Importer un fichier Excel
          </h1>

          {message && <Alert variant="success">{message}</Alert>}
          {error && <Alert variant="danger">{error}</Alert>}

          <form onSubmit={handleUpload}>
            <div className="ui-form-group">
              <label htmlFor="fileImport" className="ui-form-group__label">
                Fichier Excel
              </label>
              <input
                id="fileImport"
                type="file"
                accept=".xlsx,.xls"
                onChange={handleFileChange}
                className="ui-form-control"
              />
            </div>
            {selectedFile && (
              <div className="ui-text-muted" style={{ marginBottom: 24 }}>
                Fichier sélectionné : {selectedFile.name}
              </div>
            )}
            <Button type="submit" variant="primary" className="ui-button--block" disabled={uploading || !selectedFile}>
              {uploading ? 'Importation...' : 'Importer'}
            </Button>
          </form>

          <div style={{ marginTop: 28 }}>
            <p className="ui-form-group__label" style={{ marginBottom: 8 }}>
              Format de données attendu
            </p>
            <ol className="ui-list">
              <li>DA</li>
              <li>Dossier</li>
              <li>N°</li>
              <li>code Oracle</li>
              <li>Code SAP</li>
              <li>Description</li>
              <li>UDM</li>
              <li>Q retenue</li>
              <li>Secteur</li>
              <li>Fournisseur</li>
              <li>CMD</li>
              <li>%Livraison</li>
              <li>Délai livraison</li>
              <li>Date notification</li>
              <li>Date Prévisionnelle livraison</li>
              <li>Commentaire</li>
              <li>Statut Livraison</li>
              <li>Responsable Dossier</li>
            </ol>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ImportPage;
