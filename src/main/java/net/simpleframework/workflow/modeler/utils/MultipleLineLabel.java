package net.simpleframework.workflow.modeler.utils;

import javax.swing.JTextArea;
import javax.swing.LookAndFeel;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class MultipleLineLabel extends JTextArea {

	public MultipleLineLabel() {
		this(null);
	}

	public MultipleLineLabel(final String title) {
		super();
		setEditable(false);
		setLineWrap(true);
		setWrapStyleWord(true);
		setText(title);
	}

	@Override
	public void updateUI() {
		LookAndFeel.installBorder(this, "Label.border");
		LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
		super.updateUI();
		LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
	}
}
