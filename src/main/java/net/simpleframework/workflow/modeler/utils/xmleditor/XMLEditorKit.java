package net.simpleframework.workflow.modeler.utils.xmleditor;

import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class XMLEditorKit extends StyledEditorKit {
	public static final String XML_MIME_TYPE = "text/xml";

	protected XMLContext context;

	protected ViewFactory factory = null;

	public XMLEditorKit() {
		this(null);
	}

	public XMLEditorKit(final XMLContext context) {
		super();
		factory = new XMLViewFactory();
		if (context == null) {
			this.context = new XMLContext();
		} else {
			this.context = context;
		}
	}

	public XMLContext getStylePreferences() {
		return context;
	}

	@Override
	public void install(final JEditorPane c) {
		super.install(c);

		final Object obj = context.getSyntaxFont(StyleContext.DEFAULT_STYLE);
		if (obj != null) {
			c.setFont((Font) obj);
		}
	}

	@Override
	public String getContentType() {
		return XML_MIME_TYPE;
	}

	@Override
	public Object clone() {
		final XMLEditorKit kit = new XMLEditorKit();
		kit.context = context;
		return kit;
	}

	@Override
	public Document createDefaultDocument() {
		final XMLDocument doc = new XMLDocument(context);
		return doc;
	}

	@Override
	public ViewFactory getViewFactory() {
		return factory;
	}

	protected class XMLViewFactory implements ViewFactory {

		@Override
		public View create(final Element elem) {
			return new XMLView(context, elem);
		}
	}

	private static final long serialVersionUID = -1836626541753594580L;
}
