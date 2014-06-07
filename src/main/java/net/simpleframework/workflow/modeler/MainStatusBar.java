package net.simpleframework.workflow.modeler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class MainStatusBar extends JPanel {
	class MemoryPanel extends JLabel {
		private class TimerListener implements ActionListener {

			@Override
			public void actionPerformed(final ActionEvent evt) {
				updateMemoryStatus();
			}
		}

		private Timer timer;

		public MemoryPanel() {
			super("", SwingConstants.CENTER);
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2)) {
						System.gc();
						updateMemoryStatus();
					}
				}
			});
		}

		@Override
		public void addNotify() {
			super.addNotify();
			updateMemoryStatus();
			ToolTipManager.sharedInstance().registerComponent(this);
			timer = new Timer(2000, new TimerListener());
			timer.start();
		}

		private String formatSize(final long longSize) {
			return formatSize(longSize, -1);
		}

		private String formatSize(final long longSize, final int decimalPos) {
			final NumberFormat fmt = NumberFormat.getNumberInstance();
			if (decimalPos >= 0) {
				fmt.setMaximumFractionDigits(decimalPos);
			}
			final double size = longSize;
			double val = size / (1024 * 1024);
			if (val > 1) {
				return fmt.format(val).concat(" MB");
			}
			val = size / 1024;
			if (val > 10) {
				return fmt.format(val).concat(" KB");
			}
			return fmt.format(val).concat(" bytes");
		}

		@Override
		public Dimension getPreferredSize() {
			final Dimension dim = super.getPreferredSize();
			final FontMetrics fm = getFontMetrics(getFont());
			dim.width = fm.stringWidth("99.9MB/99.9MB");
			final Border border = getBorder();
			if (border != null) {
				final Insets ins = border.getBorderInsets(this);
				if (ins != null) {
					dim.width += (ins.left + ins.right);
				}
			}
			final Insets ins = getInsets();
			if (ins != null) {
				dim.width += (ins.left + ins.right);
			}
			dim.width += 40;
			return dim;
		}

		@Override
		public String getToolTipText() {
			final Runtime rt = Runtime.getRuntime();
			final long totalMemory = rt.totalMemory();
			final long freeMemory = rt.freeMemory();
			final long usedMemory = totalMemory - freeMemory;
			final StringBuilder buf = new StringBuilder();
			buf.append(formatSize(usedMemory)).append(" used from ").append(formatSize(totalMemory))
					.append(" total leaving ").append(formatSize(freeMemory)).append(" free");
			return buf.toString();
		}

		@Override
		public void removeNotify() {
			ToolTipManager.sharedInstance().unregisterComponent(this);
			if (timer != null) {
				timer.stop();
				timer = null;
			}
			super.removeNotify();
		}

		private void updateMemoryStatus() {
			final Runtime rt = Runtime.getRuntime();
			final long totalMemory = rt.totalMemory();
			final long freeMemory = rt.freeMemory();
			final long usedMemory = totalMemory - freeMemory;
			final StringBuilder buf = new StringBuilder();
			buf.append(formatSize(usedMemory, 1)).append("/").append(formatSize(totalMemory, 1));
			setText(buf.toString());
		}
	}

	class TimePanel extends JLabel implements ActionListener {

		private Timer timer;

		private final DateFormat fmt = DateFormat.getTimeInstance(DateFormat.LONG);

		public TimePanel() {
			super("", SwingConstants.CENTER);
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			setText(fmt.format(Calendar.getInstance().getTime()));
		}

		@Override
		public void addNotify() {
			super.addNotify();
			timer = new Timer(1000, this);
			timer.start();
		}

		@Override
		public Dimension getPreferredSize() {
			final Dimension dim = super.getPreferredSize();
			final FontMetrics fm = getFontMetrics(getFont());
			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			dim.width = fm.stringWidth(fmt.format(cal.getTime()));
			final Border border = getBorder();
			if (border != null) {
				final Insets ins = border.getBorderInsets(this);
				if (ins != null) {
					dim.width += (ins.left + ins.right);
				}
			}
			final Insets ins = getInsets();
			if (ins != null) {
				dim.width += (ins.left + ins.right);
			}
			return dim;
		}

		@Override
		public void removeNotify() {
			if (timer != null) {
				timer.stop();
				timer = null;
			}
			super.removeNotify();
		}
	}

	final JLabel statusLabel = new JLabel();

	public MainStatusBar() {
		super(new BorderLayout());
		statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
		add(statusLabel, BorderLayout.CENTER);
		final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(rightPanel, BorderLayout.EAST);
		rightPanel.add(new MemoryPanel());
		rightPanel.add(new TimePanel());
	}
}
