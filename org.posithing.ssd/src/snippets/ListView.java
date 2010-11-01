package snippets;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ListView {
	private TableViewer viewer;

	private class Share {
		private String name;
		private float ratio;

		public Share(String name, float ratio) {
			this.name = name;
			this.ratio = ratio;
		}

		@Override
		public String toString() {
			return name;
		}

		public float getRatio() {
			return ratio;
		}
	}

	private class ListViewLabelProvider extends LabelProvider implements ITableColorProvider {
		public Color getBackground(Object element, int columnIndex) {
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			float ratio = ((Share) element).getRatio();
			if (ratio < 0.4) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
			} else if (ratio < 0.9) {
				return null;
			} else {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
		}
	}

	public ListView() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		viewer = new TableViewer(shell, SWT.NONE);
		viewer.setLabelProvider(new ListViewLabelProvider());
		appendShare(new Share("test1", 0.0f));
		appendShare(new Share("test2", 0.4f));
		appendShare(new Share("test3", 0.7f));
		appendShare(new Share("test4", 1.5f));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void appendShare(Share share) {
		viewer.add(share);
	}

	public void clearView() {
		viewer.getTable().removeAll();
	}

	public static void main(String[] args) {
		new ListView();
	}
}