package net.simpleframework.workflow.modeler.utils;

import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class TreeNodeEx extends DefaultMutableTreeNode {
	public abstract Icon getIcon();

	protected JPopupMenu popupMenu;

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public boolean isExpanded() {
		return false;
	}

	public void mouseDbClicked(final MouseEvent e) {
	}

	public void valueChanged(final TreeSelectionEvent e) {
	}
}
