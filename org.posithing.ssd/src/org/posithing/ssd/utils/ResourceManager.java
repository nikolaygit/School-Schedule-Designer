package org.posithing.ssd.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class ResourceManager {

	private Map<RGB, Color> colors;
	private RGB whiteRGB;
	private RGB yellowRGB;

	private ResourceManager() {
		colors = new HashMap<RGB, Color>();
		whiteRGB = new RGB(255, 255, 255);
		yellowRGB = new RGB(255, 255, 92);
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class ResourceManagerHolder {
		private static final ResourceManager INSTANCE = new ResourceManager();
	}

	public static ResourceManager getInstance() {
		return ResourceManagerHolder.INSTANCE;
	}

	public Color getColor(RGB rgb) {
		if (colors.containsKey(rgb)) {
			return colors.get(rgb);
		} else {
			Color color = new Color(ResourceManager.getDisplay(), rgb);
			colors.put(rgb, color);
			return color;
		}
	}

	public void dispose(RGB rgb) {
		Color color = colors.remove(rgb);
		color.dispose();
	}

	public void disposeColors() {
		Collection<Color> containedColors = colors.values();
		for (Iterator<Color> iterator = containedColors.iterator(); iterator.hasNext();) {
			Color color = iterator.next();
			color.dispose();
		}
		colors.clear();
	}

	public static Display getDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}

	public RGB getWhiteRGB() {
		return whiteRGB;
	}

	public RGB getYellowRGB() {
		return yellowRGB;
	}
}
