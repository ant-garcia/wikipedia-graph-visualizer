package controllers;

import javax.inject.Inject;

import models.Page;
import models.Edge;
import models.Graph;
import models.Vertex;

import utils.Crawler;
import utils.JsonParser;

import play.mvc.Result;
import play.mvc.Controller;
import play.data.FormFactory;

public class CrawlerController extends Controller{
	@Inject
    FormFactory formFactory;
	private static String seed;

	public Result initCrawl(){
		seed = formFactory.form().bindFromRequest().get("seed");
		Crawler crawler = new Crawler();
		JsonParser parser = new JsonParser();
		long startTime = System.nanoTime();
		crawler.search(seed);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		Graph graph = initGraph(crawler.getPages().toArray(new Page[0]));
		System.out.println("PAGES: " + crawler.getPages().size() + " IN: " + duration + "ms");

		parser.createJsonFile(graph);
		return redirect(routes.Application.graph());

	}

	public Graph initGraph(Page[] pages){
		Graph g = new Graph();
		for(Page p : pages){
			Vertex v = new Vertex(p);
			if(!g.addVertex(v))
				System.out.println("FAILED: \n" + v.getPage().toString());				
		}

		for(Vertex v : g.getVertices()){
			Vertex parent = g.getVertex(v.getId(), v.getPage().getParent());
			if(parent != null && !v.getPage().isRoot()){
				g.addEdge(parent, v);
				Vertex dup = g.getVertex(v.getId(), v.getLabel());
				if(dup != null && v.getId() < dup.getId()){
					g.addEdge(g.getVertex(dup.getId(), dup.getPage().getParent()), v);
					g.removeVertex(dup.getId());
				}	
			}
		}

		return g;
	}
}