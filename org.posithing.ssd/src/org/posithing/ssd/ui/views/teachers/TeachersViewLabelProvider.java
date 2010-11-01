package org.posithing.ssd.ui.views.teachers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.utils.StringUtil;


public class TeachersViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	public TeachersViewLabelProvider() {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0)
			return StringUtil.EMPTY;
		else {
			if (element instanceof Teacher) {
				Teacher teacher = (Teacher) element;
				return teacher.getId();
			}
		}

		return null;
	}

}
