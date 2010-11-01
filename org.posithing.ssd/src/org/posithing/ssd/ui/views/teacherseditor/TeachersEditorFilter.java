package org.posithing.ssd.ui.views.teacherseditor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.posithing.ssd.model.Teacher;


public class TeachersEditorFilter extends ViewerFilter {

	private String searchString;

	public void setSearchText(String s) {
		// Search must be a substring of the existing value
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Teacher teacher = (Teacher) element;
		if (teacher.getFirstName().matches(searchString)) {
			return true;
		}
		if (teacher.getLastName().matches(searchString)) {
			return true;
		}
		if (teacher.getId().matches(searchString)) {
			return true;
		}

		return false;
	}
}
