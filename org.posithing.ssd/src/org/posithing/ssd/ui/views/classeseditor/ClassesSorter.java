package org.posithing.ssd.ui.views.classeseditor;

import org.eclipse.jface.viewers.Viewer;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.ui.common.AbstractViewerSorter;
import org.posithing.ssd.utils.Messenger;


public class ClassesSorter extends AbstractViewerSorter {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		try {
			SchoolClass c1 = (SchoolClass) e1;
			SchoolClass c2 = (SchoolClass) e2;

			int rc = 0;
			switch (propertyIndex) {
			case 0:
				rc = c1.getName().compareToIgnoreCase(c2.getName());
				break;
			case 1:
				rc = new Integer(c1.getGrade()).compareTo(new Integer(c2.getGrade()));
				break;
			case 2:
				rc = c1.getId().compareToIgnoreCase(c2.getId());
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
