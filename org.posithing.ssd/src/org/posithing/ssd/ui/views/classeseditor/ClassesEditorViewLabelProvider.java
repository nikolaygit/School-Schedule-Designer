package org.posithing.ssd.ui.views.classeseditor;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class ClassesEditorViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	public ClassesEditorViewLabelProvider() {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof SchoolClass) {
			SchoolClass schoolClass = (SchoolClass) element;
			try {

				switch (columnIndex) {
				case 0:
					return schoolClass.getName();
				case 1:
					return schoolClass.getGrade() + StringUtil.EMPTY;
				case 2:
					return schoolClass.getId();
				default:
					break;
				}
			} catch (Exception e) {
				Messenger.openExceptionBox(e, "Can not get column text",
						"Can not get column text for class: " + schoolClass.toString());
			}
		}
		return null;
	}

}
