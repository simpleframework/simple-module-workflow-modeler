package net.simpleframework.workflow.modeler.navigation;

import javax.swing.tree.DefaultTreeModel;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NavigationTreeModel extends DefaultTreeModel {

	public NavigationTreeModel() {
		super(new NodeRoot());
	}

	@Override
	public NodeRoot getRoot() {
		return (NodeRoot) super.getRoot();
	}
}
