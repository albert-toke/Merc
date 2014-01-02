package merc.gui.preferences;

import org.eclipse.jface.dialogs.IInputValidator;

public class JobTypeInputValidator implements IInputValidator {

    private static final String ERROR_MESSAGE = "Input should only contain Alphanumeric Characters and the two Characters '.' and '#'!";

    /**
     * Validates the String. Returns null for no error, or an error message
     * 
     * @param newText
     *            the String to validate
     * @return String
     */
    public String isValid(String newText) {
	char[] chars = newText.toCharArray();

	for (char c : chars) {
	    if (!Character.isLetter(c) && !Character.isDigit(c) && c != '.' && c != '#') {
		return ERROR_MESSAGE;
	    }
	}
	// Input must be OK
	return null;
    }
}