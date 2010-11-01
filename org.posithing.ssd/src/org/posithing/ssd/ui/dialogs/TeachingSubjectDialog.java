package org.posithing.ssd.ui.dialogs;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;


public class TeachingSubjectDialog extends Dialog {

	private TeachingSubject teachingSubject;
	private String title;

	private List subjectsList;
	private List classesList;

	private String selectedSubjectStr;
	private String[] selectedSchoolClassesStrArray;
	private TeachingSubject newTeachingSubject;

	private Set<Subject> subjects = null;
	private Set<SchoolClass> schoolClasses = null;

	public TeachingSubjectDialog(Shell parent, TeachingSubject teachingSubject) {
		super(parent);
		this.teachingSubject = teachingSubject;

		ExtensionManager extensionManager = ExtensionManager.getInstance();
		SchoolDataProvider dataProvider = extensionManager.getDefaultSchoolDataProvider();

		try {
			subjects = dataProvider.getSubjects();
			schoolClasses = dataProvider.getSchoolClasses();
		} catch (SchoolDataProviderException e) {
			Messenger.openExceptionBox(e, "Could not get data",
					"Could not get data from the data provider:");
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);

		subjectsList = new List(composite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		subjectsList.setFont(parent.getFont());
		subjectsList.setLayoutData(getListGridData());
		subjectsList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("subjectsList widgetDefaultSelected");
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				String[] selection = subjectsList.getSelection();
				if (selection.length > 0) {
					selectedSubjectStr = selection[0];
				}
			}
		});
		setSubjects(subjects);
		if (teachingSubject != null) {
			selectSubject(teachingSubject.getSubject());
		}

		classesList = new List(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		classesList.setFont(parent.getFont());
		classesList.setLayoutData(getListGridData());
		classesList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("classesList widgetDefaultSelected");
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				selectedSchoolClassesStrArray = classesList.getSelection();
			}
		});
		setSchoolClasses(schoolClasses);
		if (teachingSubject != null) {
			selectSchoolClasses(teachingSubject.getSchoolClasses());
		}

		applyDialogFont(composite);
		return composite;
	}

	private void setSubjects(Set<Subject> subjectsSet) {
		if (subjectsSet.size() > 0) {

			String[] subjectsStrings = new String[subjectsSet.size()];
			int index = 0;
			for (Subject subject : subjectsSet) {
				subjectsStrings[index] = subject.getName();
				index++;
			}

			subjectsList.setItems(subjectsStrings);
		} else {
			subjectsList.removeAll();
		}
	}

	private void setSchoolClasses(Set<SchoolClass> classesSet) {
		if (classesSet.size() > 0) {

			String[] classesStrings = new String[classesSet.size()];
			int index = 0;
			for (SchoolClass subject : classesSet) {
				classesStrings[index] = subject.getName();
				index++;
			}

			classesList.setItems(classesStrings);
		} else {
			classesList.removeAll();
		}
	}

	private void selectSubject(Subject subject) {
		String subjectName = subject.getName();
		String[] subjectStrArr = subjectsList.getItems();
		for (int i = 0; i < subjectStrArr.length; i++) {
			String subjectStr = subjectStrArr[i];
			if (subjectStr.equals(subjectName)) {
				subjectsList.setSelection(i);
				selectedSubjectStr = subjectName;
				break;
			}
		}
	}

	private void selectSchoolClasses(Set<SchoolClass> classes) {

		Set<String> classesNames = new TreeSet<String>();
		for (SchoolClass schoolClass : classes) {
			classesNames.add(schoolClass.getName());
		}
		String[] array = classesNames.toArray(new String[0]);
		classesList.setSelection(array);
		selectedSchoolClassesStrArray = array;
	}

	private Object getListGridData() {
		GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		gridData.heightHint = 300;
		return gridData;
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
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {

			Subject selectedSubject = null;
			for (Subject subject : subjects) {
				if (subject.getName().equals(selectedSubjectStr)) {
					selectedSubject = subject;
					break;
				}
			}

			Set<SchoolClass> selectedSchoolClasses = new TreeSet<SchoolClass>();
			for (int i = 0; i < selectedSchoolClassesStrArray.length; i++) {
				String selectedName = selectedSchoolClassesStrArray[i];
				for (SchoolClass schoolClass : schoolClasses) {
					if (schoolClass.getName().equals(selectedName)) {
						selectedSchoolClasses.add(schoolClass);
						break;
					}
				}
			}

			if (selectedSubject != null) {
				newTeachingSubject = new TeachingSubject(selectedSubject, selectedSchoolClasses);
			}
		} else {
			selectedSchoolClassesStrArray = null;
			selectedSubjectStr = null;
			newTeachingSubject = null;
			teachingSubject = null;
			subjects = null;
			schoolClasses = null;
		}
		super.buttonPressed(buttonId);
	}

	public TeachingSubject getNewTeachingSubject() {
		return newTeachingSubject;
	}
}
