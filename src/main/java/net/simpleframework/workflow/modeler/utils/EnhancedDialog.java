package net.simpleframework.workflow.modeler.utils;

import static net.simpleframework.common.I18n.$m;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.modeler.Application;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class EnhancedDialog extends JDialog {
	private boolean enterOk = false;

	private KeyHandler keyHandler;

	protected Object[] params;

	public EnhancedDialog(final String title, final Object... params) {
		super((Window) Application.get().getMainPane(), title);
		init(params);
	}

	protected void cancel() {
		dispose();
	}

	protected abstract void createUI();

	protected void loadValues() {
	}

	private void init(final Object... params) {
		this.params = params;

		((Container) getLayeredPane()).addContainerListener(new ContainerHandler());
		getContentPane().addContainerListener(new ContainerHandler());

		keyHandler = new KeyHandler();
		addKeyListener(keyHandler);
		addWindowListener(new WindowHandler());

		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		createUI();

		pack();
		setLocationRelativeTo(getOwner());

		loadValues();

		setVisible(true);
	};

	public void ok() {
	}

	public void setEnterOk(final boolean enterOk) {
		this.enterOk = enterOk;
	}

	class ContainerHandler extends ContainerAdapter {

		private void componentAdded(final Component comp) {
			comp.addKeyListener(keyHandler);
			if (comp instanceof Container) {
				final Container cont = (Container) comp;
				cont.addContainerListener(this);
				final Component[] comps = cont.getComponents();
				for (final Component element : comps) {
					componentAdded(element);
				}
			}
		}

		@Override
		public void componentAdded(final ContainerEvent evt) {
			componentAdded(evt.getChild());
		}

		private void componentRemoved(final Component comp) {
			comp.removeKeyListener(keyHandler);
			if (comp instanceof Container) {
				final Container cont = (Container) comp;
				cont.removeContainerListener(this);
				final Component[] comps = cont.getComponents();
				for (final Component element : comps) {
					componentRemoved(element);
				}
			}
		}

		@Override
		public void componentRemoved(final ContainerEvent evt) {
			componentRemoved(evt.getChild());
		}
	}

	class KeyHandler extends KeyAdapter {

		@Override
		public void keyPressed(final KeyEvent evt) {
			if (evt.isConsumed()) {
				return;
			}
			if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
				cancel();
				evt.consume();
			} else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
				final Component cmp = evt.getComponent();
				if (enterOk && !((cmp instanceof JEditorPane) || (cmp instanceof JTextArea))) {
					ok();
				}
			}
		}
	}

	class WindowHandler extends WindowAdapter {

		@Override
		public void windowClosing(final WindowEvent evt) {
			cancel();
		}
	}

	protected boolean assertNull(final JComponent... fields) {
		if (fields == null) {
			return false;
		}
		for (JComponent comp : fields) {
			String text = null;
			if (comp instanceof JTextComponent) {
				text = ((JTextComponent) comp).getText();
			} else if (comp instanceof JComboBox) {
				final ComboBoxEditor editor = ((JComboBox) comp).getEditor();
				comp = (JComponent) editor.getEditorComponent();
				text = String.valueOf(editor.getItem());
			}
			if (!StringUtils.hasText(text)) {
				comp.setBackground(Color.yellow);
				comp.requestFocus();
				comp.setToolTipText($m("EnhancedDialog.0"));
				return true;
			} else {
				comp.setBackground(UIManager.getColor("TextField.background"));
				comp.setToolTipText(null);
			}
		}
		return false;
	}
}
