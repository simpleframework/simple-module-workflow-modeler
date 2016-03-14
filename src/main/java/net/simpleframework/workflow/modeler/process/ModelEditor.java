package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.simpleframework.common.Convert;
import net.simpleframework.common.Version;
import net.simpleframework.common.coll.ArrayUtils;
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
public class ModelEditor extends AbstractEditorDialog {

	private static final Vector<String> startupTypes = ArrayUtils.asVector(
			$m("AbstractProcessStartupType.Manual"), $m("AbstractProcessStartupType.Email"));

	private static final Vector<String> participantTypes = ArrayUtils.asVector(
			$m("AbstractParticipantType.BaseRole"), $m("AbstractParticipantType.User"));

	protected ListenerPane listenerPane;

	public ModelEditor(final ModelGraph modelGraph) {
		super($m("ModelEditor.0"), modelGraph, null);
	}

	private JTextField nameTf, versionTf, authorTf, pgroupTf, oorderTf, formClassTf, viewClassTf;

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
		final JPanel l4 = SwingUtils.createKV(new JLabel($m("ModelEditor.14")),
				pgroupTf = new JTextField());
		final JPanel l5 = SwingUtils.createKV(new JLabel($m("ModelEditor.13")),
				oorderTf = new JTextField());
		final JPanel l6 = SwingUtils.createKV(new JLabel($m("ModelEditor.6")),
				formClassTf = new JTextField());
		final JPanel l7 = SwingUtils.createKV(new JLabel($m("ModelEditor.12")),
				viewClassTf = new JTextField());
		final JPanel l8 = SwingUtils.createKV(new JLabel(), instanceSharedCb = new JCheckBox(
				$m("ModelEditor.7")));
		p1.add(SwingUtils.createVertical(l1, l2, l3, l4, l5, l6, l7, l8));

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
	protected void initComponents() {
		super.initComponents();

		final ProcessNode processNode = (ProcessNode) getNode();
		nameTf.setText(processNode.getName());
		versionTf.setText(processNode.getVersion().toString());
		authorTf.setText(processNode.getAuthor());
		pgroupTf.setText(processNode.getPgroup());
		oorderTf.setText(Convert.toString(processNode.getOorder()));
		instanceSharedCb.setSelected(processNode.isInstanceShared());
		formClassTf.setText(processNode.getFormClass());
		viewClassTf.setText(processNode.getViewClass());

		final AbstractProcessStartupType st = processNode.getStartupType();
		if (st instanceof Email) {
			startupTypeCb.setSelectedIndex(1);
		}
		startupTypeChanged(st);
	}

	@Override
	public void pack() {
		super.pack();
		Dimension di = getSize();
		if (di.getWidth() > 600 || di.getHeight() > 400) {
			setSize(new Dimension(600, 400));
		}
	}

	@Override
	public void ok() {
		final ProcessNode processNode = (ProcessNode) getNode();
		final Version ver = Version.getVersion(versionTf.getText());
		processNode.setName(nameTf.getText());
		processNode.setVersion(ver);
		processNode.setAuthor(authorTf.getText());
		processNode.setPgroup(pgroupTf.getText());
		processNode.setOorder(Convert.toInt(oorderTf.getText()));
		processNode.setInstanceShared(instanceSharedCb.isSelected());
		processNode.setFormClass(formClassTf.getText());
		processNode.setViewClass(viewClassTf.getText());

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
	protected Map<String, Object> getTabbedComponents() {
		final Node node = getNode();
		return new KVMap().add($m("ModelEditor.1"), createBasePane())
				.add(VariablePane.title, createVariablePane(node))
				.add(ListenerPane.title, listenerPane = new ListenerPane(node));
	}

	private static final long serialVersionUID = -5206704097540939803L;
}
