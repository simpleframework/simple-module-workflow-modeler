package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.simpleframework.common.Version;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.AbstractParticipantType;
import net.simpleframework.workflow.schema.AbstractParticipantType.BaseRole;
import net.simpleframework.workflow.schema.AbstractParticipantType.User;
import net.simpleframework.workflow.schema.AbstractProcessStartupType;
import net.simpleframework.workflow.schema.AbstractProcessStartupType.Email;
import net.simpleframework.workflow.schema.AbstractProcessStartupType.Manual;
import net.simpleframework.workflow.schema.Node;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ModelEditor extends AbstractEditorDialog {

	private static final Vector<String> startupTypes = new Vector<String>(Arrays.asList(
			$m("AbstractProcessStartupType.Manual"), $m("AbstractProcessStartupType.Email")));

	private static final Vector<String> participantTypes = new Vector<String>(Arrays.asList(
			$m("AbstractParticipantType.BaseRole"), $m("AbstractParticipantType.User")));

	protected ListenerPane listenerPane;

	public ModelEditor(final ModelGraph modelGraph) {
		super($m("ModelEditor.0"), modelGraph, null);
	}

	private JTextField nameTf, versionTf, authorTf, formClassTf;

	private JCheckBox instanceSharedCb;

	private JComboBox startupTypeCb;

	private JComboBox participantTypeCb;

	private JTextField participantTf;

	private JPanel m1, m2, e1;

	private JPanel createBasePane() {
		final JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(SwingUtils.createTitleBorder($m("ModelEditor.2")));

		final JPanel l1 = SwingUtils.createKV(new JLabel($m("ModelEditor.3")),
				nameTf = new JTextField());
		final JPanel l2 = SwingUtils.createKV(new JLabel($m("ModelEditor.4")),
				versionTf = new JTextField());
		final JPanel l3 = SwingUtils.createKV(new JLabel($m("ModelEditor.5")),
				authorTf = new JTextField());
		final JPanel l4 = SwingUtils.createKV(new JLabel($m("ModelEditor.6")),
				formClassTf = new JTextField());
		final JPanel l5 = SwingUtils.createKV(new JLabel(), instanceSharedCb = new JCheckBox(
				$m("ModelEditor.7")));
		p1.add(SwingUtils.createVertical(l1, l2, l3, l4, l5));

		final JPanel startup = SwingUtils.createKV(new JLabel($m("ModelEditor.11")),
				startupTypeCb = new JComboBox(startupTypes), false);
		startupTypeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				startupTypeChanged(null);
			}
		});

		m1 = SwingUtils.createKV(new JLabel($m("ModelEditor.9")), participantTypeCb = new JComboBox(
				participantTypes));
		participantTypeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				participantTf.setText(null);
			}
		});
		m2 = SwingUtils.createKV(new JLabel($m("ModelEditor.10")), participantTf = new JTextField());

		e1 = new JPanel();

		final JPanel p2 = new JPanel(new BorderLayout());
		p2.setBorder(SwingUtils.createTitleBorder($m("ModelEditor.8")));
		p2.add(SwingUtils.createVertical(startup, m1, m2, e1));
		return SwingUtils.createVertical(p1, p2);
	}

	private void startupTypeChanged(final AbstractProcessStartupType st) {
		if (st == null) {
			participantTypeCb.setSelectedIndex(0);
			participantTf.setText(null);
		} else if (st instanceof Manual) {
			final Manual manual = (Manual) st;
			final AbstractParticipantType pt = manual.getParticipantType();
			if (pt instanceof User) {
				participantTypeCb.setSelectedIndex(1);
			}
			participantTf.setText(pt.getParticipant());
		} else if (st instanceof Email) {
		}

		final int i = startupTypeCb.getSelectedIndex();
		m1.setVisible(i == 0);
		m2.setVisible(i == 0);
		e1.setVisible(i == 1);
	}

	@Override
	protected void load() {
		super.load();

		final ProcessNode processNode = (ProcessNode) getNode();
		nameTf.setText(processNode.getName());
		versionTf.setText(processNode.getVersion().toString());
		authorTf.setText(processNode.getAuthor());
		instanceSharedCb.setSelected(processNode.isInstanceShared());
		formClassTf.setText(processNode.getFormClass());

		final AbstractProcessStartupType st = processNode.getStartupType();
		if (st instanceof Email) {
			startupTypeCb.setSelectedIndex(1);
		}
		startupTypeChanged(st);
	}

	@Override
	public void ok() {
		final ProcessNode processNode = (ProcessNode) getNode();
		final Version ver = Version.getVersion(versionTf.getText());
		processNode.setName(nameTf.getText());
		processNode.setVersion(ver);
		processNode.setAuthor(authorTf.getText());
		processNode.setInstanceShared(instanceSharedCb.isSelected());
		processNode.setFormClass(formClassTf.getText());

		final int i = startupTypeCb.getSelectedIndex();
		if (i == 0) {
			final Manual m = new Manual(null, processNode);
			processNode.setStartupType(m);
			final int j = participantTypeCb.getSelectedIndex();
			if (j == 0) {
				final BaseRole r = new BaseRole(null, m);
				r.setParticipant(participantTf.getText());
				m.setParticipantType(r);
			} else if (j == 1) {
				final User u = new User(null, m);
				u.setParticipant(participantTf.getText());
				m.setParticipantType(u);
			}
		} else if (i == 1) {
			final Email e = new Email(null, processNode);
			processNode.setStartupType(e);
		}
		super.ok();
	}

	@Override
	protected KVMap getTabbedComponents() {
		final Node node = getNode();
		return new KVMap().add($m("ModelEditor.1"), createBasePane())
				.add(VariablePane.title, createVariablePane(node))
				.add(ListenerPane.title, listenerPane = new ListenerPane(node));
	}
}
