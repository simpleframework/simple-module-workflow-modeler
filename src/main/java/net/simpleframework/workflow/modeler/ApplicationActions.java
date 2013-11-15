package net.simpleframework.workflow.modeler;

import static net.simpleframework.common.I18n.$m;

import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class ApplicationActions {
	public static final Icon newIcon = SwingUtils.loadIcon("new.png");
	public static final Icon saveIcon = SwingUtils.loadIcon("save.png");
	public static final Icon helpIcon = SwingUtils.loadIcon("help.gif");
	public static final Icon deleteIcon = SwingUtils.loadIcon("delete.png");
	public static final Icon openIcon = SwingUtils.loadIcon("open.png");
	public static final Icon connectionIcon = SwingUtils.loadIcon("connection.png");
	public static final Icon propertyIcon = SwingUtils.loadIcon("property.png");
	public static final Icon processModelIcon = SwingUtils.loadIcon("process_model.gif");
	public static final Icon processNodeIcon = SwingUtils.loadIcon("process_node.png");

	public static abstract class ApplicationAction extends AbstractAction {
		public ApplicationAction(final String name, final Icon icon) {
			this(name, null, icon);
		}

		public ApplicationAction(final String name, final Integer mnemonic, final Icon icon) {
			putValue(NAME, name);
			if (mnemonic != null) {
				putValue(Action.MNEMONIC_KEY, mnemonic);
			}
			if (icon != null) {
				putValue(SMALL_ICON, icon);
			}
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final Window window = (Window) Application.get().getMainPane();
			try {
				window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				actionInvoked(e);
			} finally {
				window.setCursor(Cursor.getDefaultCursor());
			}
		}

		protected abstract void actionInvoked(final ActionEvent e);
	}

	public static class AboutAction extends ApplicationAction {

		public AboutAction() {
			super($m("AboutDialog.8"), helpIcon);
		}

		@Override
		protected void actionInvoked(final ActionEvent e) {
			new AboutDialog();
		}
	}

	public static class NewConnectionAction extends ApplicationAction {

		public NewConnectionAction() {
			super($m("ApplicationActions.0"), connectionIcon);
		}

		@Override
		protected void actionInvoked(final ActionEvent e) {
			new ConnectionDialog(null);
		}
	}
}
