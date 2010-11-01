package org.posithing.ssd.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.dialogs.SubjectDialog;
import org.posithing.ssd.ui.validators.SubjectIdValidator;
import org.posithing.ssd.ui.views.subjectseditor.SubjectsEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;


public class AddSubjectHandler extends AbstractHandler {

	boolean toSynchronize;

	public AddSubjectHandler() {
		toSynchronize = true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Subject newSubject = null;
		try {
			SubjectIdValidator subjectIdValidator = new SubjectIdValidator();

			SubjectDialog dialog = new SubjectDialog(EclipseUtil.getShell());
			dialog.setIdValidator(subjectIdValidator);
			int res = dialog.open();
			if (res == Window.OK) {
				String subjectName = dialog.getName();
				String subjectId = dialog.getId();

				newSubject = new Subject(subjectId, subjectName);
				SchoolDataProvider dataProvider = ExtensionManager.getInstance()
						.getDefaultSchoolDataProvider();
				dataProvider.addSubject(newSubject, toSynchronize);

				SubjectsEditorView subjectsEditorView = EclipseUtil
						.<SubjectsEditorView> getView(SubjectsEditorView.ID);
				subjectsEditorView.getViewer().setInput(dataProvider.getSubjects());
			}
		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Could not add subject", "Could not add the subject "
					+ newSubject);
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not open the dialog",
					"Could not open the add subject dialog.");
		}
		return null;
	}

}
