import React, { useState } from 'react';
import reportingService from '../../services/reportingService';
import Alert from '../../components/common/Alert';
import { FileSpreadsheet, UploadCloud, FileText, AlertCircle } from 'lucide-react';

const expectedColumns = [
  'DA', 'Dossier', 'N°', 'Code Oracle', 'Code SAP', 'Description',
  'UDM', 'Q. Retenue', 'Secteur', 'Fournisseur', 'CMD', '% Livraison',
  'Délai livraison', 'Date notification', 'Date Prévisionnelle', 'Commentaire', 'Statut Livraison', 'Responsable'
];

const ImportPage: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [dragActive, setDragActive] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage('');
    setError('');
    if (event.target.files && event.target.files[0]) {
      setSelectedFile(event.target.files[0]);
    }
  };

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const file = e.dataTransfer.files[0];
      if (file.name.endsWith('.xlsx') || file.name.endsWith('.xls')) {
        setSelectedFile(file);
        setMessage('');
        setError('');
      } else {
        setError('Seuls les fichiers Excel (.xlsx, .xls) sont pris en charge.');
      }
    }
  };

  const handleUpload = async (event: React.FormEvent) => {
    event.preventDefault();
    setMessage('');
    setError('');

    if (!selectedFile) {
      setError('Veuillez choisir un fichier Excel à importer.');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    setUploading(true);
    try {
      const response = await reportingService.importFile(formData);
      setMessage(response.data || 'Fichier Excel importé avec succès.');
      setSelectedFile(null);
    } catch (uploadError: any) {
      const serverData = uploadError?.response?.data;
      const serverMessage = formatServerError(serverData);
      setError(
        serverMessage
          ? `Erreur d'importation : ${serverMessage}`
          : 'Impossible d’importer le fichier. Vérifiez le format et réessayez.'
      );
    } finally {
      setUploading(false);
    }
  };

  const formatServerError = (data: any): string | null => {
    if (!data) return null;
    if (typeof data === 'string') return data;
    if (typeof data === 'object') {
      if ('message' in data && typeof data.message === 'string') return data.message;
      if ('errors' in data && Array.isArray(data.errors)) {
        return data.errors.map((i: any) => (typeof i === 'string' ? i : i.message || JSON.stringify(i))).join(' / ');
      }
    }
    return String(data);
  };

  return (
    <div className="app-content" style={{ maxWidth: '840px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <div>
        <h2 style={{ fontSize: '22px', fontWeight: 700, color: 'var(--ocp-text)' }}>Importation de Fichiers Excel</h2>
        <p style={{ fontSize: '13px', color: 'var(--ocp-text-muted)', marginTop: '2px' }}>
          Mettez à jour ou importez vos données de suivi des pièces de rechange et demandes d'achat.
        </p>
      </div>

      <div className="card-ocp" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        {message && <Alert variant="success">{message}</Alert>}
        {error && <Alert variant="danger">{error}</Alert>}

        <form onSubmit={handleUpload} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <div
            className={`dropzone-ocp ${dragActive ? 'active' : ''}`}
            onDragEnter={handleDrag}
            onDragLeave={handleDrag}
            onDragOver={handleDrag}
            onDrop={handleDrop}
            onClick={() => document.getElementById('fileImport')?.click()}
          >
            <input
              id="fileImport"
              type="file"
              accept=".xlsx,.xls"
              onChange={handleFileChange}
              style={{ display: 'none' }}
            />
            <FileSpreadsheet size={48} color="var(--ocp-primary)" style={{ marginBottom: '12px' }} />
            <h4 style={{ fontSize: '16px', fontWeight: 700, color: 'var(--ocp-text)' }}>
              Glissez-déposez votre fichier Excel ici
            </h4>
            <p style={{ fontSize: '13px', color: 'var(--ocp-text-muted)', marginTop: '4px' }}>
              ou cliquez pour parcourir vos fichiers (.xlsx, .xls)
            </p>
          </div>

          {selectedFile && (
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '14px 18px',
                background: 'var(--ocp-surface-soft)',
                border: '1px solid var(--ocp-border)',
                borderRadius: 'var(--radius-btn)',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <FileText size={24} color="var(--ocp-primary)" />
                <div>
                  <strong style={{ fontSize: '14px', color: 'var(--ocp-text)' }}>{selectedFile.name}</strong>
                  <div style={{ fontSize: '12px', color: 'var(--ocp-text-muted)' }}>
                    {(selectedFile.size / 1024).toFixed(1)} KB
                  </div>
                </div>
              </div>
              <span className="badge-ocp badge-ocp--livre">Prêt pour l'import</span>
            </div>
          )}

          <button
            type="submit"
            className="btn-ocp btn-ocp-primary"
            disabled={uploading || !selectedFile}
            style={{ width: '100%', padding: '12px' }}
          >
            <UploadCloud size={18} />
            <span>{uploading ? 'Importation et analyse en cours...' : 'Lancer l’importation Excel'}</span>
          </button>
        </form>

        <div style={{ marginTop: '12px', borderTop: '1px solid var(--ocp-border-light)', paddingTop: '20px' }}>
          <h4 style={{ fontSize: '14px', fontWeight: 700, color: 'var(--ocp-text)', marginBottom: '10px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <AlertCircle size={16} color="var(--ocp-primary)" />
            Colonnes attendues dans le fichier Excel :
          </h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
            {expectedColumns.map((col) => (
              <span
                key={col}
                style={{
                  background: 'var(--ocp-surface-soft)',
                  border: '1px solid var(--ocp-border)',
                  padding: '4px 10px',
                  borderRadius: 'var(--radius-sm)',
                  fontSize: '12px',
                  color: 'var(--ocp-text-muted)',
                  fontWeight: 500,
                }}
              >
                {col}
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ImportPage;
