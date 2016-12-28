package utils;

import models.Edge;
import models.Graph;
import models.Vertex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParser{

	private JSONObject initNode(Vertex v){
		JSONObject obj = new JSONObject();

		obj.put("id", v.getId());
		obj.put("x", Math.random());
		obj.put("y", Math.random());
		obj.put("label", v.getLabel());
		obj.put("size", v.getEdgeCount());
		obj.put("color", String.format("#%X", v.getPage().getParent().hashCode()));

		return obj;
	}

	private JSONObject initLink(Edge e){
		JSONObject obj = new JSONObject();

		obj.put("id", e.hashCode());
		obj.put("target", e.getTo().getId());
		obj.put("source", e.getFrom().getId());

		return obj;
	}

	public String createJsonFile(Graph g){
		JSONArray nodes = new JSONArray();
		JSONArray links = new JSONArray();
		JSONObject graph = new JSONObject();

		for(Vertex v : g.getVertices())
			nodes.add(initNode(v));

		for(Edge e : g.getEdges())
			links.add(initLink(e));

		graph.put("nodes", nodes);
		graph.put("edges", links);

		return graph.toJSONString();
	}
}