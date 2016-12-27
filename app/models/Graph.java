package models;

import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.HashMap;

public class Graph{
	private HashMap<Integer, Edge> mEdges;
	private HashMap<Integer, Vertex> mVertices;

	public Graph(){
		this.mEdges = new HashMap<Integer, Edge>();
		this.mVertices = new HashMap<Integer, Vertex>();
	}

	public boolean addEdge(Vertex v1, Vertex v2){
		if(v1.equals(v2))
			return false;

		Edge e = new Edge(v1, v2);
		
		if(mEdges.containsKey(e.hashCode()))
			return false;
		else if(v1.containsEdge(e) || v2.containsEdge(e))
			return false;

		mEdges.put(e.hashCode(), e);
		v1.addEdge(e);
		v2.addEdge(e);

		return true;
	}

	public boolean addEdge(Vertex v1, Vertex v2, int weight){
		if(v1.equals(v2))
			return false;

        Edge e = new Edge(v1, v2, weight);

        if(mEdges.containsKey(e.hashCode()))
			return false;
		else if(v1.containsEdge(e) || v2.containsEdge(e))
			return false;

		mEdges.put(e.hashCode(), e);
		v1.addEdge(e);
		v2.addEdge(e);

		return true;
	}

	public boolean containsEdge(Edge e){
		if(e.getFrom() == null || e.getTo() == null)
			return false;

		return this.mEdges.containsKey(e.hashCode());
	}

	public Edge removeEdge(Edge e){
		e.getFrom().removeEdge(e);
		e.getTo().removeEdge(e);

		return this.mEdges.remove(e.hashCode());
	}

	public boolean containsVertex(Vertex v){
		return this.mVertices.get(v.getId()) != null;
	}

	public Vertex getVertex(int id){
		return mVertices.get(id);
	}

	public Vertex getVertex(int id, String label){
		return this.mVertices.values().stream()
			.filter(x -> label.equals(x.getLabel()) && x.getId() != id)
			.findAny()
			.orElse(null);
	}

	public boolean addVertex(Vertex v){
		Vertex current = this.mVertices.get(v.getId());
		if(current != null)
			return false;

		mVertices.put(v.getId(), v);
		return true;
	}

	public Vertex removeVertex(int id){
		Vertex v = mVertices.remove(id);

		while(v.getEdgeCount() > 0)
			this.removeEdge(v.getEdge(0));

		return v;
	}

	public Set<Vertex> getVertices(){
		return new HashSet<Vertex>(this.mVertices.values());
	}

	public Set<Edge> getEdges(){
		return new HashSet<Edge>(this.mEdges.values());
	}

	public void dfs(int id){
		Vertex v;
		Stack<Integer> s = new Stack<Integer>();
		HashMap<Integer, Vertex> tmp = new HashMap<>();
		tmp.putAll(this.mVertices);
		s.push(id);
		System.out.print(id);
		while(!s.isEmpty()){
			int i = s.pop();
			if(!tmp.get(i).getChecked()){
				tmp.get(i).setChecked(true);
				for(Edge e : tmp.get(i).getNeighbors())
					s.push(e.getNeighbor(tmp.get(i)).getId());
				System.out.print(id != i ? " -> " + i : "");
			}
		}
		System.out.println("");
		clearFlags();
	}

	public void clearFlags(){
		for(Vertex v : this.mVertices.values())
			mVertices.get(v.getId()).setChecked(false);
	}	
}