package org.posithing.ssd.ui.dialogs;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.ResourceManager;
import org.posithing.ssd.utils.StringUtil;


/**
 */
public class TeacherDialog extends Dialog {
	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * The message to display, or <code>null if none.
	 */
	private String message;

	private String firstNameLabelStr;

	private String firstName = StringUtil.EMPTY;

	private String lastNameLabelStr;

	private String lastName = StringUtil.EMPTY;

	private String idLabelStr;

	private String id = StringUtil.EMPTY;

	private String shapeLabelStr;

	private String shape = StringUtil.EMPTY;

	private String rgbColorLabelStr;

	private String colorButtonText;

	private String colorDialogTitle;

	private String subjectsLabelStr;

	private TeachingSubjects teachingSubjects;

	/**
	 * The input value; the empty string by default.
	 */
	private RGB rgb = null;

	/**
	 * The input validator, or <code>null if none.
	 */
	private IInputValidator idValidator;

	/**
	 * Input text widget.
	 */
	private Text firstText;

	/**
	 * Input text widget.
	 */
	private Text secondText;

	/**
	 * Input text widget.
	 */
	private Text thirdText;

	/**
	 * Input combo widget.
	 */
	private Combo fourthCombo;

	private Label colorLabel;

	private List subjectsList;

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

	public TeacherDialog(Shell parentShell) {
		super(parentShell);
		this.title = Messages.TeacherDialog_dialogTitle;
		message = Messages.TeacherDialog_dialogMessage;
		firstNameLabelStr = Messages.TeacherDialog_firstName;
		firstName = StringUtil.EMPTY;
		lastNameLabelStr = Messages.TeacherDialog_lastName;
		lastName = StringUtil.EMPTY;
		idLabelStr = Messages.TeacherDialog_id;
		id = StringUtil.EMPTY;
		shapeLabelStr = Messages.TeacherDialog_shape;
		shape = StringUtil.EMPTY;
		rgbColorLabelStr = Messages.TeacherDialog_color;
		ResourceManager resourceManager = ResourceManager.getInstance();
		rgb = resourceManager.getYellowRGB();
		colorButtonText = Messages.TeacherDialog_colorOpen;
		colorDialogTitle = Messages.TeacherDialog_colorDialogTitle;
		subjectsLabelStr = Messages.TeacherDialog_subjects;
		teachingSubjects = new TeachingSubjects();
		checkBoxText = Messages.TeacherDialog_checkBoxText;
		selected = true;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			firstName = firstText.getText();
			lastName = secondText.getText();
			id = thirdText.getText();
			shape = fourthCombo.getText();
			selected = checkBox.getSelection();
		} else {
			firstName = null;
			lastName = null;
			id = null;
			shape = null;
			rgb = null;
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
		firstText.setFocus();
		if (firstName != null) {
			firstText.setText(firstName);
			firstText.selectAll();
		}
		if (lastName != null) {
			secondText.setText(lastName);
		}
		if (id != null) {
			thirdText.setText(id);
		}
		if (shape != null) {
			fourthCombo.setText(shape);
		}
		if (rgb != null) {
			ResourceManager resourceManager = ResourceManager.getInstance();
			colorLabel.setBackground(resourceManager.getColor(rgb));
		}
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		int numberOfColumns = 3;
		// create composite
		final Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numberOfColumns;
		composite.setLayout(gridLayout);

		// create message
		if (message != null) {
			Label label = new Label(composite, SWT.WRAP);
			label.setText(message);
			GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
					| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH - 100);
			data.horizontalSpan = numberOfColumns;
			label.setLayoutData(data);
			label.setFont(parent.getFont());
		}

		Label firstLabel = new Label(composite, SWT.WRAP);
		firstLabel.setText(firstNameLabelStr);
		firstLabel.setFont(parent.getFont());
		firstLabel.setLayoutData(getLabelGridData());

		firstText = new Text(composite, getInputTextStyle());
		firstText.setLayoutData(getFillHGridData(numberOfColumns));
		firstText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				onTextModified();
			}
		});

		Label secondLabel = new Label(composite, SWT.WRAP);
		secondLabel.setText(lastNameLabelStr);
		secondLabel.setFont(parent.getFont());
		secondLabel.setLayoutData(getLabelGridData());

		secondText = new Text(composite, getInputTextStyle());
		secondText.setLayoutData(getFillHGridData(numberOfColumns));
		secondText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				onTextModified();
			}
		});

		Label thirdLabel = new Label(composite, SWT.WRAP);
		thirdLabel.setText(idLabelStr);
		thirdLabel.setFont(parent.getFont());
		thirdLabel.setLayoutData(getLabelGridData());

		thirdText = new Text(composite, getInputTextStyle());
		thirdText.setLayoutData(getFillHGridData(numberOfColumns));
		thirdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateInput();
			}
		});

		Label fourthLabel = new Label(composite, SWT.WRAP);
		fourthLabel.setText(shapeLabelStr);
		fourthLabel.setFont(parent.getFont());
		fourthLabel.setLayoutData(getLabelGridData());

		fourthCombo = new Combo(composite, getInputTextStyle());
		fourthCombo.setLayoutData(getFillHGridData(numberOfColumns));
		Set<String> supportedShapes = ExtensionManager.getInstance().getSupportedShapes();
		fourthCombo.setItems(supportedShapes.toArray(new String[0]));

		Label fifthLabel = new Label(composite, SWT.WRAP);
		fifthLabel.setText(rgbColorLabelStr);
		fifthLabel.setFont(parent.getFont());
		fifthLabel.setLayoutData(getLabelGridData());

		// Use a label full of spaces to show the color
		colorLabel = new Label(composite, SWT.WRAP);
		colorLabel.setText("                              "); //$NON-NLS-1$
		colorLabel.setBackground(ResourceManager.getInstance().getColor(rgb));

		Button button = new Button(composite, SWT.PUSH);
		button.setText(colorButtonText);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Create the color-change dialog
				ColorDialog dlg = new ColorDialog(composite.getShell());

				// Set the selected color in the dialog from
				// user's selected color
				dlg.setRGB(colorLabel.getBackground().getRGB());

				// Change the title bar text
				dlg.setText(colorDialogTitle);

				// Open the dialog and retrieve the selected color
				RGB selectedRGB = dlg.open();
				if (selectedRGB != null) {
					// Dispose the old color, create the
					// new one, and set into the label
					ResourceManager resourceManager = ResourceManager.getInstance();

					Color oldBackground = colorLabel.getBackground();
					oldBackground.dispose();

					rgb = selectedRGB;
					colorLabel.setBackground(resourceManager.getColor(selectedRGB));
				}
			}
		});

		Label subjectsLabel = new Label(composite, SWT.WRAP);
		subjectsLabel.setText(subjectsLabelStr);
		subjectsLabel.setFont(parent.getFont());
		subjectsLabel.setLayoutData(getLabelGridData());

		subjectsList = new List(composite, SWT.V_SCROLL | SWT.BORDER);
		subjectsList.setLayoutData(getFillHGridData(numberOfColumns - 1));
		subjectsList.setSize(50, 20);

		Set<TeachingSubject> teachingSubjectsSet = teachingSubjects.getTeachingSubjects();
		setTeachingSubjects(teachingSubjectsSet);

		final Composite buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayout(new FillLayout(SWT.VERTICAL));

		Button addSubjectButton = new Button(buttonComposite, SWT.PUSH);
		addSubjectButton.setText("Add");
		addSubjectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				addTeachingSubject(buttonComposite.getShell());
			}
		});
		Button changeSubjectButton = new Button(buttonComposite, SWT.PUSH);
		changeSubjectButton.setText("Change");
		changeSubjectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				changeTeachingSubject(buttonComposite.getShell());
			}
		});
		Button removeSubjectButton = new Button(buttonComposite, SWT.PUSH);
		removeSubjectButton.setText("Delete");
		removeSubjectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				deleteTeachingSubject();
			}
		});

		checkBox = new Button(composite, SWT.CHECK);
		checkBox.setText(checkBoxText);
		checkBox.setSelection(selected);
		GridData gridData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER
				| GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = numberOfColumns;
		checkBox.setLayoutData(gridData);

		errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		gridData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER
				| GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = numberOfColumns;
		errorMessageText.setLayoutData(gridData);
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		// Set the error message text
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=66292
		setErrorMessage(errorMessage);

		applyDialogFont(composite);
		return composite;
	}

	private void setTeachingSubjects(Set<TeachingSubject> teachingSubjectsSet) {
		if (teachingSubjectsSet.size() > 0) {

			String[] teachingSubjectsStrings = new String[teachingSubjectsSet.size()];
			int index = 0;
			for (TeachingSubject teachingSubject : teachingSubjectsSet) {
				teachingSubjectsStrings[index] = teachingSubject.toString();
				index++;
			}

			subjectsList.setItems(teachingSubjectsStrings);
		}
	}

	public TeachingSubject getSelected() {
		int selectionIndex = subjectsList.getSelectionIndex();
		if (selectionIndex == -1)
			return null;
		String selectedString = subjectsList.getItems()[selectionIndex];
		for (TeachingSubject teachingSubject : teachingSubjects.getTeachingSubjects()) {
			if (selectedString.equals(teachingSubject.toString())) {
				return teachingSubject;
			}
		}
		return null;
	}

	private void setSelection(TeachingSubject teachingSubject) {
		String teachingSubjectStr = teachingSubject.toString();
		String[] items = subjectsList.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(teachingSubjectStr)) {
				subjectsList.setSelection(i);
			}
		}
	}

	private void addTeachingSubject(Shell shell) {
		try {
			TeachingSubjectDialog dialog = new TeachingSubjectDialog(shell, null);
			int res = dialog.open();
			if (res == Window.OK) {
				TeachingSubject newTeachingSubject = dialog.getNewTeachingSubject();
				if (newTeachingSubject != null) {
					teachingSubjects.addTeachingSubject(newTeachingSubject);
					setTeachingSubjects(teachingSubjects.getTeachingSubjects());
					setSelection(newTeachingSubject);
				}
			}
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not add teaching subject",
					"Could not add teaching subject");
		}
	}

	private void changeTeachingSubject(Shell shell) {
		try {
			TeachingSubject teachingSubject = getSelected();
			if (teachingSubject != null) {
				TeachingSubjectDialog dialog = new TeachingSubjectDialog(shell, teachingSubject);
				int res = dialog.open();
				if (res == Window.OK) {
					TeachingSubject newTeachingSubject = dialog.getNewTeachingSubject();
					if (newTeachingSubject == null) {
						System.out.println("newTeachingSubject is null");
					} else {
						teachingSubjects.removeTeachingSubject(teachingSubject);
						teachingSubjects.addTeachingSubject(newTeachingSubject);
						setTeachingSubjects(teachingSubjects.getTeachingSubjects());
						setSelection(newTeachingSubject);
					}
				}
			} else {
				Messenger.openWarningBox("No Subject selected", "No Subject selected");
				this.getShell().forceFocus();
			}
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not change teaching subject",
					"Could not change teaching subject");
		}
	}

	private void deleteTeachingSubject() {
		try {
			TeachingSubject teachingSubject = getSelected();
			if (teachingSubject != null) {
				teachingSubjects.removeTeachingSubject(teachingSubject);
				setTeachingSubjects(teachingSubjects.getTeachingSubjects());
			} else {
				Messenger.openWarningBox("No Subject selected", "No Subject selected");
				this.getShell().forceFocus();
			}
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Could not delete teaching subject",
					"Could not delete teaching subject");
		}
	}

	private GridData getLabelGridData() {
		GridData gridData = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER);
		return gridData;
	}

	private GridData getFillHGridData(int numberOfColumns) {
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = numberOfColumns - 1;
		return gridData;
	}

	private void onTextModified() {
		if (checkBox.getSelection()) {
			String firstName = firstText.getText();
			String secondName = secondText.getText();

			StringBuilder sb = new StringBuilder();
			sb.append(firstName);
			sb.append(secondName);

			thirdText.setText(sb.toString());
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
	protected void validateInput() {
		String errorMessage = null;
		if (idValidator != null) {
			errorMessage = idValidator.isValid(thirdText.getText());
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

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getId() {
		return id;
	}

	public String getShape() {
		return shape;
	}

	public RGB getRGB() {
		return rgb;
	}

	public TeachingSubjects getTeachingSubjects() {
		return teachingSubjects;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdValidator(IInputValidator idValidator) {
		this.idValidator = idValidator;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public void setRgb(RGB rgb) {
		this.rgb = rgb;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setTeachingSubjects(TeachingSubjects teachingSubjects) {
		this.teachingSubjects = teachingSubjects;
	}
}
