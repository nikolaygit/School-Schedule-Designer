package org.posithing.ssd.ui.common;

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A provider of set of elements.
 * 
 * @param <T>
 *            type of elements in the set
 */
public class SetContentProvider<T> implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Set<?>) {
			Set<T> aSet = (Set<T>) inputElement;
			return aSet.toArray();
		}
		return null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

}
