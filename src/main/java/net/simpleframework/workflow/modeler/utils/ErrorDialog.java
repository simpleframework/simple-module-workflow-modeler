package net.simpleframework.workflow.modeler.utils;

import static net.simpleframework.common.I18n.$m;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public final class ErrorDialog extends EnhancedDialog {
	private final class CloseButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent evt) {
			dispose();
		}
	}

	static class MessagePanel extends MultipleLineLabel {

		MessagePanel(final String msg) {
			super();
			setText(msg);
			setBackground(ErrorDialog.getTextAreaBackgroundColor());
			setRows(3);
			final Dimension dim = getPreferredSize();
			dim.width = PREFERRED_WIDTH;
			setPreferredSize(dim);
		}
	}

	private final class MoreButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent evt) {
			stackTraceScroller.setVisible(!stackTraceScroller.isVisible());
			moreBtn.setText(stackTraceScroller.isVisible() ? LESS : MORE);
			pack();
		}
	}

	static class StackTracePanel extends MultipleLineLabel {

		StackTracePanel(final Throwable th) {
			super();
			setBackground(ErrorDialog.getTextAreaBackgroundColor());
			if (th != null) {
				setText(getStackTrace(th));
				setRows(10);
			}
		}

		private String getStackTrace(final Throwable th) {
			if (th == null) {
				throw new IllegalArgumentException("Throwable == null");
			}

			final StringWriter sw = new StringWriter();
			try {
				final PrintWriter pw = new PrintWriter(sw);
				try {
					th.printStackTrace(pw);
					return sw.toString();
				} finally {
					pw.close();
				}
			} finally {
				try {
					sw.close();
				} catch (final IOException ignore) {
				}
			}
		}
	}

	private static final String ERROR = $m("ErrorDialog.0");

	private static final String UNKNOWN_ERROR = $m("ErrorDialog.1");

	private static final String MORE = $m("ErrorDialog.2");

	private static final String LESS = $m("ErrorDialog.3");

	private static final String CLOSE = $m("ErrorDialog.4");

	private static final int PREFERRED_WIDTH = 400;

	private static Color getTextAreaBackgroundColor() {
		return (Color) UIManager.get("TextArea.background");
	}

	private JButton closeBtn;

	private final ActionListener closeHandler = new CloseButtonHandler();

	private JButton moreBtn;

	private final ActionListener moreHandler = null;

	private JScrollPane stackTraceScroller;

	public ErrorDialog(final String msg) {
		super(ERROR, msg, null);
	}

	public ErrorDialog(final String msg, final Throwable th) {
		super(ERROR, msg, th);
	}

	public ErrorDialog(final Throwable th) {
		super(ERROR, null, th);
	}

	@Override
	protected void createUI() {
		String msg = (String) params[0];
		final Throwable th = (Throwable) params[1];
		if ((msg == null) || (msg.length() == 0)) {
			if (th != null) {
				msg = th.getMessage();
				if ((msg == null) || (msg.length() == 0)) {
					msg = th.toString();
				}
			}
		}
		if ((msg == null) || (msg.length() == 0)) {
			msg = UNKNOWN_ERROR;
		}

		final MessagePanel msgPnl = new MessagePanel(msg);
		stackTraceScroller = new JScrollPane(new StackTracePanel(th));
		stackTraceScroller.setVisible(false);

		final JPanel btnsPnl = new JPanel();
		if (th != null) {
			moreBtn = new JButton(MORE);
			btnsPnl.add(moreBtn);
			moreBtn.addActionListener(new MoreButtonHandler());
		}
		closeBtn = new JButton(CLOSE);
		closeBtn.addActionListener(new CloseButtonHandler());
		btnsPnl.add(closeBtn);

		final Container content = getContentPane();
		content.setLayout(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		content.add(new JScrollPane(msgPnl), gbc);
		++gbc.gridy;
		content.add(btnsPnl, gbc);
		++gbc.gridy;
		content.add(stackTraceScroller, gbc);
		setResizable(false);

		SwingUtils.initComponentHeight(moreBtn, closeBtn);
	}

	@Override
	public void dispose() {
		if ((closeBtn != null) && (closeHandler != null)) {
			closeBtn.removeActionListener(closeHandler);
		}
		if ((moreBtn != null) && (moreHandler != null)) {
			moreBtn.removeActionListener(moreHandler);
		}
		super.dispose();
	}

	@Override
	public void ok() {
	}
}
