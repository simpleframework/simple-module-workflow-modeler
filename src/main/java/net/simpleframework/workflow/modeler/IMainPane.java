package net.simpleframework.workflow.modeler;

import java.io.PrintStream;

import net.simpleframework.workflow.modeler.navigation.NavigationTree;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IMainPane {

	void setStatusText(final String text);

	PrintStream getLogger();

	NavigationTree getNavigationTree();

	MainTabbedPane getTabbedPane();
}
