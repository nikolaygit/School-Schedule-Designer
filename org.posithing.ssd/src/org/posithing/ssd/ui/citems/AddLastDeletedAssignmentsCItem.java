package org.posithing.ssd.ui.citems;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.posithing.ssd.model.AssignmentValue;


public class AddLastDeletedAssignmentsCItem extends AbstractAddAssignmentCItem {

	private int colIndex;

	@Override
	protected List<AssignmentValue> getListAssignmentValues(int colIndex) {
		this.colIndex = colIndex;
		return scheduleManager.getLastDeletedAssignmentValues();
	}

	@Override
	public void fill(Menu menu, int index) {
		super.fill(menu, index);
		if (scheduleManager.getLastDeletedAssignmentValues().size() > 0) {
			new MenuItem(menu, SWT.SEPARATOR, index);
		}
	}

	@Override
	protected void modifyMenuItem(MenuItem menuItem, AssignmentValue assignmentValue) {
		List<AssignmentValue> possible = possibleAssignments[colIndex];
		if (!possible.contains(assignmentValue)) {
			menuItem.setEnabled(false);
		}
	}

}
