package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class StartNodeEditor extends AbstractEditorDialog {

	public StartNodeEditor(final ModelGraph modelGraph, final TaskCell cell) {
		super($m("StartNodeEditor.0"), modelGraph, cell);
	}

	@Override
	protected Map<String, Object> getTabbedComponents() {
		return null;
	}

	private static final long serialVersionUID = -602721048000349300L;
}
