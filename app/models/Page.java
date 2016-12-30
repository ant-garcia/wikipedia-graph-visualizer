package models;

public class Page{
	private int mId;
	private String mUrl;
	private String mName;
	private String mParent;
	private String mParentUrl;

	public Page(String parent, String name){
		mUrl = name;
		mParentUrl = parent;
		mName = formatUrl(name);
		mParent = formatUrl(parent);
	}
	
	public void setId(int id){
		mId = id;
	}

	public int getId(){
		return mId;
	}

	public String getParentUrl(){
		return mParentUrl;
	}

	public String getUrl(){
		return mUrl;
	}

	public boolean isRoot(){
		return mParentUrl.equals(mUrl);
	}

	public String getName(){
		return mName;
	}

	public String getParent(){
		return mParent;
	}

	public String toString(){
		return "ID: " + mId + "\nPARENT: " + mParentUrl + "\nCURRENT: " + mUrl + "\n";
	}

	public int hashCode(){
		return mUrl.hashCode();
	}

	public boolean equals(Object obj){
		if(!(obj instanceof Page))
			return false;

		Page p = (Page) obj;
		return mUrl.equals(p.mUrl) && mParentUrl.equals(p.mParentUrl);
	}

	private String formatUrl(String url){
		return url.replace("https://en.wikipedia.org/wiki/", "");
	}
}