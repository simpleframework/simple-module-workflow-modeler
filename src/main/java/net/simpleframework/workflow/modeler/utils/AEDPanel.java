package net.simpleframework.workflow.modeler.utils;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import net.simpleframework.workflow.modeler.ApplicationActions.ApplicationAction;
import net.simpleframework.workflow.modeler.utils.JTableEx.ColumnEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AEDPanel extends JPanel {

	protected Object[] params;

	public AEDPanel(final Object... params) {
		this.params = params;
		createUI();
	}

	protected JTableEx table;

	protected void createUI() {
		setLayout(new BorderLayout());

		add(createActionPanel(), BorderLayout.SOUTH);
		add(new JScrollPane(table = createTable()), BorderLayout.CENTER);
		load();
	}

	protected Container createActionPanel() {
		final JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
		jp.add(new JButton(new ApplicationAction($m("AEDPanel.0"), new Integer('A'), null) {

			@Override
			protected void actionInvoked(final ActionEvent e) {
				add();
			}
		}));
		jp.add(new JButton(new ApplicationAction($m("AEDPanel.1"), new Integer('E'), null) {

			@Override
			protected void actionInvoked(final ActionEvent e) {
				final int row = table.getSelectedRow();
				if (row > -1) {
					edit(table.convertRowIndexToModel(row));
				}
			}
		}));
		jp.add(new JButton(new ApplicationAction($m("AEDPanel.2"), new Integer('D'), null) {

			@Override
			protected void actionInvoked(final ActionEvent e) {
				final int[] rows = table.getSelectedRows();
				if (rows.length > 0) {
					if (!SwingUtils.confirm($m("AEDPanel.3"))) {
						return;
					}
					delete(rows);
					table.deleteRows();
				}
			}
		}));
		return jp;
	}

	protected Object getSlectedValueAt(final int col) {
		final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		final int row = table.getSelectedRow();
		return row > -1 ? tableModel.getValueAt(table.convertRowIndexToModel(row), col) : null;
	}

	protected abstract void load();

	protected abstract void add();

	protected abstract void edit(int row);

	protected abstract void delete(int[] rows);

	protected abstract ColumnEx[] getColumns();

	protected MyTable createTable() {
		return new MyTable();
	}

	protected class MyTable extends JTableEx {
		public MyTable() {
			super(getColumns());

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					int row;
					if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2)
							&& (row = table.getSelectedRow()) > -1) {
						edit(table.convertRowIndexToModel(row));
					}
				}
			});
		}
	}
}
