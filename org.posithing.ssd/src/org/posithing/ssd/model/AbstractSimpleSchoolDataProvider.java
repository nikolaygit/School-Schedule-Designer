package org.posithing.ssd.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.posithing.ssd.utils.StringUtil;


/**
 * A school data provider which synchronizes <strong>all</strong> the data, when
 * a synchronization is requested.
 * 
 */
public abstract class AbstractSimpleSchoolDataProvider implements SchoolDataProvider {

	private Set<Teacher> teachers;
	private Set<Subject> subjects;
	private Set<SchoolClass> schoolClasses;
	private Map<String, String> options;

	private Map<Teacher, TeachingSubjects> teacherSubjects;
	private Map<AssignmentSlot, AssignmentValue> assignments;

	private Map<String, Teacher> teachersMap;
	private Map<String, Subject> subjectsMap;

	private boolean isSynchronized;

	public AbstractSimpleSchoolDataProvider() {
		teachers = new TreeSet<Teacher>();
		subjects = new TreeSet<Subject>();
		schoolClasses = new TreeSet<SchoolClass>();
		teacherSubjects = new HashMap<Teacher, TeachingSubjects>();
		assignments = new HashMap<AssignmentSlot, AssignmentValue>();

		teachersMap = new HashMap<String, Teacher>();
		subjectsMap = new HashMap<String, Subject>();

		isSynchronized = false;
	}

	@Override
	public Set<Teacher> getTeachers() throws SchoolDataProviderException {
		return Collections.unmodifiableSet(teachers);
	}

	@Override
	public Teacher getTeacher(String id) throws SchoolDataProviderException {
		return teachersMap.get(id);
	}

	@Override
	public Set<Subject> getSubjects() throws SchoolDataProviderException {
		return Collections.unmodifiableSet(subjects);
	}

	@Override
	public Subject getSubject(String id) throws SchoolDataProviderException {
		return subjectsMap.get(id);
	}

	@Override
	public Set<SchoolClass> getSchoolClasses() throws SchoolDataProviderException {
		return Collections.unmodifiableSet(schoolClasses);
	}

	/**
	 * The school class for the given class ID or <code>null</code> if not
	 * found.
	 * 
	 * @param classId
	 * @return
	 * @throws SchoolDataProviderException
	 */
	public SchoolClass getSchoolClass(String classId) throws SchoolDataProviderException {
		for (SchoolClass schoolClass : schoolClasses) {
			if (schoolClass.getId().equals(classId)) {
				return schoolClass;
			}
		}

		return null;
	}

	@Override
	public Map<String, String> getOptions() throws SchoolDataProviderException {
		return Collections.unmodifiableMap(options);
	}

	@Override
	public String getOption(String name) throws SchoolDataProviderException {
		return options.get(name);
	}

	@Override
	public Map<Teacher, TeachingSubjects> getTeacherSubjects() {
		return Collections.unmodifiableMap(teacherSubjects);
	}

	@Override
	public TeachingSubjects getTeachingSubjects(Teacher teacher) throws SchoolDataProviderException {
		return teacherSubjects.get(teacher);
	}

	@Override
	public List<Assignment> getAssignments() throws SchoolDataProviderException {

		Set<AssignmentSlot> slotsSet = assignments.keySet();
		List<Assignment> assignmentsList = new LinkedList<Assignment>();

		for (AssignmentSlot assignmentSlot : slotsSet) {
			AssignmentValue assignmentValue = assignments.get(assignmentSlot);
			Assignment assignment = new Assignment(assignmentSlot, assignmentValue);
			assignmentsList.add(assignment);
		}

		return Collections.unmodifiableList(assignmentsList);
	}

	@Override
	public void addTeacher(Teacher teacher, boolean synchronize) throws SchoolDataProviderException {
		teachers.add(teacher);
		teachersMap.put(teacher.getId(), teacher);
		sync(synchronize);
	}

	@Override
	public void addSubject(Subject subject, boolean synchronize) throws SchoolDataProviderException {
		subjects.add(subject);
		subjectsMap.put(subject.getId(), subject);
		sync(synchronize);
	}

	@Override
	public void addSchoolClass(SchoolClass schoolClass, boolean synchronize)
			throws SchoolDataProviderException {
		schoolClasses.add(schoolClass);
		sync(synchronize);
	}

	@Override
	public void addOption(String name, String value, boolean synchronize)
			throws SchoolDataProviderException {
		options.put(name, value);
		sync(synchronize);
	}

	@Override
	public void addTeacherSubjects(Teacher teacher, TeachingSubjects teachingSubjects,
			boolean synchronize) throws SchoolDataProviderException {
		TeachingSubjects currentTeachingSubjects = teacherSubjects.get(teacher);
		if (currentTeachingSubjects == null) {
			teacherSubjects.put(teacher, teachingSubjects);
		} else {
			currentTeachingSubjects.merge(teachingSubjects);
		}

		sync(synchronize);
	}

	@Override
	public void addAssignment(Assignment assignment, boolean synchronize)
			throws SchoolDataProviderException {
		assignments.put(assignment.getSlot(), assignment.getValue());
		sync(synchronize);
	}

	@Override
	public void removeTeacher(Teacher teacher, boolean synchronize)
			throws SchoolDataProviderException {
		teachers.remove(teacher);
		teachersMap.remove(teacher.getId());
		teacherSubjects.remove(teacher);
		sync(synchronize);
	}

	@Override
	public void removeSubject(Subject subject, boolean synchronize)
			throws SchoolDataProviderException {
		subjects.remove(subject);
		subjectsMap.remove(subject.getId());
		sync(synchronize);
	}

	@Override
	public void removeSchoolClass(SchoolClass schoolClass, boolean synchronize)
			throws SchoolDataProviderException {
		schoolClasses.remove(schoolClass);
		sync(synchronize);
	}

	@Override
	public void removeOption(String name, boolean synchronize) throws SchoolDataProviderException {
		options.remove(name);
		sync(synchronize);
	}

	@Override
	public void removeTeacherSubjects(Teacher teacher, TeachingSubjects teachingSubjects,
			boolean synchronize) throws SchoolDataProviderException {
		TeachingSubjects currentTeachingSubjects = teacherSubjects.get(teacher);
		if (currentTeachingSubjects != null) {

			currentTeachingSubjects.remove(teachingSubjects);
			sync(synchronize);
		}
	}

	@Override
	public void removeAssignment(AssignmentSlot assignmentSlot, boolean synchronize)
			throws SchoolDataProviderException {
		assignments.remove(assignmentSlot);
		sync(synchronize);
	}

	/**
	 * Not doing anything
	 */
	@Override
	public void updateTeacher(Teacher teacher, boolean synchronize)
			throws SchoolDataProviderException {
		// teachers.remove(teacher);
		// teachers.add(teacher);
		System.out.println(teacher);
		sync(synchronize);
	}

	/**
	 * Not doing anything
	 */
	@Override
	public void updateSubject(Subject subject, boolean synchronize)
			throws SchoolDataProviderException {
		System.out.println(subject);
		sync(synchronize);
	}

	/**
	 * Not doing anything
	 */
	@Override
	public void updateSchoolClass(SchoolClass schoolClass, boolean synchronize)
			throws SchoolDataProviderException {
		System.out.println(schoolClass);
		sync(synchronize);
	}

	@Override
	public void updateOption(String name, String value, boolean synchronize)
			throws SchoolDataProviderException {
		options.put(name, value);
		sync(synchronize);
	}

	@Override
	public void updateTeacherSubjects(Teacher teacher, TeachingSubjects teachingSubjects,
			boolean synchronize) throws SchoolDataProviderException {
		teacherSubjects.put(teacher, teachingSubjects);
		sync(synchronize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.posithing.ssd.model.IdentifiableExtension#onClose()
	 */
	@Override
	public void onClose() {
	}

	private void sync(boolean synchronize) throws SchoolDataProviderException {
		setSynchronized(false); // may be moved from here -> new method change
		if (synchronize) {
			try {
				synchronize();
				setSynchronized(true);
			} catch (Exception e) {
				throw new SchoolDataProviderSynchronizationException(
						Messages.AbstractSchoolDataProvider_dataNotSynchronized, e);
			}
		}
	}

	@Override
	public boolean isSynchronized() throws SchoolDataProviderException {
		return isSynchronized;
	}

	public void setSynchronized(boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractSimpleSchoolDataProvider) {
			AbstractSimpleSchoolDataProvider another = (AbstractSimpleSchoolDataProvider) obj;
			return getId().equals(another.getId());
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(StringUtil.OPENED_BRACKET_SQUARE_WITH_SPACE);
		sb.append(getId());
		sb.append(StringUtil.CLOSED_BRACKET_SQUARE);

		return sb.toString();
	}
}
