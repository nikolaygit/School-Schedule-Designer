package org.posithing.ssd.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.RGB;
import org.posithing.ssd.utils.StringUtil;


public class Teacher implements PropertyChangeListener, Comparable<Teacher> {

	private String id;
	private String firstName;
	private String lastName;
	private RGB color;
	private String shape;

	public Teacher(String id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getWholeName() {
		StringBuilder sb = new StringBuilder();

		sb.append(firstName);
		sb.append(StringUtil.SPACE);
		sb.append(lastName);

		return sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO propertyChange
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + id.hashCode();
		hash = hash * 31 + (firstName == null ? 0 : firstName.hashCode());
		hash = hash * 31 + (lastName == null ? 0 : lastName.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Teacher))
			return false;
		Teacher teacher = (Teacher) obj;
		return (id.equals(teacher.getId())
				&& ((firstName == null) ? teacher.getFirstName() == null : firstName.equals(teacher
						.getFirstName())) && ((lastName == null) ? teacher.getLastName() == null
				: lastName.equals(teacher.getLastName())));
	}

	@Override
	public int compareTo(Teacher o) {
		int compareTo = getFirstName().compareTo(o.getFirstName());
		if (compareTo == 0) {
			return getLastName().compareTo(o.getLastName());
		}
		return compareTo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(firstName);
		sb.append(StringUtil.SPACE);
		sb.append(lastName);
		sb.append(StringUtil.SPACE);
		sb.append(StringUtil.OPENED_BRACKET_SQUARE);
		sb.append(id);
		RGB color = getColor();
		if (color != null) {
			sb.append(StringUtil.COMMA);
			sb.append(StringUtil.SPACE);
			sb.append(color.toString());
		}
		if (shape != null) {
			sb.append(StringUtil.COMMA);
			sb.append(StringUtil.SPACE);
			sb.append(shape);
		}
		sb.append(StringUtil.CLOSED_BRACKET_SQUARE);

		return sb.toString();
	}

	public String toShortString() {
		StringBuilder sb = new StringBuilder();

		sb.append(firstName);
		sb.append(StringUtil.SPACE);
		sb.append(lastName);
		sb.append(StringUtil.SPACE);
		sb.append(StringUtil.OPENED_BRACKET_SQUARE);
		sb.append(id);
		sb.append(StringUtil.CLOSED_BRACKET_SQUARE);

		return sb.toString();
	}
}
