package org.posithing.ssd.ui.views.teacherseditor;

import java.util.Map;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class TeachersEditorViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	private SchoolDataProvider defDataProvider;

	public TeachersEditorViewLabelProvider() {
		defDataProvider = ExtensionManager.getInstance().getDefaultSchoolDataProvider();
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Teacher) {
			Teacher teacher = (Teacher) element;
			try {
				Map<Teacher, TeachingSubjects> teacherSubjects;
				teacherSubjects = defDataProvider.getTeacherSubjects();
				TeachingSubjects teachingSubjects = teacherSubjects.get(teacher);

				switch (columnIndex) {
					case 0:
						return StringUtil.EMPTY;
					case 1:
						return teacher.getFirstName();
					case 2:
						return teacher.getLastName();
					case 3:
						return teacher.getId();
					case 4:
						if (teachingSubjects == null) {
							return StringUtil.EMPTY;
						} else {
							return teachingSubjects.toString();
						}
					default:
						break;
				}
			} catch (Exception e) {
				Messenger.openExceptionBox(e, "Can not get column text",
						"Can not get column text for teacher: " + teacher.toString());
			}
		}
		return null;
	}

}
