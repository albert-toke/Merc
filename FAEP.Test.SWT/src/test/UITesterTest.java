package test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.Test;

public class UITesterTest {

    public SWTWorkbenchBot bot = null;

    public UITesterTest() {
	bot = new SWTWorkbenchBot();
	SWTBotPreferences.PLAYBACK_DELAY = 1000;
    }

    @Test
    public void test() {
	bot.viewByTitle("Welcome").close();
	SWTBotMenu windowMenu = bot.menu("Window");
	windowMenu.click();
	SWTBotMenu showViewMenu = windowMenu.menu("Preferences");
	bot.comboBoxWithLabel("Provider:").setSelection(2);
	;
	showViewMenu.click();
	SWTBotMenu consoleMenu = showViewMenu.menu("Console");
	consoleMenu.click();
	bot.viewByTitle("Faep View").close();
	bot.resetWorkbench();
    }
}
