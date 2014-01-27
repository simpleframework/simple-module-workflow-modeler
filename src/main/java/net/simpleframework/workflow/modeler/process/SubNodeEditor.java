package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.utils.JTableEx;
import net.simpleframework.workflow.modeler.utils.JTableEx.ColumnEx;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.SubNode;
import net.simpleframework.workflow.schema.SubNode.VariableMapping;

import com.mxgraph.model.mxCell;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SubNodeEditor extends AbstractEditorDialog {

	private JTextField urlTf, modelTf;

	private JCheckBox syncCb;

	private JTableEx tableEx;

	public SubNodeEditor(final ModelGraph modelGraph, final mxCell cell) {
		super($m("SubNodeEditor.0"), modelGraph, cell);
	}

	private JPanel createBasePane() {
		final JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(SwingUtils.createTitleBorder("子流程"));

		final JPanel l1 = SwingUtils.createKV(new JLabel("服务地址"), urlTf = new JTextField());
		final JPanel l2 = SwingUtils.createKV(new JLabel("子流程名称"), modelTf = new JTextField());
		final JPanel l3 = SwingUtils.createKV(new JLabel(), syncCb = new JCheckBox("同步模式"));
		p1.add(SwingUtils.createVertical(l1, l2, l3));

		final JPanel p2 = new JPanel(new BorderLayout());
		p2.setBorder(SwingUtils.createTitleBorder("变量映射"));
		tableEx = new JTableEx(new ColumnEx("主流程变量"), new ColumnEx("子流程变量")) {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return true;
			}

			private static final long serialVersionUID = -3647643723183036148L;
		};

		final JScrollPane sp = new JScrollPane(tableEx);
		sp.setPreferredSize(new Dimension(0, 180));
		p2.add(sp, BorderLayout.CENTER);

		final Box p22 = Box.createVerticalBox();
		p22.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		p2.add(p22, BorderLayout.EAST);

		final JButton add = new JButton("增加");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				tableEx.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
				final DefaultTableModel tableModel = (DefaultTableModel) tableEx.getModel();
				tableModel.addRow(new Object[] { null, null });
			}
		});
		final JButton remove = new JButton("删除");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				tableEx.deleteRows();
			}
		});
		p22.add(add);
		p22.add(Box.createVerticalStrut(4));
		p22.add(remove);

		return SwingUtils.createVertical(p1, p2);
	}

	@Override
	protected void load() {
		super.load();

		final SubNode node = (SubNode) getNode();
		urlTf.setText(node.getUrl());
		modelTf.setText(node.getModel());
		syncCb.setSelected(node.isSync());

		final DefaultTableModel tableModel = (DefaultTableModel) tableEx.getModel();
		for (final VariableMapping vMapping : node.getMappingSet()) {
			tableModel.addRow(new Object[] { vMapping.variable, vMapping.mapping });
		}
	}

	@Override
	public void ok() {
		if (assertNull(modelTf)) {
			return;
		}

		final SubNode node = (SubNode) getNode();
		node.setUrl(urlTf.getText());
		node.setModel(modelTf.getText());
		node.setSync(syncCb.isSelected());

		final TableCellEditor cellEditor = tableEx.getCellEditor();
		if (cellEditor != null) {
			cellEditor.cancelCellEditing();
		}
		final DefaultTableModel tableModel = (DefaultTableModel) tableEx.getModel();
		final Set<VariableMapping> mappingSet = node.getMappingSet();
		mappingSet.clear();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			final String val1 = (String) tableModel.getValueAt(i, 0);
			final String val2 = (String) tableModel.getValueAt(i, 1);
			if (StringUtils.hasText(val1) && StringUtils.hasText(val2)) {
				mappingSet.add(new VariableMapping(val1, val2));
			}
		}
		super.ok();
	}

	@Override
	protected Map<String, Object> getTabbedComponents() {
		return new KVMap().add($m("SubNodeEditor.1"), createBasePane());
	}

	private static final long serialVersionUID = -7958514524267240825L;
}
