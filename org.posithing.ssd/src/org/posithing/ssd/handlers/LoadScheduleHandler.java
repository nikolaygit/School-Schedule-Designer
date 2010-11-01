package org.posithing.ssd.handlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.posithing.ssd.model.Assignment;
import org.posithing.ssd.model.AssignmentSlot;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.views.schedule.AssignmentRow;
import org.posithing.ssd.ui.views.schedule.InfoRow;
import org.posithing.ssd.ui.views.schedule.ScheduleManager;
import org.posithing.ssd.ui.views.schedule.ScheduleTableRow;
import org.posithing.ssd.ui.views.schedule.ScheduleView;
import org.posithing.ssd.ui.views.teachers.TeachersView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class LoadScheduleHandler extends AbstractHandler {

	private ScheduleManager scheduleManager;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		SchoolDataProvider dataProvider = ExtensionManager.getInstance()
				.getDefaultSchoolDataProvider();
		try {
			Set<Teacher> teachers = dataProvider.getTeachers();
			Set<SchoolClass> schoolClasses = dataProvider.getSchoolClasses();
			List<Assignment> assignments = dataProvider.getAssignments();

			TeachersView teachersView = EclipseUtil.<TeachersView> getView(TeachersView.ID);
			teachersView.getViewer().setInput(teachers);

			List<ScheduleTableRow> assignmentRows = new LinkedList<ScheduleTableRow>();
			populate(assignmentRows, schoolClasses, assignments);

			ScheduleView scheduleView = EclipseUtil.<ScheduleView> getView(ScheduleView.ID);
			scheduleView.getViewer().setInput(assignmentRows);

			scheduleManager = ScheduleManager.getInstance();
			scheduleManager.initData(); // init somewhere else
			scheduleManager.setAssignmentRows(assignmentRows);

			System.out.println("Assignments loaded");

		} catch (Exception e) {
			// TODO - message fix
			Messenger.openExceptionBox(e, StringUtil.EMPTY, StringUtil.EMPTY);
		}
		return null;
	}

	Random random = new Random();
	int numberOfAssignments = 22;

	private void populate(List<ScheduleTableRow> tableRows, Set<SchoolClass> schoolClasses,
			List<Assignment> assignments) throws SchoolDataProviderException {
		int days = 5;
		int slotsPerDay = 7;

		int count = 0;
		Map<String, Integer> idToRowIndex = new HashMap<String, Integer>();

		for (SchoolClass schoolClass : schoolClasses) {
			idToRowIndex.put(schoolClass.getId(), new Integer(count));
			count++;
		}

		for (int curDay = 1; curDay <= days; curDay++) {
			for (int curSlot = 1; curSlot <= slotsPerDay; curSlot++) {
				AssignmentRow row = new AssignmentRow(curDay, curSlot, numberOfAssignments);

				for (Assignment assignment : assignments) {
					AssignmentSlot assignmentSlot = assignment.getSlot();
					int assignmentDay = assignmentSlot.getDay();
					int assignmentTimeSlot = assignmentSlot.getTimeSlot();

					if (curDay == assignmentDay && curSlot == assignmentTimeSlot) {
						Integer rowIndex = idToRowIndex.get(assignmentSlot.getClassId());
						row.setAssignment(assignment.getValue(), rowIndex);
					}
				}

				tableRows.add(row);
			}

			InfoRow infoRow = new InfoRow(curDay, numberOfAssignments);
			tableRows.add(infoRow);
		}
	}
}
