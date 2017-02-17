package controllers;

import javax.inject.Inject;

import models.Graph;

import utils.Crawler;
import utils.JsonParser;

import play.mvc.Result;
import play.mvc.Controller;
import play.data.FormFactory;

public class CrawlerController extends Controller{
	@Inject
    FormFactory mFormFactory;

	private String mSeed;
	private String mJsonApi;
	private String mBenchmark;

	public Result initSeed(){
		Crawler crawler = new Crawler();
		JsonParser parser = new JsonParser();
		mSeed = mFormFactory.form().bindFromRequest().get("seed");

		crawler.search(mSeed);

		Graph graph = new Graph(crawler.toPageArray());
		mBenchmark = String.format("Initial seed: %s<br>%s<br>%s<br>", mSeed, crawler.getBenchmark(), graph.getBenchmark());
		mJsonApi = parser.createJsonFile(graph);

		return redirect(routes.Application.graph());
	}

	public Result benchmark(){
		if(mBenchmark == null)
			return notFound("");

		return ok(mBenchmark);
	}

	public Result api(){
		if(mJsonApi == null)
			return notFound("");

		return ok(mJsonApi);
	}
}