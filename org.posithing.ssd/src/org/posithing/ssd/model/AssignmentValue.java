package org.posithing.ssd.model;

import org.posithing.ssd.utils.StringUtil;

/**
 * Represents an assignment value, corresponding to the teacher and subject.
 */
public class AssignmentValue implements Comparable<AssignmentValue> {

	private Teacher teacher;
	private Subject subject;

	public AssignmentValue(Teacher teacher, Subject subject) {
		this.teacher = teacher;
		this.subject = subject;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public int compareTo(AssignmentValue o) {
		int compareTo = teacher.compareTo(o.getTeacher());
		if (compareTo == 0) {
			return subject.compareTo(o.getSubject());
		} else {
			return compareTo;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AssignmentValue))
			return false;
		AssignmentValue aValue = (AssignmentValue) obj;
		return ((teacher == null) ? aValue.getTeacher() == null : teacher.equals(aValue
				.getTeacher()))
				&& ((subject == null) ? aValue.getSubject() == null : subject.equals(aValue
						.getSubject()));
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + (teacher == null ? 0 : teacher.hashCode());
		hash = hash * 31 + (subject == null ? 0 : teacher.hashCode());
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(teacher.getId());
		sb.append(StringUtil.LINE);
		sb.append(subject.getId());

		return sb.toString();
	}
}
