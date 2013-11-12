package net.simpleframework.workflow.modeler;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.PlainDocument;

import net.simpleframework.workflow.modeler.utils.JTextAreaOutputStream;
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
public class LoggerPanel extends JPanel {
	private JTextAreaOutputStream logger;

	private JTextArea editor;

	public LoggerPanel() {
		super(new BorderLayout());
		createUI();
	}

	private void createUI() {
		setBorder(BorderFactory.createEmptyBorder());
		editor = new JTextArea();
		editor.setEditable(false);
		editor.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		editor.setForeground(Color.BLUE);
		editor.getDocument().putProperty(PlainDocument.tabSizeAttribute, new Integer(2));
		editor.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				loggerMousePressed(e);
			}
		});

		logger = new JTextAreaOutputStream(editor);
		System.setOut(logger);
		System.setErr(logger);

		add(new JToolBarEx() {

			@Override
			protected void createActions(final Object... objects) {
				setOrientation(VERTICAL);

				addSeparator();
				add(getCloseAction());
				add(getSaveAction());
				add(getClearAction());
			}
		}, BorderLayout.WEST);
		add(new JScrollPane(editor));
	}

	public JTextAreaOutputStream getLogger() {
		return logger;
	}

	private void loggerMousePressed(final MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			final JPopupMenu popup = new JPopupMenu();
			popup.add(getClearAction());
			popup.add(getCopyAction());
			popup.addSeparator();
			popup.add(getSaveAction());
			popup.addSeparator();
			popup.add(getCloseAction());
			final Point p = e.getPoint();
			SwingUtils.showPopupMenu(popup, editor, (int) p.getX(), (int) p.getY());
		}
	}

	private Action closeAction, copyAction, clearAction, saveAction;

	private Action getCloseAction() {
		if (closeAction == null) {
			closeAction = new AbstractAction() {
				{
					putValue(Action.NAME, $m("LoggerPanel.0"));
					putValue(Action.SMALL_ICON, SwingUtils.loadIcon("close.gif"));
				}

				@Override
				public void actionPerformed(final ActionEvent e) {
					// frame.updateContentPanel(false);
				}
			};
		}
		return closeAction;
	}

	private Action getSaveAction() {
		if (saveAction == null) {
			saveAction = new AbstractAction() {
				{
					putValue(Action.NAME, $m("LoggerPanel.1"));
					putValue(Action.SMALL_ICON, SwingUtils.loadIcon("save.png"));
				}

				@Override
				public void actionPerformed(final ActionEvent e) {
					// final JFileChooser chooser =
					// SwingHelper.createJFileChooser(new String[] { "txt" },
					// "文本文件");
					// final int ret = chooser.showSaveDialog(frame);
					// if (ret == JFileChooser.APPROVE_OPTION) {
					// try {
					// String filename = chooser.getSelectedFile().getPath();
					// if (!filename.toLowerCase().endsWith(".txt")) {
					// filename += ".txt";
					// }
					// IoHelper.writeToFile(new File(filename),
					// editor.getText().getBytes());
					// } catch (final IOException e1) {
					// }
					// }
				}
			};
		}
		return saveAction;
	}

	private Action getClearAction() {
		if (clearAction == null) {
			clearAction = new AbstractAction() {
				{
					putValue(Action.NAME, $m("LoggerPanel.2"));
					putValue(Action.SMALL_ICON, SwingUtils.loadIcon("clear.png"));
				}

				@Override
				public void actionPerformed(final ActionEvent e) {
					editor.setText(null);
				}
			};
		}
		return clearAction;
	}

	private Action getCopyAction() {
		if (copyAction == null) {
			copyAction = new AbstractAction() {
				{
					putValue(Action.NAME, $m("LoggerPanel.3"));
					putValue(Action.SMALL_ICON, SwingUtils.loadIcon("copy.png"));
					final boolean selectedText = editor.getSelectionEnd() - editor.getSelectionStart() > 0;
					setEnabled(selectedText);
				}

				@Override
				public void actionPerformed(final ActionEvent e) {
					editor.copy();
				}
			};
		}
		return copyAction;
	}
}
