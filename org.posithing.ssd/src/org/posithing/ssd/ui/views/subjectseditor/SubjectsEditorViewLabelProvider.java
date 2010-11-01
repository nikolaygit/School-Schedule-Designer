package org.posithing.ssd.ui.views.subjectseditor;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.utils.Messenger;


public class SubjectsEditorViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	public SubjectsEditorViewLabelProvider() {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Subject) {
			Subject subject = (Subject) element;
			try {

				switch (columnIndex) {
				case 0:
					return subject.getName();
				case 1:
					return subject.getId();
				default:
					break;
				}
			} catch (Exception e) {
				Messenger.openExceptionBox(e, "Can not get column text",
						"Can not get column text for subject: " + subject.toString());
			}
		}
		return null;
	}

}
