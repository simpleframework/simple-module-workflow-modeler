package net.simpleframework.workflow.modeler.utils.xmleditor;

public class XMLToken {
	private final int context;

	private final int startOffset;

	private final int endOffset;

	public XMLToken(final int context, final int startOffset, final int endOffset) {
		this.context = context;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public int getContext() {
		return context;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}
}
