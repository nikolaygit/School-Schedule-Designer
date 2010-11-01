package org.posithing.ssd.ui.views.teacherseditor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;


public class TeachersEditingSupport extends EditingSupport {

	private CellEditor editor;
	private int column;
	private SchoolDataProvider dataProvider;
	private TeachingSubjects teachingSubjects;
	private boolean toSynchronize;

	public TeachersEditingSupport(ColumnViewer viewer, int column) {
		super(viewer);

		if (column == 4) {
			editor = new TeachingSubjectsCellEditor(((TableViewer) viewer).getTable());
			dataProvider = ExtensionManager.getInstance().getDefaultSchoolDataProvider();
			toSynchronize = true;
		} else {
			editor = new TextCellEditor(((TableViewer) viewer).getTable());
		}
		this.column = column;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected Object getValue(Object element) {
		try {
			Teacher teacher = (Teacher) element;

			switch (this.column) {
				case 0:
					return teacher.getShape();
				case 1:
					return teacher.getFirstName();
				case 2:
					return teacher.getLastName();
				case 3:
					return teacher.getId();
				case 4:
					teachingSubjects = dataProvider.getTeachingSubjects(teacher);
					return teachingSubjects;
				default:
					break;
			}
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "getValue", "getValue");
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		Teacher teacher = (Teacher) element;

		switch (this.column) {
			case 0:
				teacher.setShape(String.valueOf(value));
				break;
			case 1:
				teacher.setFirstName(String.valueOf(value));
				break;
			case 2:
				teacher.setLastName(String.valueOf(value));
				break;
			case 3:
				teacher.setId(String.valueOf(value));
				break;
			case 4:
				if (value instanceof TeachingSubject) {
					TeachingSubject newTeachingSubject = (TeachingSubject) value;
					try {
						dataProvider
								.removeTeacherSubjects(teacher, teachingSubjects, toSynchronize);
						TeachingSubjects newSubjects = new TeachingSubjects();
						newSubjects.addTeachingSubject(newTeachingSubject);
						dataProvider.addTeacherSubjects(teacher, newSubjects, toSynchronize);
					} catch (SchoolDataProviderException e) {
						Messenger.openExceptionBox(e, "Could not update teaching subjects",
								"Could not update teaching subjects for teacher "
										+ teacher.toString());
					}
				}
				break;
			default:
				break;
		}

		getViewer().update(element, null);
	}
}
