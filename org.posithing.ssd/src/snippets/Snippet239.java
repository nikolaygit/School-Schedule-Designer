package snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Snippet239 {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Text spans two columns in a TableItem");
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		int columnCount = 4;
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column " + i);
		}
		int itemCount = 8;
		for (int i = 0; i < itemCount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "item " + i + " a");
			item.setText(3, "item " + i + " d");
		}
		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be as
		 * efficient as possible.
		 */
		final String string = "text that spans two columns";
		GC gc = new GC(table);
		final Point extent = gc.stringExtent(string);
		gc.dispose();
		final Color red = display.getSystemColor(SWT.COLOR_RED);
		Listener paintListener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
					case SWT.MeasureItem: {
						if (event.index == 1 || event.index == 2) {
							event.width = extent.x / 2;
							event.height = Math.max(event.height, extent.y + 2);
						}
						break;
					}
					case SWT.PaintItem: {
						if (event.index == 1 || event.index == 2) {
							int offset = 0;
							if (event.index == 2) {
								TableColumn column1 = table.getColumn(1);
								offset = column1.getWidth();
							}
							event.gc.setForeground(red);
							int y = event.y + (event.height - extent.y) / 2;
							event.gc.drawString(string, event.x - offset, y, true);
						}
						break;
					}
				}
			}
		};
		table.addListener(SWT.MeasureItem, paintListener);
		table.addListener(SWT.PaintItem, paintListener);
		for (int i = 0; i < columnCount; i++) {
			table.getColumn(i).pack();
		}
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}