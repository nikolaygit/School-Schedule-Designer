package org.posithing.ssd.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class DeleteTeacherHandler extends AbstractHandler {

	private static Log LOG = LogFactory.getLog(DeleteClassHandler.class);

	private ExtensionManager extensionManager;
	private boolean toSynchrnoze;

	public DeleteTeacherHandler() {
		extensionManager = ExtensionManager.getInstance();
		toSynchrnoze = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object applicationContext = event.getApplicationContext();
		if (applicationContext instanceof EvaluationContext) {
			EvaluationContext evaluationContext = (EvaluationContext) applicationContext;
			Object defaultVariable = evaluationContext.getDefaultVariable();
			if (defaultVariable instanceof List<?>) {
				List<Teacher> teachers = (List<Teacher>) defaultVariable;

				if (teachers.size() > 0) {

					SchoolDataProvider dataProvider = extensionManager
							.getDefaultSchoolDataProvider();

					List<Teacher> failed = new LinkedList<Teacher>();

					try {
						for (int i = 0; i < teachers.size(); i++) {
							Teacher teacher = teachers.get(i);
							try {
								dataProvider.removeTeacher(teacher, toSynchrnoze);
								LOG.info("Deleted teacher: " + teacher);
							} catch (SchoolDataProviderException e) {
								failed.add(teacher);
								throw e;
							}
						}
					} catch (SchoolDataProviderException e) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < failed.size(); i++) {
							Teacher failedTeacher = failed.get(i);
							sb.append(failedTeacher.toString());
							sb.append(StringUtil.NEWLINE);
						}

						Messenger.openExceptionBox(e, "Could not delete teachers",
								"Could not delete the following teachers:\n\n" + sb.toString());
					} finally {
						try {
							Set<Teacher> newTeachers = dataProvider.getTeachers();

							TeachersEditorView teachersEditorView = EclipseUtil
									.<TeachersEditorView> getView(TeachersEditorView.ID);
							teachersEditorView.getViewer().setInput(newTeachers);
						} catch (SchoolDataProviderException e) {
							Messenger.openExceptionBox(e, "Could not get new teachers",
									"Could not get new teachers");
						}
					}
				}
			}
		}
		return null;
	}

}
