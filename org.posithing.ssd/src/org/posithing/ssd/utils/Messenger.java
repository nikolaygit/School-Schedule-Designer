package org.posithing.ssd.utils;

import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.posithing.ssd.Activator;


public class Messenger {

	private static Log LOG = LogFactory.getLog(Messenger.class);
	
	public static void openInformationBox(String title, String message){
		Shell shell = getActiveShell();
		openInformationBox(title, message, shell);
	}

	public static void openInformationBox(String title, String message, Shell shell) {
		showMessageBox(title, message, shell, System.out, SWT.ICON_INFORMATION);
	}
	
	public static void openErrorBox(String title, String message){
		Shell shell = getActiveShell();
		openErrorBox(title, message, shell);
	}

	public static void openErrorBox(String title, String message, Shell shell) {
		showMessageBox(title, message, shell, System.err, SWT.ICON_ERROR);
	}

	public static void openWarningBox(String title, String message){
		Shell shell = getActiveShell();
		openWarningBox(title, message, shell);
	}

	public static void openWarningBox(String title, String message, Shell shell) {
		showMessageBox(title, message, shell, System.out, SWT.ICON_WARNING);
	}

	public static void showMessageBox(String title, String message, Shell shell, PrintStream printStream, int iconStyle) {
		if(shell==null){
			printStream.println(title);
			printStream.println(message);
		}
		else{
			MessageBox box = new MessageBox(shell, iconStyle);
			box.setText(title);
			box.setMessage(message);
			box.open();
		}
	}
	
	public static void openExceptionBox(Throwable e, String title, String message){
		Shell shell = getActiveShell();
		openExceptionBox(e, title, message, shell);
	}

	public static void openExceptionBox(Throwable e, String title, String message, Shell shell) {
		if(shell==null){
			LOG.error(title, e);
			LOG.error(message, e);
		}
		e.printStackTrace();
		String eMessage = e.getMessage() == null? e.toString(): e.getMessage(); 
		
		if(shell!=null){
			ExceptionDialog.openError(
					shell, 
					title, 
					message, 
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
							IStatus.ERROR, 
							eMessage, e)
					);
		}
	}
	
	/**
	 * Opens a information box with a toggle checkbox, stored in the given preference.
	 * 
	 * @param parent the parent shell of the dialog, or null if none
	 * @param title the dialog's title, or null if none
	 * @param message the message
	 * @param toggleMessage the message for the toggle control, or null for the default message
	 * @param toggleState the initial state for the toggle
	 * @param store the IPreference store in which the user's preference should be persisted; null if you don't want it persisted automatically.
	 * @param key the key to use when persisting the user's preference; null if you don't want it persisted.
	 * @return MessageDialogWithToggle
	 */
	public static MessageDialogWithToggle openToggleInformationBox(String title, String message,
			String toggleMessage, boolean initToggle, IPreferenceStore store, String key){
        return MessageDialogWithToggle.openInformation(
        		getActiveShell(), title, message, 
        		toggleMessage, initToggle, 
        		store, key);
	}
	
	/**
	 * Opens a yes/no question box with a toggle checkbox, stored in the given preference.
	 * 
	 * @param parent the parent shell of the dialog, or null if none
	 * @param title the dialog's title, or null if none
	 * @param message the message
	 * @param toggleMessage the message for the toggle control, or null for the default message
	 * @param toggleState the initial state for the toggle
	 * @param store the IPreference store in which the user's preference should be persisted; null if you don't want it persisted automatically.
	 * @param key the key to use when persisting the user's preference; null if you don't want it persisted.
	 * @return MessageDialogWithToggle
	 */
	public static MessageDialogWithToggle openToggleYesNoQuestion(String title, String message,
			String toggleMessage, boolean initToggle, IPreferenceStore store, String key){
        return MessageDialogWithToggle.openYesNoQuestion(
        		getActiveShell(), title, message, 
        		toggleMessage, initToggle, 
        		store, key);
	}

	public static boolean showStatusMessage(final String message, final String statusLineId){
		
		LOG.info(message);

		StatusLineContributionItem statusLineCItem = getStatusLineCItem(statusLineId);
		if(statusLineCItem == null){
			return false;
		}
		else{
			statusLineCItem.setText(message);
			return true;
		}
	}

	public static StatusLineContributionItem getStatusLineCItem(
			final String statusLineId) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		
		if(win == null)
			return null;
		IWorkbenchPage page = win.getActivePage();

		if(page == null)
			return null;
		IWorkbenchPart part = page.getActivePart();
		if(part == null)
			return null;
		IWorkbenchPartSite site = part.getSite();
		
		IViewSite vSite = (IViewSite) site;
		
		IActionBars actionBars = vSite.getActionBars();
		
		if(actionBars == null)
			return null;
		
		IStatusLineManager statusLineManager =
		 actionBars.getStatusLineManager();
		
		if(statusLineManager == null)
			return null;
		
		StatusLineContributionItem StatusLineCItem = (StatusLineContributionItem) statusLineManager.find(statusLineId);
		return StatusLineCItem;
	}
	
	/**
	 * @return <code>null</code> or the active Shell
	 */
	public static Shell getActiveShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench == null){
			return null;
		}
		else{
			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
			if(activeWorkbenchWindow == null){
				return null;
			}
			else{
				return activeWorkbenchWindow.getShell();
			}
		}
	}
}
