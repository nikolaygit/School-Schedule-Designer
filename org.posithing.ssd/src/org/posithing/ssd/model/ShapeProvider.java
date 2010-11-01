package org.posithing.ssd.model;

import java.util.Collection;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public interface ShapeProvider extends IdentifiableExtension {

	/**
	 * @return a collection of supported shape IDs.
	 */
	public Collection<String> getSupportedShapes();

	/**
	 * Returns whether the given shape ID is supported by this shape provider.
	 * 
	 * @param shapeId
	 * @return whether the given shape ID is supported by this shape provider.
	 */
	public boolean isSupported(String shapeId);

	/**
	 * Fills a shape with the given shape id.
	 * 
	 * @param gc
	 *            GC
	 * @param bounds
	 *            the bounds of where the shape can fit in
	 * @param shapeId
	 *            the shape id
	 * @param shapeColor
	 *            the shape color
	 * @param backgroundColor
	 *            the background color on which the shape is filled
	 * @return whether the shape was filled (drawn)
	 */
	public boolean fillShape(GC gc, Rectangle bounds, String shapeId, Color shapeColor,
			Color backgroundColor);
}
