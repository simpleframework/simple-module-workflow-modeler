package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.simpleframework.common.Convert;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.graph.TaskCell;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.MergeNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MergeNodeEditor extends AbstractEditorDialog {

	public MergeNodeEditor(final ModelGraph modelGraph, final TaskCell cell) {
		super($m("MergeNodeEditor.0"), modelGraph, cell);
	}

	private JTextField countTf;

	private JPanel createBasePane() {
		final JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(SwingUtils.createTitleBorder($m("MergeNodeEditor.2")));
		p1.add(SwingUtils.createKV(new JLabel($m("MergeNodeEditor.3")), countTf = new JTextField()));

		return SwingUtils.createVertical(p1);
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		final MergeNode node = (MergeNode) getNode();
		countTf.setText(String.valueOf(node.getCount()));
	}

	@Override
	public void ok() {
		final MergeNode node = (MergeNode) getNode();
		node.setCount(Convert.toInt(countTf.getText()));
		super.ok();
	}

	@Override
	protected Map<String, Object> getTabbedComponents() {
		return new KVMap().add($m("MergeNodeEditor.1"), createBasePane());
	}

	private static final long serialVersionUID = -7921845955285149909L;
}
