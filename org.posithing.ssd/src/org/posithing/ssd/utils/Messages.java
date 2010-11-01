package org.posithing.ssd.utils;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.posithing.ssd.utils.messages"; //$NON-NLS-1$
	public static String MessagesUtil_Friday;
	public static String MessagesUtil_Monday;
	public static String MessagesUtil_Thursday;
	public static String MessagesUtil_Tuesday;
	public static String MessagesUtil_Unknown_Day;
	public static String MessagesUtil_Wednesday;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
