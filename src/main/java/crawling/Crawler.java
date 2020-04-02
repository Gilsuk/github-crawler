package crawling;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class Crawler<T> implements Iterator<List<T>> {
	
	private String urlToCrawl;
	
	public Crawler(String url) {
		this.urlToCrawl = url;
	}
	
	@Override
	public boolean hasNext() {
		return urlToCrawl == null ? false : true;
	}

	@Override
	public List<T> next() {
		Document doc = crawl();
		List<T> items = parse(doc);
		this.urlToCrawl = setNextUrl(doc);
		return items;
	}
	
	private Document crawl() {
		try {
			return Jsoup.connect(urlToCrawl).get();
		} catch (IOException e) { e.printStackTrace(); }
		
		return new Document(null);
	}

	/**
	 * Should return full url to crawl or null
	 * Return this to null when the iterator is on end
	 * @param doc 
	 */
	protected abstract String setNextUrl(Document doc);

	protected abstract List<T> parse(Document doc);	
}
