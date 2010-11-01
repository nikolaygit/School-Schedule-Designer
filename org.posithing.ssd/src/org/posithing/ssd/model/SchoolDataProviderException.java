package org.posithing.ssd.model;

/**
 * SchoolDataProviderException is thrown when the procedure can not be executed 
 * by the school data provider.
 */
public class SchoolDataProviderException extends Exception {

	private static final long serialVersionUID = -6049829495075426137L;

	public SchoolDataProviderException(String message) {
		super(message);
	}

	public SchoolDataProviderException(Throwable cause) {
		super(cause);
	}

	public SchoolDataProviderException(String message, Throwable cause) {
		super(message, cause);
	}

}
