package faep.controller.listeners;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

import faep.custom.widgets.FaepExpandItem;

public class ExpandBarAdapter extends ControlAdapter{

	@Override
    public void controlResized(final ControlEvent e) {
		ExpandBar exp = (ExpandBar)e.widget;
		for(ExpandItem item:exp.getItems()){
			((FaepExpandItem)item).calculateCorrectHeight();
		}
    }

	
}
