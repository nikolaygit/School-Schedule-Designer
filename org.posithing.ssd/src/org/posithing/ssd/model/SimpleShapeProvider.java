package org.posithing.ssd.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class SimpleShapeProvider implements ShapeProvider {

	private static final String ID = "org.posithing.ssd.extensions.shapeprovider.simple";
	private static final String NAME = "Simple Shape Provider";

	private static final String OVAL = "oval";
	private static final String OVAL_EMPTY = "oval_empty";
	private static final String RECTANGLE = "rectangle";
	private static final String RECTANGLE_EMPTY = "rectangle_empty";
	private static final String TRIANGLE = "triangle";
	private static final String TRIANGLE_EMPTY = "triangle_empty";

	private Color lineColor;

	private Set<String> shapeIds;

	public SimpleShapeProvider() {
		shapeIds = new TreeSet<String>();
		shapeIds.add(OVAL);
		shapeIds.add(OVAL_EMPTY);
		shapeIds.add(RECTANGLE);
		shapeIds.add(RECTANGLE_EMPTY);
		shapeIds.add(TRIANGLE);
		shapeIds.add(TRIANGLE_EMPTY);

		lineColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Collection<String> getSupportedShapes() {
		return shapeIds;
	}

	@Override
	public boolean isSupported(String shapeId) {
		if (shapeId == null) {
			return false;
		}
		return shapeIds.contains(shapeId);
	}

	@Override
	public boolean fillShape(GC gc, Rectangle bounds, String shapeId, Color shapeColor,
			Color backgroundColor) {
		gc.setBackground(shapeColor);

		if (OVAL.equals(shapeId)) {
			int[] oval = new int[] { bounds.x + 2, bounds.y, bounds.height - 1, bounds.height - 1 };
			gc.fillOval(oval[0], oval[1], oval[2], oval[3]);
			drawOval(gc, oval[0], oval[1], oval[2], oval[3]);
			return true;

		} else if (OVAL_EMPTY.equals(shapeId)) {
			int[] oval = new int[] { bounds.x + 2, bounds.y, bounds.height - 1, bounds.height - 1 };
			gc.fillOval(oval[0], oval[1], oval[2], oval[3]);
			drawOval(gc, oval[0], oval[1], oval[2], oval[3]);

			int m = oval[2] / 4;
			int[] oval_empty = new int[] { oval[0] + m, oval[1] + m, oval[2] - 2 * m,
					oval[3] - 2 * m };

			Color origColor = gc.getBackground();
			gc.setBackground(backgroundColor);
			gc.fillOval(oval_empty[0], oval_empty[1], oval_empty[2], oval_empty[3]);
			gc.setBackground(origColor);

			drawOval(gc, oval_empty[0], oval_empty[1], oval_empty[2], oval_empty[3]);
			return true;

		} else if (RECTANGLE.equals(shapeId)) {
			int[] rect = new int[] { bounds.x + 2, bounds.y, bounds.height, bounds.height - 1 };
			gc.fillRectangle(rect[0], rect[1], rect[2], rect[3]);
			drawRectangle(gc, rect[0], rect[1], rect[2], rect[3]);
			return true;

		} else if (RECTANGLE_EMPTY.equals(shapeId)) {
			int[] rect = new int[] { bounds.x + 2, bounds.y, bounds.height, bounds.height - 1 };
			gc.fillRectangle(rect[0], rect[1], rect[2], rect[3]);
			drawRectangle(gc, rect[0], rect[1], rect[2], rect[3]);

			int m = rect[2] / 4;
			int[] rect_empty = new int[] { rect[0] + m, rect[1] + m, rect[2] - 2 * m,
					rect[3] - 2 * m };

			Color origColor = gc.getBackground();
			gc.setBackground(backgroundColor);
			gc.fillRectangle(rect_empty[0], rect_empty[1], rect_empty[2], rect_empty[3]);
			gc.setBackground(origColor);

			drawRectangle(gc, rect_empty[0], rect_empty[1], rect_empty[2], rect_empty[3]);
			return true;

		} else if (TRIANGLE.equals(shapeId)) {
			int[] polygon = new int[] { bounds.x + bounds.height / 2, bounds.y + 1, bounds.x + 2,
					bounds.y + bounds.height - 2, bounds.x + bounds.height,
					bounds.y + bounds.height - 2 };
			gc.fillPolygon(polygon);
			drawPolygon(gc, polygon);
			return true;

		} else if (TRIANGLE_EMPTY.equals(shapeId)) {
			// TODO: first point is sometimes 1px on the left
			int[] polygon = new int[] { bounds.x + bounds.height / 2, bounds.y + 1, bounds.x + 2,
					bounds.y + bounds.height - 2, bounds.x + bounds.height,
					bounds.y + bounds.height - 2 };
			gc.fillPolygon(polygon);
			drawPolygon(gc, polygon);

			int m = (int) ((polygon[3] - polygon[1]) / 2.5f);
			int h = m / 2;
			int w = (int) ((1.4f / 2f) * m);
			int[] rect_empty = new int[] { polygon[0], polygon[1] + m, polygon[2] + w,
					polygon[3] - h, polygon[4] - w, polygon[5] - h };

			Color origColor = gc.getBackground();
			gc.setBackground(backgroundColor);
			gc.fillPolygon(rect_empty);
			gc.setBackground(origColor);

			drawPolygon(gc, rect_empty);
			return true;
		}

		return false;
	}

	private void drawOval(GC gc, int x, int y, int width, int height) {
		Color origColor = gc.getForeground();
		gc.setForeground(lineColor);
		gc.drawOval(x, y, width, height);
		gc.setForeground(origColor);
	}

	private void drawRectangle(GC gc, int x, int y, int width, int height) {
		Color origColor = gc.getForeground();
		gc.setForeground(lineColor);
		gc.drawRectangle(x, y, width, height);
		gc.setForeground(origColor);
	}

	private void drawPolygon(GC gc, int[] polygon) {
		Color origColor = gc.getForeground();
		gc.setForeground(lineColor);
		gc.drawPolygon(polygon);
		gc.setForeground(origColor);
	}

	@Override
	public void onClose() {
		lineColor.dispose();
	}

}
