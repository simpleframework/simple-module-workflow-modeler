package net.simpleframework.workflow.modeler.utils;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.simpleframework.common.coll.ArrayUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class OkCancelDialog extends EnhancedDialog implements ActionListener {
	private boolean ok;

	protected JButton okButton;

	protected JButton cancelButton;

	protected JButton helpButton;

	public OkCancelDialog(final String title, final Object... element) {
		super(title, element);
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Object source = evt.getSource();
		if (source == okButton) {
			try {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				ok();
			} catch (final Exception ex) {
				SwingUtils.showError(ex);
			} finally {
				setCursor(Cursor.getDefaultCursor());
			}
		} else if (source == cancelButton) {
			cancel();
		} else if (source == helpButton) {
			help();
		}
	}

	@Override
	protected void cancel() {
		ok = false;
		super.cancel();
	}

	protected JButton[] getBottomButtons() {
		return null;
	}

	protected abstract Component createContentUI();

	@Override
	protected void createUI() {
		setEnterOk(true);
		ok = false;

		final JPanel panel = new JPanel(new BorderLayout());

		okButton = new JButton($m("OkCancelDialog.0"));
		okButton.setMnemonic('O');
		cancelButton = new JButton($m("OkCancelDialog.1"));
		cancelButton.setMnemonic('C');
		helpButton = new JButton($m("OkCancelDialog.2"));
		helpButton.setMnemonic('H');

		final JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 8));

		final JButton[] bottomButtons = getBottomButtons();
		if (bottomButtons != null) {
			for (final JButton bottomButton : bottomButtons) {
				bp.add(bottomButton);
			}
		}

		if (showOk()) {
			bp.add(okButton);
		}
		bp.add(cancelButton);
		if (showHelp()) {
			bp.add(Box.createHorizontalStrut(2));
			bp.add(helpButton);
		}
		panel.setBorder(getBorder());
		final Component content = createContentUI();
		if (content != null) {
			panel.add(content, BorderLayout.CENTER);
		}
		panel.add(bp, BorderLayout.SOUTH);

		getContentPane().add(panel);

		JButton[] btns = new JButton[] { okButton, cancelButton, helpButton };
		if ((bottomButtons != null) && (bottomButtons.length > 0)) {
			btns = ArrayUtils.add(btns, JButton.class, bottomButtons);
		}
		SwingUtils.setJButtonSizesTheSame(btns);

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		helpButton.addActionListener(this);
	}

	protected Border getBorder() {
		return BorderFactory.createEmptyBorder();
	}

	protected void help() {
	}

	public boolean isOk() {
		return ok;
	}

	@Override
	public void ok() {
		dispose();
		ok = true;
	}

	protected boolean showOk() {
		return true;
	}

	protected boolean showHelp() {
		return false;
	}
}
