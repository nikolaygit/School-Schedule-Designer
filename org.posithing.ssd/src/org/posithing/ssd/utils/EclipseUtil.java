package org.posithing.ssd.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.posithing.ssd.Activator;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorView;


public class EclipseUtil {

	private static URL pluginFolderUrl;
	private static File pluginFolder;

	public static URL getPluginFolderUrl() {
		if (pluginFolderUrl == null) {
			pluginFolderUrl = Platform.getBundle(Activator.PLUGIN_ID).getEntry("/");
		}

		return pluginFolderUrl;
	}

	public static File getPluginFolder() {
		if (pluginFolder == null) {
			if (pluginFolderUrl == null) {
				getPluginFolderUrl();
			}
			try {
				pluginFolderUrl = FileLocator.resolve(pluginFolderUrl);
			} catch (IOException ex) {
				Messenger.openExceptionBox(ex, "Can not resolve the plugin folder URL",
						"Can not resolve the plugin folder URL:\n" + pluginFolderUrl.toString());
			}
			pluginFolder = new File(pluginFolderUrl.getPath());
		}

		return pluginFolder;
	}

	public static TeachersEditorView getTeachersEditorView() {
		String id = TeachersEditorView.ID;
		IViewPart viewPart = EclipseUtil.getViewPart(id);
		if (viewPart instanceof TeachersEditorView) {
			return (TeachersEditorView) viewPart;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ViewPart> T getView(String id) {
		IViewPart viewPart = EclipseUtil.getViewPart(id);
		return (T) viewPart;
	}

	public static IViewPart getViewPart(String id) {
		IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (id.equals(viewReferences[i].getId())) {
				return viewReferences[i].getView(false);
			}
		}
		return null;
	}

	/**
	 * Gets a color from a RGB String (e.g. "255, 255, 0").
	 * 
	 * @param RGB
	 *            Color string, separated with commas
	 * @return
	 */
	public static RGB getRGBColor(String rgbColorString) {
		if (!StringUtil.isEmpty(rgbColorString)) {

			String[] splitted = rgbColorString.split(StringUtil.COMMA);

			int red = Integer.parseInt(splitted[0]);
			int green = Integer.parseInt(splitted[1]);
			int blue = Integer.parseInt(splitted[2]);

			RGB color = new RGB(red, green, blue);
			return color;
		}

		return null;
	}

	public static Shell getShell() {
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

}
