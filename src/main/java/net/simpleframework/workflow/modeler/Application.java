package net.simpleframework.workflow.modeler;

import java.util.Enumeration;
import java.util.Map;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.IScanResourcesCallback;
import net.simpleframework.common.I18n;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.workflow.modeler.utils.SplashScreen;
import net.simpleframework.workflow.modeler.utils.SwingUtils;
import net.simpleframework.workflow.remote.IWorkflowRemote;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public class Application extends ObjectEx {

	public static void main(final String[] args) {
		get().startup(args);
	}

	public static Application get() {
		return singleton(Application.class);
	}

	SplashScreen splash;

	private IMainPane mainPane;

	void startup(final String[] args) {
		try {
			ClassUtils.scanResources("net.simpleframework",
					new IScanResourcesCallback[] { I18n.getBasenamesCallback() });

			// ui
			splash = new SplashScreen(0) {

				@Override
				protected String getInfo() {
					return null;
				}
			};
			mainPane = new MainFrame();
		} catch (final Exception e) {
			getLog().error(e);
		} finally {
			if (splash != null) {
				splash.dispose();
			}
		}
	}

	public IMainPane getMainPane() {
		return mainPane;
	}

	public void shutdown() {
		System.exit(0);
	}

	static {
		final UIDefaults defaults = UIManager.getDefaults();
		final Enumeration<?> keys = defaults.keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			if (key instanceof String && ((String) key).endsWith(".font")) {
				defaults.put(key, SwingUtils.defautFont);
			}
		}
		// try {
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		// } catch (final Exception e) {
		// e.printStackTrace();
		// }
	}

	public static boolean isError(final Map<String, Object> kv) {
		final String error = (String) kv.get("error");
		if (error != null) {
			SwingUtils.showError(error);
			return true;
		}
		return false;
	}

	public static IWorkflowRemote remote() {
		return singleton(DefaultModelerRemote.class);
	}
}
