package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.Application;
import net.simpleframework.workflow.modeler.ApplicationActions;
import net.simpleframework.workflow.modeler.ApplicationActions.ApplicationAction;
import net.simpleframework.workflow.modeler.navigation.NodeProcessModel;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ModelSaveAction extends ApplicationAction {
	private final ModelGraph modelGraph;

	public ModelSaveAction(final ModelGraph modelGraph) {
		super($m("ModelSaveAction.0"), ApplicationActions.saveIcon);
		this.modelGraph = modelGraph;
	}

	@Override
	protected void actionInvoked(final ActionEvent e) {
		modelGraph.save();

		final ModelTabbedContent tabbedContent = modelGraph.getTabbedContent();
		final NodeProcessModel nodeProcessModel = tabbedContent.getTreeNode();
		try {
			final Map<String, Object> kv = Application.remote().call(
					nodeProcessModel.getUrl(),
					"saveModel",
					new KVMap().add("id", nodeProcessModel.getJsonModel().get("id")).add("doc",
							tabbedContent.getDocument().toString()));
			if (Application.isError(kv)) {
				return;
			}
		} catch (final IOException ex) {
			SwingUtils.showError(ex);
		}
	}
}
