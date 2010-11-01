package org.posithing.ssd.ui.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.posithing.ssd.ui.views.messages"; //$NON-NLS-1$
	public static String ClassesEditorView_class;
	public static String ClassesEditorView_grade;
	public static String ClassesEditorView_id;
	public static String SubjectsEditorView_id;
	public static String SubjectsEditorView_subject;
	public static String TeachersEditorView_canNotGetTeachers;
	public static String TeachersEditorView_canNotGetTeachersFromTheCurrentDataProvider;
	public static String TeachersEditorView_color;
	public static String TeachersEditorView_firstName;
	public static String TeachersEditorView_id;
	public static String TeachersEditorView_lastName;
	public static String TeachersEditorView_searchLabel;
	public static String TeachersEditorView_shape;
	public static String TeachersEditorView_teachingSubjects;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
