package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.Application;
import net.simpleframework.workflow.modeler.ApplicationActions;
import net.simpleframework.workflow.modeler.ApplicationActions.ApplicationAction;
import net.simpleframework.workflow.modeler.navigation.NodeConnection;
import net.simpleframework.workflow.modeler.navigation.NodeProcessModels;
import net.simpleframework.workflow.modeler.utils.OkCancelDialog;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ModelNewAction extends ApplicationAction {

	private final NodeProcessModels nodeProcessModels;

	private ModelGraph modelGraph;

	public ModelNewAction(final NodeProcessModels nodeProcessModels) {
		super($m("ModelNewAction.0"), ApplicationActions.newIcon);
		this.nodeProcessModels = nodeProcessModels;
	}

	public ModelNewAction(final ModelGraph modelGraph) {
		super($m("ModelNewAction.0"), ApplicationActions.newIcon);
		this.modelGraph = modelGraph;
		this.nodeProcessModels = modelGraph.getTabbedContent().getTreeNode().getNodeProcessModels();
	}

	@Override
	protected void actionInvoked(final ActionEvent e) {
		new ModelNewDialog();
	}

	public class ModelNewDialog extends OkCancelDialog {
		private JTextField nameTf;

		private JTextArea descriptionTa;

		public ModelNewDialog() {
			super($m("ModelNewAction.0"));
		}

		@Override
		protected Component createContentUI() {
			setMinimumSize(new Dimension(450, 240));

			final JPanel jp = new JPanel(new BorderLayout());
			jp.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

			final Container c1 = SwingUtils.createKV(new JLabel($m("ModelNewDialog.0")),
					nameTf = new JTextField(), 75);

			JScrollPane js;
			final Container c2 = SwingUtils.createKV(new JLabel($m("ModelNewDialog.1")),
					js = new JScrollPane(descriptionTa = new JTextArea()), 75);
			js.setPreferredSize(new Dimension(js.getPreferredSize().width, 100));

			JButton btn;
			final Container c3 = SwingUtils.createFlow(Box.createHorizontalStrut(79),
					btn = new JButton($m("ModelNewDialog.2")));
			btn.setMnemonic('F');
			SwingUtils.bindTextEvent(btn, descriptionTa);

			jp.add(SwingUtils.createVertical(c1, c2, c3));
			return jp;
		}

		@Override
		public void ok() {
			if (assertNull(nameTf)) {
				return;
			}

			final KVMap kv = new KVMap();
			kv.add("name", nameTf.getText());
			final String description = descriptionTa.getText();
			if (StringUtils.hasText(description)) {
				kv.add("description", description);
			}

			final NodeConnection nodeConnection = nodeProcessModels.getNodeConnection();
			final Map<String, Object> kv2;
			try {
				kv2 = Application.remote().call(nodeConnection.getUrl(), "newModel", kv);
				if (Application.isError(kv2)) {
					return;
				}
			} catch (final IOException e) {
				SwingUtils.showError(e);
				return;
			}

			nodeConnection.addNode(kv2);
			nodeConnection.getTree().updateUI();
			if (modelGraph != null) {
			}
			super.ok();
		}
	}
}
