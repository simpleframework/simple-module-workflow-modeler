package net.simpleframework.workflow.modeler;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.simpleframework.workflow.modeler.utils.EnhancedDialog;
import net.simpleframework.workflow.modeler.utils.SplashScreen;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class AboutDialog extends EnhancedDialog {
	class ResourceThread implements Runnable {
		final Runtime rt = Runtime.getRuntime();

		final double MB = Math.pow(2d, 20d);

		boolean stop = false;

		Thread t;

		String str;

		NumberFormat Nf = NumberFormat.getNumberInstance();

		public ResourceThread() {
			t = new Thread(this, "ResourceMonitor Thread");
			Nf.setMaximumFractionDigits(2);
			Nf.setMinimumFractionDigits(2);
			Nf.setMaximumIntegerDigits(8);
			t.start();
		}

		private void halt() {
			synchronized (t) {
				stop = true;
				t.notify();
				Nf = null;
				t = null;
			}
		}

		@Override
		public void run() {
			try {
				while (!stop) {
					final long UsedMemory = rt.totalMemory() - rt.freeMemory();

					if (resourceMeter.getMaximum() != (int) rt.totalMemory()) {
						resourceMeter.setMaximum((int) rt.totalMemory());
					}
					if (resourceMeter.getValue() != (int) UsedMemory) {
						resourceMeter.setValue((int) UsedMemory);
						str = Nf.format((rt.totalMemory() / MB));
						resourceMeter.setString(Nf.format(UsedMemory / MB) + "MB/" + str + "MB");
						System.runFinalization();
					}
					Thread.sleep(2000);
				}
			} catch (final Exception e) {
			}
		}
	}

	private ResourceThread rThread;

	private JProgressBar resourceMeter;

	public AboutDialog() {
		super($m("AboutDialog.8"));
	}

	@Override
	public void cancel() {
		rThread.halt();
		System.runFinalization();
		super.cancel();
	}

	@Override
	protected void createUI() {
		final JPanel p1 = new JPanel(new BorderLayout());
		final JPanel p2 = new JPanel(new GridBagLayout());

		final JLabel logoLabel = new JLabel(getLogo());
		p1.add(logoLabel, BorderLayout.CENTER);
		p2.setPreferredSize(logoLabel.getPreferredSize());

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 8, 5, 8);
		p2.add(new JLabel($m("AboutDialog.0")), gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 8, 5, 8);
		p2.add(new JLabel($m("AboutDialog.1")), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 8, 5, 8);
		p2.add(new JLabel(getProduct()), gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 8, 5, 8);
		p2.add(new JLabel(getVersion()), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		final Set<Entry<Object, Object>> sets = System.getProperties().entrySet();
		final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		final Iterator<Entry<Object, Object>> it = sets.iterator();
		while (it.hasNext()) {
			final Entry<Object, Object> entry = it.next();
			final Vector<Object> row = new Vector<Object>(Arrays.asList(entry.getKey(),
					entry.getValue()));
			data.add(row);
		}
		final DefaultTableModel tm = new DefaultTableModel(data, new Vector<String>(Arrays.asList(
				$m("AboutDialog.2"), $m("AboutDialog.3")))) {

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return false;
			}
		};
		final JTable table = new JTable(tm);
		p2.add(new JScrollPane(table), gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		p2.add(new JLabel($m("AboutDialog.4")), gbc);
		gbc.gridx++;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		p2.add(resourceMeter = new JProgressBar(), gbc);
		resourceMeter.setStringPainted(true);
		gbc.gridy++;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		final JButton gcBtn = new JButton($m("AboutDialog.5"));
		p2.add(gcBtn, gbc);

		gcBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent actionEvent) {
				System.gc();
			}
		});

		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab($m("AboutDialog.6"), p1);
		tabbedPane.addTab($m("AboutDialog.7"), p2);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		rThread = new ResourceThread();
	}

	protected Icon getLogo() {
		return SplashScreen.splashIcon;
	}

	protected String getProduct() {
		return null;
	}

	protected String getVersion() {
		return null;
	}

	@Override
	public void ok() {
		dispose();
	}
}
