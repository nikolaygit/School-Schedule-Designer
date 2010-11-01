package org.posithing.ssd.ui.validators;

import java.util.Set;

import org.eclipse.jface.dialogs.IInputValidator;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class TeacherIdValidator implements IInputValidator {

	private Set<Teacher> teachers;

	public TeacherIdValidator() {
		try {
			teachers = ExtensionManager.getInstance().getDefaultSchoolDataProvider().getTeachers();
		} catch (SchoolDataProviderException e) {
			teachers = null;
			Messenger.openExceptionBox(e, Messages.TeacherIdValidator_validatorNotInitializedTitle,
					Messages.TeacherIdValidator_validatorNotInitializedMessage);
		}
	}

	@Override
	public String isValid(String newText) {
		if (StringUtil.isEmpty(newText)) {
			return Messages.Validator_idEmpty;
		}
		for (Teacher teacher : teachers) {
			if (teacher.getId().equalsIgnoreCase(newText)) {
				return Messages.Validator_idExists;
			}
		}
		return null;
	}
}
