package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JPanel;

import net.simpleframework.common.coll.KVMap;

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

	@Override
	protected void load() {
		super.load();
	}

	private JPanel createBasePane() {
		final JPanel p1 = new JPanel(new BorderLayout());
		return p1;
	}

	@Override
	protected Map<String, Object> getTabbedComponents() {
		return new KVMap().add($m("MergeNodeEditor.1"), createBasePane());
	}

	private static final long serialVersionUID = -7921845955285149909L;
}
