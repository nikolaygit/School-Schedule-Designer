package org.posithing.ssd.ui.views.schedule;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.common.ListContentProvider;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class ScheduleView extends ViewPart {

	public static final String ID = "org.posithing.ssd.ui.ScheduleView"; //$NON-NLS-1$

	private TableViewer viewer;

	private TableItem lastSelectedTableItem;
	private Point lastSelectedCell;

	public ScheduleView() {
	}

	@Override
	public void createPartControl(final Composite parent) {

		int numberOfLayoutColumns = 1;
		GridLayout layout = new GridLayout(numberOfLayoutColumns, false);
		parent.setLayout(layout);

		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setUseHashlookup(true);

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		ScheduleDropAdapter dropAdapter = new ScheduleDropAdapter(viewer);
		dropAdapter.setScrollExpandEnabled(true);
		viewer.addDropSupport(operations, transferTypes, dropAdapter);

		try {
			createColumns();
		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Schedule View Exception", "Message:");
		}

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.EraseItem, new ScheduleViewEraseItemListener());

		viewer.setContentProvider(new ListContentProvider<AssignmentRow>());
		viewer.setLabelProvider(new ScheduleViewLabelProvider());

		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = 20;
			}
		});
		table.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(event.x, event.y);
				int rowIndex = table.getTopIndex();
				while (rowIndex < table.getItemCount()) {
					boolean visible = false;
					TableItem item = table.getItem(rowIndex);
					int columnCount = table.getColumnCount();
					for (int col = 0; col < columnCount; col++) {
						Rectangle rect = item.getBounds(col);
						if (rect.contains(pt)) {
							lastSelectedCell = new Point(col, rowIndex);
							lastSelectedTableItem = item;
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					rowIndex++;
				}
			}
		});

		hookContextMenu();
		contributeToActionBars();

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = numberOfLayoutColumns;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	private void createColumns() throws SchoolDataProviderException {

		SchoolDataProvider dataProvider = ExtensionManager.getInstance()
				.getDefaultSchoolDataProvider();
		Set<SchoolClass> schoolClasses = dataProvider.getSchoolClasses();

		List<String> titlesList = new LinkedList<String>();
		titlesList.add(StringUtil.EMPTY);
		titlesList.add(StringUtil.EMPTY);
		for (SchoolClass schoolClass : schoolClasses) {
			titlesList.add(schoolClass.getId());
		}

		String[] titles = titlesList.toArray(new String[0]);
		int headerColumnWidth = 30;
		int columnWidth = 45;

		for (int i = 0; i < titles.length; i++) {
			final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();

			column.setText(titles[i]);
			if (i < 2) {
				column.setWidth(headerColumnWidth);
			} else {
				column.setWidth(columnWidth);
			}
			column.setResizable(false);
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		// menuMgr.setRemoveAllWhenShown(true);
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		makeExtendable(menuMgr);

		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager, menu);
			}
		});

		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager manager, Menu menu) {
		// System.out.println("filling ...");
		// manager.add();
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

	public void redrawLastSelectedCell() {
		Rectangle bounds = getLastSelectedTableItem().getBounds(getLastSelectedCell().x);
		getViewer().getTable().redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}

	@Override
	public void setFocus() {
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public Point getLastSelectedCell() {
		return lastSelectedCell;
	}

	public void setLastSelectedCell(Point lastSelectedCell) {
		this.lastSelectedCell = lastSelectedCell;
	}

	public TableItem getLastSelectedTableItem() {
		return lastSelectedTableItem;
	}

	public void setLastSelectedTableItem(TableItem lastSelectedTableItem) {
		this.lastSelectedTableItem = lastSelectedTableItem;
	}

}
