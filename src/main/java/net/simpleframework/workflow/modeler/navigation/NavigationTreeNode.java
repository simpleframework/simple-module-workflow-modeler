package net.simpleframework.workflow.modeler.navigation;

import javax.swing.tree.TreePath;

import net.simpleframework.workflow.modeler.Application;
import net.simpleframework.workflow.modeler.utils.TreeNodeEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class NavigationTreeNode extends TreeNodeEx {

	public NavigationTree getTree() {
		return Application.get().getMainPane().getNavigationTree();
	}

	public void expand() {
		getTree().expandPath(new TreePath(getPath()));
	}
}
