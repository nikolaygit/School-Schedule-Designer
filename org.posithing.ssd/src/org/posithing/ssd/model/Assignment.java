package org.posithing.ssd.model;

/**
 * An assignment in the schedule. Contains an assignment slot and value.
 * 
 * @author Nikolay Georgiev
 */
public class Assignment {

	private AssignmentSlot slot;
	private AssignmentValue value;

	public Assignment(AssignmentSlot slot, AssignmentValue value) {
		this.slot = slot;
		this.value = value;
	}

	public AssignmentSlot getSlot() {
		return slot;
	}

	public void setSlot(AssignmentSlot slot) {
		this.slot = slot;
	}

	public AssignmentValue getValue() {
		return value;
	}

	public void setValue(AssignmentValue value) {
		this.value = value;
	}

}
