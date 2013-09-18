package faep.controller.listeners;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class FaepPreferenceSelectionListener implements SelectionListener {

    private PreferencePage preferencePage;

    public FaepPreferenceSelectionListener(PreferencePage prefPage) {
	this.preferencePage = prefPage;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
	// TODO Auto-generated method stub

    }

}
