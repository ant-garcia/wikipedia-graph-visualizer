package utils;

import models.Page;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CrawlerLeg implements Runnable{
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
                        + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private String  mUrl;
	private Crawler mCrawler;
	private Document mHTMLDocument;

	public CrawlerLeg(String url, Crawler crawler){
		mUrl = url;
		mCrawler = crawler;
	}

	public void run(){
		if(mCrawler.getCap()) //exit only if cap is hit
			return;
		
		crawl();
		mCrawler.getLatch().countDown();
	}

	private void crawl(){
		try{
			Connection conn = Jsoup.connect(mUrl);
			Document doc = conn.get();
			mHTMLDocument = doc;
			Elements linksOnPage = mHTMLDocument.select("a[href^=/wiki/]");

			for(Element link : linksOnPage){
				String absUrl = link.absUrl("href");
				if(shouldVisit(absUrl) && !mUrl.equals(absUrl)){
					Page p = new Page(mUrl, absUrl);
					if(!mCrawler.getPages().contains(p)){
						p.setId(mCrawler.updatePos());
						mCrawler.getPages().add(p);
						mCrawler.getUrls().add(absUrl);
						if(mCrawler.getPos() >= mCrawler.getMAX_THRESHOLD()){
							mCrawler.setCap(true);
							return;
						}
					}
				}
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private boolean shouldVisit(String url){
		String href = url.toLowerCase();
		int count = 0;
		for(int i = 0; i < href.length();i++)
			if(href.charAt(i) == ':' || href.charAt(i) == '#')
				count++;
		if(count > 1)
			return false;
		return !FILTERS.matcher(href).matches() &&
			href.startsWith("https://en.wikipedia.org/wiki/") &&
			!href.equals("https://en.wikipedia.org/wiki/main_page"); 
	}
}