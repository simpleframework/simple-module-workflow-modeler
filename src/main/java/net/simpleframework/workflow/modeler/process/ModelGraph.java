package net.simpleframework.workflow.modeler.process;

import static net.simpleframework.common.I18n.$m;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;

import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.graph.CellValue;
import net.simpleframework.workflow.graph.GraphUtils;
import net.simpleframework.workflow.graph.TaskCell;
import net.simpleframework.workflow.graph.TransitionCell;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.schema.AbstractTaskNode;
import net.simpleframework.workflow.schema.EndNode;
import net.simpleframework.workflow.schema.MergeNode;
import net.simpleframework.workflow.schema.Node;
import net.simpleframework.workflow.schema.ProcessNode;
import net.simpleframework.workflow.schema.StartNode;
import net.simpleframework.workflow.schema.SubNode;
import net.simpleframework.workflow.schema.TransitionNode;
import net.simpleframework.workflow.schema.UserNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class ModelGraph extends mxGraphComponent {
	private final ModelActions actions = new ModelActions(this);

	private final ModelTabbedContent tabbedContent;

	protected mxKeyboardHandler keyboardHandler;

	public ModelGraph(final ModelTabbedContent tabbedContent) {
		super(GraphUtils.createGraph(tabbedContent.getDocument()));

		this.tabbedContent = tabbedContent;

		setBorder(BorderFactory.createEmptyBorder());

		new mxRubberband(this); // 创建框线选择

		// keyboardHandler = new mxKeyboardHandler(this); // 创建键盘handler

		final JViewport jv = getViewport();
		jv.setOpaque(true);
		jv.setBackground(Color.WHITE);

		initGraph();

		final mxGraphControl gc = getGraphControl();
		gc.setBorder(BorderFactory.createEtchedBorder());
		gc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent e) {
				processModelMouseReleased(e);
			}
		});

		getConnectionHandler().setConnectPreview(new mxConnectPreview(this) {
			@Override
			protected Object createCell(final mxCellState startState, final String style) {
				final TaskCell startCell = (TaskCell) startState.getCell();
				final TransitionCell edge = new TransitionCell();
				edge.setSource(startCell);
				startCell.insertEdge(edge, true);
				return edge;
			}
		});

		graph.addListener(mxEvent.CELL_CONNECTED, new mxIEventListener() {
			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				TaskCell previous;
				if ((previous = (TaskCell) evt.getProperty("previous")) != null) {
					final TaskCell terminal = (TaskCell) evt.getProperty("terminal");
					final TransitionNode transition = (TransitionNode) getProcessNode()
							.getNodeById(((TransitionCell) evt.getProperty("edge")).getId());
					final String id = previous.getId();
					final String terminalId = terminal.getId();
					if (id.equals(transition.getFrom())) {
						transition.setFrom(terminalId);
					} else if (id.equals(transition.getTo())) {
						transition.setTo(terminalId);
					}
				}
			}
		});

		graph.addListener(mxEvent.ADD_CELLS, new mxIEventListener() {
			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				final Object[] cells = (Object[]) evt.getProperties().get("cells");
				if (cells != null && cells.length > 0) {
					if (cells[0] instanceof TransitionCell) {
						final TransitionCell edge = (TransitionCell) cells[0];
						final TaskCell source = (TaskCell) edge.getSource();
						final TaskCell target = (TaskCell) edge.getTarget();
						final ProcessNode processNode = getProcessNode();
						edge.setTransition(processNode.addTransition(
								(AbstractTaskNode) processNode.getNodeById(source.getId()),
								(AbstractTaskNode) processNode.getNodeById(target.getId())));
					}
				}
			}
		});

		graph.addListener(mxEvent.REMOVE_CELLS, new mxIEventListener() {
			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				final Object[] cells = (Object[]) evt.getProperties().get("cells");
				if (cells != null && cells.length > 0) {
					final ProcessNode processNode = getProcessNode();
					if (cells[0] instanceof TaskCell) {
						final TaskCell task = (TaskCell) cells[0];
						final AbstractTaskNode node = (AbstractTaskNode) processNode
								.getNodeById(task.getId());
						if (node != null) {
							for (final TransitionNode t : node.fromTransitions()) {
								processNode.removeNode(t.getId());
							}
							for (final TransitionNode t : node.toTransitions()) {
								processNode.removeNode(t.getId());
							}
							processNode.removeNode(task.getId());
						}
					} else if (cells[0] instanceof TransitionCell) {
						final TransitionCell edge = (TransitionCell) cells[0];
						processNode.removeNode(edge.getId());
					}
				}
			}
		});
	}

	public ModelTabbedContent getTabbedContent() {
		return tabbedContent;
	}

	ProcessNode getProcessNode() {
		return tabbedContent.getDocument().getProcessNode();
	}

	public void save() {
		for (final Object o : graph.getChildCells(graph.getDefaultParent())) {
			if (o == null) {
				continue;
			}
			final mxCell cell = (mxCell) o;
			final Node node = getProcessNode().getNodeById(cell.getId());
			final mxGeometry geometry = cell.getGeometry();
			node.setProperty("x", geometry.getX());
			node.setProperty("y", geometry.getY());
			if (o instanceof TransitionCell) {
				final mxPoint point = geometry.getOffset();
				if (point != null) {
					node.setProperty("offset-x", point.getX());
					node.setProperty("offset-y", point.getY());
				}
				final List<mxPoint> points = geometry.getPoints();
				if (points != null) {
					final List<String> al = new ArrayList<>();
					for (final mxPoint p : points) {
						al.add(p.getX() + "," + p.getY());
					}
					if (al.size() > 0) {
						node.setProperty("points", StringUtils.join(al, ";"));
					}
				}
			}
		}
	}

	private void initGraph() {
		graph.setAllowDanglingEdges(false); // 不允许空边
		graph.setMultigraph(false); // 不允许多个连接

		final mxMultiplicity startNodeValidation = new mxMultiplicity(false, null, null, null, 0, "0",
				null, $m("ModelGraph.0"), null, true) {

			@Override
			public boolean checkType(final mxGraph graph, final Object value, final String type,
					final String attr, final String attrValue) {
				return value instanceof CellValue
						&& StartNode.class.equals(((CellValue) value).getNodeClass());
			}
		};

		final mxMultiplicity endNodeValidation = new mxMultiplicity(true, null, null, null, 0, "0",
				null, $m("ModelGraph.1"), null, true) {

			@Override
			public boolean checkType(final mxGraph graph, final Object value, final String type,
					final String attr, final String attrValue) {
				return value instanceof CellValue
						&& EndNode.class.equals(((CellValue) value).getNodeClass());
			}
		};
		graph.setMultiplicities(new mxMultiplicity[] { startNodeValidation, endNodeValidation });
	}

	public void processModelMouseReleased(final MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		final Object cell = getCellAt(x, y);
		if (SwingUtilities.isRightMouseButton(e)) {
			final JPopupMenu popup = new JPopupMenu();
			if (cell != null) {
				if (cell instanceof TransitionCell) {
					popup.add(actions.createCellDeleteAction(cell));
					popup.addSeparator();
				}
				if (cell instanceof TaskCell) {
					final Class<?> nclass = ((CellValue) ((TaskCell) cell).getValue()).getNodeClass();
					if (!StartNode.class.equals(nclass) && !EndNode.class.equals(nclass)) {
						popup.add(actions.createCellDeleteAction(cell));
						popup.addSeparator();
					}
				}
				popup.add(actions.createCellPropertiesAction(cell));
			} else {
				popup.add(actions.createModelPropertiesAction());
			}
			SwingUtils.showPopupMenu(popup, this, x, y);
		} else if (SwingUtilities.isLeftMouseButton(e)) {
			if (e.getClickCount() == 2) {
				if (cell != null) {
					actions.createCellPropertiesAction(cell).actionPerformed(null);
				} else {
					actions.createModelPropertiesAction().actionPerformed(null);
				}
			} else {
				final ModelGraphToolbar tb = getTabbedContent().getToolbar();
				Class<? extends AbstractTaskNode> nodeClass = null;
				if (tb.user.isSelected()) {
					nodeClass = UserNode.class;
				} else if (tb.sub.isSelected()) {
					nodeClass = SubNode.class;
				} else if (tb.merge.isSelected()) {
					nodeClass = MergeNode.class;
				}
				if (nodeClass != null) {
					graph.addCell(
							new TaskCell((AbstractTaskNode) getProcessNode().addNode(nodeClass), x, y));
				}
				tb.normal.doClick();
			}
		}
	}
}
