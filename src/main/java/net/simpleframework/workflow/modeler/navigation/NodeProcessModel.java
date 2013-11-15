package net.simpleframework.workflow.modeler.navigation;

import static net.simpleframework.common.I18n.$m;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreeNode;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.Application;
import net.simpleframework.workflow.modeler.ApplicationActions;
import net.simpleframework.workflow.modeler.ApplicationActions.ApplicationAction;
import net.simpleframework.workflow.modeler.MainTabbedPane;
import net.simpleframework.workflow.modeler.process.ModelTabbedContent;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.ProcessDocument;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class NodeProcessModel extends NavigationTreeNode {

	private final Map<String, Object> jsonModel;

	public NodeProcessModel(final Map<String, Object> jsonModel) {
		this.jsonModel = jsonModel;
	}

	public NodeProcessModels getNodeProcessModels() {
		TreeNode treeNode = getParent();
		if (treeNode instanceof NodeProcessModelPackage) {
			treeNode = treeNode.getParent();
		}
		return (NodeProcessModels) treeNode;
	}

	public String getUrl() {
		return getNodeProcessModels().getNodeConnection().getUrl();
	}

	public Map<String, Object> getJsonModel() {
		return jsonModel;
	}

	@Override
	public void mouseDbClicked(final MouseEvent e) {
		open();
	}

	@Override
	public JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			popupMenu.add(new ApplicationAction($m("Menu.Open"), ApplicationActions.openIcon) {

				@Override
				protected void actionInvoked(final ActionEvent e) {
					open();
				}
			});
			popupMenu.addSeparator();
			popupMenu.add(new ApplicationAction($m("Menu.Delete"), ApplicationActions.deleteIcon) {

				@Override
				protected void actionInvoked(final ActionEvent e) {
					if (!SwingUtils.confirm($m("NodeProcessModel.0"))) {
						return;
					}
					delete();
				}
			});
		}
		return popupMenu;
	}

	private ModelTabbedContent tabbedContent;

	public ModelTabbedContent getTabbedContent() {
		return tabbedContent;
	}

	public void open() {
		try {
			final Map<String, Object> json = Application.remote().call(getUrl(), "model",
					new KVMap().add("id", jsonModel.get("id")));
			if (Application.isError(json)) {
				return;
			}

			final MainTabbedPane jp = Application.get().getMainPane().getTabbedPane();
			tabbedContent = new ModelTabbedContent(this, new ProcessDocument(new StringReader(
					(String) json.get("doc"))));
			jp.addTab(tabbedContent);
		} catch (final IOException e) {
			SwingUtils.showError(e);
			return;
		}
	}

	private void delete() {
		try {
			final Map<String, Object> json = Application.remote().call(getUrl(), "deleteModel",
					new KVMap().add("id", jsonModel.get("id")));
			if (Application.isError(json)) {
				return;
			}

			removeFromParent();
			getTree().updateUI();

			final ModelTabbedContent tabbedContent = getTabbedContent();
			if (tabbedContent != null) {
				final JTabbedPane jp = Application.get().getMainPane().getTabbedPane();
				final int index = jp.indexOfComponent(tabbedContent.getComponent());
				if (index > -1) {
					jp.remove(index);
				}
			}
		} catch (final IOException e) {
			SwingUtils.showError(e);
			return;
		}
	}

	@Override
	public Icon getIcon() {
		return ApplicationActions.processNodeIcon;
	}

	@Override
	public String toString() {
		final String text = (String) jsonModel.get("text");
		final int p = text.lastIndexOf(".");
		return p > 0 ? text.substring(p + 1) : text;
	}
}
