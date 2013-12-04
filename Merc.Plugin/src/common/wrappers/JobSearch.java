package common.wrappers;

public class JobSearch {

	private String searchKeyword;
	private String searchJobTypeCSV;
	private boolean isFullTime;
	private boolean isTrial;
	private int count;
	private int page;

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchJobTypeCSV() {
		return searchJobTypeCSV;
	}

	public void setSearchJobTypeCSV(String searchJobTypeCSV) {
		this.searchJobTypeCSV = searchJobTypeCSV;
	}

	public boolean isFullTime() {
		return isFullTime;
	}

	public void setFullTime(boolean isFullTime) {
		this.isFullTime = isFullTime;
	}

	public boolean isTrial() {
		return isTrial;
	}

	public void setTrial(boolean isTrial) {
		this.isTrial = isTrial;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
