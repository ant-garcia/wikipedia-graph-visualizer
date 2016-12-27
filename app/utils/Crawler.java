package utils;

import models.Page;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Crawler{
	private static final int MAX_PAGES_TO_SEARCH = 70;
	private static final int MAX_NODES = 18000;

	private AtomicInteger mPos;
	private AtomicBoolean isCapped;
	private CountDownLatch mLatch;
	private ExecutorService mExecutor;
	private ConcurrentLinkedQueue<Page> mPages;
	private ConcurrentLinkedQueue<String> mUrls;
	private ConcurrentLinkedQueue<String> mPagesVisited;

	public Crawler(){
		this.mPos = new AtomicInteger();
		this.isCapped = new AtomicBoolean();
		this.mLatch = new CountDownLatch(1);
		this.mExecutor = Executors.newFixedThreadPool(5);
		this.mPagesVisited = new ConcurrentLinkedQueue<String>();
		this.mPages = new ConcurrentLinkedQueue<Page>();
		this.mUrls = new ConcurrentLinkedQueue<String>();
	}

	public int updatePos(){
		return this.mPos.getAndIncrement();	
	}

	public int getPos(){
		return this.mPos.get();	
	}

	public void setCap(boolean b){
		this.isCapped.set(b);
	}

	public boolean getCap(){
		return this.isCapped.get();
	}

	public CountDownLatch getLatch(){
		return this.mLatch;
	}

	public ConcurrentLinkedQueue<Page> getPages(){
		return this.mPages;
	}

	public ConcurrentLinkedQueue<String> getUrls(){
		return this.mUrls;
	}


	private String nextUrl(){
		String nextUrl;

		do{
			nextUrl = this.mUrls.poll();
		}while(this.mPagesVisited.contains(nextUrl));

		if(nextUrl == null)
			return null;

		this.mPagesVisited.add(nextUrl);

		return nextUrl;
	}

	public void search(String url){
		mExecutor.execute(new CrawlerLeg(url, this));
		this.mPagesVisited.add(url);
		try{
			this.mLatch.await(); //wait for first iteration to finish
			while(!this.isCapped.get()){
				String nextUrl = nextUrl();
				if(nextUrl != null)
					mExecutor.execute(new CrawlerLeg(nextUrl, this));
			}
			try{
				mExecutor.shutdown();
	    		mExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	    		System.out.println(String.format("**Done** Visited %s web page(s)", this.mPagesVisited.size()));
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}