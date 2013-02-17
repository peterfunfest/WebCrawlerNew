package webcrawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Observable;

public class WebCrawler extends Observable {

	private final static int MAXIMUM_DEPTH = 3;

	private HTMLReader hr;

	private URLList urlsToVisit;
	private URLList urlsVisited;

	WebCrawler(HTMLReader hr) {
		this.hr = hr;
		urlsToVisit = new URLList();
		urlsVisited = new URLList();
	}

	private String expandURL(URL url, String urlString) {

		if (!urlString.startsWith("http")) {
			return url.getProtocol() + "://" + 
		                          url.getHost() + 
		                          ((url.getPort()==-1)?"":url.getPort()) + 
		                          ((url.getPath().endsWith("/"))?url.getPath().substring(0,url.getPath().length()-1):url.getPath()) + 
		                          ((!urlString.equals("") && urlString.charAt(0)=='/')?urlString:"/"+urlString);
			} else {
			return urlString;
		}

	}

	public URLList visitURL(int level, String url) throws IOException {
		
		System.out.println(level + " - " + url);

		URLList uRLList = new URLList();

		URL u = new URL(url);

//TODO - REMOVE THIS
        System.out.println("Host      : " + u.getHost());

		InputStream ins;

		ins = u.openStream();

		// Trundle through the HTML one element at a time.

		while (hr.readUntil(ins, '<', '<')) {

			// Inside an element - may be a closing element - but an element at
			// least.

			String token = hr.readString(ins, ' ', '>');
			String element;
			String attribute;
			String attributeValue=null;

			if (token != null) {
				element = token.trim();
				token = hr.readString(ins, '=', '>');
				while (token != null) {
					attribute = token.replace(" ", "").replace("=", "");
					char nextChar = hr.skipSpace(ins, '>');
					if (nextChar == '"') {
						// Looks like the element value is enclosed in quotes so
						// read to the next double quote.
						// This time there is no need for special terminal test
						// of '>' as it will be valid inside a quote.
						token = hr.readString(ins, '"', '"');
						if (token != null) {
							attributeValue = token.substring(0,token.length() - 1);
//							uRLList.add(level+1, attributeValue);
//							uRLList.add(level+1, expandURL(u, attributeValue));
						}
					} else {
						// TODO - element value is not quoted - presents a
						// problem!!
						// need some test cases for this.
						// ignore for now as it is a rare event - I hope.
						token = hr.readString(ins, ' ', '>');
						attributeValue = "**********"+token;
//						if (element.equals("a")) {
	//						uRLList.add(level+1, "TODO-UNKNOWN:" + token);
		//				}
					}
					setChanged();
					notifyObservers(new WebCrawlerEvent(element,attribute,attributeValue));
//	                System.out.print(element);
	//                System.out.print("-" + attribute);
	  //              System.out.println("      " + attributeValue);
					token = hr.readString(ins, '=', '>');
				}

			}

		}

		ins.close();

		for (URLListElement e: uRLList.getUrls()) {
			System.out.println("   " + e.toString());			
		}

		return uRLList;

	}

	public void crawl(String url) {

		urlsToVisit.add(0, url);

		//
		// Because we are about to transmogrify urlsToVisit, we can't iterate
		// over it in the conventional way - lest there is a
		// java.util.ConcurrentModificationException
		//

		int idx = 0;

		while (idx < urlsToVisit.getUrls().size()) {

			try {
				
				URLListElement e = urlsToVisit.getUrls().get(idx);

				if (e.getPriority() < MAXIMUM_DEPTH) {
					
					URLList extractedURLs = visitURL(e.getPriority(), e.getUrl());
					urlsVisited.add(e);

					Iterator<URLListElement> innerItr = extractedURLs.getUrls().iterator();

					while (innerItr.hasNext()) {
						urlsToVisit.add(innerItr.next());
					}

				}
			} catch (IOException e) {
				// Display the error, but continue.
				e.printStackTrace();
			}

			idx++;

		}

	}


}
