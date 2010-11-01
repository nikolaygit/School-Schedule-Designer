package org.posithing.ssd.model;

public class SchoolDataProviderSynchronizationException extends
		SchoolDataProviderException {
	
	private static final long serialVersionUID = -678544264570577737L;

	public SchoolDataProviderSynchronizationException(String message) {
		super(message);
	}

	public SchoolDataProviderSynchronizationException(Throwable cause) {
		super(cause);
	}

	public SchoolDataProviderSynchronizationException(String message,
			Throwable cause) {
		super(message, cause);
	}
}
