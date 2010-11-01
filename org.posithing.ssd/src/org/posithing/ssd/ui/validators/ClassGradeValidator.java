package org.posithing.ssd.ui.validators;

import org.eclipse.jface.dialogs.IInputValidator;

public class ClassGradeValidator implements IInputValidator {

	@Override
	public String isValid(String newText) {
		try {
			int parsedGrade = Integer.parseInt(newText);
			if (parsedGrade < 0) {
				return Messages.ClassGradeValidator_negativeInt;
			} else if (parsedGrade > 20) {
				return Messages.ClassGradeValidator_bigInt;
			}
		} catch (NumberFormatException e) {
			return Messages.ClassGradeValidator_isNotInt;
		}
		return null;
	}
}
