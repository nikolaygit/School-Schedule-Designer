package org.posithing.ssd.ui.views.subjectseditor;

import org.eclipse.jface.viewers.Viewer;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.ui.common.AbstractViewerSorter;
import org.posithing.ssd.utils.Messenger;


public class SubjectsSorter extends AbstractViewerSorter {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		try {
			Subject s1 = (Subject) e1;
			Subject s2 = (Subject) e2;

			int rc = 0;
			switch (propertyIndex) {
			case 0:
				rc = s1.getName().compareToIgnoreCase(s2.getName());
				break;
			case 1:
				rc = s1.getId().compareToIgnoreCase(s2.getId());
				break;
			default:
				rc = 0;
			}
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
			return rc;

		} catch (Exception e) {
			Messenger.openExceptionBox(e, "E compare", "E compare");
		}

		return 0;
	}
}
