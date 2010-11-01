package org.posithing.ssd.ui.views.subjectseditor;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.ui.common.DefaultSorter;
import org.posithing.ssd.ui.common.SetContentProvider;
import org.posithing.ssd.ui.views.Messages;


public class SubjectsEditorView extends ViewPart implements ISelectionListener {

	public static final String ID = "org.posithing.ssd.ui.SubjectsEditorView"; //$NON-NLS-1$

	private TableViewer viewer;
	private SubjectsSorter sorter;

	private ISelection lastSelection;

	public SubjectsEditorView() {

	}

	@Override
	public void createPartControl(Composite parent) {

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION);

		sorter = new SubjectsSorter();
		viewer.setSorter(sorter);

		createColumns();

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(false);

		viewer.setContentProvider(new SetContentProvider<Subject>());
		viewer.setLabelProvider(new SubjectsEditorViewLabelProvider());

		getSite().setSelectionProvider(viewer);

		hookContextMenu();
		contributeToActionBars();
	}

	private void createColumns() {
		String[] titles = { Messages.SubjectsEditorView_subject, Messages.SubjectsEditorView_id };
		int[] bounds = { 100, 50 };

		for (int i = 0; i < titles.length; i++) {
			final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();

			column.setText(titles[i]);
			column.setWidth(bounds[i]);
			column.setResizable(true);

			// Setting the right sorter
			column.addSelectionListener(new DefaultSorter(sorter, viewer, column, i));
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		// menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		makeExtendable(menuMgr);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		makeExtendable(manager);
	}

	private void makeExtendable(IContributionManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-top")); //$NON-NLS-1$
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end")); //$NON-NLS-1$
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if ((lastSelection == null)
				|| (lastSelection != null && lastSelection.hashCode() != selection.hashCode())) {
			lastSelection = selection;
		}
	}

	public ISelection getLastSelection() {
		return lastSelection;
	}

	@Override
	public void setFocus() {
		viewer.getTable().forceFocus();
	}

	public TableViewer getViewer() {
		return viewer;
	}

}
