package org.posithing.ssd.model;

import org.posithing.ssd.utils.StringUtil;

/**
 * Represents an assignment in a time slot, corresponding to the day, day slot
 * and school class.
 */
public class AssignmentSlot {

	private int day;
	private int timeSlot;
	private String classId;

	/**
	 * Creates a new assignment with the given day, slot and classId.
	 * 
	 * @param day
	 *            day (e.g. 1 to 5)
	 * @param timeSlot
	 *            (e.g. 1 to 7)
	 * @param classId
	 *            the class Id
	 */
	public AssignmentSlot(int day, int timeSlot, String classId) {
		this.day = day;
		this.timeSlot = timeSlot;
		this.classId = classId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + (classId == null ? 0 : classId.hashCode());
		hash = hash * 31 + day;
		hash = hash * 31 + timeSlot;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AssignmentSlot))
			return false;
		AssignmentSlot aAssignmentSlot = (AssignmentSlot) obj;
		return (day == aAssignmentSlot.getDay() && timeSlot == aAssignmentSlot.getTimeSlot() && ((classId == null) ? aAssignmentSlot
				.getClassId() == null
				: classId.equals(aAssignmentSlot.getClassId())));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(day);
		sb.append(StringUtil.LINE);
		sb.append(timeSlot);
		sb.append(StringUtil.LINE);
		sb.append(classId);

		return sb.toString();
	}
}
