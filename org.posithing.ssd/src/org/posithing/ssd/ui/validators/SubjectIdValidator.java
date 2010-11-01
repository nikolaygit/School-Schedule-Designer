package org.posithing.ssd.ui.validators;

import java.util.Set;

import org.eclipse.jface.dialogs.IInputValidator;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class SubjectIdValidator implements IInputValidator {

	private Set<Subject> subjects;

	public SubjectIdValidator() {
		try {
			subjects = ExtensionManager.getInstance().getDefaultSchoolDataProvider().getSubjects();
		} catch (SchoolDataProviderException e) {
			subjects = null;
			Messenger.openExceptionBox(e, "Subject ID Validator not initialized",
					"Could not initialize the Subject ID Validator");
		}
	}

	@Override
	public String isValid(String newText) {
		if (StringUtil.isEmpty(newText)) {
			return Messages.Validator_idEmpty;
		}
		for (Subject subject : subjects) {
			if (subject.getId().equalsIgnoreCase(newText)) {
				return Messages.Validator_idExists;
			}
		}
		return null;
	}
}
