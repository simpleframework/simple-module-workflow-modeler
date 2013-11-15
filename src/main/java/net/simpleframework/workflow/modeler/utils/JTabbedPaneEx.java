package net.simpleframework.workflow.modeler.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class JTabbedPaneEx extends JTabbedPane implements MouseListener, MouseMotionListener {
	private EventListenerList listenerList = null;

	private JViewport headerViewport = null;

	private Icon normalCloseIcon = null;

	private Icon hooverCloseIcon = null;

	private Icon pressedCloseIcon = null;

	public JTabbedPaneEx() {
		super();
		init(SwingConstants.LEFT);
	}

	public JTabbedPaneEx(final int horizontalTextPosition) {
		super();
		init(horizontalTextPosition);
	}

	public void addCloseableTabbedPaneListener(final ICloseableTabbedPaneListener l) {
		listenerList.add(ICloseableTabbedPaneListener.class, l);
	}

	@Override
	public void addTab(final String title, final Component component) {
		addTab(title, component, null);
	}

	public void addTab(final String title, final Component component, final Icon extraIcon) {
		boolean doPaintCloseIcon = true;
		try {
			Object prop = null;
			if ((prop = ((JComponent) component).getClientProperty("isClosable")) != null) {
				doPaintCloseIcon = (Boolean) prop;
			}
		} catch (final Exception ignored) {
		}

		component.addPropertyChangeListener("isClosable", new PropertyChangeListener() {

			@Override
			public void propertyChange(final PropertyChangeEvent e) {
				final Object newVal = e.getNewValue();
				int index = -1;
				if (e.getSource() instanceof Component) {
					index = indexOfComponent((Component) e.getSource());
				}
				if ((index != -1) && (newVal != null) && (newVal instanceof Boolean)) {
					setCloseIconVisibleAt(index, (Boolean) newVal);
				}
			}
		});

		super.addTab(title, doPaintCloseIcon ? new CloseTabIcon(extraIcon) : null, component);

		if (headerViewport == null) {
			for (final Component c : getComponents()) {
				if ("TabbedPane.scrollableViewport".equals(c.getName())) {
					headerViewport = (JViewport) c;
				}
			}
		}
	}

	protected boolean fireCloseTab(final int tabIndexToClose) {
		boolean closeit = true;
		final Object[] listeners = listenerList.getListenerList();
		for (final Object i : listeners) {
			if (i instanceof ICloseableTabbedPaneListener) {
				if (!((ICloseableTabbedPaneListener) i).closeTab(tabIndexToClose)) {
					closeit = false;
					break;
				}
			}
		}
		return closeit;
	}

	public ICloseableTabbedPaneListener[] getCloseableTabbedPaneListener() {
		return listenerList.getListeners(ICloseableTabbedPaneListener.class);
	}

	private void init(final int horizontalTextPosition) {
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		listenerList = new EventListenerList();
		addMouseListener(this);
		addMouseMotionListener(this);

		if (getUI() instanceof MetalTabbedPaneUI) {
			setUI(new CloseableMetalTabbedPaneUI(horizontalTextPosition));
		} else {
			setUI(new CloseableTabbedPaneUI(horizontalTextPosition));
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		processMouseEvents(e);
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		processMouseEvents(e);
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		for (int i = 0; i < getTabCount(); i++) {
			final CloseTabIcon icon = (CloseTabIcon) getIconAt(i);
			if (icon != null) {
				icon.mouseover = false;
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		processMouseEvents(e);
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		processMouseEvents(e);
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	private void processMouseEvents(final MouseEvent e) {
		final int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
		if (tabNumber < 0) {
			return;
		}
		boolean otherWasOver = false;
		for (int i = 0; i < getTabCount(); i++) {
			if (i != tabNumber) {
				final CloseTabIcon ic = (CloseTabIcon) getIconAt(i);
				if (ic != null) {
					if (ic.mouseover) {
						otherWasOver = true;
					}
					ic.mouseover = false;
				}
			}
		}
		if (otherWasOver) {
			repaint();
		}
		final CloseTabIcon icon = (CloseTabIcon) getIconAt(tabNumber);
		if (icon != null) {
			final Rectangle rect = icon.getBounds();
			final boolean vpIsNull = headerViewport == null;
			final Point pos = vpIsNull ? new Point() : headerViewport.getViewPosition();
			final int vpDiffX = (vpIsNull ? 0 : headerViewport.getX());
			final int vpDiffY = (vpIsNull ? 0 : headerViewport.getY());
			final Rectangle drawRect = new Rectangle(rect.x - pos.x + vpDiffX, rect.y - pos.y
					+ vpDiffY, rect.width, rect.height);

			if (e.getID() == MouseEvent.MOUSE_PRESSED) {
				icon.mousepressed = e.getModifiers() == InputEvent.BUTTON1_MASK;
				repaint(drawRect);
			} else if ((e.getID() == MouseEvent.MOUSE_MOVED)
					|| (e.getID() == MouseEvent.MOUSE_DRAGGED)
					|| (e.getID() == MouseEvent.MOUSE_CLICKED)) {
				pos.x += e.getX() - vpDiffX;
				pos.y += e.getY() - vpDiffY;
				if (rect.contains(pos)) {
					if (e.getID() == MouseEvent.MOUSE_CLICKED) {
						final int selIndex = getSelectedIndex();
						if (fireCloseTab(selIndex)) {
							if (selIndex > 0) {
								final Rectangle rec = getUI().getTabBounds(this, selIndex - 1);

								final MouseEvent event = new MouseEvent((Component) e.getSource(),
										e.getID() + 1, System.currentTimeMillis(), e.getModifiers(), rec.x,
										rec.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
								dispatchEvent(event);
							}
							remove(selIndex);
						} else {
							icon.mouseover = false;
							icon.mousepressed = false;
							repaint(drawRect);
						}
					} else {
						icon.mouseover = true;
						icon.mousepressed = e.getModifiers() == InputEvent.BUTTON1_MASK;
					}
				} else {
					icon.mouseover = false;
				}
				repaint(drawRect);
			}
		}
	}

	public void removeCloseableTabbedPaneListener(final ICloseableTabbedPaneListener l) {
		listenerList.remove(ICloseableTabbedPaneListener.class, l);
	}

	public void setCloseIcons(final Icon normal, final Icon hoover, final Icon pressed) {
		normalCloseIcon = normal;
		hooverCloseIcon = hoover;
		pressedCloseIcon = pressed;
	}

	private void setCloseIconVisibleAt(final int index, final boolean iconVisible)
			throws IndexOutOfBoundsException {
		super.setIconAt(index, iconVisible ? new CloseTabIcon(null) : null);
	}

	class CloseableMetalTabbedPaneUI extends MetalTabbedPaneUI {
		private int horizontalTextPosition = SwingConstants.LEFT;

		public CloseableMetalTabbedPaneUI() {
		}

		public CloseableMetalTabbedPaneUI(final int horizontalTextPosition) {
			this.horizontalTextPosition = horizontalTextPosition;
		}

		@Override
		protected void layoutLabel(final int tabPlacement, final FontMetrics metrics,
				final int tabIndex, final String title, final Icon icon, final Rectangle tabRect,
				final Rectangle iconRect, final Rectangle textRect, final boolean isSelected) {

			textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

			final javax.swing.text.View v = getTextViewForTab(tabIndex);
			if (v != null) {
				tabPane.putClientProperty("html", v);
			}

			SwingUtilities.layoutCompoundLabel(tabPane, metrics, title, icon, SwingConstants.CENTER,
					SwingConstants.CENTER, SwingConstants.CENTER, horizontalTextPosition, tabRect,
					iconRect, textRect, textIconGap + 2);

			tabPane.putClientProperty("html", null);

			final int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
			final int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
			iconRect.x += xNudge;
			iconRect.y += yNudge;
			textRect.x += xNudge;
			textRect.y += yNudge;
		}
	}

	class CloseableTabbedPaneUI extends BasicTabbedPaneUI {

		private int horizontalTextPosition = SwingConstants.LEFT;

		public CloseableTabbedPaneUI() {
		}

		public CloseableTabbedPaneUI(final int horizontalTextPosition) {
			this.horizontalTextPosition = horizontalTextPosition;
		}

		@Override
		protected void layoutLabel(final int tabPlacement, final FontMetrics metrics,
				final int tabIndex, final String title, final Icon icon, final Rectangle tabRect,
				final Rectangle iconRect, final Rectangle textRect, final boolean isSelected) {

			textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

			final javax.swing.text.View v = getTextViewForTab(tabIndex);
			if (v != null) {
				tabPane.putClientProperty("html", v);
			}

			SwingUtilities.layoutCompoundLabel(tabPane, metrics, title, icon, SwingConstants.CENTER,
					SwingConstants.CENTER, SwingConstants.CENTER, horizontalTextPosition, tabRect,
					iconRect, textRect, textIconGap + 2);

			tabPane.putClientProperty("html", null);

			final int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
			final int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
			iconRect.x += xNudge;
			iconRect.y += yNudge;
			textRect.x += xNudge;
			textRect.y += yNudge;
		}
	}

	class CloseTabIcon implements Icon {
		private int x_pos;

		private int y_pos;

		private final int width;

		private final int height;

		private final Icon fileIcon;

		private boolean mouseover = false;

		private boolean mousepressed = false;

		public CloseTabIcon(final Icon fileIcon) {
			this.fileIcon = fileIcon;
			width = 16;
			height = 16;
		}

		public Rectangle getBounds() {
			return new Rectangle(x_pos, y_pos, width, height);
		}

		@Override
		public int getIconHeight() {
			return height;
		}

		@Override
		public int getIconWidth() {
			return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
			boolean doPaintCloseIcon = true;
			try {
				final JTabbedPane tabbedpane = (JTabbedPane) c;
				final int tabNumber = tabbedpane.getUI().tabForCoordinate(tabbedpane, x, y);
				final JComponent curPanel = (JComponent) tabbedpane.getComponentAt(tabNumber);
				Object prop = null;
				if ((prop = curPanel.getClientProperty("isClosable")) != null) {
					doPaintCloseIcon = (Boolean) prop;
				}
			} catch (final Exception ignored) {
			}
			if (doPaintCloseIcon) {
				x_pos = x;
				y_pos = y;
				int y_p = y + 1;

				if ((normalCloseIcon != null) && !mouseover) {
					normalCloseIcon.paintIcon(c, g, x, y_p);
				} else if ((hooverCloseIcon != null) && mouseover && !mousepressed) {
					hooverCloseIcon.paintIcon(c, g, x, y_p);
				} else if ((pressedCloseIcon != null) && mousepressed) {
					pressedCloseIcon.paintIcon(c, g, x, y_p);
				} else {
					y_p++;

					final Color col = g.getColor();

					if (mousepressed && mouseover) {
						g.setColor(Color.WHITE);
						g.fillRect(x + 1, y_p, 12, 13);
					}

					g.setColor(Color.black);
					g.drawLine(x + 1, y_p, x + 12, y_p);
					g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
					g.drawLine(x, y_p + 1, x, y_p + 12);
					g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
					g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
					if (mouseover) {
						g.setColor(Color.GRAY);
					}
					g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
					g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
					g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
					g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
					g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
					g.setColor(col);
					if (fileIcon != null) {
						fileIcon.paintIcon(c, g, x + width, y_p);
					}
				}
			}
		}
	}
}
