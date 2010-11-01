package org.posithing.ssd.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.posithing.ssd.ui.views.classeseditor.ClassesEditorView;
import org.posithing.ssd.ui.views.subjectseditor.SubjectsEditorView;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorView;


public class SchoolDataPerspective implements IPerspectiveFactory {

	public static final String ID = "org.posithing.ssd.ui.perspective.ShoolData";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addView(TeachersEditorView.ID, IPageLayout.LEFT, 0.70f, editorArea);
		layout.addView(SubjectsEditorView.ID, IPageLayout.RIGHT, 0.85f, editorArea);
		layout.addView(ClassesEditorView.ID, IPageLayout.RIGHT, 0.50f, SubjectsEditorView.ID);

		layout.getViewLayout(TeachersEditorView.ID).setCloseable(false);
		layout.getViewLayout(ClassesEditorView.ID).setCloseable(false);
		layout.getViewLayout(SubjectsEditorView.ID).setCloseable(false);
	}

}
