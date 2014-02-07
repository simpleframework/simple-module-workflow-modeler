package net.simpleframework.workflow.modeler.utils;

import java.util.EventListener;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ICloseableTabbedPaneListener extends EventListener {

	boolean closeTab(int tabIndexToClose);
}
