package net.simpleframework.workflow.modeler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.ctx.settings.PropertiesContextSettings;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModelerSettings extends PropertiesContextSettings {
	private static final File settingsFile = new File("settings.properties");
	static {
		if (!settingsFile.exists()) {
			try {
				settingsFile.createNewFile();
			} catch (final IOException e) {
			}
		}
	}

	public static ModelerSettings get() {
		return singleton(ModelerSettings.class);
	}

	public ModelerSettings() throws IOException {
		super(new FileInputStream(settingsFile));
	}

	public void save() {
		OutputStream oStream = null;
		try {
			properties.store(oStream = new FileOutputStream(settingsFile), null);
		} catch (final IOException e) {
		} finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	/*********************************** Connection **********************************/

	private String hashkey(final String name, final String key) {
		return ObjectUtils.hashStr(name) + "." + key;
	}

	public String getConnectionUrl(final String name) {
		return getProperty(hashkey(name, "url"));
	}

	public void setConnectionUrl(final String name, final String url) {
		final String key = hashkey(name, "url");
		if (url == null) {
			remove(key);
		} else {
			setProperty(key, url);
		}
	}

	public String getConnectionLogin(final String name) {
		return getProperty(hashkey(name, "login"));
	}

	public void setConnectionLogin(final String name, final String login) {
		final String key = hashkey(name, "login");
		if (login == null) {
			remove(key);
		} else {
			setProperty(key, login);
		}
	}

	public String getConnectionPassword(final String name) {
		return getProperty(hashkey(name, "password"));
	}

	public void setConnectionPassword(final String name, final String password) {
		final String key = hashkey(name, "password");
		if (password == null) {
			remove(key);
		} else {
			setProperty(key, password);
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getConnections() {
		final String[] connections = StringUtils.split(getProperty("connections"));
		return connections == null ? Collections.EMPTY_SET : new HashSet<String>(
				Arrays.asList(connections));
	}

	public void setConnections(final Collection<String> coll) {
		setProperty("connections", StringUtils.join(coll, ";"));
	}

	/*********************************** UI **********************************/

	public int getWindowX() {
		return getIntProperty("window_x");
	}

	public void setWindowX(final int x) {
		setProperty("window_x", x);
	}

	public int getWindowY() {
		return getIntProperty("window_y");
	}

	public void setWindowY(final int y) {
		setProperty("window_y", y);
	}

	public int getWindowWidth() {
		return getIntProperty("window_width");
	}

	public void setWindowWidth(final int width) {
		setProperty("window_width", width);
	}

	public int getWindowHeight() {
		return getIntProperty("window_height");
	}

	public void setWindowHeight(final int height) {
		setProperty("window_height", height);
	}

	public boolean isWindowMaximum() {
		return getBoolProperty("window_maximum");
	}

	public void setWindowMaximum(final boolean maximum) {
		setProperty("window_maximum", maximum);
	}

	public int getHorizontalSplitLocation() {
		return getIntProperty("horizontal_split_location", 200);
	}

	public void setHorizontalSplitLocation(final int location) {
		setProperty("horizontal_split_location", location);
	}

	public int getVerticalSplitLocation() {
		return getIntProperty("vertical_split_location", 380);
	}

	public void setVerticalSplitLocation(final int location) {
		setProperty("vertical_split_location", location);
	}
}
