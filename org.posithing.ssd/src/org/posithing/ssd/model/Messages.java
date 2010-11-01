package org.posithing.ssd.model;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.posithing.ssd.model.messages"; //$NON-NLS-1$
	public static String AbstractSchoolDataProvider_dataNotSynchronized;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}

}
