package net.simpleframework.workflow.modeler.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class SplashScreen extends JWindow {
	public static Icon splashIcon = SwingUtils.loadIcon("splash.png");

	public final static int SPLASH_IMAGE_BACKGROUND = 0xFFFFFF; // 0;

	private JProgressBar progressBar;

	public SplashScreen(final int progressBarSize) {
		final JPanel mainPnl = new JPanel(new BorderLayout());
		mainPnl.setBackground(new Color(SPLASH_IMAGE_BACKGROUND));
		mainPnl.add(BorderLayout.NORTH, new JLabel(getLogo()));

		final String info = getInfo();
		if (StringUtils.hasText(info)) {
			final MultipleLineLabel versionLbl = new MultipleLineLabel();
			versionLbl.setOpaque(false);
			versionLbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			versionLbl.append(info);
			mainPnl.add(BorderLayout.CENTER, versionLbl);
		}

		if (progressBarSize > 0) {
			progressBar = new JProgressBar(0, progressBarSize);
			progressBar.setPreferredSize(new Dimension(0, 15));
			progressBar.setStringPainted(true);
			progressBar.setString("");
			progressBar.setBorder(BorderFactory.createEmptyBorder(5, 1, 5, 2));
			progressBar.setBackground(new Color(SPLASH_IMAGE_BACKGROUND));
			progressBar.setForeground(Color.PINK);
			mainPnl.add(BorderLayout.SOUTH, progressBar);
		}

		getContentPane().add(mainPnl);

		pack();

		SwingUtils.centerWithinScreen(this);
		setVisible(true);
	}

	public void indicateNewTask() {
		indicateNewTask(null);
	}

	public void indicateNewTask(final String text) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					if (text != null) {
						progressBar.setString(text);
					}
					progressBar.setValue(progressBar.getValue() + 1);
				}
			});
			Thread.yield();
		} catch (final Exception ignore) {
		}
	}

	protected Icon getLogo() {
		return splashIcon;
	}

	protected abstract String getInfo();
}
