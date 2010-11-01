package org.posithing.ssd.model;

public interface IdentifiableExtension {

	/**
	 * Returns the id of this provider. E.g.
	 * "org.posithing.ssd.extensions.extensiontype.name"
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * Returns the name of this provider.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Call before window is closed. Resources can be released here.
	 */
	public void onClose();
}
