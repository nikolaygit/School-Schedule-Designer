package org.posithing.ssd.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.RGB;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.dialogs.TeacherDialog;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;


public class EditTeacherHandler extends AbstractHandler {

	boolean toSynchronize;

	public EditTeacherHandler() {
		toSynchronize = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Teacher selectedTeacher = null;

		try {
			EvaluationContext evaluationContext = (EvaluationContext) event.getApplicationContext();
			Object defaultVariable = evaluationContext.getDefaultVariable();
			if (defaultVariable instanceof List<?>) {
				List<Teacher> teachers = (List<Teacher>) defaultVariable;

				if (teachers.size() > 0) {

					selectedTeacher = teachers.get(0);

					TeacherDialog dialog = new TeacherDialog(EclipseUtil.getShell());

					dialog.setTitle("Edit Teacher");
					dialog.setTitle("Edit the teacher:");
					// dialog.setIdValidator(new TeacherIdValidator());
					dialog.setSelected(false);

					SchoolDataProvider dataProvider = ExtensionManager.getInstance()
							.getDefaultSchoolDataProvider();
					TeachingSubjects teachingSubjects = dataProvider
							.getTeachingSubjects(selectedTeacher);

					dialog.setFirstName(selectedTeacher.getFirstName());
					dialog.setLastName(selectedTeacher.getLastName());
					dialog.setId(selectedTeacher.getId());
					dialog.setRgb(selectedTeacher.getColor());
					dialog.setShape(selectedTeacher.getShape());
					dialog.setTeachingSubjects(teachingSubjects);

					int res = dialog.open();
					if (res == Window.OK) {
						String teacherFirstName = dialog.getFirstName();
						String teacherLastName = dialog.getLastName();
						String teacherId = dialog.getId();
						String teacherShape = dialog.getShape();
						RGB teacherRGB = dialog.getRGB();
						TeachingSubjects teacherTeachingSubjects = dialog.getTeachingSubjects();
						if (teacherTeachingSubjects.getTeachingSubjects().size() == 0) {

						}

						// remove the selected one
						dataProvider.removeTeacher(selectedTeacher, toSynchronize);

						// add the edited one
						Teacher editedTeacher = new Teacher(teacherId, teacherFirstName,
								teacherLastName);
						editedTeacher.setShape(teacherShape);
						editedTeacher.setColor(teacherRGB);

						dataProvider.addTeacher(editedTeacher, toSynchronize);
						dataProvider.addTeacherSubjects(editedTeacher, teacherTeachingSubjects,
								toSynchronize);

						TeachersEditorView teachersEditorView = EclipseUtil
								.<TeachersEditorView> getView(TeachersEditorView.ID);
						teachersEditorView.getViewer().setInput(dataProvider.getTeachers());
					}
				}
			}

		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Could not edit teacher", "Could not edit the teacher "
					+ selectedTeacher);
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not open the dialog",
					"Could not open the edit teacher dialog.");
		}
		return null;
	}

}
