package net.simpleframework.workflow.modeler.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.modeler.Application;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class JToolBarEx extends JToolBar {

	public JToolBarEx(final Object... objects) {
		setFloatable(false);
		setRollover(true);
		setBorderPainted(false);
		setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

		createActions(objects);

		final Component[] comp = getComponents();
		for (final Component element : comp) {
			if (!(element instanceof AbstractButton)) {
				continue;
			}
			element.setPreferredSize(new Dimension(32, 32));
			final AbstractButton btn = (AbstractButton) element;
			final Action action = btn.getAction();
			if (action != null) {
				final String str = StringUtils.text((String) action.getValue(Action.LONG_DESCRIPTION),
						(String) action.getValue(Action.SHORT_DESCRIPTION),
						(String) action.getValue(Action.NAME));
				btn.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(final MouseEvent e) {
						if (str != null) {
							Application.get().getMainPane().setStatusText(str);
						}
					}

					@Override
					public void mouseExited(final MouseEvent e) {
						Application.get().getMainPane().setStatusText(null);
					}
				});
			}
		}
	}

	protected abstract void createActions(Object... objects);

	@Override
	public void addSeparator() {
		addSeparator(new Dimension(4, 4));
	}
}
