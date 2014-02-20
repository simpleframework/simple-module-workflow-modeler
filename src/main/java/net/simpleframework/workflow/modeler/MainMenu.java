package net.simpleframework.workflow.modeler;

import static net.simpleframework.common.I18n.$m;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import net.simpleframework.workflow.modeler.ApplicationActions.AboutAction;
import net.simpleframework.workflow.modeler.ApplicationActions.SaveAsAction;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MainMenu extends JMenuBar {
	public MainMenu() {
		super();
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setBorderPainted(false);
		add(createFileMenu());
		add(createHelpMenu());
	}

	private JMenu createFileMenu() {
		final JMenu menu = new JMenu($m("MainMenu.0") + "(F)");
		menu.add(new SaveAsAction());
		return menu;
	}

	private JMenu createHelpMenu() {
		final JMenu menu = new JMenu($m("MainMenu.1") + "(H)");
		// menu.setMnemonic('H');
		// menu.add(new HomePageAction());
		menu.addSeparator();
		menu.add(new AboutAction());
		return menu;
	}

	private static final long serialVersionUID = -3338086251896571082L;
}
