package net.simpleframework.workflow.modeler.utils;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.simpleframework.ado.ColumnData;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class JTableEx extends JTable {

	ColumnEx[] columns;

	public JTableEx(final ColumnEx... columns) {
		this.columns = columns;

		final DefaultTableModel tableModel = new DefaultTableModel();
		for (final ColumnEx column : columns) {
			tableModel.addColumn(column.getName());
		}

		setModel(tableModel);

		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			final TableColumn column = getColumn(columns[i].getName());
			if (columns[i].width > 0) {
				column.setPreferredWidth(columns[i].getWidth());
				column.setMaxWidth(800);
			}
		}
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return false;
	}

	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		TableCellRenderer renderer = null;
		if (getModel().getRowCount() > 0) {
			final Object object = getValueAt(row, column);
			if (object != null) {
				if (object instanceof String) {
					// return new DefaultTableRenderer(StringValue.TO_STRING,
					// columns[convertColumnIndexToModel(column)].alignment);
				} else {
					renderer = getDefaultRenderer(object.getClass());
				}
			}
		}
		if (renderer == null) {
			renderer = super.getCellRenderer(row, column);
		}
		return renderer;
	}

	public void deleteRows() {
		final DefaultTableModel tm = (DefaultTableModel) getModel();
		int index;
		while ((index = getSelectedRow()) > -1) {
			tm.removeRow(convertRowIndexToModel(index));
		}
	}

	public static class ColumnEx extends ColumnData {
		private int width = 0;

		private int alignment = SwingConstants.LEFT;

		public ColumnEx(final String columnName) {
			super(columnName);
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(final int width) {
			this.width = width;
		}

		public int getAlignment() {
			return alignment;
		}

		public void setAlignment(final int alignment) {
			this.alignment = alignment;
		}
	}
}
