package net.simpleframework.workflow.modeler.utils.xmleditor;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.StyleContext;

public class XMLContext extends StyleContext {
	public static final String XML_DECLARATION_STYLE = "xml_declaration";

	public static final String DOCTYPE_STYLE = "doctype";

	public static final String COMMENT_STYLE = "comment";

	public static final String ELEMENT_STYLE = "element";

	public static final String CHARACTER_DATA_STYLE = "character_data";

	public static final String ATTRIBUTE_NAME_STYLE = "attribute_name";

	public static final String ATTRIBUTE_VALUE_STYLE = "attribute_value";

	public static final String CDATA_STYLE = "cdata";

	protected Map<String, Color> syntaxForegroundMap = null;

	protected Map<String, Font> syntaxFontMap = null;

	public XMLContext() {
		String syntaxName;
		Font font;
		Color fontForeground;
		syntaxFontMap = new HashMap<String, Font>();
		syntaxForegroundMap = new HashMap<String, Color>();

		final Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);

		syntaxName = StyleContext.DEFAULT_STYLE;
		font = defaultFont;
		fontForeground = Color.black;
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.XML_DECLARATION_STYLE;
		font = defaultFont.deriveFont(Font.ITALIC);
		fontForeground = new Color(0, 0, 124);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.DOCTYPE_STYLE;
		font = defaultFont.deriveFont(Font.ITALIC);
		fontForeground = new Color(0, 0, 124);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.COMMENT_STYLE;
		font = defaultFont;
		fontForeground = new Color(128, 128, 128);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.ELEMENT_STYLE;
		font = defaultFont;
		fontForeground = new Color(0, 0, 255);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.CHARACTER_DATA_STYLE;
		font = defaultFont;
		fontForeground = Color.black;
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.ATTRIBUTE_NAME_STYLE;
		font = defaultFont;
		fontForeground = new Color(0, 124, 0);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.ATTRIBUTE_VALUE_STYLE;
		font = defaultFont;
		fontForeground = new Color(153, 0, 107);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);

		syntaxName = XMLContext.CDATA_STYLE;
		font = defaultFont;
		fontForeground = new Color(124, 98, 0);
		syntaxFontMap.put(syntaxName, font);
		syntaxForegroundMap.put(syntaxName, fontForeground);
	}

	public XMLContext(final Map<String, Font> syntaxFontMap,
			final Map<String, Color> syntaxForegroundMap) {
		setSyntaxFont(syntaxFontMap);
		setSyntaxForeground(syntaxForegroundMap);
	}

	public void setSyntaxForeground(final Map<String, Color> syntaxForegroundMap) {
		if (syntaxForegroundMap == null) {
			throw new IllegalArgumentException("syntaxForegroundMap can not be null");
		}
		this.syntaxForegroundMap = syntaxForegroundMap;
	}

	public void setSyntaxFont(final Map<String, Font> syntaxFontMap) {
		if (syntaxFontMap == null) {
			throw new IllegalArgumentException("syntaxFontMap can not be null");
		}
		this.syntaxFontMap = syntaxFontMap;
	}

	public Color getSyntaxForeground(final int ctx) {
		final String name = getSyntaxName(ctx);
		return getSyntaxForeground(name);
	}

	public Color getSyntaxForeground(final String name) {
		return syntaxForegroundMap.get(name);
	}

	public Font getSyntaxFont(final int ctx) {
		final String name = getSyntaxName(ctx);
		return getSyntaxFont(name);
	}

	public Font getSyntaxFont(final String name) {
		return syntaxFontMap.get(name);
	}

	public String getSyntaxName(final int ctx) {
		String name = CHARACTER_DATA_STYLE;
		switch (ctx) {
		case XMLScanner.XML_DECLARATION_CONTEXT:
			name = XML_DECLARATION_STYLE;
			break;
		case XMLScanner.DOCTYPE_CONTEXT:
			name = DOCTYPE_STYLE;
			break;
		case XMLScanner.COMMENT_CONTEXT:
			name = COMMENT_STYLE;
			break;
		case XMLScanner.ELEMENT_CONTEXT:
			name = ELEMENT_STYLE;
			break;
		case XMLScanner.ATTRIBUTE_NAME_CONTEXT:
			name = ATTRIBUTE_NAME_STYLE;
			break;
		case XMLScanner.ATTRIBUTE_VALUE_CONTEXT:
			name = ATTRIBUTE_VALUE_STYLE;
			break;
		case XMLScanner.CDATA_CONTEXT:
			name = CDATA_STYLE;
			break;
		default:
			// should not go here, just incase
			name = DEFAULT_STYLE;
			break;
		}
		return name;
	}

	private static final long serialVersionUID = 7510634914896708035L;
}
