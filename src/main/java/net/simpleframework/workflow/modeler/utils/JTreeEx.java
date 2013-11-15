package net.simpleframework.workflow.modeler.utils;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class JTreeEx extends JTree implements Autoscroll {

	public JTreeEx() {
		setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 1));

		setCellRenderer(new DefaultTreeCellRenderer() {

			@Override
			public Component getTreeCellRendererComponent(final JTree tree, final Object value,
					final boolean selected, final boolean expanded, final boolean leaf, final int row,
					final boolean hasFocus) {
				final Component comp = super.getTreeCellRendererComponent(tree, value, selected,
						expanded, leaf, row, hasFocus);
				if (value instanceof TreeNodeEx) {
					final Icon icon = ((TreeNodeEx) value).getIcon();
					if (icon != null) {
						setIcon(icon);
					}
				}
				return comp;
			}
		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				TreePath tp = getPathForLocation(e.getX(), e.getY());
				if (!SwingUtilities.isRightMouseButton(e)) {
					return;
				}
				if (tp != null) {
					setSelectionPath(tp);
				} else {
					tp = getSelectionPath();
				}

				if (tp != null) {
					final Object comp = tp.getLastPathComponent();
					if (comp instanceof TreeNodeEx) {
						JPopupMenu pm0 = getPopupMenu();
						final JPopupMenu pm1 = ((TreeNodeEx) comp).getPopupMenu();
						if (pm0 != null) {
							if (pm1 != null) {
								pm1.addSeparator();
								for (int i = pm1.getComponentCount() - 1; i >= 0; i--) {
									pm0.insert(pm1.getComponent(i), getInsertMenuItemIndex());
								}
							}
						} else {
							pm0 = pm1;
						}
						if (pm0 != null) {
							SwingUtils.showPopupMenu(pm0, JTreeEx.this, e.getX(), e.getY());
						}
					}
				} else {
					final JPopupMenu pm = getPopupMenu();
					if (pm != null) {
						SwingUtils.showPopupMenu(pm, JTreeEx.this, e.getX(), e.getY());
					}
				}
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				final TreePath tp = getPathForLocation(e.getX(), e.getY());
				if ((tp != null) && SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2)) {
					final Object comp = tp.getLastPathComponent();
					if (!(comp instanceof TreeNodeEx)) {
						return;
					}
					try {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						((TreeNodeEx) comp).mouseDbClicked(e);
					} finally {
						setCursor(Cursor.getDefaultCursor());
					}
				}
			}
		});
	}

	public int getInsertMenuItemIndex() {
		return 0;
	}

	public JPopupMenu getPopupMenu() {
		return null;
	}

	protected void expand(final TreeNodeEx root) {
		final Enumeration<?> en = root.depthFirstEnumeration();
		while (en.hasMoreElements()) {
			final Object o = en.nextElement();
			if (!(o instanceof TreeNodeEx)) {
				continue;
			}
			final TreeNodeEx node = (TreeNodeEx) o;
			if (node.isExpanded()) {
				expandPath(new TreePath(node.getPath()));
			}
		}
	}

	private static final int AUTOSCROLL_MARGIN = 25;

	private final Insets autoscrollInsets = new Insets(0, 0, 0, 0);

	@Override
	public Insets getAutoscrollInsets() {
		final Dimension size = getSize();
		final Rectangle rect = getVisibleRect();
		autoscrollInsets.top = rect.y + AUTOSCROLL_MARGIN;
		autoscrollInsets.left = rect.x + AUTOSCROLL_MARGIN;
		autoscrollInsets.bottom = size.height - (rect.y + rect.height) + AUTOSCROLL_MARGIN;
		autoscrollInsets.right = size.width - (rect.x + rect.width) + AUTOSCROLL_MARGIN;
		return autoscrollInsets;
	}

	@Override
	public void autoscroll(final Point location) {
		int top = 0, left = 0, bottom = 0, right = 0;
		final Dimension size = getSize();
		final Rectangle rect = getVisibleRect();
		final int bottomEdge = rect.y + rect.height;
		final int rightEdge = rect.x + rect.width;
		if ((location.y - rect.y <= AUTOSCROLL_MARGIN) && (rect.y > 0)) {
			top = AUTOSCROLL_MARGIN;
		}
		if ((location.x - rect.x <= AUTOSCROLL_MARGIN) && (rect.x > 0)) {
			left = AUTOSCROLL_MARGIN;
		}
		if ((bottomEdge - location.y <= AUTOSCROLL_MARGIN) && (bottomEdge < size.height)) {
			bottom = AUTOSCROLL_MARGIN;
		}
		if ((rightEdge - location.x <= AUTOSCROLL_MARGIN) && (rightEdge < size.width)) {
			right = AUTOSCROLL_MARGIN;
		}
		rect.x += right - left;
		rect.y += bottom - top;
		scrollRectToVisible(rect);
	}
}
