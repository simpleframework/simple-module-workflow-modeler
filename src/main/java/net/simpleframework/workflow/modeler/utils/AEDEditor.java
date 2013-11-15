package net.simpleframework.workflow.modeler.utils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AEDEditor extends OkCancelDialog {

	protected Object rowData;

	public AEDEditor(final String title, final Object... element) {
		super(title, element);
	}

	public Object getRowData() {
		return rowData;
	}

	public void setRowData(final Object rowData) {
		this.rowData = rowData;
	}
}
