package net.simpleframework.workflow.modeler;

import static net.simpleframework.common.I18n.$m;

import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.workflow.modeler.utils.ITabbedContent;
import net.simpleframework.workflow.modeler.utils.SwingUtils;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class ApplicationActions {
	public static final Icon newIcon = SwingUtils.loadIcon("new.png");
	public static final Icon saveIcon = SwingUtils.loadIcon("save.png");
	public static final Icon helpIcon = SwingUtils.loadIcon("help.gif");
	public static final Icon deleteIcon = SwingUtils.loadIcon("delete.png");
	public static final Icon openIcon = SwingUtils.loadIcon("open.png");
	public static final Icon connectionIcon = SwingUtils.loadIcon("connection.png");
	public static final Icon propertyIcon = SwingUtils.loadIcon("property.png");
	public static final Icon processModelIcon = SwingUtils.loadIcon("process_model.gif");
	public static final Icon processNodeIcon = SwingUtils.loadIcon("process_node.png");

	public static abstract class ApplicationAction extends AbstractAction {

		public ApplicationAction(final String name) {
			this(name, null);
		}

		public ApplicationAction(final String name, final Icon icon) {
			this(name, null, icon);
		}

		public ApplicationAction(final String name, final Integer mnemonic, final Icon icon) {
			putValue(NAME, name);
			if (mnemonic != null) {
				putValue(Action.MNEMONIC_KEY, mnemonic);
			}
			if (icon != null) {
				putValue(SMALL_ICON, icon);
			}
		}

		protected Window mainPane;

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (mainPane == null) {
				mainPane = (Window) Application.get().getMainPane();
			}
			try {
				mainPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				actionInvoked(e);
			} finally {
				mainPane.setCursor(Cursor.getDefaultCursor());
			}
		}

		protected abstract void actionInvoked(final ActionEvent e);
	}

	public static class AboutAction extends ApplicationAction {

		public AboutAction() {
			super($m("AboutDialog.8"), helpIcon);
		}

		@Override
		protected void actionInvoked(final ActionEvent e) {
			new AboutDialog();
		}
	}

	public static class SaveAsAction extends ApplicationAction {
		public SaveAsAction() {
			super($m("SaveAsAction.0"));
		}

		@Override
		protected void actionInvoked(final ActionEvent e) {
			final ITabbedContent tc = ((IMainPane) mainPane).getTabbedPane()
					.getSelectedTabbedContent();
			if (tc == null) {
				SwingUtils.showError($m("SaveAsAction.1"));
				return;
			}
			final String[] fileFilters = new String[] { "svg|SVG文件 (.svg)", "png|PNG文件 (.png)",
					"html|VML文件 (.html)" };
			final JFileChooser chooser = SwingUtils.createJFileChooser(fileFilters);
			if (chooser.showSaveDialog(mainPane) == JFileChooser.APPROVE_OPTION) {
				final String type = "."
						+ StringUtils.split(
								fileFilters[ArrayUtils.indexOf(chooser.getChoosableFileFilters(),
										chooser.getFileFilter())], "|")[0];
				String filename = chooser.getSelectedFile().getAbsolutePath();
				if (!filename.toLowerCase().endsWith(type)) {
					filename += type;
				}
				try {

					final mxGraphComponent gc = tc.getGraphComponent();
					if (".svg".equals(type)) {
						final mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(gc.getGraph(),
								null, 1, null, new CanvasFactory() {
									@Override
									public mxICanvas createCanvas(final int width, final int height) {
										final mxSvgCanvas canvas = new mxSvgCanvas(mxDomUtils
												.createSvgDocument(width, height)) {
											@Override
											public String getImageForStyle(final Map<String, Object> style) {
												return super.getImageForStyle(style);
											}
										};
										canvas.setEmbedded(true);
										return canvas;
									}
								});
						mxUtils.writeFile(mxXmlUtils.getXml(canvas.getDocument()), filename);
					} else if (".png".equals(type)) {
						final BufferedImage image = mxCellRenderer.createBufferedImage(gc.getGraph(),
								null, 1, null, true, null, gc.getCanvas());
						ImageIO.write(image, "PNG", new File(filename));
					} else if (".html".equals(type)) {
						mxUtils.writeFile(
								mxXmlUtils.getXml(mxCellRenderer.createVmlDocument(gc.getGraph(), null, 1,
										null, null).getDocumentElement()), filename);
					}
				} catch (final IOException e1) {
					SwingUtils.showError(e1);
				}
			}
		}
	}

	public static class NewConnectionAction extends ApplicationAction {

		public NewConnectionAction() {
			super($m("ApplicationActions.0"), connectionIcon);
		}

		@Override
		protected void actionInvoked(final ActionEvent e) {
			new ConnectionDialog(null);
		}
	}
}
