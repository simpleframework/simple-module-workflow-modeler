package net.simpleframework.workflow.modeler;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.workflow.modeler.utils.ITabbedContent;
import net.simpleframework.workflow.modeler.utils.TreeNodeEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TabbedContent implements ITabbedContent {
	public TabbedContent() {
	}

	@Override
	public JComponent getComponent() {
		return null;
	}

	@Override
	public Object getIdentifier() {
		return getTreeNode();
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public TreeNodeEx getTreeNode() {
		return null;
	}

	@Override
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isModify() {
		return false;
	}

	@Override
	public void onModified(final boolean modify) {
		final JTabbedPane jt = Application.get().getMainPane().getTabbedPane();
		final int index = jt.indexOfComponent(getComponent());
		if (index > -1) {
			if (modify) {
				jt.setTitleAt(index, "* " + getTitle());
			} else {
				jt.setTitleAt(index, getTitle());
			}
		}
	}

	@Override
	public void save() {
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof ITabbedContent ? ObjectUtils.objectEquals(getIdentifier(),
				((ITabbedContent) obj).getIdentifier()) : false;
	}

	@Override
	public String toString() {
		return getTitle();
	}
}
