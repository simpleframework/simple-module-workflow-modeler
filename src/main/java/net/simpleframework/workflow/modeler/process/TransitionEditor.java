package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.AbstractTransitionType;
import net.simpleframework.workflow.schema.AbstractTransitionType.Conditional;
import net.simpleframework.workflow.schema.AbstractTransitionType.Interface;
import net.simpleframework.workflow.schema.AbstractTransitionType.LogicConditional;
import net.simpleframework.workflow.schema.ETransitionLogic;
import net.simpleframework.workflow.schema.TransitionNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class TransitionEditor extends AbstractEditorDialog {
	private static final Vector<String> transitionTypes = new Vector<String>(Arrays.asList(
			$m("AbstractTransitionType.Conditional"), $m("AbstractTransitionType.LogicConditional"),
			$m("AbstractTransitionType.Interface")));

	public TransitionEditor(final ModelGraph modelGraph, final TransitionCell cell) {
		super($m("TransitionEditor.0"), modelGraph, cell);
	}

	private JComboBox transitionTypeCb, logicCb;

	private JCheckBox manualCb;

	private JTextField expressionTf, transitionIdTf, handleClassTf;

	private JPanel p2, p3, p4, p5, p6;

	private JPanel createBasePane() {
		final JPanel p1 = SwingUtils.createKV(new JLabel($m("TransitionEditor.2")),
				transitionTypeCb = new JComboBox(transitionTypes), false);
		p2 = SwingUtils.createKV(new JLabel($m("TransitionEditor.3")),
				expressionTf = new JTextField());
		p3 = SwingUtils.createKV(new JLabel($m("TransitionEditor.4")), logicCb = new JComboBox(
				ETransitionLogic.values()), false);
		p4 = SwingUtils.createKV(new JLabel($m("TransitionEditor.5")),
				SwingUtils.createSelected(transitionIdTf = new JTextField(), new JButton()));
		p5 = SwingUtils.createKV(new JLabel(), manualCb = new JCheckBox($m("TransitionEditor.6")));
		p6 = SwingUtils.createKV(new JLabel($m("TransitionEditor.7")),
				handleClassTf = new JTextField());

		transitionTypeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				transitionTypeChanged(null);
			}
		});

		final JPanel pane = SwingUtils.createVertical(p1, p2, p3, p4, p5, p6, new JPanel());
		pane.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		return pane;
	}

	private void transitionTypeChanged(final AbstractTransitionType tt) {
		if (tt == null) {
			expressionTf.setText(null);
			manualCb.setSelected(false);
			logicCb.setSelectedIndex(0);
			transitionIdTf.setText(null);
			handleClassTf.setText(null);
		} else if (tt instanceof Conditional) {
			final Conditional c = (Conditional) tt;
			expressionTf.setText(c.getExpression());
			manualCb.setSelected(c.isManual());
		} else if (tt instanceof LogicConditional) {
			final LogicConditional lc = (LogicConditional) tt;
			logicCb.setSelectedItem(lc.getLogic());
			transitionIdTf.setText(lc.getTransitionId());
			manualCb.setSelected(lc.isManual());
		} else if (tt instanceof Interface) {
			handleClassTf.setText(((Interface) tt).getHandleClass());
		}
		final int i = transitionTypeCb.getSelectedIndex();
		p2.setVisible(i == 0);
		p3.setVisible(i == 1);
		p4.setVisible(i == 1);
		p5.setVisible(i != 2);
		p6.setVisible(i == 2);
	}

	@Override
	protected void load() {
		super.load();

		final TransitionNode transition = (TransitionNode) getNode();
		final AbstractTransitionType tt = transition.getTransitionType();
		if (tt instanceof LogicConditional) {
			transitionTypeCb.setSelectedIndex(1);
		} else if (tt instanceof Interface) {
			transitionTypeCb.setSelectedIndex(2);
		}
		transitionTypeChanged(tt);
	}

	@Override
	public void ok() {
		final int i = transitionTypeCb.getSelectedIndex();
		if (i == 1 && assertNull(transitionIdTf)) {
			return;
		}
		final TransitionNode transition = (TransitionNode) getNode();
		if (i == 0) {
			final Conditional c = new Conditional(null, transition);
			transition.setTransitionType(c);
			c.setExpression(expressionTf.getText());
			c.setManual(manualCb.isSelected());
		} else if (i == 1) {
			final LogicConditional lc = new LogicConditional(null, transition);
			transition.setTransitionType(lc);
			lc.setLogic((ETransitionLogic) logicCb.getSelectedItem());
			lc.setTransitionId(transitionIdTf.getText());
			lc.setManual(manualCb.isSelected());
		} else if (i == 2) {
			final Interface intr = new Interface(null, transition);
			transition.setTransitionType(intr);
			intr.setHandleClass(handleClassTf.getText());
		}

		super.ok();
	}

	@Override
	protected KVMap getTabbedComponents() {
		return new KVMap().add($m("TransitionEditor.1"), createBasePane());
	}
}
