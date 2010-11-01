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
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.views.subjectseditor.SubjectsEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class DeleteSubjectHandler extends AbstractHandler {

	private static Log LOG = LogFactory.getLog(DeleteClassHandler.class);

	private ExtensionManager extensionManager;
	private boolean toSynchrnoze;

	public DeleteSubjectHandler() {
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
				List<Subject> subjects = (List<Subject>) defaultVariable;

				if (subjects.size() > 0) {

					SchoolDataProvider dataProvider = extensionManager
							.getDefaultSchoolDataProvider();

					List<Subject> failed = new LinkedList<Subject>();

					try {
						for (int i = 0; i < subjects.size(); i++) {
							Subject subject = subjects.get(i);
							try {
								dataProvider.removeSubject(subject, toSynchrnoze);
								LOG.info("Deleted Subject: " + subject);
							} catch (SchoolDataProviderException e) {
								failed.add(subject);
								throw e;
							}
						}
					} catch (SchoolDataProviderException e) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < failed.size(); i++) {
							Subject failedSubject = failed.get(i);
							sb.append(failedSubject.toString());
							sb.append(StringUtil.NEWLINE);
						}

						Messenger.openExceptionBox(e, "Could not delete subjects",
								"Could not delete the following subjects:\n\n" + sb.toString());
					} finally {
						try {
							Set<Subject> newSubjects = dataProvider.getSubjects();

							SubjectsEditorView subjectsEditorView = EclipseUtil
									.<SubjectsEditorView> getView(SubjectsEditorView.ID);
							subjectsEditorView.getViewer().setInput(newSubjects);
						} catch (SchoolDataProviderException e) {
							Messenger.openExceptionBox(e, "Could not get new Subjects",
									"Could not get new Subjects");
						}
					}
				}
			}
		}
		return null;
	}

}
