package org.posithing.ssd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides data needed for the school schedule.
 */
public interface SchoolDataProvider extends IdentifiableExtension {

	/**
	 * Initializes the data provider
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void initialize() throws SchoolDataProviderException;

	/**
	 * Return list of the teachers for the schedule
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public Set<Teacher> getTeachers() throws SchoolDataProviderException;

	/**
	 * Returns the teacher with the given id.
	 * 
	 * @param id
	 * @return the teacher or <code>null</code> if not found
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public Teacher getTeacher(String id) throws SchoolDataProviderException;

	/**
	 * Return list of the subjects for the schedule
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public Set<Subject> getSubjects() throws SchoolDataProviderException;

	/**
	 * Returns the subject with the given id.
	 * 
	 * @param id
	 *            subject id
	 * @return the subject or <code>null</code> if not found
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public Subject getSubject(String id) throws SchoolDataProviderException;

	/**
	 * Return list of the school classes for the schedule
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public Set<SchoolClass> getSchoolClasses() throws SchoolDataProviderException;

	/**
	 * Returns a map with the options (name and value).
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public Map<String, String> getOptions() throws SchoolDataProviderException;

	/**
	 * Returns the value of the given option.
	 * 
	 * @param name
	 *            option name
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public String getOption(String name) throws SchoolDataProviderException;

	/**
	 * The teacher subjects as a map from teacher to his teaching subjects.
	 * 
	 * @return a map from teacher to his teaching subjects
	 * @throws SchoolDataProviderException
	 */
	public Map<Teacher, TeachingSubjects> getTeacherSubjects() throws SchoolDataProviderException;

	/**
	 * The teaching subjects for the given teacher.
	 * 
	 * @param teacher
	 *            a teacher
	 * @return TeachingSubjects or <code>null</code> if there are no teaching
	 *         subjects defined for the given teacher
	 * @throws SchoolDataProviderException
	 */
	public TeachingSubjects getTeachingSubjects(Teacher teacher) throws SchoolDataProviderException;

	/**
	 * A list with the current assignments
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public List<Assignment> getAssignments() throws SchoolDataProviderException;

	/**
	 * Adds a teacher to the data provider.
	 * 
	 * @param teacher
	 *            the teacher to add
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void addTeacher(Teacher teacher, boolean synchronize) throws SchoolDataProviderException;

	/**
	 * Adds a subject to the data provider.
	 * 
	 * @param subject
	 *            the subject to add
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void addSubject(Subject subject, boolean synchronize) throws SchoolDataProviderException;

	/**
	 * Adds a schoolClass to the data provider.
	 * 
	 * @param schoolClass
	 *            the schoolClass to add
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void addSchoolClass(SchoolClass schoolClass, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Adds an option with the given name and value to the data provider.
	 * 
	 * @param name
	 *            option name
	 * @param value
	 *            option value
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void addOption(String name, String value, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Adds a teaching subject object to the given teacher.
	 * 
	 * @param teacher
	 * @param teachingSubjects
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 */
	public void addTeacherSubjects(Teacher teacher, TeachingSubjects teachingSubjects,
			boolean synchronize) throws SchoolDataProviderException;

	/**
	 * Adds an assignment to the data provider. If another assignment with the
	 * same slot exist, its value should be overridden.
	 * 
	 * @param assignment
	 *            the assignment to be added
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void addAssignment(Assignment assignment, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Removes a teacher from the data provider.
	 * 
	 * @param teacher
	 *            the teacher to remove
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void removeTeacher(Teacher teacher, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Removes a subject from the data provider.
	 * 
	 * @param subject
	 *            the subject to remove
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void removeSubject(Subject subject, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Removes a schoolClass from the data provider.
	 * 
	 * @param schoolClass
	 *            the schoolClass to remove
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void removeSchoolClass(SchoolClass schoolClass, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Removes an option with the given name from the data provider.
	 * 
	 * @param name
	 *            option name
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void removeOption(String name, boolean synchronize) throws SchoolDataProviderException;

	/**
	 * Removes the given teaching subjects from the given teacher.
	 * 
	 * @param teacher
	 * @param teachingSubjects
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 */
	void removeTeacherSubjects(Teacher teacher, TeachingSubjects teachingSubjects,
			boolean synchronize) throws SchoolDataProviderException;

	/**
	 * Removes the assignment from the given slot from the data provider.
	 * 
	 * @param assignmentSlot
	 *            the assignment slot
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void removeAssignment(AssignmentSlot assignmentSlot, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Updates the teacher in the data provider.
	 * 
	 * @param teacher
	 *            the teacher to add
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void updateTeacher(Teacher teacher, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Updates the subject in the data provider.
	 * 
	 * @param subject
	 *            the subject to add
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void updateSubject(Subject subject, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Updates the schoolClass in the data provider.
	 * 
	 * @param schoolClass
	 *            the schoolClass to add
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void updateSchoolClass(SchoolClass schoolClass, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Update the option with the given name and value in the data provider.
	 * 
	 * @param name
	 *            option name
	 * @param value
	 *            option value
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void updateOption(String name, String value, boolean synchronize)
			throws SchoolDataProviderException;

	/**
	 * Updates the teaching subjects for the givne teacher.
	 * 
	 * @param teacher
	 * @param teachingSubjects
	 * @param synchronize
	 *            whether to synchronize to the data source
	 * @throws SchoolDataProviderException
	 */
	void updateTeacherSubjects(Teacher teacher, TeachingSubjects teachingSubjects,
			boolean synchronize) throws SchoolDataProviderException;

	/**
	 * Synchronizes the data to its source. If the synchronization fails, a
	 * {@link SchoolDataProviderException} is thrown.
	 * 
	 * @throws SchoolDataProviderException
	 *             if could not successfully execute the method
	 */
	public void synchronize() throws SchoolDataProviderException;

	/**
	 * Checks whether the data is synchronized with its source.
	 * 
	 * @return
	 * @throws SchoolDataProviderException
	 */
	public boolean isSynchronized() throws SchoolDataProviderException;

}
