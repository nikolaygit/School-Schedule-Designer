package org.posithing.ssd.handlers;

import java.io.File;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.impl.XMLDataProvider;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.views.classeseditor.ClassesEditorView;
import org.posithing.ssd.ui.views.subjectseditor.SubjectsEditorView;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;


public class OpenHandler extends AbstractHandler {

	private static Log LOG = LogFactory.getLog(OpenHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ExtensionManager extensionManager = ExtensionManager.getInstance();
		SchoolDataProvider dataProvider = extensionManager.getDefaultSchoolDataProvider();

		// TODO: open a wizard for the default data provider instead
		FileDialog fileDialog = new FileDialog(HandlerUtil.getActiveShell(event));
		fileDialog.setText("Open a file");
		String selectedFile = fileDialog.open();
		if (selectedFile != null) {
			LOG.info("Opening file: " + selectedFile);
			File file = new File(selectedFile);
			if (dataProvider instanceof XMLDataProvider) {
				XMLDataProvider xmlDataProvider = (XMLDataProvider) dataProvider;
				try {
					xmlDataProvider.parseFile(file);
					LOG.info("  file was parsed successfully.");
					updateViews(xmlDataProvider);
				} catch (SchoolDataProviderException e) {
					Messenger.openExceptionBox(e, "Can't open file",
							"The file could not be opened:\n" + file.getAbsolutePath());
				}
			}
		}

		return null;
	}

	private void updateViews(SchoolDataProvider dataProvider) throws SchoolDataProviderException {
		Set<Teacher> teachers = dataProvider.getTeachers();
		Set<SchoolClass> schoolClasses = dataProvider.getSchoolClasses();
		Set<Subject> subjects = dataProvider.getSubjects();

		TeachersEditorView teachersEditorView = EclipseUtil
				.<TeachersEditorView> getView(TeachersEditorView.ID);
		teachersEditorView.getViewer().setInput(teachers);

		ClassesEditorView classesEditorView = EclipseUtil
				.<ClassesEditorView> getView(ClassesEditorView.ID);
		classesEditorView.getViewer().setInput(schoolClasses);

		SubjectsEditorView subjectsEditorView = EclipseUtil
				.<SubjectsEditorView> getView(SubjectsEditorView.ID);
		subjectsEditorView.getViewer().setInput(subjects);
	}

}
