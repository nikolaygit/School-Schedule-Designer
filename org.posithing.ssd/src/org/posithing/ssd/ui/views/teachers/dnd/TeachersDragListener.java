package org.posithing.ssd.ui.views.teachers.dnd;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.posithing.ssd.model.Teacher;


public class TeachersDragListener implements DragSourceListener {

	private final TableViewer viewer;
	private Teacher selectedTeacher;

	public TeachersDragListener(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		System.out.println("drag started ...");
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		selectedTeacher = (Teacher) selection.getFirstElement();
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		System.out.println("drag set data " + event);

		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = selectedTeacher.getId();
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		System.out.println("drag finished.");
	}

	public Teacher getSelectedTeacher() {
		return selectedTeacher;
	}

}
