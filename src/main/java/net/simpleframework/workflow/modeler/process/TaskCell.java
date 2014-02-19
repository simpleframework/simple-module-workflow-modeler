package net.simpleframework.workflow.modeler.process;

import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.IWorkflowHandler;
import net.simpleframework.workflow.schema.AbstractTaskNode;
import net.simpleframework.workflow.schema.EndNode;
import net.simpleframework.workflow.schema.MergeNode;
import net.simpleframework.workflow.schema.StartNode;
import net.simpleframework.workflow.schema.SubNode;
import net.simpleframework.workflow.schema.UserNode;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TaskCell extends mxCell {

	public TaskCell(final AbstractTaskNode taskNode, final double x, final double y) {
		setVertex(true);
		setId(taskNode.getId());
		setConnectable(true);
		setValue(new CellValue(taskNode));

		final mxGeometry geometry = new mxGeometry(x, y, 38, 38);
		setGeometry(geometry);

		final StringBuilder sb = new StringBuilder();
		sb.append("shape=image;align=center;");
		sb.append("verticalLabelPosition=bottom;spacingBottom=6;");
		sb.append("resizable=0;editable=0;autosize=1;");
		sb.append("image=/")
				.append(StringUtils.replace(IWorkflowHandler.class.getPackage().getName(), ".", "/"))
				.append("/$resource/images/");
		if (taskNode instanceof UserNode) {
			sb.append("node_user.png");
		} else if (taskNode instanceof MergeNode) {
			sb.append("node_merge.png");
		} else if (taskNode instanceof SubNode) {
			sb.append("node_sub.png");
		} else if (taskNode instanceof StartNode) {
			sb.append("node_start.png");
		} else if (taskNode instanceof EndNode) {
			sb.append("node_end.png");
		} else {
			sb.append("node_auto.gif");
		}
		setStyle(sb.toString());
	}

	private static final long serialVersionUID = -6483469577079287775L;
}
