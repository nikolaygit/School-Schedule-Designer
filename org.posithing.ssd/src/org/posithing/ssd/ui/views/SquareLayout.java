package org.posithing.ssd.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class SquareLayout extends Layout {

	public static final int MARGIN = 2;
	public static final int SPACING = 2;

	private int num;
	private Point[] sizes;
	private int partialWidth;
	private int partialHeight;
	private int totalWidth;
	private int totalHeight;

	private void initialize(Control[] children) {

		num = (int) Math.ceil(Math.sqrt(children.length));

		sizes = new Point[children.length];
		int tempWidth = 0, tempHeight = 0;
		for (int i = 0; i < children.length; i += num) {
			sizes[i] = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			partialWidth = Math.max(sizes[i].x, tempWidth);
			partialHeight = Math.max(sizes[i].y, tempHeight);
		}
		// find the height and width for the composite
		totalWidth = partialWidth * num + (children.length - 1) * SPACING;
		totalHeight = partialHeight * num + (children.length - 1) * SPACING;
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		long start = System.currentTimeMillis();
		Control children[] = composite.getChildren();
		if (flushCache || sizes == null || sizes.length != children.length) {
			initialize(children);
		}
		Rectangle rect = composite.getClientArea();
		int x, y = MARGIN;
		int width = Math.max(partialWidth, rect.width / num - 2 * MARGIN);
		int height = Math.max(partialHeight, rect.height / num - 2 * MARGIN);
		for (int i = 0; i < children.length; i += num) {
			x = MARGIN;
			for (int j = i; j < children.length && j < i + num; j++) {
				children[j].setBounds(x, y, width, height);
				x += width + SPACING;
			}
			y += height + SPACING; // add 2 for spacing
		}
		long end = System.currentTimeMillis();
		System.out.println("time = " + (end-start));
	}

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {

		Control children[] = composite.getChildren();
		if (flushCache || sizes == null || sizes.length != children.length) {
			initialize(children);
		}
		// if they have given a width or height, use it
		// otherwise, use the calculated width and height
		int width = wHint, height = hHint;
		if (wHint == SWT.DEFAULT)
			width = totalWidth;
		if (hHint == SWT.DEFAULT)
			height = totalHeight;
		// account for margin
		return new Point(width + 2 * MARGIN, height + 2 * MARGIN);
	}

}
