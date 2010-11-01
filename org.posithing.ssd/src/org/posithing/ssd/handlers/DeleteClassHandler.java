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
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.views.classeseditor.ClassesEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class DeleteClassHandler extends AbstractHandler {

	private static Log LOG = LogFactory.getLog(DeleteClassHandler.class);

	private ExtensionManager extensionManager;
	private boolean toSynchrnoze;

	public DeleteClassHandler() {
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
				List<SchoolClass> schoolClasses = (List<SchoolClass>) defaultVariable;

				if (schoolClasses.size() > 0) {

					SchoolDataProvider dataProvider = extensionManager
							.getDefaultSchoolDataProvider();

					List<SchoolClass> failed = new LinkedList<SchoolClass>();

					try {
						for (int i = 0; i < schoolClasses.size(); i++) {
							SchoolClass schoolClass = schoolClasses.get(i);
							try {
								dataProvider.removeSchoolClass(schoolClass, toSynchrnoze);
								LOG.info("Deleted class: " + schoolClass);
							} catch (SchoolDataProviderException e) {
								failed.add(schoolClass);
								throw e;
							}
						}
					} catch (SchoolDataProviderException e) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < failed.size(); i++) {
							SchoolClass failedSchoolClass = failed.get(i);
							sb.append(failedSchoolClass.toString());
							sb.append(StringUtil.NEWLINE);
						}

						Messenger.openExceptionBox(e, "Could not delete classes",
								"Could not delete the following classes:\n\n" + sb.toString());
					} finally {
						try {
							Set<SchoolClass> newSchoolClasses = dataProvider.getSchoolClasses();

							ClassesEditorView classesEditorView = EclipseUtil
									.<ClassesEditorView> getView(ClassesEditorView.ID);
							classesEditorView.getViewer().setInput(newSchoolClasses);
						} catch (SchoolDataProviderException e) {
							Messenger.openExceptionBox(e, "Could not get new classes",
									"Could not get new classes");
						}
					}
				}
			}
		}
		return null;
	}

}
