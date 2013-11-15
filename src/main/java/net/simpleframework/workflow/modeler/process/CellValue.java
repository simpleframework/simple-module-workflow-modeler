package net.simpleframework.workflow.modeler.process;

import net.simpleframework.workflow.schema.Node;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CellValue implements java.io.Serializable {
	private final String text;

	private final Class<? extends Node> nodeClass;

	public CellValue(final Node node) {
		nodeClass = node.getClass();
		text = node.toString();
	}

	public Class<?> getNodeClass() {
		return nodeClass;
	}

	@Override
	public String toString() {
		return text;
	}

	private static final long serialVersionUID = -7189892545391572964L;
}
