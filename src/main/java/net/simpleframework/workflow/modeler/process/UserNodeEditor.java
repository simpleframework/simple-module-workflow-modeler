package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.graph.TaskCell;
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
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class UserNodeEditor extends AbstractEditorDialog {

	private Vector<String> participantTypes = null;

	private static int d_rr_n = 4;

	// private static Map<String,String> ruleRoles = new LinkedHashMap<String,
	// String>();

	public UserNodeEditor(final ModelGraph modelGraph, final TaskCell cell) {
		super($m("UserNodeEditor.0"), modelGraph, cell);
	}

	private JTextField nameTf, formClassTf, timoutHoursTf;

	private JCheckBox emptyCb, fallbackCb;

	private JComboBox participantTypeCb, relativeTypeCb;

	private JTextField participantTf, preActivityTf, relativeTf, responseValueTf;

	private JTextField paramsTf;

	private JCheckBox sequentialCb, instanceSharedCb, indeptCb, manualCb, multiSelectedCb;

	private JCheckBox multiTransitionSelectedCb;

	private JPanel m2, m3, m4, m5, m6, m7, m8, m9, m10, m11;

	protected ListenerPane listenerPane;

	private ExtRuleRoles extParticipant = null;

	private JPanel createBasePane() {
		if (null == extParticipant || null == participantTypes) {
			extParticipant = new ExtRuleRoles(this);

			participantTypes = ArrayUtils.asVector($m("AbstractParticipantType.BaseRole"),
					$m("AbstractParticipantType.RelativeRole"), $m("AbstractParticipantType.User"),
					$m("AbstractParticipantType.RuleRole"));
			participantTypes.addAll(extParticipant.getRuleRoles());
		}

		final JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(SwingUtils.createTitleBorder($m("UserNodeEditor.1")));

		final JPanel l1 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.7")),
				nameTf = new JTextField());
		final JPanel l2 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.2")),
				formClassTf = new JTextField());
		final JPanel l3 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.16")),
				timoutHoursTf = new JTextField());
		final JPanel l4 = SwingUtils.createKV(new JLabel(),
				SwingUtils.createFlow(emptyCb = new JCheckBox($m("UserNodeEditor.21")),
						fallbackCb = new JCheckBox($m("UserNodeEditor.22"))));
		p1.add(SwingUtils.createVertical(l1, l2, l3, l4));

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
		m2 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.5")),
				participantTf = new JTextField());

		m3 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.6")),
				relativeTypeCb = new JComboBox(ERelativeType.values()));
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

		m9 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.17")), paramsTf = new JTextField());

		m4 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.7")),
				preActivityTf = new JTextField());
		m5 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.8")), relativeTf = new JTextField());
		m6 = SwingUtils.createKV(new JLabel($m("UserNodeEditor.9")),
				responseValueTf = new JTextField());
		m11 = SwingUtils.createFlow(indeptCb = new JCheckBox($m("UserNodeEditor.20")));
		m7 = SwingUtils.createFlow(sequentialCb = new JCheckBox($m("UserNodeEditor.10")), 10,
				instanceSharedCb = new JCheckBox($m("UserNodeEditor.11")));
		m8 = SwingUtils.createFlow(manualCb = new JCheckBox($m("UserNodeEditor.12")), 10,
				multiSelectedCb = new JCheckBox($m("UserNodeEditor.13")));

		final JPanel jp = extParticipant.getUI();
		if (null != jp) {
			p2.add(SwingUtils.createVertical(m1, m2, m9, jp, m3, m4, m5, m6, m11, m7, m8));
		} else {
			p2.add(SwingUtils.createVertical(m1, m2, m9, m3, m4, m5, m6, m11, m7, m8));
		}

		final JPanel p3 = new JPanel(new BorderLayout());
		p3.setBorder(SwingUtils.createTitleBorder($m("UserNodeEditor.18")));

		m10 = SwingUtils
				.createFlow(multiTransitionSelectedCb = new JCheckBox($m("UserNodeEditor.19")));
		p3.add(SwingUtils.createVertical(m10));

		return SwingUtils.createVertical(p1, p2, p3);
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
			indeptCb.setSelected(false);
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
				indeptCb.setSelected(rr.isIndept());
			} else {
				participantTf.setText(pt.getParticipant());
				if (pt instanceof RuleRole) {
					final RuleRole rr = (RuleRole) pt;
					paramsTf.setText(rr.getParams());
				}
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
		m9.setVisible(i == 3);
		m11.setVisible(i == 1);

		if (i >= d_rr_n) {
			final String text = extParticipant
					.getRuleRoleName(participantTypeCb.getSelectedItem().toString());
			participantTf.setText(text);
			extParticipant.initPComponentsValues(text, paramsTf.getText());
			extParticipant.show(text);
			participantTf.setEditable(false);
			paramsTf.setEditable(false);
			m2.setVisible(false);
		} else {
			// participantTf.setText("");
			participantTf.setEditable(true);
			paramsTf.setEditable(true);
			extParticipant.show("");
		}

	}

	public JTextField getParamsTf() {
		return paramsTf;
	}

	public ModelGraph getModelGraph() {
		return (ModelGraph) params[0];
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		final UserNode node = (UserNode) getNode();
		nameTf.setText(node.getName());
		formClassTf.setText(node.getFormClass());
		timoutHoursTf.setText(node.getTimoutHours());
		emptyCb.setSelected(node.isEmpty());
		fallbackCb.setSelected(node.isFallback());

		multiTransitionSelectedCb.setSelected(node.isMultiTransitionSelected());

		final AbstractParticipantType pt = node.getParticipantType();
		if (pt instanceof RuleRole) {
			if (StringUtils.hasText(pt.getParticipant())) {
				participantTypeCb
						.setSelectedIndex(3 + extParticipant.getRuleRolesI(pt.getParticipant()));
			} else {
				participantTypeCb.setSelectedIndex(3);
			}
		} else if (pt instanceof User) {
			participantTypeCb.setSelectedIndex(2);
		} else if (pt instanceof RelativeRole) {
			participantTypeCb.setSelectedIndex(1);
		}
		participantTypeChanged(pt);
	}

	@Override
	public void ok() {
		int i = participantTypeCb.getSelectedIndex();
		if (i >= d_rr_n) {
			i = 3;// 之后的都是规则角色
		}

		if (!emptyCb.isSelected() && i != 1 && assertNull(participantTf)) {
			return;
		}

		final UserNode node = (UserNode) getNode();
		final String name = nameTf.getText();
		if (StringUtils.hasText(name)) {
			for (final Node node2 : ((ProcessNode) node.getParent()).nodes()) {
				if (node != node2 && name.equals(node2.getName())) {
					SwingUtils.showError($m("UserNodeEditor.14", name));
					return;
				}
			}
		}
		node.setName(name);
		node.setFormClass(formClassTf.getText());
		node.setTimoutHours(timoutHoursTf.getText());
		node.setMultiTransitionSelected(multiTransitionSelectedCb.isSelected());
		node.setEmpty(emptyCb.isSelected());
		node.setFallback(fallbackCb.isSelected());

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
				rr.setIndept(indeptCb.isSelected());
			} else if (i == 3) {
				r = new RuleRole(null, node);
				r.setParticipant(participantTf.getText());
				((RuleRole) r).setParams(paramsTf.getText());
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
	protected Map<String, Object> getTabbedComponents() {
		final Node node = getNode();
		return new KVMap().add($m("UserNodeEditor.15"), createBasePane())
				.add(VariablePane.title, createVariablePane(node))
				.add(ListenerPane.title, listenerPane = new ListenerPane(node));
	}

	private static final long serialVersionUID = -8319883496562763808L;
}
