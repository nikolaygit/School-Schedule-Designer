package snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/** custom rendering */
public class TableExample3 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		final Table table = new Table(shell, SWT.BORDER);
		table.setLinesVisible(true);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("[header 1]");
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("[header 2]");
		for (int i = 0; i < 30; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "item " + i);
		}
		column1.pack();
		column2.setWidth(300);
		TableItem[] items = table.getItems();
		final Color blue = display.getSystemColor(SWT.COLOR_DARK_BLUE);
		final Color white = display.getSystemColor(SWT.COLOR_WHITE);
		for (int i = 0; i < items.length; i++) {
			TableItem tableItem = items[i];
			TableEditor editor = new TableEditor(table);
			editor.grabHorizontal = true;
			editor.grabVertical = true;
			final Canvas canvas = new Canvas(table, SWT.NONE);
			canvas.setData("EXAMPLE DATA", new Integer(i * 100 / items.length));
			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					Rectangle area = canvas.getClientArea();
					Integer data = (Integer) canvas.getData("EXAMPLE DATA");
					if (data == null)
						return;
					e.gc.setBackground(table.getBackground());
					e.gc.fillRectangle(area.x, area.y, area.width, area.height);
					e.gc.setBackground(blue);
					e.gc.setForeground(white);
					e.gc.fillGradientRectangle(area.x, area.y, (int) (data.doubleValue()
							* area.width / 100.0), area.height, false);
				}
			});
			editor.setEditor(canvas, tableItem, 1);
		}
		table.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}