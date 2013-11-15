package net.simpleframework.workflow.modeler.navigation;

import static net.simpleframework.common.I18n.$m;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import net.simpleframework.workflow.modeler.ApplicationActions.NewConnectionAction;
import net.simpleframework.workflow.modeler.ModelerSettings;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NodeRoot extends NavigationTreeNode {
	private static final Icon icon = SwingUtils.loadIcon("root.png");

	public NodeRoot() {
		final ModelerSettings settings = ModelerSettings.get();

		for (final String conn : settings.getConnections()) {
			add(new NodeConnection(conn, settings.getConnectionUrl(conn),
					settings.getConnectionLogin(conn), settings.getConnectionPassword(conn)));
		}
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			popupMenu.add(new NewConnectionAction());
		}
		return popupMenu;
	}

	@Override
	public String toString() {
		return $m("NodeRoot.0");
	}
}
