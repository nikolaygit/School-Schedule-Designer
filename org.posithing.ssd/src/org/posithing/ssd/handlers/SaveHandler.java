package org.posithing.ssd.handlers;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.posithing.ssd.jobs.SaveDataJob;


public class SaveHandler extends AbstractHandler {

	private static Log LOG = LogFactory.getLog(SaveHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("SAVE ...");

		FileDialog fileDialog = new FileDialog(HandlerUtil.getActiveShell(event));
		fileDialog.setText("Save as file");
		String selectedFile = fileDialog.open();
		if (selectedFile != null) {
			LOG.info("Saving file: " + selectedFile);
			File file = new File(selectedFile);
			SaveDataJob saveDataJob = new SaveDataJob(file);
			saveDataJob.setUser(true);
			saveDataJob.schedule();
		}
		return null;
	}

}
