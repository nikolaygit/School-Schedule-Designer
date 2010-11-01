package org.posithing.ssd.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.posithing.ssd.utils.StringUtil;


/**
 */
public class SchoolClassDialog extends Dialog {
	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * The message to display, or <code>null if none.
	 */
	private String message;

	private String nameLabelStr;
	/**
	 * The input value; the empty string by default.
	 */
	private String name = StringUtil.EMPTY;

	private String gradeLabelStr;
	/**
	 * The input value; the empty string by default.
	 */
	private String grade = StringUtil.EMPTY;

	private String idLabelStr;
	/**
	 * The input value; the empty string by default.
	 */
	private String id = StringUtil.EMPTY;

	/**
	 * The input validator, or <code>null if none.
	 */
	private IInputValidator gradeValidator;

	/**
	 * The input validator, or <code>null if none.
	 */
	private IInputValidator idValidator;

	/**
	 * Input text widget.
	 */
	private Text nameText;

	/**
	 * Input text widget.
	 */
	private Text gradeText;

	/**
	 * Input text widget.
	 */
	private Text idText;

	private Button checkBox;

	private String checkBoxText;

	private boolean selected;

	/**
	 * Error message label widget.
	 */
	private Text errorMessageText;

	/**
	 * Error message string.
	 */
	private String errorMessage;

	public SchoolClassDialog(Shell parentShell) {
		super(parentShell);
		this.title = Messages.SchoolClassDialog_dialogTitle;
		message = Messages.SchoolClassDialog_dialogMessage;
		nameLabelStr = Messages.SchoolClassDialog_name;
		gradeLabelStr = Messages.SchoolClassDialog_grade;
		idLabelStr = Messages.SchoolClassDialog_id;
		checkBoxText = Messages.SchoolClassDialog_checkBoxText;
		selected = true;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			name = nameText.getText();
			grade = gradeText.getText();
			id = idText.getText();
			selected = checkBox.getSelection();
		} else {
			name = null;
			grade = null;
			id = null;
			selected = false;
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
		nameText.setFocus();
		if (name != null) {
			nameText.setText(name);
			nameText.selectAll();
		}
		if (grade != null) {
			gradeText.setText(grade);
		}
		if (id != null) {
			idText.setText(id);
		}
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

		Label firstLabel = new Label(composite, SWT.WRAP);
		firstLabel.setText(nameLabelStr);
		firstLabel.setFont(parent.getFont());
		firstLabel.setLayoutData(getLabelGridData());

		nameText = new Text(composite, getInputTextStyle());
		nameText.setLayoutData(getTextGridData());
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				onTextModified(nameText);
			}
		});

		Label secondLabel = new Label(composite, SWT.WRAP);
		secondLabel.setText(gradeLabelStr);
		secondLabel.setFont(parent.getFont());
		secondLabel.setLayoutData(getLabelGridData());

		gradeText = new Text(composite, getInputTextStyle());
		gradeText.setLayoutData(getTextGridData());
		gradeText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateInput(gradeValidator, gradeText.getText());
				validateInput(idValidator, idText.getText());
			}
		});

		Label thirdLabel = new Label(composite, SWT.WRAP);
		thirdLabel.setText(idLabelStr);
		thirdLabel.setFont(parent.getFont());
		thirdLabel.setLayoutData(getLabelGridData());

		idText = new Text(composite, getInputTextStyle());
		idText.setLayoutData(getTextGridData());
		idText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateInput(idValidator, idText.getText());
				validateInput(gradeValidator, gradeText.getText());
			}
		});

		checkBox = new Button(composite, SWT.CHECK);
		checkBox.setText(checkBoxText);
		checkBox.setSelection(selected);
		GridData gridData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER
				| GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = 2;
		checkBox.setLayoutData(gridData);

		errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		gridData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER
				| GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = 2;
		errorMessageText.setLayoutData(gridData);
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		// Set the error message text
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=66292
		setErrorMessage(errorMessage);

		applyDialogFont(composite);
		return composite;
	}

	private GridData getLabelGridData() {
		GridData gridData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER);
		return gridData;
	}

	private GridData getTextGridData() {
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		return gridData;
	}

	public String getName() {
		return name;
	}

	public String getGrade() {
		return grade;
	}

	public String getId() {
		return id;
	}

	public boolean isSelected() {
		return selected;
	}

	private void onTextModified(Text text) {
		if (checkBox.getSelection()) {
			if (text == nameText) {
				String inputTextStr = nameText.getText();
				idText.setText(inputTextStr);
				if (inputTextStr.length() > 0) {
					String gradeStr1 = inputTextStr.substring(0, 1);
					String gradeStr2 = inputTextStr.substring(0, 2);
					try {
						Integer.parseInt(gradeStr2);
						gradeText.setText(gradeStr2);
					} catch (Exception e) {
						gradeText.setText(gradeStr1);
					}
				} else {
					gradeText.setText(StringUtil.EMPTY);
				}
			}
		}
	}

	/**
	 * Validates the input.
	 * <p>
	 * The default implementation of this framework method delegates the request
	 * to the supplied input validator object; if it finds the input invalid,
	 * the error message is displayed in the dialog's message line. This hook
	 * method is called whenever the text changes in the input field.
	 * </p>
	 */
	protected void validateInput(IInputValidator validator, String input) {
		String errorMessage = null;
		if (validator != null) {
			errorMessage = idValidator.isValid(idText.getText());
			errorMessage = gradeValidator.isValid(gradeText.getText());
		}
		// Bug 16256: important not to treat "" (blank error) the same as null
		// (no error)
		setErrorMessage(errorMessage);
	}

	/**
	 * Sets or clears the error message. If not
	 * <code>null, the OK button is disabled.
	 * 
	 * @param errorMessage
	 *            the error message, or <code>null to clear
	 * @since 3.0
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
			// Disable the error message text control if there is no error, or
			// no error text (empty or whitespace only). Hide it also to avoid
			// color change.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=130281
			boolean hasError = errorMessage != null
					&& (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
			errorMessageText.setEnabled(hasError);
			errorMessageText.setVisible(hasError);
			errorMessageText.getParent().update();
			// Access the ok button by id, in case clients have overridden
			// button creation.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
			Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(errorMessage == null);
			}
		}
	}

	/**
	 * Returns the style bits that should be used for the input text field.
	 * Defaults to a single line entry. Subclasses may override.
	 * 
	 * @return the integer style bits that should be used when creating the
	 *         input text
	 * 
	 * @since 3.4
	 */
	protected int getInputTextStyle() {
		return SWT.SINGLE | SWT.BORDER;
	}

	public void setGradeValidator(IInputValidator gradeValidator) {
		this.gradeValidator = gradeValidator;
	}

	public void setIdValidator(IInputValidator idValidator) {
		this.idValidator = idValidator;
	}
}
