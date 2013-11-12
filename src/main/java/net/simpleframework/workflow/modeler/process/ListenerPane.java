package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.simpleframework.workflow.modeler.utils.AEDEditor;
import net.simpleframework.workflow.modeler.utils.AEDPanel;
import net.simpleframework.workflow.modeler.utils.JTableEx.ColumnEx;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.Node;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ListenerPane extends AEDPanel {
	static final String title = $m("ListenerPane.0");

	static ColumnEx[] columns = new ColumnEx[] { new ColumnEx($m("ListenerPane.1")) };

	private Node parentNode;

	public ListenerPane(final Node parentNode) {
		super(parentNode);
	}

	@Override
	protected void load() {
		parentNode = (Node) params[0];
		final Set<String> listeners = parentNode.listeners();
		if (listeners != null) {
			final DefaultTableModel tm = (DefaultTableModel) table.getModel();
			for (final String listenerClass : listeners) {
				tm.addRow(new Object[] { listenerClass });
			}
		}
	}

	@Override
	protected void add() {
		final AEDEditor editor = new ListenerEditor(null);
		if (editor.isOk()) {
			final DefaultTableModel tm = (DefaultTableModel) table.getModel();
			final String listenerClass = (String) editor.getRowData();
			tm.addRow(new Object[] { listenerClass });
		}
	}

	@Override
	protected void edit(final int row) {
		final DefaultTableModel tm = (DefaultTableModel) table.getModel();
		final AEDEditor editor = new ListenerEditor((String) tm.getValueAt(row, 0));
		if (editor.isOk()) {
			final String listenerClass = (String) editor.getRowData();
			tm.setValueAt(listenerClass, row, 0);
		}
	}

	@Override
	protected void delete(final int[] rows) {
		final Set<String> listeners = parentNode.listeners();
		if (listeners != null) {
			final DefaultTableModel tm = (DefaultTableModel) table.getModel();
			for (final int row : rows) {
				final String listenerClass = (String) tm.getValueAt(row, 0);
				listeners.remove(listenerClass);
			}
		}
	}

	@Override
	protected ColumnEx[] getColumns() {
		return columns;
	}

	public class ListenerEditor extends AEDEditor {
		JTextField nameComp;

		public ListenerEditor(final String listenerClass) {
			super($m("ListenerEditor.0"), listenerClass);
		}

		@Override
		protected Component createContentUI() {
			setMinimumSize(new Dimension(420, 120));
			final JPanel p = SwingUtils.createKV(new JLabel($m("ListenerEditor.1")),
					nameComp = new JTextField(), 50);
			p.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 4));

			final String listenerClass = (String) params[0];
			if (listenerClass != null) {
				nameComp.setText(listenerClass);
			}
			return p;
		}

		@Override
		public void ok() {
			if (assertNull(nameComp)) {
				return;
			}
			final Set<String> listeners = parentNode.listeners();
			if (listeners != null) {
				final String name = nameComp.getText();
				final String listenerClass = (String) params[0];
				if (listenerClass == null) {
					listeners.add(name);
				} else {
					final Vector<String> tmp = new Vector<String>(listeners);
					final int j = tmp.indexOf(listenerClass);
					if (j >= 0) {
						tmp.set(j, name);
						listeners.clear();
						listeners.addAll(tmp);
					}
				}
				setRowData(name);
			}
			super.ok();
		}
	}
}
