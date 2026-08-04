import React, { useState } from 'react';
import api from '../../services/api';

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
      const response = await api.post('/reportings/import', formData);
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
    <div className="container-fluid py-4">
      <div className="card border-0 shadow-sm mx-auto" style={{ maxWidth: 720 }}>
        <div className="card-body p-4">
          <h1 className="h4 mb-3">Importer un fichier Excel</h1>

          {message && <div className="alert alert-success">{message}</div>}
          {error && <div className="alert alert-danger">{error}</div>}

          <form onSubmit={handleUpload}>
            <div className="mb-4">
              <label htmlFor="fileImport" className="form-label fw-semibold">
                Fichier Excel
              </label>
              <input
                id="fileImport"
                type="file"
                accept=".xlsx,.xls"
                onChange={handleFileChange}
                className="form-control"
              />
            </div>
            {selectedFile && (
              <div className="mb-4 text-muted">Fichier sélectionné : {selectedFile.name}</div>
            )}
            <button type="submit" disabled={uploading || !selectedFile} className="btn btn-primary w-100 mb-4">
              {uploading ? 'Importation...' : 'Importer'}
            </button>
          </form>

          <div>
            <p className="fw-semibold mb-2">Format de données attendu</p>
            <ol className="ps-3 mb-0">
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
