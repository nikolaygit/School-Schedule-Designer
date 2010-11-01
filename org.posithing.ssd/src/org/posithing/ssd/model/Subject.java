package org.posithing.ssd.model;

import org.posithing.ssd.utils.StringUtil;

/**
 * A subject taught in school. E.g. "Mathematics".
 */
public class Subject implements Comparable<Subject> {

	private String id;
	private String name;

	/**
	 * Construct a new subject with the given id and name
	 * 
	 * @param id
	 *            id of the subject
	 * @param name
	 *            visible name of the subject
	 */
	public Subject(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id of the subject
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name of the subject
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the new name of the subject
	 * 
	 * @param name
	 *            new subject name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Subject aSubject) {
		return getName().compareTo(aSubject.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Subject))
			return false;
		Subject aSubject = (Subject) obj;
		return (id.equalsIgnoreCase(aSubject.getId()))
				&& ((name == null) ? aSubject.getName() == null : name.equals(aSubject.getName()));
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + id.hashCode();
		hash = hash * 31 + (name == null ? 0 : name.hashCode());
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(name);
		sb.append(StringUtil.SPACE);
		sb.append(StringUtil.OPENED_BRACKET_SQUARE);
		sb.append(id);
		sb.append(StringUtil.CLOSED_BRACKET_SQUARE);

		return sb.toString();
	}
}
