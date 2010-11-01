package org.posithing.ssd.handlers.schedule;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.graphics.Point;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.ui.views.schedule.ScheduleManager;
import org.posithing.ssd.ui.views.schedule.ScheduleView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;


public class DeleteAssignmentHandler extends AbstractHandler {

	private ScheduleView scheduleView;
	private ScheduleManager scheduleManager;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			if (scheduleView == null) {
				scheduleView = EclipseUtil.<ScheduleView> getView(ScheduleView.ID);
			}
			if (scheduleManager == null) {
				scheduleManager = ScheduleManager.getInstance();
			}

			final Point selectedCell = scheduleView.getLastSelectedCell();
			final int colIndex = selectedCell.x - 2;
			final int rowIndex = selectedCell.y;
			AssignmentValue deletedAssignmentValue = scheduleManager.deleteAssignmentValue(
					colIndex, rowIndex);
			if (deletedAssignmentValue != null) {
				scheduleManager.addLastDeletedAssignmentValue(deletedAssignmentValue);
			}

			// redraw the changed cell
			scheduleView.redrawLastSelectedCell();
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Can not delete assignment",
					"Can not delete the selected assignment");
		}
		return null;
	}
}
