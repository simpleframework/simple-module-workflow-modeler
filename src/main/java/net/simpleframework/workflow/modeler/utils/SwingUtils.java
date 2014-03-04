package net.simpleframework.workflow.modeler.utils;

import static net.simpleframework.common.I18n.$m;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.JTextComponent;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.IoUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.workflow.modeler.Application;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class SwingUtils {
	public static final Font defautFont = new Font("Microsoft YaHei", Font.PLAIN, 12);

	public static Window parent() {
		return (Window) Application.get().getMainPane();
	}

	public static boolean confirm(final String message) {
		return JOptionPane.showConfirmDialog(parent(), message, $m("SwingUtils.0"),
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	public static void showError(final String message, final Throwable th) {
		new ErrorDialog(message, th);
	}

	public static void showError(final String message) {
		new ErrorDialog(message);
	}

	public static void showError(final Throwable th) {
		new ErrorDialog(th);
	}

	public static ImageIcon loadIcon(final String name) {
		final InputStream is = ClassUtils.getResourceAsStream(Application.class, "images/" + name);
		try {
			return new ImageIcon(ImageIO.read(is));
		} catch (final IOException e) {
		}
		return null;
	}

	public static JFileChooser createJFileChooser(final String... fileFilters) {
		final JFileChooser chooser = new JFileChooser();
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		for (final String fileFilter : fileFilters) {
			final String[] fileFilterArr = StringUtils.split(fileFilter, "|");
			chooser.addChoosableFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File f) {
					final String[] types = StringUtils.split(fileFilterArr[0], ";");
					if ((types == null) || (types.length == 0)) {
						return true;
					} else {
						if (!f.isFile()) {
							return true;
						} else {
							final String ext = FileUtils.getFilenameExtension(f.getPath());
							for (final String type : types) {
								if (type.equalsIgnoreCase(ext)) {
									return true;
								}
							}
							return false;
						}
					}
				}

				@Override
				public String getDescription() {
					return fileFilterArr.length > 1 ? fileFilterArr[1] : fileFilterArr[0];
				}
			});
		}
		return chooser;
	}

	public static void showPopupMenu(final JPopupMenu popup, final Component component, int x, int y) {
		final Point p = new Point(x, y);
		SwingUtilities.convertPointToScreen(p, component);
		final Dimension size = popup.getPreferredSize();
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		boolean horiz = false;
		boolean vert = false;

		final int origX = x;

		if ((p.x + size.width > screen.width) && (size.width < screen.width)) {
			x += (screen.width - p.x - size.width);
			horiz = true;
		}

		if ((p.y + size.height > screen.height) && (size.height < screen.height)) {
			y += (screen.height - p.y - size.height);
			vert = true;
		}

		if (horiz && vert) {
			x = origX - size.width - 2;
		}
		popup.show(component, x, y);
	}

	public static void centerWithinScreen(final Window wind) {
		if (wind == null) {
			return;
		}
		final Toolkit toolKit = Toolkit.getDefaultToolkit();
		final Rectangle rcScreen = new Rectangle(toolKit.getScreenSize());
		final Dimension windSize = wind.getSize();
		final Dimension parentSize = new Dimension(rcScreen.width, rcScreen.height);
		if (windSize.height > parentSize.height) {
			windSize.height = parentSize.height;
		}
		if (windSize.width > parentSize.width) {
			windSize.width = parentSize.width;
		}
		center(wind, rcScreen);
	}

	private static void center(final Component wind, final Rectangle rect) {
		if ((wind == null) || (rect == null)) {
			return;
		}
		final Dimension windSize = wind.getSize();
		final int x = ((rect.width - windSize.width) / 2) + rect.x;
		int y = ((rect.height - windSize.height) / 2) + rect.y;
		if (y < rect.y) {
			y = rect.y;
		}
		wind.setLocation(x, y);
	}

	public static void bindTextEvent(final JButton button, final JTextComponent tc) {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser dlg = new JFileChooser();
				final int ret = dlg.showOpenDialog(parent());
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						tc.setText(IoUtils.getStringFromReader(new FileReader(dlg.getSelectedFile())));
						tc.setCaretPosition(0);
					} catch (final IOException ex) {
					}
				}
			}
		});
	}

	public static void setJButtonSizesTheSame(final JButton[] btns) {
		if (btns == null) {
			throw new IllegalArgumentException();
		}

		final Dimension maxSize = new Dimension(0, 0);
		for (int i = 0; i < btns.length; ++i) {
			final JButton btn = btns[i];
			final FontMetrics fm = btn.getFontMetrics(btn.getFont());
			final Rectangle2D bounds = fm.getStringBounds(btn.getText(), btn.getGraphics());
			final int boundsHeight = (int) bounds.getHeight();
			final int boundsWidth = (int) bounds.getWidth();
			maxSize.width = boundsWidth > maxSize.width ? boundsWidth : maxSize.width;
			maxSize.height = boundsHeight > maxSize.height ? boundsHeight : maxSize.height;
		}

		final Insets insets = btns[0].getInsets();
		maxSize.width += insets.left + insets.right;
		maxSize.height += insets.top + insets.bottom;

		for (int i = 0; i < btns.length; ++i) {
			final JButton btn = btns[i];
			btn.setPreferredSize(maxSize);
		}

		initComponentHeight(btns);
	}

	public static Border createTitleBorder(final String title) {
		final Border tb = BorderFactory.createTitledBorder(title);
		final Border eb = BorderFactory.createEmptyBorder(0, 5, 5, 5);
		return BorderFactory.createCompoundBorder(tb, eb);
	}

	static final String LAF_WINDOWS = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";

	static final String LAF_METAL = MetalLookAndFeel.class.getName();

	public static void initComponentHeight(final Component... components) {
		if (components == null) {
			return;
		}
		for (final Component component : components) {
			if ((component instanceof JComboBox) || (component instanceof JButton)) {
				component.setPreferredSize(new Dimension(component.getPreferredSize().width, 22));
			} else if (component instanceof JTextField) {
				final String lf = UIManager.getLookAndFeel().getClass().getName();
				int i = 22;
				if (lf.equals(LAF_METAL)) {
					i = 23;
				}
				component.setPreferredSize(new Dimension(component.getPreferredSize().width, i));
			}
		}
	}

	public static JPanel createKV(final Component key, final Component value) {
		return createKV(key, value, true);
	}

	public static JPanel createKV(final Component key, final Component value, final boolean fill) {
		return createKV(key, value, 85, fill);
	}

	public static JPanel createKV(final Component key, final Component value, final int keyWidth) {
		return createKV(key, value, keyWidth, true);
	}

	public static JPanel createKV(final Component key, final Component value, final int keyWidth,
			final boolean fill) {
		initComponentHeight(key, value);
		if (keyWidth > 0) {
			key.setPreferredSize(new Dimension(keyWidth, key.getPreferredSize().height));
		}
		final JPanel jp = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 4);
		jp.add(key, gbc);
		gbc.gridx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weightx = 1.0;
		if (fill) {
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}
		jp.add(value, gbc);
		return jp;
	}

	public static JPanel createVertical(final Component... components) {
		final JPanel jp = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 5, 4, 5);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		for (final Component component : components) {
			if (gbc.gridy == components.length - 1) {
				gbc.weighty = 1.0;
			}
			jp.add(component, gbc);
			gbc.gridy++;
		}
		return jp;
	}

	public static JPanel createFlow(final Object... components) {
		return createFlow(FlowLayout.LEFT, 0, components);
	}

	public static JPanel createFlow(final int align, final int gap, final Object... components) {
		final JPanel jp = new JPanel(new FlowLayout(align, gap, gap));
		jp.setBorder(BorderFactory.createEmptyBorder());
		for (final Object component : components) {
			if (component instanceof Component) {
				initComponentHeight((Component) component);
				jp.add((Component) component);
			} else if (component instanceof Number) {
				jp.add(Box.createHorizontalStrut(((Number) component).intValue()));
			}
		}
		return jp;
	}

	public static JPanel createSelected(final JTextField textField, final JButton... btns) {
		final Dimension btnDim = new Dimension(18, 18);
		initComponentHeight(textField);
		final JPanel jp = new JPanel(new GridBagLayout());
		jp.setBorder(textField.getBorder());
		textField.setBorder(BorderFactory.createEmptyBorder());
		jp.setPreferredSize(textField.getPreferredSize());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		jp.add(textField, gbc);

		gbc.weightx = 0.0;
		if (btns != null) {
			for (final JButton btn : btns) {
				gbc.gridx++;
				btn.setMaximumSize(btnDim);
				btn.setMinimumSize(btnDim);
				btn.setPreferredSize(btnDim);
				btn.setText("..");
				jp.add(btn, gbc);
			}
		}
		return jp;
	}
}
