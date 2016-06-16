package net.simpleframework.workflow.modeler.utils.xmleditor;

import java.awt.Graphics;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

public class XMLView extends PlainView {

	protected XMLContext context = null;

	protected XMLScanner lexer = new XMLScanner();

	protected int tabSize = 4;

	public XMLView(final XMLContext context, final Element elem) {
		super(elem);
		this.context = context;
	}

	@Override
	public int getTabSize() {
		return tabSize;
	}

	@Override
	protected int drawUnselectedText(final Graphics g, int x, final int y, final int p0,
			final int p1) throws BadLocationException {

		final XMLDocument doc = (XMLDocument) getDocument();
		final XMLToken token = doc.getScannerStart(p0);

		final String str = doc.getText(token.getStartOffset(), (p1 - token.getStartOffset()) + 1);

		lexer.setString(str);
		lexer.reset();

		// read until p0
		int pos = token.getStartOffset();
		int ctx = token.getContext();
		int lastCtx = ctx;
		while (pos < p0) {
			pos = lexer.scan(ctx) + token.getStartOffset();
			lastCtx = ctx;
			ctx = lexer.getScanValue();
		}
		int mark = p0;

		while (pos < p1) {
			if (lastCtx != ctx) {
				g.setColor(context.getSyntaxForeground(lastCtx));
				g.setFont(context.getSyntaxFont(lastCtx));
				final Segment text = getLineBuffer();
				doc.getText(mark, pos - mark, text);
				x = Utilities.drawTabbedText(text, x, y, g, this, mark);
				mark = pos;
			}

			pos = lexer.scan(ctx) + token.getStartOffset();
			lastCtx = ctx;
			ctx = lexer.getScanValue();

		}

		g.setColor(context.getSyntaxForeground(lastCtx));
		g.setFont(context.getSyntaxFont(lastCtx));
		final Segment text = getLineBuffer();
		doc.getText(mark, p1 - mark, text);
		x = Utilities.drawTabbedText(text, x, y, g, this, mark);

		return x;
	}
}
