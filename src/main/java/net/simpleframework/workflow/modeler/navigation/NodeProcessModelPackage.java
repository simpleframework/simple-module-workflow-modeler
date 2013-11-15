package net.simpleframework.workflow.modeler.navigation;

import javax.swing.Icon;

import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NodeProcessModelPackage extends NavigationTreeNode {
	private static Icon packageIcon = SwingUtils.loadIcon("process_package.png");

	private final String text;

	public NodeProcessModelPackage(final String text) {
		this.text = text;
	}

	@Override
	public Icon getIcon() {
		return packageIcon;
	}

	@Override
	public String toString() {
		return text;
	}
}
