package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.simpleframework.common.Convert;
import net.simpleframework.workflow.modeler.process.AbstractEditorDialog.DescriptionPane;
import net.simpleframework.workflow.modeler.utils.AEDEditor;
import net.simpleframework.workflow.modeler.utils.AEDPanel;
import net.simpleframework.workflow.modeler.utils.JTableEx.ColumnEx;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.EVariableMode;
import net.simpleframework.workflow.schema.EVariableType;
import net.simpleframework.workflow.schema.Node;
import net.simpleframework.workflow.schema.VariableNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings({ "serial" })
public class VariablePane extends AEDPanel {

	static final String title = $m("VariablePane.0");

	static ColumnEx[] columns = new ColumnEx[] { new ColumnEx($m("VariableEditor.1")),
			new ColumnEx($m("VariableEditor.2")) };

	private Node parentNode;

	public VariablePane(final Node parentNode) {
		super(parentNode);
	}

	@Override
	protected ColumnEx[] getColumns() {
		return columns;
	}

	@Override
	protected void load() {
		parentNode = (Node) params[0];
		final Map<String, VariableNode> variables = parentNode.variables();
		if (variables != null) {
			final DefaultTableModel tm = (DefaultTableModel) table.getModel();
			for (final VariableNode variable : variables.values()) {
				tm.addRow(new Object[] { variable, variable.getType() });
			}
		}
	}

	@Override
	protected void add() {
		final AEDEditor editor = new VariableEditor(null);
		if (editor.isOk()) {
			final DefaultTableModel tm = (DefaultTableModel) table.getModel();
			final VariableNode variable = (VariableNode) editor.getRowData();
			tm.addRow(new Object[] { variable, variable.getType() });
		}
	}

	@Override
	protected void edit(final int row) {
		final DefaultTableModel tm = (DefaultTableModel) table.getModel();
		final AEDEditor editor = new VariableEditor(tm.getValueAt(row, 0));
		if (editor.isOk()) {
			final VariableNode variable = (VariableNode) editor.getRowData();
			tm.setValueAt(variable, row, 0);
			tm.setValueAt(variable.getType(), row, 1);
		}
	}

	@Override
	protected void delete(final int[] rows) {
		final DefaultTableModel tm = (DefaultTableModel) table.getModel();
		for (final int row : rows) {
			final VariableNode variable = (VariableNode) tm.getValueAt(row, 0);
			parentNode.removeVariable(variable.getName());
		}
	}

	public class VariableEditor extends AEDEditor {

		JTextField nameComp, logsComp;

		JComboBox typeComp, modeComp;

		JCheckBox staticallyComp;

		JTextArea valueComp;

		DescriptionPane descriptionPane;

		public VariableEditor(final Object bean) {
			super($m("VariableEditor.0"), bean);
		}

		@Override
		protected Component createContentUI() {
			setMinimumSize(new Dimension(580, 320));

			final JLabel lbl1 = new JLabel($m("VariableEditor.1"));
			nameComp = new JTextField(10);
			final JLabel lbl2 = new JLabel($m("VariableEditor.2"));
			typeComp = new JComboBox(EVariableType.values());
			final JLabel lbl3 = new JLabel($m("VariableEditor.3"));
			modeComp = new JComboBox(EVariableMode.values());

			final JLabel lbl4 = new JLabel($m("VariableEditor.4"));
			logsComp = new JTextField(3);

			lbl1.setPreferredSize(new Dimension(65, lbl1.getPreferredSize().height));
			lbl4.setPreferredSize(new Dimension(65, lbl4.getPreferredSize().height));

			final JPanel p1 = SwingUtils.createVertical(
					SwingUtils.createFlow(lbl1, nameComp, 10, lbl2, 10, typeComp, 10, lbl3, 10,
							modeComp),
					SwingUtils.createFlow(lbl4, logsComp, 20,
							staticallyComp = new JCheckBox($m("VariableEditor.5"))));
			p1.setBorder(SwingUtils.createTitleBorder($m("VariableEditor.6")));

			final JPanel p2 = new JPanel(new BorderLayout());
			p2.setBorder(SwingUtils.createTitleBorder($m("VariableEditor.7")));
			p2.add(new JScrollPane(valueComp = new JTextArea(4, 0)));

			final JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.add($m("VariableEditor.8"), SwingUtils.createVertical(p1, p2));
			tabbedPane.add(DescriptionPane.title, descriptionPane = new DescriptionPane());

			// load data
			final VariableNode variable = (VariableNode) params[0];
			if (variable != null) {
				nameComp.setText(variable.getName());
				nameComp.setEditable(false);
				logsComp.setText(String.valueOf(variable.getLogs()));
				typeComp.setSelectedItem(variable.getType());
				modeComp.setSelectedItem(variable.getMode());
				staticallyComp.setSelected(variable.isStatically());
				valueComp.setText(variable.getValue());
				descriptionPane.editor.setText(variable.getDescription());
			}
			return tabbedPane;
		}

		@Override
		public void ok() {
			if (assertNull(nameComp)) {
				return;
			}
			final String name = nameComp.getText();
			VariableNode.assertName(name);
			VariableNode variable = (VariableNode) params[0];
			if (variable == null) {
				variable = parentNode.addVariable(name);
			} else {
				variable.setLogs(Convert.toInt(logsComp.getText()));
				variable.setType((EVariableType) typeComp.getSelectedItem());
				variable.setMode((EVariableMode) modeComp.getSelectedItem());
				variable.setStatically(staticallyComp.isSelected());
				variable.setValue(valueComp.getText());
				variable.setDescription(descriptionPane.editor.getText());
			}
			setRowData(variable);
			super.ok();
		}
	}
}
