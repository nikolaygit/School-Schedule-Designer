package org.posithing.ssd.ui.views.teacherseditor;

import org.eclipse.jface.viewers.Viewer;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.ui.common.AbstractViewerSorter;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class TeachersSorter extends AbstractViewerSorter {

	/**
	 * TeachersSorter with starting sorted property index.
	 * 
	 * @param propertyIndex
	 *            the column index to be sorted at the beginning.
	 */
	public TeachersSorter(int propertyIndex) {
		super(propertyIndex);
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		try {
			Teacher t1 = (Teacher) e1;
			Teacher t2 = (Teacher) e2;

			int rc = 0;
			switch (propertyIndex) {
				case 0:
					String shape1 = t1.getShape();
					String shape2 = t2.getShape();
					if (shape1 == null) {
						shape1 = StringUtil.EMPTY;
					}
					if (shape2 == null) {
						shape2 = StringUtil.EMPTY;
					}
					rc = shape1.compareToIgnoreCase(shape2);
					break;
				case 1:
					rc = t1.getFirstName().compareToIgnoreCase(t2.getFirstName());
					break;
				case 2:
					rc = t1.getLastName().compareToIgnoreCase(t2.getLastName());
					break;
				case 3:
					String id1 = t1.getId();
					String id2 = t2.getId();
					try {
						int idInt1 = Integer.parseInt(id1);
						int idInt2 = Integer.parseInt(id2);

						if (idInt1 < idInt2) {
							return -1;
						} else if (idInt1 > idInt2) {
							return 1;
						} else {
							return 0;
						}
					} catch (NumberFormatException e) {
						rc = t1.getId().compareToIgnoreCase(t2.getId());
					}
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
