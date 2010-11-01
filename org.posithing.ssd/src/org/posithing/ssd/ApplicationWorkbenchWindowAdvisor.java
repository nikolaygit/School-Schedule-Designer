package org.posithing.ssd;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.posithing.ssd.model.IdentifiableExtension;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(true);
		configurer.setShowProgressIndicator(true);

		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		preferenceStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
				IWorkbenchPreferenceConstants.TOP_LEFT);
	}

	@Override
	public boolean preWindowShellClose() {
		ExtensionManager extensionManager = ExtensionManager.getInstance();
		List<? extends IdentifiableExtension> shapeProviders = extensionManager.getShapeProviders();
		closeExtensions(shapeProviders);

		List<? extends IdentifiableExtension> schoolDataProviders = extensionManager
				.getSchoolDataProviders();
		closeExtensions(schoolDataProviders);

		return super.preWindowShellClose();
	}

	private void closeExtensions(List<? extends IdentifiableExtension> shapeProviders) {
		for (IdentifiableExtension identifiableExtension : shapeProviders) {
			try {
				identifiableExtension.onClose();
			} catch (Exception e) {
				Messenger.openExceptionBox(e, "Can not close extension",
						"Can not close the extension " + identifiableExtension.toString());
			}
		}
	}
}
