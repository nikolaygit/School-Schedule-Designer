package org.posithing.ssd.ui.views.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.posithing.ssd.model.Assignment;
import org.posithing.ssd.model.AssignmentSlot;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.preferences.ExtensionManager;


public class ScheduleManager {

	private static Log LOG = LogFactory.getLog(ScheduleManager.class);

	private SchoolDataProvider dataProvider;
	private boolean toSynchronize;

	private List<AssignmentValue>[] possibleAssignments;
	private List<ScheduleTableRow> assignmentRows;

	private List<AssignmentValue> lastSelectedAssignmentValues;
	private int maxLastSelectedValues = 5;

	private List<AssignmentValue> lastDeletedAssignmentValues;
	private int maxLastDeletedValues = 2;

	private String[] classIds;
	int numberOfdays = 5;
	int numberOfSlotsPerDay = 7;
	int numberOfRowsPerDay = 8;

	private ScheduleManager() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class ScheduleManagerHolder {
		private static final ScheduleManager INSTANCE = new ScheduleManager();
	}

	public static ScheduleManager getInstance() {
		return ScheduleManagerHolder.INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public void initData() throws SchoolDataProviderException, Exception {

		lastSelectedAssignmentValues = new ArrayList<AssignmentValue>();
		lastDeletedAssignmentValues = new ArrayList<AssignmentValue>();

		dataProvider = ExtensionManager.getInstance().getDefaultSchoolDataProvider();
		toSynchronize = false;

		Set<Teacher> teachers = dataProvider.getTeachers();

		// TODO - make teachers a LIST ?
		List<Teacher> teachersList = new ArrayList<Teacher>(teachers.size());
		for (Teacher teacher : teachers) {
			teachersList.add(teacher);
		}

		Set<SchoolClass> schoolClasses = dataProvider.getSchoolClasses();

		LinkedList<LinkedList<AssignmentValue>> possibleAssignmentsList = new LinkedList<LinkedList<AssignmentValue>>();

		classIds = new String[schoolClasses.size()];

		int classIndex = 0;
		for (SchoolClass schoolClass : schoolClasses) {
			String classId = schoolClass.getId();
			classIds[classIndex] = classId;

			LinkedList<AssignmentValue> possibleAssignmentSet = new LinkedList<AssignmentValue>();

			Collections.sort(teachersList, new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					if (o1 instanceof Teacher) {
						Teacher teacher1 = (Teacher) o1;
						if (o2 instanceof Teacher) {
							Teacher teacher2 = (Teacher) o2;

							// TODO - fix for other types
							String id1 = teacher1.getId();
							String id2 = teacher2.getId();
							try {
								int id1Int = Integer.parseInt(id1);
								int id2Int = Integer.parseInt(id2);
								if (id1Int > id2Int) {
									return 1;
								} else if (id1Int < id2Int) {
									return -1;
								} else {
									return 0;
								}
							} catch (Exception e) {
								return 0;
							}
						}
					}
					return 0;
				}
			});

			// for every teacher
			for (Teacher teacher : teachersList) {
				Set<TeachingSubject> teachingSubjects = dataProvider.getTeachingSubjects(teacher)
						.getTeachingSubjects();

				// and his teaching subjects
				for (TeachingSubject teachingSubject : teachingSubjects) {
					Set<SchoolClass> teachingSchoolClasses = teachingSubject.getSchoolClasses();

					// in his teaching classes
					for (SchoolClass teachingSchoolClass : teachingSchoolClasses) {
						if (classId.equalsIgnoreCase(teachingSchoolClass.getId())) {
							// if he teaches the current class, add him with his
							// subject
							AssignmentValue assignmentValue = new AssignmentValue(teacher,
									teachingSubject.getSubject());
							possibleAssignmentSet.add(assignmentValue);
						}
					}
				}
			}

			possibleAssignmentsList.add(possibleAssignmentSet);
			classIndex++;
		}

		possibleAssignments = possibleAssignmentsList.toArray(new LinkedList[0]);
	}

	public List<AssignmentValue>[] getPossibleAssignments() {
		return possibleAssignments;
	}

	public List<ScheduleTableRow> getAssignmentRows() {
		return Collections.unmodifiableList(assignmentRows);
	}

	public void setAssignmentRows(List<ScheduleTableRow> assignmentRows) {
		this.assignmentRows = assignmentRows;
	}

	/**
	 * @param colIndex
	 * @param rowIndex
	 * @return the deleted assignment value or <code>null</code> if there was no
	 *         AV at the given position
	 * @throws SchoolDataProviderException
	 */
	public AssignmentValue deleteAssignmentValue(int colIndex, int rowIndex)
			throws SchoolDataProviderException {
		ScheduleTableRow scheduleTableRow = assignmentRows.get(rowIndex);
		if (scheduleTableRow instanceof AssignmentRow) {
			AssignmentRow assignmentRow = (AssignmentRow) scheduleTableRow;

			AssignmentSlot assignmentSlot = createNewAssignmentSlot(colIndex, rowIndex);
			dataProvider.removeAssignment(assignmentSlot, toSynchronize);

			AssignmentValue oldAssignment = assignmentRow.getAssignment(colIndex);
			if (oldAssignment != null) {
				LOG.info("Deleting assignment on " + colIndex + "/" + rowIndex + ": "
						+ oldAssignment);
				assignmentRow.setAssignment(null, colIndex);
				return oldAssignment;
			}
		}
		return null;
	}

	public void setAssignmentValue(int colIndex, int rowIndex, AssignmentValue assignmentValue)
			throws SchoolDataProviderException {
		ScheduleTableRow scheduleTableRow = assignmentRows.get(rowIndex);
		if (scheduleTableRow instanceof AssignmentRow) {
			AssignmentRow assignmentRow = (AssignmentRow) scheduleTableRow;

			// add assignment in data provider
			Assignment assignment = createNewAssignment(colIndex, rowIndex, assignmentValue);
			dataProvider.addAssignment(assignment, toSynchronize);

			// add assignment here locally
			AssignmentValue oldAssignment = assignmentRow.getAssignment(colIndex);
			if (oldAssignment != null) {
				LOG.info("Replacing assignment on " + colIndex + "/" + rowIndex + ": "
						+ oldAssignment);
			}
			assignmentRow.setAssignment(assignmentValue, colIndex);

			LOG.info("Adding assignment on " + colIndex + "/" + rowIndex + ": " + assignmentValue);
		}
	}

	private Assignment createNewAssignment(int colIndex, int rowIndex,
			AssignmentValue assignmentValue) {
		AssignmentSlot assignmentSlot = createNewAssignmentSlot(colIndex, rowIndex);
		Assignment assignment = new Assignment(assignmentSlot, assignmentValue);
		return assignment;
	}

	private AssignmentSlot createNewAssignmentSlot(int colIndex, int rowIndex) {
		int day = getDay(rowIndex);
		int timeSlot = getTimeSlot(rowIndex);
		AssignmentSlot assignmentSlot = new AssignmentSlot(day, timeSlot, classIds[colIndex]);
		return assignmentSlot;
	}

	public int getDay(int rowIndex) {
		return rowIndex / numberOfRowsPerDay + 1;
	}

	public int getTimeSlot(int rowIndex) {
		return rowIndex % numberOfRowsPerDay + 1;
	}

	public void addLastSelectedAssignmentValue(AssignmentValue assignmentValue) {
		addToLastValues(lastSelectedAssignmentValues, maxLastSelectedValues, assignmentValue);
	}

	public void addLastDeletedAssignmentValue(AssignmentValue assignmentValue) {
		addToLastValues(lastDeletedAssignmentValues, maxLastDeletedValues, assignmentValue);
	}

	private void addToLastValues(List<AssignmentValue> list, int maxListSize,
			AssignmentValue assignmentValue) {
		list.remove(assignmentValue);
		list.add(0, assignmentValue);

		if (list.size() > maxListSize) {
			for (int index = maxListSize; index < list.size(); index++) {
				list.remove(index);
			}
		}
	}

	public List<AssignmentValue> getLastSelectedAssignmentValues() {
		return Collections.unmodifiableList(lastSelectedAssignmentValues);
	}

	public List<AssignmentValue> getLastDeletedAssignmentValues() {
		return Collections.unmodifiableList(lastDeletedAssignmentValues);
	}

	public void setLastDeletedAssignmentValues(List<AssignmentValue> lastDeletedAssignmentValues) {
		this.lastDeletedAssignmentValues = lastDeletedAssignmentValues;
	}

	public int getMaxLastSelectedValues() {
		return maxLastSelectedValues;
	}

	public void setMaxLastSelectedValues(int maxLastSelectedValues) {
		this.maxLastSelectedValues = maxLastSelectedValues;
	}

	public int getMaxLastDeletedValues() {
		return maxLastDeletedValues;
	}

	public void setMaxLastDeletedValues(int maxLastDeletedValues) {
		this.maxLastDeletedValues = maxLastDeletedValues;
	}
}
