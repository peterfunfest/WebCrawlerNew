package webcrawler;

import java.util.Observable;
import java.util.Observer;

public class WebCrawlerObserver implements Observer {
	
	public void update(Observable o,
                       Object arg) {
		
		if ( (!(o instanceof WebCrawler)) || (!(arg instanceof WebCrawlerEvent)) ) {
			throw new IllegalArgumentException();			
		} else {
			WebCrawlerEvent we = (WebCrawlerEvent)arg;
			if ( we.getElement().equals("a") && we.getAttribute().equals("href")) {
				System.out.println(arg.toString());				
			}
			// TODO : Observer code here
		}
		
		
	}

}
