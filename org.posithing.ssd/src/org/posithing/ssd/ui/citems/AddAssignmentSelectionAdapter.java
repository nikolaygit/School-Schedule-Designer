package org.posithing.ssd.ui.citems;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.ui.views.schedule.ScheduleManager;
import org.posithing.ssd.ui.views.schedule.ScheduleView;
import org.posithing.ssd.utils.Messenger;


public class AddAssignmentSelectionAdapter extends SelectionAdapter {

	private ScheduleManager scheduleManager;
	private ScheduleView scheduleView;

	private int colIndex;
	private int rowIndex;
	private AssignmentValue assignmentValue;

	public AddAssignmentSelectionAdapter(ScheduleManager scheduleManager,
			ScheduleView scheduleView, int colIndex, int rowIndex, AssignmentValue assignmentValue) {
		this.scheduleManager = scheduleManager;
		this.scheduleView = scheduleView;
		this.colIndex = colIndex;
		this.rowIndex = rowIndex;
		this.assignmentValue = assignmentValue;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		try {
			scheduleManager.setAssignmentValue(colIndex, rowIndex, assignmentValue);
			scheduleManager.addLastSelectedAssignmentValue(assignmentValue);

			// redraw the changed cell
			scheduleView.redrawLastSelectedCell();

		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Could not add the assignment",
					"Could not add the given assignment");
		}
	}
}
