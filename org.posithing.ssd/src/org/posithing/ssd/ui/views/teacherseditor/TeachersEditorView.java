package org.posithing.ssd.ui.views.teacherseditor;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.ui.common.DefaultSorter;
import org.posithing.ssd.ui.common.SetContentProvider;
import org.posithing.ssd.ui.views.Messages;
import org.posithing.ssd.utils.Messenger;


public class TeachersEditorView extends ViewPart implements ISelectionListener {

	public static final String ID = "org.posithing.ssd.ui.TeachersEditorView"; //$NON-NLS-1$

	private TableViewer viewer;
	private TeachersSorter sorter;
	private TeachersEditorFilter filter;

	private ISelection lastSelection;

	public TeachersEditorView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			GridLayout layout = new GridLayout(2, false);
			parent.setLayout(layout);

			Label searchLabel = new Label(parent, SWT.NONE);
			searchLabel.setText(Messages.TeachersEditorView_searchLabel);
			final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
			searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_FILL));
			searchText.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					filter.setSearchText(searchText.getText());
					viewer.refresh();
				}
			});

			viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.FULL_SELECTION);

			ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
					viewer) {
				@Override
				protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
							|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
							|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			};

			TableViewerEditor.create(viewer, actSupport, ColumnViewerEditor.DEFAULT
					| ColumnViewerEditor.KEYBOARD_ACTIVATION);

			// Set the sorter for the table
			sorter = new TeachersSorter(3);
			viewer.setSorter(sorter);

			filter = new TeachersEditorFilter();
			viewer.addFilter(filter);

			createColumns();

			Table table = viewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.addListener(SWT.EraseItem, new TeachersEditorEraseItemListener());

			viewer.setContentProvider(new SetContentProvider<Teacher>());
			viewer.setLabelProvider(new TeachersEditorViewLabelProvider());

			viewer.getTable().addListener(SWT.MeasureItem, new Listener() {
				public void handleEvent(Event event) {
					event.height = 20;
				}
			});

			// Make the selection available
			getSite().setSelectionProvider(viewer);

			hookContextMenu();
			contributeToActionBars();

			// Layout the viewer
			GridData gridData = new GridData();
			gridData.verticalAlignment = GridData.FILL;
			gridData.horizontalSpan = 2;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalAlignment = GridData.FILL;
			viewer.getControl().setLayoutData(gridData);

		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not initialize view",
					"Could not initilize Teachers View");
		}
	}

	private void createColumns() {
		String[] titles = { Messages.TeachersEditorView_shape,
				Messages.TeachersEditorView_firstName, Messages.TeachersEditorView_lastName,
				Messages.TeachersEditorView_id, Messages.TeachersEditorView_teachingSubjects };
		int[] bounds = { 45, 100, 100, 100, 200 };

		for (int i = 0; i < titles.length; i++) {
			final int index = i;
			final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();

			column.setText(titles[i]);
			column.setWidth(bounds[i]);
			column.setResizable(true);
			column.setMoveable(true);

			// set editing support
			if (index != 3) { // TODO - fix updating the ID in this way
				viewerColumn.setEditingSupport(new TeachersEditingSupport(viewer, index));
			}

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

	public ISelection getLastSelection() {
		return lastSelection;
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TableViewer getViewer() {
		return viewer;
	}

}
