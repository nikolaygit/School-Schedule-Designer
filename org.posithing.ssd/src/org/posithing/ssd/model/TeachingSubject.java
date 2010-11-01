package org.posithing.ssd.model;

import java.util.Set;
import java.util.TreeSet;

import org.posithing.ssd.utils.StringUtil;


public class TeachingSubject implements Comparable<TeachingSubject> {

	private Subject subject;
	private Set<SchoolClass> schoolClasses;

	/**
	 * Creates a new teaching subject.
	 * 
	 * @param subject
	 *            subject
	 * @param schoolClasses
	 *            can be <code>null</code> for an empty set
	 */
	public TeachingSubject(Subject subject, Set<SchoolClass> schoolClasses) {
		this.subject = subject;
		if (schoolClasses == null) {
			schoolClasses = new TreeSet<SchoolClass>();
		} else {
			this.schoolClasses = schoolClasses;
		}
	}

	public void addSchoolClass(SchoolClass schoolClass) {
		schoolClasses.add(schoolClass);
	}

	public void addSchoolClasses(Set<SchoolClass> classes) {
		for (SchoolClass schoolClass : classes) {
			classes.add(schoolClass);
		}
	}

	public void removeSchoolClass(SchoolClass schoolClass) {
		schoolClasses.remove(schoolClass);
	}

	@Override
	public int compareTo(TeachingSubject o) {
		int compareTo = subject.compareTo(o.getSubject());
		if (compareTo != 0) {
			return compareTo;
		} else {
			Set<SchoolClass> oSchoolClasses = o.getSchoolClasses();
			boolean biggerSize = schoolClasses.size() > oSchoolClasses.size();
			if (biggerSize) {
				return 1;
			} else if (schoolClasses.size() < oSchoolClasses.size()) {
				return -1;
			}
			return 0;
		}
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Set<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}

	public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getSubject().getName());
		sb.append(StringUtil.OPENED_BRACKET_CURLY_WITH_SPACE);
		for (SchoolClass schoolClass : getSchoolClasses()) {
			sb.append(schoolClass.getName());
			sb.append(StringUtil.COMMA);
			sb.append(StringUtil.SPACE);
		}
		sb.delete(sb.length() - 2, sb.length()); // last comma
		sb.append(StringUtil.CLOSED_BRACKET_CURLY);

		return sb.toString();
	}

}
