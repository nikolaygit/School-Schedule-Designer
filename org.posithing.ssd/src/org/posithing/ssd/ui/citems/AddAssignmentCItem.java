package org.posithing.ssd.ui.citems;

import java.util.List;

import org.eclipse.swt.widgets.MenuItem;
import org.posithing.ssd.model.AssignmentValue;


public class AddAssignmentCItem extends AbstractAddAssignmentCItem {

	@Override
	protected List<AssignmentValue> getListAssignmentValues(int colIndex) {
		return possibleAssignments[colIndex];
	}

	@Override
	protected void modifyMenuItem(MenuItem menuItem, AssignmentValue assignmentValue) {
	}
}
