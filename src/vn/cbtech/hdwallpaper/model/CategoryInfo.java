package vn.cbtech.hdwallpaper.model;

public class CategoryInfo {

	String title;
	int numberOfItems;
	String coverRes;

	public String getCoverRes() {
		return coverRes;
	}

	public void setCoverRes(String coverRes) {
		this.coverRes = coverRes;
	}

	public CategoryInfo(String title, int number, String cover) {
		this.title = title;
		this.numberOfItems = number;
		this.coverRes = cover;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

}
