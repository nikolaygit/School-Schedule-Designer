package org.posithing.ssd.ui.views.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.dialogs.SelectionDialog;
import org.posithing.ssd.ui.views.teachers.TeachersView;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;


public class ScheduleDropAdapter extends ViewerDropAdapter {

	private final TableViewer viewer;
	private ScheduleManager scheduleManager;
	private SchoolDataProvider dataProvider;
	private TeachersView teachersView;

	private Point selectedPoint;
	private TableItem selectedItem;

	public ScheduleDropAdapter(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		scheduleManager = ScheduleManager.getInstance();
		dataProvider = ExtensionManager.getInstance().getDefaultSchoolDataProvider();

		teachersView = EclipseUtil.<TeachersView> getView(TeachersView.ID);
	}

	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		try {
			System.out.println("validateDrop on " + target);
			if (target == null) {
				return false;
			}
			if (!(target instanceof AssignmentRow)) {
				return false;
			}
			if (!TextTransfer.getInstance().isSupportedType(transferType)) {
				return false;
			}
			selectedPoint = getSelectedCell(getCurrentEvent());
			if (!(selectedPoint != null && selectedPoint.x > 1)) {
				return false;
			}

			if (teachersView == null) {
				teachersView = EclipseUtil.<TeachersView> getView(TeachersView.ID);
			}
			Teacher draggedTeacher = teachersView.getDraggedTeacher();

			// TODO: finding the possible assignment values may be optimzed, not
			// using a for loop, but just a quick yes/no response
			List<AssignmentValue> possible = scheduleManager.getPossibleAssignments()[selectedPoint.x - 2];
			for (AssignmentValue possibleAssignmentValue : possible) {
				if (possibleAssignmentValue.getTeacher().equals(draggedTeacher)) {
					return true;
				}
			}

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean performDrop(Object data) {
		Object target = getCurrentTarget();
		if (target instanceof AssignmentRow) {
			AssignmentRow assignmentRow = (AssignmentRow) target;
			if (data instanceof String) {
				String teacherId = (String) data;
				try {
					Teacher droppedTeacher = null;
					droppedTeacher = dataProvider.getTeacher(teacherId);
					if (droppedTeacher == null) {
						Messenger.openErrorBox("Teacher not found",
								"Teacher with the given id was not found. ID was " + teacherId);
						return false;
					}

					TeachingSubjects teachingSubjects = dataProvider
							.getTeachingSubjects(droppedTeacher);
					Set<TeachingSubject> teachingSubjectsSet = teachingSubjects
							.getTeachingSubjects();
					if (teachingSubjects == null || teachingSubjectsSet.size() == 0) {
						Messenger.openErrorBox("Teacher has no teaching subjects",
								"Teacher has no teaching subjects. Teacher is "
										+ droppedTeacher.toShortString());
					} else {
						Subject subject = null;
						if (teachingSubjectsSet.size() > 1) {
							SelectionDialog dialog = new SelectionDialog(viewer.getTable()
									.getShell());
							dialog.setTitle("Select a subject");
							dialog.setTitle("Select one of the subjects");

							List<Object> objects = new ArrayList<Object>();
							for (TeachingSubject teachingSubject : teachingSubjectsSet) {
								objects.add(teachingSubject.getSubject());
							}

							dialog.setObjects(objects);
							int res = dialog.open();
							if (res == Window.OK) {
								Object selectedObject = dialog.getSelectedObject();
								if (selectedObject instanceof Subject) {
									subject = (Subject) selectedObject;
								}
							}
						} else {
							TeachingSubject teachingSubject = teachingSubjectsSet.iterator().next();
							subject = teachingSubject.getSubject();
						}
						AssignmentValue assignmentValue = new AssignmentValue(droppedTeacher,
								subject);

						System.out.println("SETTING ASSIGNMENT ...");

						scheduleManager.setAssignmentValue(selectedPoint.x - 2, selectedPoint.y,
								assignmentValue);

						// redraw the table item
						Table table = viewer.getTable();
						Rectangle bounds = selectedItem.getBounds(selectedPoint.x);
						table.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
					}

				} catch (SchoolDataProviderException e) {
					Messenger.openExceptionBox(e, "Droping teacher Exception",
							"There was an expcetion in the data provider while dropping the teacher with id "
									+ teacherId);
				}
			} else {
				Messenger.openErrorBox("Drop data invalid",
						"Drop data should be a String representing the teacher ID!");
			}
		}

		return false;
	}

	/**
	 * @param event
	 * @return a point containing the column and row index of the selected table
	 *         or <code>null</code>.
	 */
	private Point getSelectedCell(DropTargetEvent event) {
		TableItem tableItem = (TableItem) event.item;
		Table table = tableItem.getParent();
		Rectangle clientArea = table.getClientArea();
		Point pt = table.toControl(event.x, event.y);
		int rowIndex = table.getTopIndex();
		while (rowIndex < table.getItemCount()) {
			boolean visible = false;
			TableItem item = table.getItem(rowIndex);
			int columnCount = table.getColumnCount();
			for (int col = 0; col < columnCount; col++) {
				Rectangle rect = item.getBounds(col);
				if (rect.contains(pt)) {
					selectedItem = item;
					return new Point(col, rowIndex);
				}
				if (!visible && rect.intersects(clientArea)) {
					visible = true;
				}
			}
			if (!visible)
				break;
			rowIndex++;
		}
		return null;
	}

}
