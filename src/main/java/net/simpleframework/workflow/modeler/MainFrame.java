package net.simpleframework.workflow.modeler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import net.simpleframework.workflow.modeler.navigation.NavigationTree;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements IMainPane {
	static final ImageIcon icon = SwingUtils.loadIcon("app-icon.png");

	private MainStatusBar statusBar;

	private LoggerPanel loggerPanel;

	private JSplitPane vSplitPane, hSplitPane;

	private NavigationTree navigationTree;

	private MainTabbedPane tabbedPane;

	public MainFrame() {
		super("");
		createUserInterface();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	void createUserInterface() {
		setIconImage(icon.getImage());
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(final ComponentEvent e) {
				final int width = Math.max(getSize().width, getMinimumSize().width);
				final int height = Math.max(getSize().height, getMinimumSize().height);
				setSize(width, height);
			}
		});

		final ModelerSettings settings = ModelerSettings.get();
		if (settings.isWindowMaximum()) {
			setExtendedState(MAXIMIZED_BOTH);
		} else {
			final int width = settings.getWindowWidth();
			final int height = settings.getWindowHeight();
			setBounds(new Rectangle(settings.getWindowX(), settings.getWindowY(),
					width == 0 ? getMinimumSize().width : width,
					height == 0 ? getMinimumSize().height : height));
		}

		setJMenuBar(new MainMenu());

		final JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(new MainToolBar(), BorderLayout.NORTH);
		contentPane.add(statusBar = new MainStatusBar(), BorderLayout.SOUTH);

		vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		hSplitPane.setBorder(BorderFactory.createEmptyBorder());
		hSplitPane.setDividerSize(5);
		hSplitPane.setDividerLocation(settings.getHorizontalSplitLocation());
		hSplitPane.setLeftComponent(new JScrollPane(navigationTree = new NavigationTree()));
		hSplitPane.setRightComponent(tabbedPane = new MainTabbedPane());

		vSplitPane.setBorder(BorderFactory.createEmptyBorder());
		vSplitPane.setDividerSize(5);
		vSplitPane.setDividerLocation(settings.getVerticalSplitLocation());
		vSplitPane.setTopComponent(hSplitPane);
		vSplitPane.setBottomComponent(loggerPanel = new LoggerPanel());

		contentPane.add(vSplitPane, BorderLayout.CENTER);
		getContentPane().add(contentPane);
	}

	@Override
	public NavigationTree getNavigationTree() {
		return navigationTree;
	}

	@Override
	public MainTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	@Override
	public PrintStream getLogger() {
		return loggerPanel.getLogger();
	}

	@Override
	public void setStatusText(final String text) {
		statusBar.statusLabel.setText(text);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800, 600);
	}

	@Override
	public void dispose() {
		// if (!Application.instance.closeConfirm(false)) {
		// return;
		// }

		final ModelerSettings settings = ModelerSettings.get();
		settings.setWindowMaximum(getExtendedState() == MAXIMIZED_BOTH);

		final Rectangle rect = getBounds();
		settings.setWindowX(rect.x);
		settings.setWindowY(rect.y);
		settings.setWindowWidth(rect.width);
		settings.setWindowHeight(rect.height);

		settings.setVerticalSplitLocation(vSplitPane.getDividerLocation());
		settings.setHorizontalSplitLocation(hSplitPane.getDividerLocation());

		settings.save();

		super.dispose();
		Application.get().shutdown();
	}
}
