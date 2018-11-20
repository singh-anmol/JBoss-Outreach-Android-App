package gci.jbossoutreach.anmol.model;

public class ContributorItem {

	private String name;
	private String contributions;
	private String avatar;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getContributions() {
		return contributions;
	}

	public void setContributions(String contributions) {
		this.contributions = contributions;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContributorItem(String name, String avatar, String url, String contributions) {
		this.name = name;
		this.contributions = contributions;
		this.url = url;
		this.avatar = avatar;
	}



}
