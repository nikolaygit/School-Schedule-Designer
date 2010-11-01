package org.posithing.ssd.ui.common;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;


public class DefaultSorter extends SelectionAdapter {

	private AbstractViewerSorter sorter;
	private TableViewer viewer;
	private TableColumn column;
	private int index;

	public DefaultSorter(AbstractViewerSorter sorter, TableViewer viewer, TableColumn column,
			int index) {
		this.sorter = sorter;
		this.viewer = viewer;
		this.column = column;
		this.index = index;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		sorter.setColumn(index);
		int dir = viewer.getTable().getSortDirection();
		if (viewer.getTable().getSortColumn() == column) {
			dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
		} else {
			dir = SWT.DOWN;
		}
		viewer.getTable().setSortDirection(dir);
		viewer.getTable().setSortColumn(column);
		viewer.refresh();
	}
}
