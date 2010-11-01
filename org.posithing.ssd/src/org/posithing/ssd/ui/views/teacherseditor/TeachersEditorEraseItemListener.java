package org.posithing.ssd.ui.views.teacherseditor;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.posithing.ssd.model.ShapeProvider;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.ResourceManager;
import org.posithing.ssd.utils.StringUtil;


public class TeachersEditorEraseItemListener implements Listener {

	private List<ShapeProvider> shapeProviders;
	private ResourceManager resourceManager;
	private Color listFGColor;

	public TeachersEditorEraseItemListener() {
		shapeProviders = ExtensionManager.getInstance().getShapeProviders();
		resourceManager = ResourceManager.getInstance();
		listFGColor = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	}

	@Override
	public void handleEvent(Event event) {
		try {
			event.detail &= ~SWT.HOT;

			if ((event.detail & SWT.SELECTED) == 0) {
				if (event.index == 0) { // if not selected and first column,
					// draw the shape
					drawShape(event);
				}
				return;
			} else {

				TableItem item = (TableItem) event.item;
				GC gc = event.gc;
				Color oldBackground = event.gc.getBackground();

				if (event.index == 0) {
					drawShape(event);
				}

				Table table = (Table) event.widget;
				int clientWidth = table.getClientArea().width;
				Rectangle bounds2 = item.getBounds(1);

				gc.setBackground(listFGColor);
				gc.fillRectangle(bounds2.x, event.y, clientWidth, event.height);

				gc.setBackground(oldBackground);

				event.detail &= ~SWT.SELECTED;
			}

		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Teachers View Exception",
					"Could not react on table change");
		}
	}

	private void drawShape(Event event) {
		TableItem item = (TableItem) event.item;
		Object itemData = item.getData();
		Teacher teacher = (Teacher) itemData;
		RGB rgbColor = teacher.getColor();

		if (rgbColor != null) {
			String shapeId = teacher.getShape();
			if (!StringUtil.isEmpty(shapeId)) {
				for (int i = 0; i < shapeProviders.size(); i++) {
					ShapeProvider shapeProvider = shapeProviders.get(i);
					if (shapeProvider.isSupported(shapeId)) {
						GC gc = event.gc;
						Rectangle bounds0 = item.getBounds(0);
						Color color = resourceManager.getColor(rgbColor);
						Color backgroundColor = item.getBackground();
						shapeProvider.fillShape(gc, bounds0, shapeId, color, backgroundColor);
					}
				}
			}
		}
	}

}
