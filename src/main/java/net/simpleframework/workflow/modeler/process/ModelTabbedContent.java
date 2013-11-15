package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.modeler.TabbedContent;
import net.simpleframework.workflow.modeler.navigation.NodeProcessModel;
import net.simpleframework.workflow.modeler.utils.xmleditor.XMLEditorKit;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModelTabbedContent extends TabbedContent {

	private JTabbedPane tabbedPane;

	private final NodeProcessModel nodeProcessModel;

	private ProcessDocument document;

	private ModelGraphToolbar toolbar;

	public ModelTabbedContent(final NodeProcessModel nodeProcessModel, final ProcessDocument document) {
		this.nodeProcessModel = nodeProcessModel;
		this.document = document;
	}

	@Override
	public JTabbedPane getComponent() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);

			final JPanel tab1 = new JPanel(new BorderLayout());

			final ModelGraph modelGraph = new ModelGraph(this);
			tab1.add(toolbar = new ModelGraphToolbar(modelGraph), BorderLayout.NORTH);
			tab1.add(modelGraph);

			final JEditorPane editor = new JEditorPane();
			editor.setBackground(Color.white);
			editor.setEditable(false);
			editor.setContentType(XMLEditorKit.XML_MIME_TYPE);
			final XMLEditorKit kit = new XMLEditorKit();
			editor.setEditorKit(kit);

			tabbedPane.add($m("ModelTabbedContent.0"), tab1);
			tabbedPane.add($m("ModelTabbedContent.1"), new JScrollPane(editor));
			tabbedPane.getModel().addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(final ChangeEvent e) {
					if (tabbedPane.getSelectedIndex() == 1) {
						editor.setText(document.toString());
						editor.setCaretPosition(0);
					}
				}
			});
		}
		return tabbedPane;
	}

	public ProcessDocument getDocument() {
		return document;
	}

	public void setDocument(final ProcessDocument document) {
		this.document = document;
	}

	@Override
	public String getTitle() {
		final ProcessNode processNode = document.getProcessNode();
		return StringUtils.text(processNode.getText(), processNode.getName());
	}

	@Override
	public NodeProcessModel getTreeNode() {
		return nodeProcessModel;
	}

	public ModelGraphToolbar getToolbar() {
		return toolbar;
	}
}
