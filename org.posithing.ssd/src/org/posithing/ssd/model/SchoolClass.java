package org.posithing.ssd.model;

import org.posithing.ssd.utils.StringUtil;

/**
 * A school class. It can have a name and a grade.
 */
public class SchoolClass implements Comparable<SchoolClass> {

	private String id;
	private String name;
	private int grade;

	/**
	 * Creates a new school class. The class group
	 * 
	 * @param id
	 * @param name
	 * @param grade
	 */
	public SchoolClass(String id, String name, int grade) {
		this.id = id;
		this.name = name;
		this.grade = grade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public int compareTo(SchoolClass o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SchoolClass))
			return false;
		SchoolClass aSchoolClass = (SchoolClass) obj;
		return ((id == null) ? aSchoolClass.getId() == null : id.equals(aSchoolClass.getId()))
				&& ((name == null) ? aSchoolClass.getName() == null : name.equals(aSchoolClass
						.getName())) && grade == aSchoolClass.getGrade();
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + id.hashCode();
		hash = hash * 31 + (name == null ? 0 : name.hashCode());
		hash = hash * 31 + grade;
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(name);
		sb.append(StringUtil.SPACE);
		sb.append(StringUtil.AT);
		sb.append(StringUtil.SPACE);
		sb.append(grade);
		sb.append(StringUtil.SPACE);
		sb.append(StringUtil.OPENED_BRACKET_SQUARE);
		sb.append(id);
		sb.append(StringUtil.CLOSED_BRACKET_SQUARE);

		return sb.toString();
	}
}
