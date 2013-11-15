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

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.AbstractParticipantType;
import net.simpleframework.workflow.schema.AbstractParticipantType.User;
import net.simpleframework.workflow.schema.Node;
import net.simpleframework.workflow.schema.ProcessNode;
import net.simpleframework.workflow.schema.UserNode;
import net.simpleframework.workflow.schema.UserNode.ERelativeType;
import net.simpleframework.workflow.schema.UserNode.RelativeRole;
import net.simpleframework.workflow.schema.UserNode.Role;
import net.simpleframework.workflow.schema.UserNode.RuleRole;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class UserNodeEditor extends AbstractEditorDialog {

	private static final Vector<String> participantTypes = new Vector<String>(Arrays.asList(
			$m("AbstractParticipantType.Role"), $m("AbstractParticipantType.RelativeRole"),
			$m("AbstractParticipantType.User"), $m("AbstractParticipantType.RuleRole")));

	public UserNodeEditor(final ModelGraph modelGraph, final TaskCell cell) {
		super($m("UserNodeEditor.0"), modelGraph, cell);
	}

	private JTextField nameTf, formClassTf;

	private JComboBox participantTypeCb, relativeTypeCb;

	private JTextField participantTf, preActivityTf, relativeTf, responseValueTf;

	private JCheckBox sequentialCb, instanceSharedCb, manualCb, multiSelectedCb;

	private JPanel m2, m3, m4, m5, m6, m7, m8;

	protected ListenerPane listenerPane;

	private JPanel createBasePane() {
		final JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(SwingUtils.createTitleBorder($m("UserNodeEditor.1")));

		final JPanel l1 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.7")),
				nameTf = new JTextField());
		final JPanel l2 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.2")),
				formClassTf = new JTextField());
		p1.add(SwingUtils.createVertical(l1, l2));

		final JPanel p2 = new JPanel(new BorderLayout());
		p2.setBorder(SwingUtils.createTitleBorder($m("UserNodeEditor.3")));

		final JPanel m1 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.4")),
				participantTypeCb = new JComboBox(participantTypes));
		participantTypeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				participantTypeChanged(null);
			}
		});
		m2 = SwingUtils
				.createKV(new JLabel($m("UserNodeEditor.5")), participantTf = new JTextField());
		m3 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.6")), relativeTypeCb = new JComboBox(
				ERelativeType.values()));
		relativeTypeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				m4.setVisible(e.getItem() == ERelativeType.preNamedActivityParticipant);
				preActivityTf.setText(null);
			}
		});
		m4 = SwingUtils
				.createKV(new JLabel($m("UserNodeEditor.7")), preActivityTf = new JTextField());
		m5 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.8")), relativeTf = new JTextField());
		m6 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.9")),
				responseValueTf = new JTextField());
		m7 = SwingUtils.createFlow(sequentialCb = new JCheckBox($m("UserNodeEditor.10")), 10,
				instanceSharedCb = new JCheckBox($m("UserNodeEditor.11")));
		m8 = SwingUtils.createFlow(manualCb = new JCheckBox($m("UserNodeEditor.12")), 10,
				multiSelectedCb = new JCheckBox($m("UserNodeEditor.13")));

		p2.add(SwingUtils.createVertical(m1, m2, m3, m4, m5, m6, m7, m8));
		return SwingUtils.createVertical(p1, p2);
	}

	private void participantTypeChanged(final AbstractParticipantType pt) {
		if (pt == null) {
			participantTf.setText(null);
			relativeTypeCb.setSelectedIndex(0);
			preActivityTf.setText(null);
			relativeTf.setText(null);
			responseValueTf.setText(null);
			sequentialCb.setSelected(false);
			instanceSharedCb.setSelected(true);
			manualCb.setSelected(false);
			multiSelectedCb.setSelected(false);
		} else if (pt instanceof User) {
			participantTf.setText(pt.getParticipant());
		} else {
			if (pt instanceof RelativeRole) {
				final RelativeRole rr = (RelativeRole) pt;
				final ERelativeType relativeType = rr.getRelativeType();
				relativeTypeCb.setSelectedItem(relativeType);
				if (relativeType == ERelativeType.preNamedActivityParticipant) {
					preActivityTf.setText(rr.getPreActivity());
				}
				relativeTf.setText(rr.getRelative());
			} else {
				participantTf.setText(pt.getParticipant());
			}

			final Role r = (Role) pt;
			responseValueTf.setText(r.getResponseValue());
			sequentialCb.setSelected(r.isSequential());
			instanceSharedCb.setSelected(r.isInstanceShared());
			manualCb.setSelected(r.isManual());
			multiSelectedCb.setSelected(r.isMultiSelected());
		}

		final int i = participantTypeCb.getSelectedIndex();
		m2.setVisible(i != 1);
		m3.setVisible(i == 1);
		m4.setVisible(i == 1
				&& relativeTypeCb.getSelectedItem() == ERelativeType.preNamedActivityParticipant);
		m5.setVisible(i == 1);
		m6.setVisible(i != 2);
		m7.setVisible(i != 2);
		m8.setVisible(i != 2);
	}

	@Override
	protected void load() {
		super.load();

		final UserNode node = (UserNode) getNode();
		nameTf.setText(node.getName());
		formClassTf.setText(node.getFormClass());

		final AbstractParticipantType pt = node.getParticipantType();
		if (pt instanceof RuleRole) {
			participantTypeCb.setSelectedIndex(3);
		} else if (pt instanceof User) {
			participantTypeCb.setSelectedIndex(2);
		} else if (pt instanceof RelativeRole) {
			participantTypeCb.setSelectedIndex(1);
		}
		participantTypeChanged(pt);
	}

	@Override
	public void ok() {
		final int i = participantTypeCb.getSelectedIndex();
		if (i != 1 && assertNull(participantTf)) {
			return;
		}

		final UserNode node = (UserNode) getNode();
		final String name = nameTf.getText();
		if (StringUtils.hasText(name)) {
			for (final Node node2 : ((ProcessNode) node.parent()).nodes()) {
				if (node != node2 && name.equals(node2.getName())) {
					SwingUtils.showError($m("UserNodeEditor.14", name));
					return;
				}
			}
		}
		node.setName(name);
		node.setFormClass(formClassTf.getText());

		if (i == 2) {
			final User u = new User(null, node);
			u.setParticipant(participantTf.getText());
			node.setParticipantType(u);
		} else {
			Role r;
			if (i == 1) {
				final RelativeRole rr = new RelativeRole(null, node);
				r = rr;
				final ERelativeType relativeType = (ERelativeType) relativeTypeCb.getSelectedItem();
				rr.setRelativeType(relativeType);
				if (relativeType == ERelativeType.preNamedActivityParticipant) {
					rr.setPreActivity(preActivityTf.getText());
				}
				rr.setRelative(relativeTf.getText());
			} else if (i == 3) {
				r = new RuleRole(null, node);
				r.setParticipant(participantTf.getText());
			} else {
				r = new Role(null, node);
				r.setParticipant(participantTf.getText());
			}

			r.setResponseValue(responseValueTf.getText());
			r.setSequential(sequentialCb.isSelected());
			r.setInstanceShared(instanceSharedCb.isSelected());
			r.setManual(manualCb.isSelected());
			r.setMultiSelected(multiSelectedCb.isSelected());

			node.setParticipantType(r);
		}
		super.ok();
	}

	@Override
	protected KVMap getTabbedComponents() {
		final Node node = getNode();
		return new KVMap().add($m("UserNodeEditor.15"), createBasePane())
				.add(VariablePane.title, createVariablePane(node))
				.add(ListenerPane.title, listenerPane = new ListenerPane(node));
	}
}
