package org.posithing.ssd.ui.views.teacherseditor;

import java.util.Set;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.ui.dialogs.TeachingSubjectDialog;


public class TeachingSubjectsCellEditor extends DialogCellEditor {

	public TeachingSubjectsCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	public TeachingSubjectsCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		Object value = getValue();
		TeachingSubject teachingSubject = null;
		if (value != null && value instanceof TeachingSubjects) {
			TeachingSubjects teachingSubjects = (TeachingSubjects) value;
			Set<TeachingSubject> teachingSubjectsSet = teachingSubjects.getTeachingSubjects();
			for (TeachingSubject teachingSubjectElem : teachingSubjectsSet) {
				teachingSubject = teachingSubjectElem;
			}
		}

		// TODO - shows only last teaching subject - change to show all
		TeachingSubjectDialog dialog = new TeachingSubjectDialog(cellEditorWindow.getShell(),
				teachingSubject);
		int res = dialog.open();
		if (res == Window.OK) {
			return dialog.getNewTeachingSubject();
		}
		return null;
	}
}
