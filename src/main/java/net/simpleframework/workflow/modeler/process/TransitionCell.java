package net.simpleframework.workflow.modeler.process;

import java.util.List;

import net.simpleframework.workflow.schema.TransitionNode;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TransitionCell extends mxCell {
	public TransitionCell() {
		this(null, 0, 0, null, null);
	}

	public TransitionCell(final TransitionNode transition, final double x, final double y,
			final List<mxPoint> points, final mxPoint offset) {
		setEdge(true);
		setTransition(transition);

		final mxGeometry geometry = new mxGeometry(x, y, 0, 0);
		geometry.setRelative(false);
		geometry.setPoints(points);
		geometry.setOffset(offset);
		setGeometry(geometry);

		final StringBuilder sb = new StringBuilder();
		sb.append("edgeStyle=mxEdgeStyle.EntityRelation;");
		sb.append("editable=0;");
		setStyle(sb.toString());
	}

	public void setTransition(final TransitionNode transition) {
		if (transition != null) {
			setId(transition.getId());
			setValue(transition.getText());
		}
	}

	private static final long serialVersionUID = 2862639942193995614L;
}
