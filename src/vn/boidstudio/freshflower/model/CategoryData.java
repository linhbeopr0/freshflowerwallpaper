package vn.boidstudio.freshflower.model;

public class CategoryData {

	String thumb;
	String link;
	
	public CategoryData(String link) {
		this.link = link;
	}

	public CategoryData(String link, String thumb) {
		this.thumb = thumb;
		this.link = link;
	}
	
	public String getlink() {
		return link;
	}

	public void setData(String link) {
		this.link = link;
	}

	public String getthumb() {
		return thumb;
	}

	public void setCover(String thumb) {
		this.thumb = thumb;
	}

}
