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
	private static final int MAX_NODES = 15000;

	private long mCrawlTime;
	private AtomicInteger mPos;
	private AtomicBoolean isCapped;
	private CountDownLatch mLatch;
	private ExecutorService mExecutor;
	private ConcurrentLinkedQueue<Page> mPages;
	private ConcurrentLinkedQueue<String> mUrls;
	private ConcurrentLinkedQueue<String> mPagesVisited;

	public Crawler(){
		mCrawlTime = 0;
		mPos = new AtomicInteger();
		isCapped = new AtomicBoolean();
		mLatch = new CountDownLatch(1);
		mPages = new ConcurrentLinkedQueue<Page>();
		mUrls = new ConcurrentLinkedQueue<String>();
		mExecutor = Executors.newFixedThreadPool(5);
		mPagesVisited = new ConcurrentLinkedQueue<String>();
	}

	public int getMAX_THRESHOLD(){
		return MAX_NODES;
	}

	public long getCrawlTime(){
		return mCrawlTime;
	}

	public int updatePos(){
		return mPos.getAndIncrement();	
	}

	public int getPos(){
		return mPos.get();	
	}

	public void setCap(boolean b){
		isCapped.set(b);
	}

	public boolean getCap(){
		return isCapped.get();
	}

	public CountDownLatch getLatch(){
		return mLatch;
	}

	public ConcurrentLinkedQueue<Page> getPages(){
		return mPages;
	}

	public Page[] toPageArray(){
		return mPages.toArray(new Page[0]);
	}

	public ConcurrentLinkedQueue<String> getUrls(){
		return mUrls;
	}

	public int getPagesVisited(){
		return mPagesVisited.size();
	}

	private String nextUrl(){
		String nextUrl;

		do{
			nextUrl = mUrls.poll();
		}while(mPagesVisited.contains(nextUrl));

		if(nextUrl == null) //prevents nullpointerexception if queue is empty
			return null;

		mPagesVisited.add(nextUrl);

		return nextUrl;
	}

	public void search(String url){
		long startTime = System.nanoTime();
		mExecutor.execute(new CrawlerLeg(url, this));
		mPagesVisited.add(url);

		try{
			mLatch.await(); //wait for first iteration to finish
			while(!isCapped.get()){
				String nextUrl = nextUrl();
				if(nextUrl != null)
					mExecutor.execute(new CrawlerLeg(nextUrl, this));
			}
			try{
				mExecutor.shutdown();
	    		mExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		mCrawlTime = (System.nanoTime() - startTime) / 1000000;
	}

	public String getBenchmark(){
		return String.format("Visited %s web page(s) in %s ms", mPagesVisited.size(), mCrawlTime);
	}
}