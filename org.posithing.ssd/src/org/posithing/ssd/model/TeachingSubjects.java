package org.posithing.ssd.model;

import java.util.Set;
import java.util.TreeSet;

import org.posithing.ssd.utils.StringUtil;


public class TeachingSubjects {

	// subject to set of school classes
	private Set<TeachingSubject> teachingSubjects;

	public TeachingSubjects() {
		teachingSubjects = new TreeSet<TeachingSubject>();
	}

	public void addTeachingSubject(TeachingSubject teachingSubject) {
		teachingSubjects.add(teachingSubject);
	}

	public void removeTeachingSubject(TeachingSubject teachingSubject) {
		teachingSubjects.remove(teachingSubject);
	}

	/**
	 * Adds a subject with an empty set of school classes (if not already
	 * existing).
	 * 
	 * @param subject
	 */
	public void addSubject(Subject subject) {
		TeachingSubject teachingSubject = contains(subject);
		if (teachingSubject == null) {
			teachingSubject = new TeachingSubject(subject, null);
			teachingSubjects.add(teachingSubject);
		}
	}

	/**
	 * Adds the subject for the given class.
	 * 
	 * @param subject
	 * @param schoolClass
	 */
	public void addSubject(Subject subject, SchoolClass schoolClass) {
		TeachingSubject teachingSubject = contains(subject);
		if (teachingSubject == null) {
			Set<SchoolClass> schoolClasses = new TreeSet<SchoolClass>();
			schoolClasses.add(schoolClass);
			teachingSubject = new TeachingSubject(subject, schoolClasses);
			teachingSubjects.add(teachingSubject);
		} else {
			teachingSubject.addSchoolClass(schoolClass);
		}
	}

	/**
	 * Adds the subject for the given class IDs.
	 * 
	 * @param subject
	 * @param schoolClasses
	 */
	public void addSubject(Subject subject, Set<SchoolClass> schoolClasses) {
		TeachingSubject teachingSubject = contains(subject);
		if (teachingSubject == null) {
			teachingSubject = new TeachingSubject(subject, schoolClasses);
			teachingSubjects.add(teachingSubject);
		} else {
			teachingSubject.addSchoolClasses(schoolClasses);
		}
	}

	/**
	 * Removes the subject for all school classes.
	 * 
	 * @param subject
	 */
	public void removeSubject(Subject subject) {
		TeachingSubject teachingSubject = contains(subject);
		if (teachingSubject != null) {
			removeTeachingSubject(teachingSubject);
		}
	}

	/**
	 * Merges the given teaching subjects with the already contained ones.
	 * 
	 * @param anotherTeachingSubjects
	 */
	public void merge(TeachingSubjects anotherTeachingSubjects) {

		Set<TeachingSubject> aTeachingSubjects = anotherTeachingSubjects.getTeachingSubjects();
		for (TeachingSubject aTeachingSubject : aTeachingSubjects) {

			boolean aSubjectFound = false;
			for (TeachingSubject teachingSubject : teachingSubjects) {

				if (teachingSubject.getSubject().equals(aTeachingSubject.getSubject())) {
					aSubjectFound = true;

					// add all other classes to the subject
					teachingSubject.addSchoolClasses(aTeachingSubject.getSchoolClasses());
				}
			}

			if (!aSubjectFound) {
				teachingSubjects.add(aTeachingSubject);
			}
		}
	}

	/**
	 * Merges the given teaching subjects with the already contained ones.
	 * 
	 * @param anotherTeachingSubjects
	 */
	public void remove(TeachingSubjects anotherTeachingSubjects) {
		Set<TeachingSubject> aTeachingSubjects = anotherTeachingSubjects.getTeachingSubjects();
		for (TeachingSubject aTeachingSubject : aTeachingSubjects) {
			for (TeachingSubject teachingSubject : teachingSubjects) {

				if (teachingSubject.getSubject().equals(aTeachingSubject.getSubject())) {
					// remove the found teaching subject
					teachingSubjects.remove(teachingSubject);
				}
			}
		}
	}

	public TeachingSubject contains(Subject subject) {
		for (TeachingSubject teachingSubject : teachingSubjects) {
			if (teachingSubject.getSubject().equals(subject)) {
				return teachingSubject;
			}
		}
		return null;
	}

	/**
	 * Returns the teaching subjects with their class IDs as a map.
	 * 
	 * @return a map of subjectId to set of classIds
	 */
	public Set<TeachingSubject> getTeachingSubjects() {
		return teachingSubjects;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		boolean teachingSubjectExitsts = false;
		for (TeachingSubject teachingSubject : teachingSubjects) {
			teachingSubjectExitsts = true;
			sb.append(teachingSubject.toString());
			sb.append(StringUtil.COMMA);
			sb.append(StringUtil.SPACE);
		}
		if (teachingSubjectExitsts) {
			sb.delete(sb.length() - 2, sb.length()); // last comma
		}

		return sb.toString();
	}
}
