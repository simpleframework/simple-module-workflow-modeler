package net.simpleframework.workflow.modeler.process;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.workflow.modeler.utils.JToolBarEx;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ModelGraphToolbar extends JToolBarEx {

	JToggleButton normal, user, b4, sub, merge, b7;

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

		merge = new JToggleButton(SwingUtils.loadIcon("model_merge.gif"));
		b7 = new JToggleButton(SwingUtils.loadIcon("text.gif"));
		final ButtonGroup bg = new ButtonGroup();
		for (final JToggleButton btn : ArrayUtils.asList(normal, user, sub, b4, merge, b7)) {
			bg.add(btn);
		}

		add(normal);
		// add(b2);
		addSeparator();
		add(user);
		add(sub);
		add(b4);
		add(merge);
		add(b7);
	}
}
