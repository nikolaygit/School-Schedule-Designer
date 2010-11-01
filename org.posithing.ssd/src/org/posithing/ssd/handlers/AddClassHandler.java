package org.posithing.ssd.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.dialogs.SchoolClassDialog;
import org.posithing.ssd.ui.validators.ClassGradeValidator;
import org.posithing.ssd.ui.validators.ClassIdValidator;
import org.posithing.ssd.ui.views.classeseditor.ClassesEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;


public class AddClassHandler extends AbstractHandler {

	boolean toSynchronize;

	public AddClassHandler() {
		toSynchronize = true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		SchoolClass newSchoolClass = null;
		try {
			ClassIdValidator classIdValidator = new ClassIdValidator();
			ClassGradeValidator classGradeValidator = new ClassGradeValidator();

			SchoolClassDialog dialog = new SchoolClassDialog(EclipseUtil.getShell());
			dialog.setIdValidator(classIdValidator);
			dialog.setGradeValidator(classGradeValidator);

			int res = dialog.open();
			if (res == Window.OK) {
				String className = dialog.getName();
				int classGrade = Integer.parseInt(dialog.getGrade());
				String classId = dialog.getId();

				newSchoolClass = new SchoolClass(classId, className, classGrade);
				SchoolDataProvider dataProvider = ExtensionManager.getInstance()
						.getDefaultSchoolDataProvider();
				dataProvider.addSchoolClass(newSchoolClass, toSynchronize);

				ClassesEditorView classesEditorView = EclipseUtil
						.<ClassesEditorView> getView(ClassesEditorView.ID);
				classesEditorView.getViewer().setInput(dataProvider.getSchoolClasses());
			}
		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Could not add class", "Could not add the class "
					+ newSchoolClass);
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not open the dialog",
					"Could not open the add class dialog.");
		}
		return null;
	}
}
