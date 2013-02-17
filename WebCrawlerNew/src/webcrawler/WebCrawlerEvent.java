package webcrawler;

public class WebCrawlerEvent {

	private String element;
	private String attribute;
	private String attributeValue;
	
	public WebCrawlerEvent(String element, String attribute, String attributeValue) {
		this.element = element;
		this.attribute = attribute;
		this.attributeValue = attributeValue;
	}
	
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public String toString() {
		return "WebCrawlerEvent [element=" + element + ", attribute="
				+ attribute + ", attributeValue=" + attributeValue + "]";
	}
	
	
	
}
