package controllers;

import views.html.index;
import views.html.graph;

import play.mvc.Result;
import play.mvc.Controller;

public class Application extends Controller {
    public Result index() {
        return ok(index.render());
    }

    public Result graph(){
    	return ok(graph.render());
    }
}
