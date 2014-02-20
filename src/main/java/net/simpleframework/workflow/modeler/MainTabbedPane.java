package net.simpleframework.workflow.modeler;

import javax.swing.JComponent;

import net.simpleframework.workflow.modeler.utils.ITabbedContent;
import net.simpleframework.workflow.modeler.utils.JTabbedPaneEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class MainTabbedPane extends JTabbedPaneEx {

	public static final String TABBED_CONTENT = "__tabbedContent";

	public ITabbedContent getTabbedContent(final JComponent component) {
		return component == null ? null : (ITabbedContent) component
				.getClientProperty(TABBED_CONTENT);
	}

	public ITabbedContent getSelectedTabbedContent() {
		return getTabbedContent((JComponent) getSelectedComponent());
	}

	public void addTab(final ITabbedContent tabbedContent) {
		final Object identifier = tabbedContent.getIdentifier();
		if (identifier != null) {
			for (int i = 0; i < getTabCount(); i++) {
				final ITabbedContent tc = getTabbedContent((JComponent) getComponentAt(i));
				if ((tc != null) && identifier.equals(tc.getIdentifier())) {
					setSelectedIndex(i);
					return;
				}
			}
		}

		final JComponent component = tabbedContent.getComponent();
		component.putClientProperty(TABBED_CONTENT, tabbedContent);
		addTab(tabbedContent.getTitle(), component);
		final int index = getTabCount() - 1;
		setSelectedIndex(index);

		// frame.mainMenu.updateUI();
	}
}
