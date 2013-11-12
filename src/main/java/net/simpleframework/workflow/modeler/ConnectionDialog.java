package net.simpleframework.workflow.modeler;

import static net.simpleframework.common.I18n.$m;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.simpleframework.workflow.modeler.navigation.NavigationTree;
import net.simpleframework.workflow.modeler.navigation.NodeConnection;
import net.simpleframework.workflow.modeler.navigation.NodeRoot;
import net.simpleframework.workflow.modeler.utils.OkCancelDialog;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ConnectionDialog extends OkCancelDialog {
	private JTextField nameField, urlField;

	private JTextField loginField, passwordField;

	public ConnectionDialog(final NodeConnection connection) {
		super($m("ApplicationActions.0"), connection);
	}

	@Override
	protected Component createContentUI() {
		setMinimumSize(new Dimension(380, 270));

		final JLabel imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(64, 64));
		imageLabel.setIcon(SwingUtils.loadIcon("connection2.png"));
		nameField = new JTextField();
		urlField = new JTextField("http://");

		loginField = new JTextField();
		passwordField = new JPasswordField();

		final JPanel jp = SwingUtils.createKV(imageLabel, SwingUtils.createVertical(new JLabel(
				$m("ConnectionDialog.0")), nameField, new JLabel($m("ConnectionDialog.1")), urlField,
				new JLabel($m("ConnectionDialog.3")), loginField, new JLabel($m("ConnectionDialog.4")),
				passwordField), 64);
		jp.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
		return SwingUtils.createVertical(jp);
	}

	@Override
	protected void loadValues() {
		final NodeConnection connection = (NodeConnection) params[0];
		if (connection != null) {
			nameField.setText(connection.getName());
			urlField.setText(connection.getUrl());
			loginField.setText(connection.getLogin());
			passwordField.setText(connection.getPassword());
		}
	}

	@Override
	public void ok() {
		if (assertNull(nameField, urlField)) {
			return;
		}

		final ModelerSettings settings = ModelerSettings.get();
		final Vector<String> connections = new Vector<String>(settings.getConnections());
		final NodeConnection connection = (NodeConnection) params[0];
		final String name = nameField.getText();
		if (connections.contains(name)) {
			if (connection == null || !name.equals(connection.getName())) {
				SwingUtils.showError($m("ConnectionDialog.2"));
				return;
			}
		}

		super.ok();

		final NavigationTree tree = Application.get().getMainPane().getNavigationTree();
		final String url = urlField.getText();
		final String login = loginField.getText();
		final String password = passwordField.getText();
		if (connection != null) {
			final String old = connection.getName();
			final int p = connections.indexOf(old);
			if (p > -1) {
				connections.set(p, name);
				connection.setName(name);
				connection.setUrl(url);
				connection.setLogin(login);
				connection.setPassword(password);

				settings.setConnectionUrl(old, null);
				settings.setConnectionLogin(old, null);
				settings.setConnectionPassword(old, null);
			}
		} else {
			connections.add(name);
		}

		settings.setConnections(connections);
		settings.setConnectionUrl(name, url);
		settings.setConnectionLogin(name, login);
		settings.setConnectionPassword(name, password);
		settings.save();

		if (connection == null) {
			final NodeRoot root = tree.getModel().getRoot();
			root.add(new NodeConnection(name, url, login, password));
		}
		tree.updateUI();
	}
}
