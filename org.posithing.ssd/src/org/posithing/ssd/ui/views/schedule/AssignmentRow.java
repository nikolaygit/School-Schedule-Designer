package org.posithing.ssd.ui.views.schedule;

import org.posithing.ssd.model.AssignmentValue;

/**
 * Represents one assignment row in the schedule table.
 */
public class AssignmentRow implements ScheduleTableRow {

	private int day;
	private int daySlot;
	private AssignmentValue[] assignments;

	public AssignmentRow(int day, int daySlot, int numberOfAssignments) {
		this.day = day;
		this.daySlot = daySlot;
		assignments = new AssignmentValue[numberOfAssignments];
	}

	public int getSize() {
		return assignments.length;
	}

	public AssignmentValue getAssignment(int index) {
		return assignments[index];
	}

	public void setAssignment(AssignmentValue assignment, int index) {
		assignments[index] = assignment;
	}

	public AssignmentValue[] getAssignments() {
		return assignments;
	}

	public void setAssignments(AssignmentValue[] assignments) {
		this.assignments = assignments;
	}

	public int getDay() {
		return day;
	}

	public int getDaySlot() {
		return daySlot;
	}

}
