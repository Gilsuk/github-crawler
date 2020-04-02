package crawling;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RepoCrawler extends Crawler<Repo> {

	public RepoCrawler(String url) {
		super(url);
	}

	@Override
	protected String setNextUrl(Document doc) {
		Elements nextAnchor = doc.select("a.next_page");
		String relUrl = nextAnchor.attr("href");
		return "https://github.com" + relUrl;
	}

	@Override
	protected List<Repo> parse(Document doc) {
		
		List<Repo> list = new ArrayList<>();
		
		for (Element elem : doc.select("li.repo-list-item"))
			list.add(makeRepo(elem));

		return list;
	}

	private Repo makeRepo(Element elem) {
		Repo repo = new Repo();
		
		repo.setDescription(parseDescription(elem));
		repo.setLangs(parseLangs(elem));
		repo.setStar(parseStar(elem));
		repo.setTags(parseTags(elem));
		repo.setTitle(getTitle(elem));

		return repo;
	}

	private List<String> parseTags(Element elem) {
		return parseElems(elem.getElementsByClass("topic-tag"), x -> x.text());
	}

	private List<String> parseLangs(Element elem) {
		return parseElems(elem.getElementsByAttributeValue("itemprop", "programmingLanguage"), x -> x.text());
	}

	private int parseStar(Element elem) {
		Element starElem = elem.selectFirst("a.muted-link");
		if (starElem == null) return 0;
		String starStr = starElem.text();
		if (starStr == null) return 0;
		return Integer.parseInt(starStr.trim());
	}

	private String parseDescription(Element elem) {
		Element descElem = elem.selectFirst("p.mb-1");
		if (descElem == null) return "";
		return descElem.text();
	}
	
	private String getTitle(Element elem) {
		Element titleElem = elem.selectFirst("a.v-align-middle");
		if (titleElem == null) return "";
		return titleElem.text();
	}

	private <T> List<T> parseElems(Elements elems, Function<Element, T> func) {
		List<T> list = new ArrayList<>();

		if (elems != null)
			elems.forEach(elem -> list.add(func.apply(elem)));

		return list;
	}

}
