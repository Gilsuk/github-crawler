package crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class Crawler<T> implements Iterator<List<T>> {
	
	private String urlToCrawl;
	private int attempt;
	
	public Crawler(String url, int attempt) {
		this.urlToCrawl = url;
		this.attempt = attempt;
	}
	
	@Override
	public boolean hasNext() {
		return urlToCrawl == null ? false : true;
	}

	@Override
	public List<T> next() {
		Document doc;
		try {
			doc = crawl(attempt);
			List<T> items = parse(doc);
			this.urlToCrawl = setNextUrl(doc);
			return items;
		} catch (TooManyAttemptException e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
		
	}
	
	private Document crawl(int attempt) throws TooManyAttemptException {
		
		if (attempt < 1) throw new TooManyAttemptException();
		
		try {
			return Jsoup.connect(urlToCrawl).get();
		} catch (IOException e) { 
			System.out.printf("접속오류, 남은 재시도 횟수: %d, %s\n", attempt, urlToCrawl );
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) { e1.printStackTrace(); }
			
			return crawl(--attempt);
		}
	}

	/**
	 * Should return full url to crawl or null
	 * Return this to null when the iterator is on end
	 * @param doc 
	 */
	protected abstract String setNextUrl(Document doc);

	protected abstract List<T> parse(Document doc);	
}
