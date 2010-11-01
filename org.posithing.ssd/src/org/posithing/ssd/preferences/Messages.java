package org.posithing.ssd.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.posithing.ssd.preferences.messages"; //$NON-NLS-1$
	public static String DataProvidersPage_useDefaultDataProvider;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
