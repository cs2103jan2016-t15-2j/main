package gui;

import netscape.javascript.JSObject;

/**
 * @@author A0149527W
 */
public class HelpPage extends AppPage {
    // This is the html path for help page
    private static final String PATH_HTML = "/view/html/help.html";

    public HelpPage() {
        super(PATH_HTML);

        // communicate Java and JavaScript
        JSObject win = (JSObject) webEngine.executeScript(SCRIPT_WINDOW);
        win.setMember(NAME_BRIDGE, JSBridge.getInstance());
    }
}
