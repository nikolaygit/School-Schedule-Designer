package snippets;

/*
 * Table example snippet: place arbitrary controls in a table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Snippet126ArbitryControlsInTable {
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setLinesVisible(true);
		for (int i = 0; i < 4; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setWidth(100);
		}
		for (int i = 0; i < 16; i++) {
			new TableItem(table, SWT.NONE);
		}
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			TableEditor editor = new TableEditor(table);
			CCombo combo = new CCombo(table, SWT.NONE);
			combo.setText("CCombo");
			combo.add("item 1");
			combo.add("item 2");
			editor.grabHorizontal = true;
			editor.setEditor(combo, items[i], 0);

			editor = new TableEditor(table);
			Text text = new Text(table, SWT.NONE);
			text.setText("Text");
			editor.grabHorizontal = true;
			editor.setEditor(text, items[i], 1);

			editor = new TableEditor(table);
			Composite paintComp = new Composite(table, SWT.NONE);
			paintComp.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent event) {
					GC gc = event.gc;
					System.out.println("paint");
					gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
					gc.drawOval(event.x, event.y, event.width, event.height);
				}
			});

			// editor.minimumWidth = paintComp.getSize().x;
			// editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.setEditor(paintComp, items[i], 2);

			editor = new TableEditor(table);
			text = new Text(table, SWT.NONE);
			text.setText("Text2");
			editor.grabHorizontal = true;
			editor.setEditor(text, items[i], 3);
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
