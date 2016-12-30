package controllers;

import views.html.index;

import play.mvc.Result;
import play.mvc.Controller;

public class Application extends Controller {
    public Result index() {
        return ok(index.render());
    }
}
