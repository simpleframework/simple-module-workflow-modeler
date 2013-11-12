package net.simpleframework.workflow.modeler.navigation;

import javax.swing.tree.TreeSelectionModel;

import net.simpleframework.workflow.modeler.utils.JTreeEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NavigationTree extends JTreeEx {

	public NavigationTree() {
		setModel(new NavigationTreeModel());

		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		expand(getModel().getRoot());
	}

	@Override
	public NavigationTreeModel getModel() {
		return (NavigationTreeModel) super.getModel();
	}
}
