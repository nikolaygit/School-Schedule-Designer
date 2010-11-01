package snippets;

import java.util.Arrays;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

/** Different editors in action */
public class TableExample5 {

	static Button create;
	static String[] listComb = new String[] { "vert", "bleu", "rouge" };

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setSize(400, 400);
		shell.setLayout(new FillLayout());

		DummyElement[] datas = new DummyElement[] {
				new DummyElement(new RGB(255, 12, 40), "row1col2", listComb[0]),
				new DummyElement(new RGB(70, 255, 40), "row2col2", listComb[0]) };

		create = new Button(shell, SWT.PUSH);
		create.setText("Create New");

		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
		table.setLayout(tableLayout);
		new TableColumn(table, SWT.LEFT).setText("col1");
		new TableColumn(table, SWT.NONE).setText("col2");
		new TableColumn(table, SWT.RIGHT).setText("col3");
		final TableViewer tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new DummyContentProvider());
		tableViewer.setLabelProvider(new DummyLabelProvider(table));

		ITableLabelProvider labelProvider = new ITableLabelProvider() {
			Label label = new Label(tableViewer.getTable(), SWT.NORMAL);

			public Image getColumnImage(Object element, int columnIndex) {
				label.setText(element.toString() + "Test ");
				return label.getImage();
			}

			public String getColumnText(Object element, int columnIndex) {
				return null;
			}

			public void addListener(ILabelProviderListener listener) {
				System.out.println("");
			}

			public void dispose() {
				label.dispose();
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				System.out.println("");
			}
		};

		// tableViewer.setLabelProvider(labelProvider);

		CellEditor editor = new ColorCellEditor(table);
		editor.getControl().setSize(50, 50);
		tableViewer.setCellEditors(new CellEditor[] { editor, new TextCellEditor(table),
				new ComboBoxCellEditor(table, listComb) });
		create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.getTable().removeAll();
			}
		});

		tableViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				DummyElement e = (DummyElement) element;
				if (property == "col1")
					return e.col1;
				else if (property == "col2")
					return e.col2;
				else if (property == "col3") {
					int i = Arrays.asList(listComb).indexOf(e.col3);
					return i == -1 ? null : new Integer(i);
				} else
					return null;
			}

			public void modify(Object element, String property, Object value) {
				// workaround for bug 1938 where element is Item rather than
				// model element
				if (element instanceof Item)
					element = ((Item) element).getData();

				DummyElement e = (DummyElement) element;

				if (property == "col1")
					e.col1 = (RGB) value;
				else if (property == "col2")
					e.col2 = (String) value;
				else if (property == "col3")
					if (value instanceof Integer) {
						int i = ((Integer) value).intValue();
						e.col3 = listComb[i];
					}

				// This is a hack. Changing the model above should cause it to
				// notify
				// the content provider, which should update the viewer.
				// It should not be done directly here.
				tableViewer.update(e, null);
			}
		});

		tableViewer.setColumnProperties(new String[] { "col1", "col2", "col3" });

		tableViewer.setInput(datas);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();

		}

		display.dispose();

	}

	public static class DummyElement {
		public RGB col1;
		public String col2;
		public String col3;

		public DummyElement(RGB col1, String col2, String col3) {
			this.col1 = col1;
			this.col2 = col2;
			this.col3 = col3;
		}
	}

	public static class DummyLabelProvider extends LabelProvider implements ITableLabelProvider {

		Widget fWidget = null;

		public DummyLabelProvider(Widget parentWidget) {
			fWidget = parentWidget;
		}

		@Override
		public void dispose() {
			if (fWidget != null)
				fWidget.dispose();
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String columnText = null;
			DummyElement dummyElement = (DummyElement) element;
			switch (columnIndex) {
			case 0:
				columnText = "" + dummyElement.col1;
				break;
			case 1:
				columnText = "" + dummyElement.col2;
				break;
			case 2:
				columnText = "" + dummyElement.col3;
				break;
			}
			return columnText;

		}

	}

	public static class DummyContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object input) {
			return (DummyElement[]) input;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			// should hook a listener on the model here (newInput)
		}

	}
}