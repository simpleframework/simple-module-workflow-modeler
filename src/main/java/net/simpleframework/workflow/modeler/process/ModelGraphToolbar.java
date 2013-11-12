package net.simpleframework.workflow.modeler.process;

import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

import net.simpleframework.workflow.modeler.utils.JToolBarEx;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ModelGraphToolbar extends JToolBarEx {

	JToggleButton normal, user, b4, sub, b6, b7;

	public ModelGraphToolbar(final ModelGraph modelGraph) {
		super(modelGraph);
	}

	@Override
	protected void createActions(final Object... objects) {
		final ModelGraph modelGraph = (ModelGraph) objects[0];
		add(new ModelNewAction(modelGraph));
		add(new ModelSaveAction(modelGraph));

		addSeparator();

		normal = new JToggleButton(SwingUtils.loadIcon("model_normal.png"));
		normal.setSelected(true);
		// b2 = new JToggleButton(SwingUtils.loadIcon("model_link.gif"));
		user = new JToggleButton(SwingUtils.loadIcon("model_user.gif"));
		sub = new JToggleButton(SwingUtils.loadIcon("model_sub.gif"));
		b4 = new JToggleButton(SwingUtils.loadIcon("model_auto.png"));

		b6 = new JToggleButton(SwingUtils.loadIcon("model_merge.gif"));
		b7 = new JToggleButton(SwingUtils.loadIcon("text.gif"));
		final ButtonGroup bg = new ButtonGroup();
		for (final JToggleButton btn : Arrays.asList(normal, user, sub, b4, b6, b7)) {
			bg.add(btn);
		}

		add(normal);
		// add(b2);
		addSeparator();
		add(user);
		add(sub);
		add(b4);
		add(b6);
		add(b7);
	}
}
