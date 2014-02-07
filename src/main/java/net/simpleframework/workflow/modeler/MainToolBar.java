package net.simpleframework.workflow.modeler;

import net.simpleframework.workflow.modeler.ApplicationActions.AboutAction;
import net.simpleframework.workflow.modeler.ApplicationActions.NewConnectionAction;
import net.simpleframework.workflow.modeler.utils.JToolBarEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class MainToolBar extends JToolBarEx {

	@Override
	protected void createActions(final Object... objects) {
		add(new NewConnectionAction());
		add(new AboutAction());
		addSeparator();
		add(new AboutAction());
	}
}
