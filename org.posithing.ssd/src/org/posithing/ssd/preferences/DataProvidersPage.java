package org.posithing.ssd.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DataProvidersPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public DataProvidersPage() {
		super(GRID);
	}

	public DataProvidersPage(int style) {
		super(style);
	}

	public DataProvidersPage(String title, int style) {
		super(title, style);
	}

	public DataProvidersPage(String title, ImageDescriptor image, int style) {
		super(title, image, style);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(SSMPreferences.DATA_PROVIDERS_USE_DEFAULT,
				Messages.DataProvidersPage_useDefaultDataProvider, getFieldEditorParent()));
	}

}
