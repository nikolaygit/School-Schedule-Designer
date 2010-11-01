package org.posithing.ssd.ui.validators;

import java.util.Set;

import org.eclipse.jface.dialogs.IInputValidator;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class ClassIdValidator implements IInputValidator {

	private Set<SchoolClass> schoolClasses;

	public ClassIdValidator() {
		try {
			schoolClasses = ExtensionManager.getInstance().getDefaultSchoolDataProvider()
					.getSchoolClasses();
		} catch (SchoolDataProviderException e) {
			schoolClasses = null;
			Messenger.openExceptionBox(e, "Class ID Validator not initialized",
					"Could not initialize the Class ID Validator");
		}
	}

	@Override
	public String isValid(String newText) {
		if (StringUtil.isEmpty(newText)) {
			return Messages.Validator_idEmpty;
		}
		for (SchoolClass schoolClass : schoolClasses) {
			if (schoolClass.getId().equalsIgnoreCase(newText)) {
				return Messages.Validator_idExists;
			}
		}
		return null;
	}
}
