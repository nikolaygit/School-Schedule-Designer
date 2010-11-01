package org.posithing.ssd.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.posithing.ssd.ui.views.schedule.ScheduleView;
import org.posithing.ssd.ui.views.teachers.TeachersView;


public class SchoolSchedulePerspective implements IPerspectiveFactory {

	public static final String ID = "org.posithing.ssd.ui.perspective.ShoolSchedule";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addView(ScheduleView.ID, IPageLayout.LEFT, 0, editorArea);
		layout.addView(TeachersView.ID, IPageLayout.LEFT, 0.10f, ScheduleView.ID);
	}

}
