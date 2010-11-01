package org.posithing.ssd.ui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public abstract class AbstractTableItemTooltipListener implements Listener {

	private static String TABLEITEM = "_TABLEITEM";

	private Table table;
	private Display display;
	private Shell shell;

	private Shell tip = null;
	private Label label = null;

	private Listener labelListener = new Listener() {
		public void handleEvent(Event event) {
			Label label = (Label) event.widget;
			Shell shell = label.getShell();
			switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData(AbstractTableItemTooltipListener.TABLEITEM);
					// Assuming table is single select, set the selection as
					// if
					// the mouse down event went through to the table
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					// fall through
				case SWT.MouseExit:
					shell.dispose();
					break;
			}
		}
	};

	public AbstractTableItemTooltipListener(Table table, Display display) {
		this.table = table;
		this.display = display;
		this.shell = display.getActiveShell();
	}

	public void handleEvent(Event event) {
		switch (event.type) {
			case SWT.Dispose:
			case SWT.KeyDown:
			case SWT.MouseMove: {
				if (tip == null)
					break;
				tip.dispose();
				tip = null;
				label = null;
				break;
			}
			case SWT.MouseHover: {
				TableItem item = table.getItem(new Point(event.x, event.y));
				if (item != null) {
					if (tip != null && !tip.isDisposed())
						tip.dispose();
					tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
					tip.setLayout(new FillLayout());
					label = new Label(tip, SWT.NONE);
					label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
					label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
					label.setData(AbstractTableItemTooltipListener.TABLEITEM, item);
					label.setText(getTooltip(item));
					label.addListener(SWT.MouseExit, labelListener);
					label.addListener(SWT.MouseDown, labelListener);
					Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					Rectangle rect = item.getBounds(0);
					Point pt = table.toDisplay(rect.x, rect.y);
					tip.setBounds(pt.x + 20, pt.y - 7, size.x, size.y);
					tip.setVisible(true);
				}
			}
		}
	}

	/**
	 * Return the tooltip string for the given table item.
	 * 
	 * @param item
	 *            TableItem
	 * @return the tooltip string to be displayed
	 */
	protected abstract String getTooltip(TableItem item);

}
