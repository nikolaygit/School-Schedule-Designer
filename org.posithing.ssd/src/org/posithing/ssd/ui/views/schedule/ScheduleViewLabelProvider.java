package org.posithing.ssd.ui.views.schedule;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ScheduleViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	public ScheduleViewLabelProvider() {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof AssignmentRow) {
			AssignmentRow assignmentRow = (AssignmentRow) element;
			if (columnIndex == 0 && assignmentRow.getDaySlot() == 1) {
				return new StringBuilder().append(assignmentRow.getDay()).toString();
			} else if (columnIndex == 1) {
				return new StringBuilder().append(assignmentRow.getDaySlot()).toString();
			}
			return null;
		}

		return null;
	}

}
