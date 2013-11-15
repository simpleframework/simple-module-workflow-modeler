package net.simpleframework.workflow.modeler.navigation;

import static net.simpleframework.common.I18n.$m;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import net.simpleframework.workflow.modeler.ApplicationActions;
import net.simpleframework.workflow.modeler.process.ModelNewAction;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NodeProcessModels extends NavigationTreeNode {

	@Override
	public Icon getIcon() {
		return ApplicationActions.processModelIcon;
	}

	public NodeConnection getNodeConnection() {
		return (NodeConnection) getParent();
	}

	@Override
	public JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			popupMenu.add(new ModelNewAction(this));
		}
		return popupMenu;
	}

	@Override
	public String toString() {
		return $m("NodeProcessModels.0");
	}
}
