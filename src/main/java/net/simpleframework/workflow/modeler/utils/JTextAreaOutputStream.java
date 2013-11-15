package net.simpleframework.workflow.modeler.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class JTextAreaOutputStream extends PrintStream {
	private JTextComponent target = null;

	private PrintStream oPrintStream = null;

	private boolean show = false;

	public JTextAreaOutputStream(final JTextComponent t) {
		this(t, null, false);
	}

	public JTextAreaOutputStream(final JTextComponent t, final PrintStream oPrintStream,
			final boolean show) {
		super(new ByteArrayOutputStream());
		target = t;
		this.oPrintStream = oPrintStream;
		this.show = show;
	}

	private void addString(final String str) {
		final Document doc = target.getDocument();
		if (doc != null) {
			try {
				// Application.instance.getMainFrame().updateContentPanel(true);
				doc.insertString(doc.getLength(), str, null);
				target.setCaretPosition(target.getText().length());
			} catch (final BadLocationException e) {
			}
		}
	}

	@Override
	public void print(final boolean b) {
		if (show) {
			oPrintStream.print(b);
		}
		if (b) {
			addString("true");
		} else {
			addString("false");
		}
	}

	@Override
	public void println(final boolean b) {
		if (show) {
			oPrintStream.println(b);
		}
		if (b) {
			addString("true\n");
		} else {
			addString("false\n");
		}
	}

	@Override
	public void print(final char c) {
		if (show) {
			oPrintStream.print(c);
		}
		final char[] tmp = new char[1];
		tmp[0] = c;
		addString(new String(tmp));
	}

	@Override
	public void println(final char c) {
		if (show) {
			oPrintStream.println(c);
		}
		final char[] tmp = new char[2];
		tmp[0] = c;
		tmp[1] = '\n';
		addString(new String(tmp));
	}

	@Override
	public void print(final char[] s) {
		if (show) {
			oPrintStream.print(s);
		}
		addString(new String(s));
	}

	@Override
	public void println(final char[] s) {
		if (show) {
			oPrintStream.println(s);
		}
		addString(new String(s) + "\n");
	}

	@Override
	public void print(final double d) {
		if (show) {
			oPrintStream.print(d);
		}
		addString(Double.toString(d));
	}

	@Override
	public void println(final double d) {
		if (show) {
			oPrintStream.println(d);
		}
		addString(Double.toString(d) + "\n");
	}

	@Override
	public void print(final float f) {
		if (show) {
			oPrintStream.print(f);
		}
		addString(Float.toString(f));
	}

	@Override
	public void println(final float f) {
		if (show) {
			oPrintStream.println(f);
		}
		addString(Float.toString(f) + "\n");
	}

	@Override
	public void print(final int i) {
		if (show) {
			oPrintStream.print(i);
		}
		addString(Integer.toString(i));
	}

	@Override
	public void println(final int i) {
		if (show) {
			oPrintStream.println(i);
		}
		addString(Integer.toString(i) + "\n");
	}

	@Override
	public void print(final long l) {
		if (show) {
			oPrintStream.print(l);
		}
		addString(Long.toString(l));
	}

	@Override
	public void println(final long l) {
		if (show) {
			oPrintStream.println(l);
		}
		addString(Long.toString(l) + "\n");
	}

	@Override
	public void print(final Object o) {
		if (show) {
			oPrintStream.print(o);
		}
		addString(o.toString());
	}

	@Override
	public void println(final Object o) {
		if (show) {
			oPrintStream.println(o);
		}
		addString(o.toString() + "\n");
	}

	@Override
	public void print(final String s) {
		if (show) {
			oPrintStream.print(s);
		}
		addString(s);
	}

	@Override
	public void println(final String s) {
		if (show) {
			oPrintStream.println(s);
		}
		addString(s + "\n");
	}

	@Override
	public void println() {
		if (show) {
			oPrintStream.println();
		}
		addString(new String("\n"));
	}
}
