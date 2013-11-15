package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.coll.KVMap;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class EndNodeEditor extends AbstractEditorDialog {

	public EndNodeEditor(final ModelGraph modelGraph, final TaskCell cell) {
		super($m("EndNodeEditor.0"), modelGraph, cell);
	}

	@Override
	protected KVMap getTabbedComponents() {
		return null;
	}
}
