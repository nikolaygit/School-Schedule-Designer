package org.posithing.ssd.ui.views.schedule;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.ShapeProvider;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.ResourceManager;
import org.posithing.ssd.utils.StringUtil;


public class ScheduleViewEraseItemListener implements Listener {

	private ResourceManager resourceManager;
	private List<ShapeProvider> shapeProviders;
	private ShapeProvider shapeProvider;

	private Color borderColor;

	public ScheduleViewEraseItemListener() {
		resourceManager = ResourceManager.getInstance();
		shapeProviders = ExtensionManager.getInstance().getShapeProviders();
		shapeProvider = shapeProviders.get(0);

		borderColor = ResourceManager.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	}

	@Override
	public void handleEvent(Event event) {
		try {
			event.detail &= ~SWT.HOT;
			drawShapeFaster(event);

		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Schedule View Exception",
					"Could not react on table change");
		}
	}

	private void drawShapeFaster(Event event) {
		// System.out.println("drawing " + event);

		TableItem item = (TableItem) event.item;
		Object itemData = item.getData();
		if (event.index == 1) {
			Rectangle bounds = item.getBounds(event.index);
			event.gc.setBackground(borderColor);
			event.gc.fillRectangle(bounds.x + bounds.width - 3, bounds.y, 5, bounds.height);
		}

		if (itemData instanceof AssignmentRow) {
			if (event.index > 1) {
				AssignmentRow assignmentRow = (AssignmentRow) itemData;
				AssignmentValue assignment = assignmentRow.getAssignment(event.index - 2);
				if (assignment != null) {
					Teacher teacher = assignment.getTeacher();
					RGB rgbColor = teacher.getColor();

					if (rgbColor != null) {
						String shapeId = teacher.getShape();
						if (!StringUtil.isEmpty(shapeId)) {
							GC gc = event.gc;
							Rectangle bounds = item.getBounds(event.index);
							Color color = resourceManager.getColor(rgbColor);
							Color backgroundColor = item.getBackground();
							shapeProvider.fillShape(gc, bounds, shapeId, color, backgroundColor);
							gc.setBackground(backgroundColor);
							gc.drawText(teacher.getId(), bounds.x + 25, bounds.y + 6);
						}
					}
				}
			}
		} else if (itemData instanceof InfoRow) {
			Rectangle bounds = item.getBounds(event.index);
			event.gc.setBackground(borderColor);
			event.gc.fillRectangle(bounds.x, bounds.y + bounds.height - 3, bounds.width,
					bounds.height);
		}
	}

	// private void drawShape(Event event) {
	// TableItem item = (TableItem) event.item;
	// Object itemData = item.getData();
	// Teacher teacher = (Teacher) itemData;
	// RGB rgbColor = teacher.getColor();
	//
	// if (rgbColor != null) {
	// String shapeId = teacher.getShape();
	// if (!StringUtil.isEmpty(shapeId)) {
	// for (int i = 0; i < shapeProviders.size(); i++) {
	// ShapeProvider shapeProvider = shapeProviders.get(i);
	// if (shapeProvider.isSupported(shapeId)) {
	// GC gc = event.gc;
	// Rectangle bounds0 = item.getBounds(event.index);
	// Color color = resourceManager.getColor(rgbColor);
	// Color backgroundColor = item.getBackground();
	// shapeProvider.fillShape(gc, bounds0, shapeId, color, backgroundColor);
	// }
	// }
	// }
	// }
	// }

}
