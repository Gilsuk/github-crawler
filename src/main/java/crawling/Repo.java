package crawling;

import java.util.List;

public class Repo {
	
	private int id;
	private String title;
	private int star;
	private String description;
	private List<String> lang;
	private List<String> tag;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getLang() {
		return lang;
	}
	public void setLang(List<String> lang) {
		this.lang = lang;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}

}
