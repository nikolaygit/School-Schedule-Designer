package org.posithing.ssd.ui.views.teachers;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.ui.common.AbstractTableItemTooltipListener;


public class TeacherTableItemTooltipListener extends AbstractTableItemTooltipListener {

	public TeacherTableItemTooltipListener(Table table, Display display) {
		super(table, display);
	}

	@Override
	protected String getTooltip(TableItem item) {
		Teacher teacher = (Teacher) item.getData();
		return teacher.getWholeName();
	}

}
