package net.simpleframework.workflow.modeler.utils.xmleditor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;

public class XMLDocument extends DefaultStyledDocument {
	protected XMLScanner lexer;

	protected XMLContext context;

	protected XMLToken cacheToken = null;

	public XMLDocument() {
		this(new XMLContext());
	}

	public XMLDocument(final XMLContext context) {
		this.context = context;
		lexer = new XMLScanner();
	}

	public XMLToken getScannerStart(final int pos) throws BadLocationException {
		int ctx = XMLScanner.CHARACTER_DATA_CONTEXT;
		int offset = 0;
		int tokenOffset = 0;

		if (cacheToken != null) {
			if (cacheToken.getStartOffset() > pos) {
				cacheToken = null;
			} else {
				ctx = cacheToken.getContext();
				offset = cacheToken.getStartOffset();
				tokenOffset = offset;

				final Element element = getDefaultRootElement();
				final int line1 = element.getElementIndex(pos);
				final int line2 = element.getElementIndex(offset);

				// if (pos - offset <= 1800 ) {
				if (line1 - line2 < 50) {
					return cacheToken;
				}
			}
		}

		final String str = getText(offset, pos - offset);
		lexer.setString(str);
		lexer.reset();

		// read until pos
		int lastCtx = ctx;
		int lastOffset = offset;
		while (offset < pos) {
			lastOffset = offset;
			lastCtx = ctx;

			offset = lexer.scan(ctx) + tokenOffset;
			ctx = lexer.getScanValue();
		}
		cacheToken = new XMLToken(lastCtx, lastOffset, offset);
		return cacheToken;
	}

	@Override
	public void insertString(final int offset, final String str, final AttributeSet a)
			throws BadLocationException {

		super.insertString(offset, str, a);

		if (cacheToken != null) {
			if (cacheToken.getStartOffset() >= offset) {
				cacheToken = null;
			}
		}

	}

	@Override
	public void remove(final int offs, final int len) throws BadLocationException {
		super.remove(offs, len);

		if (cacheToken != null) {
			if (cacheToken.getStartOffset() >= offs) {
				cacheToken = null;
			}
		}
	}

	public int find(String str, final int fromIndex, final boolean caseSensitive)
			throws BadLocationException {

		int offset = -1;
		int startOffset = -1;
		int len = 0;
		int charIndex = 0;

		final Element rootElement = getDefaultRootElement();

		final int elementIndex = rootElement.getElementIndex(fromIndex);
		if (elementIndex < 0) {
			return offset;
		}

		// set the initial charIndex
		charIndex = fromIndex - rootElement.getElement(elementIndex).getStartOffset();

		for (int i = elementIndex; i < rootElement.getElementCount(); i++) {
			final Element element = rootElement.getElement(i);
			startOffset = element.getStartOffset();
			if (element.getEndOffset() > getLength()) {
				len = getLength() - startOffset;
			} else {
				len = element.getEndOffset() - startOffset;
			}

			String text = getText(startOffset, len);

			if (!caseSensitive) {
				text = text.toLowerCase();
				str = str.toLowerCase();
			}

			charIndex = text.indexOf(str, charIndex);
			if (charIndex != -1) {
				offset = startOffset + charIndex;
				break;
			}
			charIndex = 0; // reset the charIndex
		}

		return offset;
	}

	private static final long serialVersionUID = -514970362734888849L;
}
