import React from 'react';

const Loader: React.FC = () => (
  <div className="ui-loading" role="status" aria-live="polite">
    <span className="ui-loading__spinner" />
    <span>Chargement...</span>
  </div>
);

export default React.memo(Loader);
