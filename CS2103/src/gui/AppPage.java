package gui;

import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @@author  Zhu Bingjing (A0149527W)
 * @date 2016å¹´3æœˆ21æ—¥ ä¸Šå�ˆ10:54:00 
 * @version 1.0 
 */
public abstract class AppPage extends Pane{
	WebView browser;
	WebEngine webEngine;
	String html;
//	JSObject win;
	
	public AppPage(String html){
		this.browser = new WebView();
		this.webEngine = browser.getEngine();
		this.html=html;
		browser. setContextMenuEnabled(false);

		//load web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(
				this.html).toExternalForm());
		
		// add the web view to the scene
		this.getChildren().add(browser);
	}
	
	
}
