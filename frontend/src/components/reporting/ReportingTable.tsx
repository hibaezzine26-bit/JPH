import React from 'react';
import Table from '../common/Table';
import Button from '../common/Button';
import Loading from '../common/Loading';
import type { ReportingDto } from '../../types/reporting';
import type { ReportingColumn } from '../../utils/reportingColumns';
import { getReportingCellValue } from '../../utils/reportingColumns';

type TableColumn = ReportingColumn | { key: 'actions'; label: string };

interface ReportingTableProps {
  reportings: ReportingDto[];
  columns: ReportingColumn[];
  loading?: boolean;
  onEdit?: (id?: number) => void;
  onDelete?: (id?: number) => void;
  className?: string;
}

const ReportingTable: React.FC<ReportingTableProps> = ({
  reportings,
  columns,
  loading = false,
  onEdit,
  onDelete,
  className = '',
}) => {
  const hasActions = Boolean(onEdit || onDelete);
  const tableColumns: TableColumn[] = hasActions ? [...columns, { key: 'actions', label: 'Actions' }] : columns;

  const renderCell = (columnKey: string, item: ReportingDto) => {
    if (columnKey === 'actions') {
      return (
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          {onEdit && (
            <Button variant="ghost" onClick={() => onEdit(item.id)} type="button">
              Modifier
            </Button>
          )}
          {onDelete && (
            <Button variant="danger" onClick={() => onDelete(item.id)} type="button">
              Supprimer
            </Button>
          )}
        </div>
      );
    }

    return getReportingCellValue(columnKey as any, item);
  };

  return (
    <Table columns={tableColumns} className={className}>
      {loading ? (
        <tr>
          <td colSpan={tableColumns.length} className="ui-text-center" style={{ padding: '2rem 0' }}>
            <Loading />
          </td>
        </tr>
      ) : reportings.length === 0 ? (
        <tr>
          <td colSpan={tableColumns.length} className="ui-text-center ui-text-muted" style={{ padding: '2rem 0' }}>
            Aucun reporting trouvé.
          </td>
        </tr>
      ) : (
        reportings.map((item) => (
          <tr key={item.id ?? `${item.numeroDA}-${item.numeroDossier}-${item.numero}`}>
            {tableColumns.map((column) => (
              <td key={column.key}>{renderCell(column.key, item)}</td>
            ))}
          </tr>
        ))
      )}
    </Table>
  );
};

export default React.memo(ReportingTable);
