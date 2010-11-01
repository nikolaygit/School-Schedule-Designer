package org.posithing.ssd.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 */
public class SelectionDialog extends Dialog {
	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * The message to display, or <code>null if none.
	 */
	private String message;

	private List list;
	private Object selectedObject;
	private java.util.List<Object> objects;

	public SelectionDialog(Shell parentShell) {
		super(parentShell);
		this.title = "Select an object";
		message = "Please select the appropriate object";
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String[] selection = list.getSelection();
			if (selection.length > 0) {
				String selectedString = selection[0];
				for (Object object : objects) {
					if (object.toString().equals(selectedString)) {
						selectedObject = object;
					}
				}
			}
		} else {
			selectedObject = null;
			objects = null;
		}
		super.buttonPressed(buttonId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, ISSMDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, ISSMDialogConstants.CANCEL_LABEL, false);
		// do this here because setting the text will set enablement on the ok
		// button
		list.setFocus();
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);

		// create message
		if (message != null) {
			Label label = new Label(composite, SWT.WRAP);
			label.setText(message);
			GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
					| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH - 100);
			data.horizontalSpan = 2;
			label.setLayoutData(data);
			label.setFont(parent.getFont());
		}

		list = new List(composite, getListStyle());
		list.setLayoutData(getGridData());

		java.util.List<String> objectStrigs = new ArrayList<String>();
		for (Object curObject : objects) {
			String string = curObject.toString();
			objectStrigs.add(string);
		}

		list.setItems(objectStrigs.toArray(new String[0]));
		list.setSelection(0);

		applyDialogFont(composite);
		return composite;
	}

	private GridData getGridData() {
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		return gridData;
	}

	protected int getListStyle() {
		return SWT.SINGLE | SWT.BORDER;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public java.util.List<Object> getObjects() {
		return objects;
	}

	public void setObjects(java.util.List<Object> objects) {
		this.objects = objects;
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

}
