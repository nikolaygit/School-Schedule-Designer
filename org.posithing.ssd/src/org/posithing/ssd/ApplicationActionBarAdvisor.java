package org.posithing.ssd;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
	}

	// @Override
	// protected void fillCoolBar(ICoolBarManager coolBar) {
	// IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
	// coolBar.add(new ToolBarContributionItem(toolbar, "main"));
	// // toolbar.add(openViewAction);
	// // toolbar.add(messagePopupAction);
	// super.fillCoolBar(coolBar);
	// }

}
