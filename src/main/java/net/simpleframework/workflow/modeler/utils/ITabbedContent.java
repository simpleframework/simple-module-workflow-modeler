package net.simpleframework.workflow.modeler.utils;

import javax.swing.JComponent;

import com.mxgraph.swing.mxGraphComponent;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITabbedContent {

	JComponent getComponent();

	Object getIdentifier();

	String getTitle();

	TreeNodeEx getTreeNode();

	Object getUserObject();

	mxGraphComponent getGraphComponent();

	boolean isModify();

	void onModified(boolean modify);

	void save();
}
