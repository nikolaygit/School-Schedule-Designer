package org.posithing.ssd.ui.citems;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.ui.views.schedule.ScheduleManager;
import org.posithing.ssd.ui.views.schedule.ScheduleView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.StringUtil;


public abstract class AbstractAddAssignmentCItem extends ContributionItem {

	protected ScheduleManager scheduleManager;
	protected List<AssignmentValue>[] possibleAssignments;
	protected ScheduleView scheduleView;

	public AbstractAddAssignmentCItem() {
		init();
	}

	public AbstractAddAssignmentCItem(String id) {
		super(id);
		init();
	}

	private void init() {
		scheduleManager = ScheduleManager.getInstance();
		possibleAssignments = scheduleManager.getPossibleAssignments();

		scheduleView = EclipseUtil.<ScheduleView> getView(ScheduleView.ID);
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public void fill(Menu menu, int index) {

		final Point selectedCell = scheduleView.getLastSelectedCell();
		final int colIndex = selectedCell.x - 2;
		final int rowIndex = selectedCell.y;
		List<AssignmentValue> selPossibleAssignments = getListAssignmentValues(colIndex);

		for (final AssignmentValue assignmentValue : selPossibleAssignments) {
			Teacher teacher = assignmentValue.getTeacher();

			MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
			menuItem.setText(getMenuLabel(teacher));

			AddAssignmentSelectionAdapter adapter = new AddAssignmentSelectionAdapter(
					scheduleManager, scheduleView, colIndex, rowIndex, assignmentValue);
			menuItem.addSelectionListener(adapter);
			modifyMenuItem(menuItem, assignmentValue);
		}

		super.fill(menu, index);
	}

	protected String getMenuLabel(Teacher teacher) {
		StringBuilder sb = new StringBuilder();
		if (teacher.getId().length() == 1) {
			sb.append(StringUtil.SPACE);
			sb.append(StringUtil.SPACE);
		}
		sb.append(teacher.getId());
		sb.append(StringUtil.SPACE);
		sb.append(StringUtil.LINE_HORIZONTAL);
		sb.append(StringUtil.SPACE);
		sb.append(teacher.getWholeName());
		return sb.toString();
	}

	protected abstract List<AssignmentValue> getListAssignmentValues(final int colIndex);

	protected abstract void modifyMenuItem(MenuItem menuItem, AssignmentValue assignmentValue);

}
