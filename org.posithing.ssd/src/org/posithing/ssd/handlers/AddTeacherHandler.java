package org.posithing.ssd.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.RGB;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.dialogs.TeacherDialog;
import org.posithing.ssd.ui.validators.TeacherIdValidator;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.ResourceManager;


public class AddTeacherHandler extends AbstractHandler {

	boolean toSynchronize;

	public AddTeacherHandler() {
		toSynchronize = true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Teacher newTeacher = null;
		try {
			TeacherIdValidator teacherIdValidator = new TeacherIdValidator();
			ResourceManager resourceManager = ResourceManager.getInstance();
			RGB yellowRGB = resourceManager.getYellowRGB();

			TeacherDialog dialog = new TeacherDialog(EclipseUtil.getShell());
			dialog.setIdValidator(teacherIdValidator);
			dialog.setSelected(false);
			dialog.setRgb(yellowRGB);
			int res = dialog.open();
			if (res == Window.OK) {
				String teacherFirstName = dialog.getFirstName();
				String teacherLastName = dialog.getLastName();
				String teacherId = dialog.getId();
				String teacherShape = dialog.getShape();
				RGB teacherRGB = dialog.getRGB();
				TeachingSubjects teachingSubjects = dialog.getTeachingSubjects();
				if (teachingSubjects.getTeachingSubjects().size() == 0) {

				}

				newTeacher = new Teacher(teacherId, teacherFirstName, teacherLastName);
				newTeacher.setShape(teacherShape);
				newTeacher.setColor(teacherRGB);

				SchoolDataProvider dataProvider = ExtensionManager.getInstance()
						.getDefaultSchoolDataProvider();
				dataProvider.addTeacher(newTeacher, toSynchronize);
				dataProvider.addTeacherSubjects(newTeacher, teachingSubjects, toSynchronize);

				TeachersEditorView teachersEditorView = EclipseUtil
						.<TeachersEditorView> getView(TeachersEditorView.ID);
				teachersEditorView.getViewer().setInput(dataProvider.getTeachers());
			}
		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Could not add teacher", "Could not add the teacher "
					+ newTeacher);
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not open the dialog",
					"Could not open the add teacher dialog.");
		}
		return null;
	}
}
