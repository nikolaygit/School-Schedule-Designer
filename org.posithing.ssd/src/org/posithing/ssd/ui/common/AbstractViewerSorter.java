package org.posithing.ssd.ui.common;

import org.eclipse.jface.viewers.ViewerSorter;

public class AbstractViewerSorter extends ViewerSorter {
	protected int propertyIndex;
	protected static final int ASCENDING = 0;
	protected static final int DESCENDING = 1;

	protected int direction = DESCENDING;

	/**
	 * A sorter which sorts at the beginning column with index 0.
	 */
	public AbstractViewerSorter() {
		this.propertyIndex = 0;
		direction = ASCENDING;
	}

	/**
	 * A sorter with starting sorted property index.
	 * 
	 * @param propertyIndex
	 *            which column to sort at the beginning.
	 */
	public AbstractViewerSorter(int propertyIndex) {
		this.propertyIndex = propertyIndex;
		direction = ASCENDING;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = ASCENDING;
		}
	}
}
