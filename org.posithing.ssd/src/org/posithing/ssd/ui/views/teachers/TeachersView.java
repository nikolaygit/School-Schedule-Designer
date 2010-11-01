package org.posithing.ssd.ui.views.teachers;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.ui.common.DefaultSorter;
import org.posithing.ssd.ui.common.SetContentProvider;
import org.posithing.ssd.ui.views.Messages;
import org.posithing.ssd.ui.views.teachers.dnd.TeachersDragListener;
import org.posithing.ssd.ui.views.teacherseditor.TeachersEditorEraseItemListener;
import org.posithing.ssd.ui.views.teacherseditor.TeachersSorter;
import org.posithing.ssd.utils.StringUtil;


public class TeachersView extends ViewPart implements ISelectionListener {

	public static final String ID = "org.posithing.ssd.ui.TeachersView"; //$NON-NLS-1$

	private TableViewer viewer;
	private TeachersSorter sorter;

	private ISelection lastSelection;

	private TeachersDragListener teachersDragListener;

	public TeachersView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		int numberOfColumns = 1;
		GridLayout layout = new GridLayout(numberOfColumns, false);
		parent.setLayout(layout);

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		viewer.setUseHashlookup(true);

		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		teachersDragListener = new TeachersDragListener(viewer);
		viewer.addDragSupport(ops, transfers, teachersDragListener);

		// Set the sorter for the table
		sorter = new TeachersSorter(3);
		viewer.setSorter(sorter);

		createColumns();

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.EraseItem, new TeachersEditorEraseItemListener());

		viewer.setContentProvider(new SetContentProvider<Teacher>());
		viewer.setLabelProvider(new TeachersViewLabelProvider());

		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = 20;
			}
		});
		addToolTipSupport(parent.getDisplay());

		// Make the selection available
		getSite().setSelectionProvider(viewer);

		hookContextMenu();
		contributeToActionBars();

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = numberOfColumns;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	private void addToolTipSupport(final Display display) {

		final Table table = viewer.getTable();

		// Disable native tooltip
		table.setToolTipText(StringUtil.EMPTY);

		// Implement a "fake" tooltip
		TeacherTableItemTooltipListener tooltipListener = new TeacherTableItemTooltipListener(
				table, display);
		table.addListener(SWT.Dispose, tooltipListener);
		table.addListener(SWT.KeyDown, tooltipListener);
		table.addListener(SWT.MouseMove, tooltipListener);
		table.addListener(SWT.MouseHover, tooltipListener);
	}

	private void createColumns() {
		String[] titles = { Messages.TeachersEditorView_shape, Messages.TeachersEditorView_id };
		int[] bounds = { 45, 35 };

		for (int i = 0; i < titles.length; i++) {
			final int index = i;
			final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();

			column.setText(titles[i]);
			column.setWidth(bounds[i]);
			column.setResizable(true);

			// Setting the right sorter
			column.addSelectionListener(new DefaultSorter(sorter, viewer, column, index));
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
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
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-top"));
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end"));
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if ((lastSelection == null)
				|| (lastSelection != null && lastSelection.hashCode() != selection.hashCode())) {
			lastSelection = selection;
		}
	}

	@Override
	public void setFocus() {
	}

	public ISelection getLastSelection() {
		return lastSelection;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public Teacher getDraggedTeacher() {
		return teachersDragListener.getSelectedTeacher();
	}

}
