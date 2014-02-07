package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.simpleframework.workflow.modeler.utils.OkCancelDialog;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.Node;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.ProcessNode;

import com.mxgraph.model.mxCell;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractEditorDialog extends OkCancelDialog {

	protected VariablePane variablePane;

	protected DescriptionPane descriptionPane;

	public AbstractEditorDialog(final String title, final ModelGraph modelGraph, final mxCell cell) {
		super(title, modelGraph, cell);
	}

	@Override
	protected void createUI() {
		setMinimumSize(new Dimension(420, 500));
		super.createUI();
		initComponents();
	}

	private ProcessDocument nDocument;

	protected Node getNode() {
		final ModelGraph modelGraph = (ModelGraph) params[0];
		final mxCell cell = (mxCell) params[1];
		if (nDocument == null) {
			nDocument = modelGraph.getTabbedContent().getDocument().clone();
		}
		final ProcessNode processNode = nDocument.getProcessNode();
		if (cell == null) {
			return processNode;
		} else {
			return processNode.getNodeById(cell.getId());
		}
	}

	protected void initComponents() {
		final Node node = getNode();
		if (descriptionPane != null) {
			if (descriptionPane.text != null) {
				descriptionPane.text.setText(node.getText());
			}
			descriptionPane.editor.setText(node.getDescription());
		}
	}

	@Override
	public void ok() {
		if (descriptionPane != null) {
			final Node node = getNode();
			if (descriptionPane.text != null) {
				final String nTxt = descriptionPane.text.getText();
				if (!nTxt.equals(node.getText())) {
					node.setText(nTxt);
					final Object cell = params[1];
					if (cell instanceof TransitionCell) {
						((TransitionCell) cell).setValue(nTxt);
					} else if (cell instanceof TaskCell) {
						((TaskCell) cell).setValue(new CellValue(node));
					}
				}
			}
			node.setDescription(descriptionPane.editor.getText());
		}
		final ModelGraph modelGraph = (ModelGraph) params[0];
		modelGraph.getGraph().refresh();
		if (nDocument != null) {
			modelGraph.getTabbedContent().setDocument(nDocument);
		}
		super.ok();
	}

	protected VariablePane createVariablePane(final Node variableNode) {
		return (variablePane = new VariablePane(variableNode));
	}

	protected abstract Map<String, Object> getTabbedComponents();

	@Override
	protected Component createContentUI() {
		final JTabbedPane tabbedPane = new JTabbedPane();
		final Map<String, Object> tabs = getTabbedComponents();
		if (tabs != null) {
			for (final Map.Entry<String, Object> tab : tabs.entrySet()) {
				tabbedPane.add(tab.getKey(), (Component) tab.getValue());
			}
		}
		tabbedPane.add(DescriptionPane.title, descriptionPane = new DescriptionPane(true));
		return tabbedPane;
	}

	public static class DescriptionPane extends JPanel {
		public static final String title = $m("DescriptionPane.0");

		JTextField text;

		JTextArea editor;

		public DescriptionPane() {
			this(false);
		}

		public DescriptionPane(final boolean showText) {
			super(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(3, 8, 0, 8);
			gbc.anchor = GridBagConstraints.WEST;
			if (showText) {
				add(new JLabel($m("DescriptionPane.1")), gbc);
				gbc.gridy++;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				add(text = new JTextField(), gbc);
				gbc.gridy++;
			}
			add(new JLabel($m("DescriptionPane.2")), gbc);
			gbc.gridy += 2;
			gbc.insets = new Insets(5, 8, 5, 8);
			gbc.fill = GridBagConstraints.NONE;
			final JButton btn = new JButton($m("DescriptionPane.3"));
			add(btn, gbc);
			btn.setMnemonic('F');
			gbc.gridy--;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.insets = new Insets(5, 8, 0, 8);
			gbc.fill = GridBagConstraints.BOTH;
			add(new JScrollPane(editor = new JTextArea()), gbc);
			editor.setFont(new Font("SansSerif", Font.PLAIN, 13));
			SwingUtils.bindTextEvent(btn, editor);
		}
	}
}
