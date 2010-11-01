package org.posithing.ssd.preferences;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.ShapeProvider;


public class ExtensionManager {

	private List<SchoolDataProvider> schoolDataProviders;
	private SchoolDataProvider defaultSchoolDataProvider;

	private List<ShapeProvider> shapeProviders;

	private ExtensionManager() {
		schoolDataProviders = new LinkedList<SchoolDataProvider>();
		shapeProviders = new LinkedList<ShapeProvider>();
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class ExtensionManagerHolder {
		private static final ExtensionManager INSTANCE = new ExtensionManager();
	}

	public static ExtensionManager getInstance() {
		return ExtensionManagerHolder.INSTANCE;
	}

	public boolean addSchoolDataProvider(SchoolDataProvider sdp) {
		return schoolDataProviders.add(sdp);
	}

	public boolean addShapeProvider(ShapeProvider shapeProvider) {
		return shapeProviders.add(shapeProvider);
	}

	public Set<String> getSupportedShapes() {

		Set<String> shapes = new TreeSet<String>();

		for (int i = 0; i < shapeProviders.size(); i++) {
			ShapeProvider shapeProvider = shapeProviders.get(i);
			Collection<String> supportedShapes = shapeProvider.getSupportedShapes();
			shapes.addAll(supportedShapes);
		}
		return shapes;
	}

	/**
	 * Draws the given shapeId with the first shape provider which supports it.
	 * 
	 * @param gc
	 *            GC
	 * @param bounds
	 *            Rectangle bounds for the drawing
	 * @param shapeId
	 *            the shape ID
	 * @param shapeColor
	 *            The shape color
	 * @param backgroundColor
	 *            The background color on which the drawing happens
	 * @return <code>true</code> if the shape was drawn by some provider,
	 *         <code>false</code> otherwise
	 */
	public boolean drawShape(GC gc, Rectangle bounds, String shapeId, Color shapeColor,
			Color backgroundColor) {
		for (int i = 0; i < shapeProviders.size(); i++) {
			ShapeProvider shapeProvider = shapeProviders.get(i);
			if (shapeProvider.isSupported(shapeId)) {
				shapeProvider.fillShape(gc, bounds, shapeId, backgroundColor, backgroundColor);
				return true;
			}
		}
		return false;
	}

	public List<SchoolDataProvider> getSchoolDataProviders() {
		return schoolDataProviders;
	}

	public List<ShapeProvider> getShapeProviders() {
		return shapeProviders;
	}

	public SchoolDataProvider getDefaultSchoolDataProvider() {
		return defaultSchoolDataProvider;
	}

	public void setDefaultSchoolDataProvider(SchoolDataProvider defaultSchoolDataProvider) {
		this.defaultSchoolDataProvider = defaultSchoolDataProvider;
	}

	public boolean isDefaultSchoolDataProvider(SchoolDataProvider schoolDataProvider) {
		return defaultSchoolDataProvider.getId().equals(schoolDataProvider.getId());
	}

}
