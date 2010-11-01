package org.posithing.ssd.ui.validators;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.posithing.ssd.ui.validators.messages"; //$NON-NLS-1$
	public static String ClassGradeValidator_bigInt;
	public static String ClassGradeValidator_isNotInt;
	public static String ClassGradeValidator_negativeInt;
	public static String Validator_idEmpty;
	public static String Validator_idExists;
	public static String TeacherIdValidator_validatorNotInitializedMessage;
	public static String TeacherIdValidator_validatorNotInitializedTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
