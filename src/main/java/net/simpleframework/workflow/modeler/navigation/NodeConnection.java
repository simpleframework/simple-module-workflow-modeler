package net.simpleframework.workflow.modeler.navigation;

import static net.simpleframework.common.I18n.$m;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.Application;
import net.simpleframework.workflow.modeler.ApplicationActions;
import net.simpleframework.workflow.modeler.ApplicationActions.ApplicationAction;
import net.simpleframework.workflow.modeler.ConnectionDialog;
import net.simpleframework.workflow.modeler.ModelerSettings;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.modeler.utils.TreeNodeEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NodeConnection extends NavigationTreeNode {
	private String name, login, password;

	private String url;

	private NodeProcessModels nodeProcessModels;

	private boolean connected = false;

	public NodeConnection(final String name, final String url, final String login,
			final String password) {
		setName(name);
		setUrl(url);
		setLogin(login);
		setPassword(password);
	}

	@Override
	public Icon getIcon() {
		return ApplicationActions.connectionIcon;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public void mouseDbClicked(final MouseEvent e) {
		connect();
	}

	@SuppressWarnings("unchecked")
	public void connect() {
		if (connected) {
			return;
		}

		List<?> models = null;
		try {
			Map<String, Object> json = Application.remote().call(url, "login",
					new KVMap().add("login", getLogin()).add("password", getPassword()));
			if (Application.isError(json)) {
				return;
			}
			json = Application.remote().call(url, "models");
			if (Application.isError(json)) {
				return;
			}
			models = (List<?>) json.get("models");
		} catch (final IOException e) {
			SwingUtils.showError(e);
			return;
		}

		add(nodeProcessModels = new NodeProcessModels());
		if (models != null) {
			for (final Object o : models) {
				addNode(new KVMap().addAll((Map<String, Object>) o));
			}
		}

		nodeProcessModels.expand();
		expand();
		getTree().updateUI();
		connected = true;
	}

	public void addNode(final Map<String, Object> jsonModel) {
		final String text = (String) jsonModel.get("text");
		final int p = text.lastIndexOf(".");
		if (p > 0) {
			final String packageName = text.substring(0, p);
			final Enumeration<?> e = nodeProcessModels.children();
			TreeNodeEx treeNode = null;
			while (e.hasMoreElements()) {
				final TreeNodeEx tmp = (TreeNodeEx) e.nextElement();
				if (tmp instanceof NodeProcessModelPackage) {
					if (packageName.equals(tmp.toString())) {
						treeNode = tmp;
						break;
					}
				}
			}
			if (treeNode != null) {
				treeNode.add(new NodeProcessModel(jsonModel));
			} else {
				final NavigationTreeNode nodePackage = new NodeProcessModelPackage(packageName);
				nodeProcessModels.add(nodePackage);
				nodePackage.add(new NodeProcessModel(jsonModel));
			}
		} else {
			nodeProcessModels.add(new NodeProcessModel(jsonModel));
		}
	}

	@Override
	public JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			popupMenu.add(new ApplicationAction($m("NodeRoot.0"), ApplicationActions.connectionIcon) {

				@Override
				protected void actionInvoked(final ActionEvent e) {
					connect();
				}
			});
			popupMenu.addSeparator();
			popupMenu.add(new ApplicationAction($m("Menu.Delete"), ApplicationActions.deleteIcon) {

				@Override
				protected void actionInvoked(final ActionEvent e) {
					if (!SwingUtils.confirm($m("NodeConnection.0"))) {
						return;
					}
					final ModelerSettings settings = ModelerSettings.get();
					final Collection<String> connections = settings.getConnections();
					connections.remove(name);
					settings.setConnections(connections);

					settings.setConnectionUrl(name, null);
					settings.setConnectionLogin(name, null);
					settings.setConnectionPassword(name, null);
					settings.save();

					parent.remove(NodeConnection.this);
					getTree().updateUI();
				}
			});
			popupMenu.addSeparator();
			popupMenu.add(new ApplicationAction($m("Menu.Property"), ApplicationActions.propertyIcon) {

				@Override
				protected void actionInvoked(final ActionEvent e) {
					new ConnectionDialog(NodeConnection.this);
				}
			});
		}
		return popupMenu;
	}

	@Override
	public String toString() {
		return getName();
	}
}
