package models;

import java.util.Arrays;
import java.util.ArrayList;

public class Vertex{
	private int mId;
	private Page mPage;
	private String mLabel;
	private boolean isChecked = false;
	private ArrayList<Edge> mNeighbors;

	public Vertex(Page p){
		this.mPage = p;
		this.mId = p.getId();
		this.mLabel = p.getName();
		this.mNeighbors = new ArrayList<Edge>();
	}

	public void addEdge(Edge e){
		if(!this.mNeighbors.contains(e))
			this.mNeighbors.add(e);
	}

	public boolean containsEdge(Edge e){
		return this.mNeighbors.contains(e);
	}

	public Edge getEdge(int index){
		return this.mNeighbors.get(index);
	}

	public Edge removeEdge(int index){
		return this.mNeighbors.remove(index);
	}

	public void removeEdge(Edge e){
		this.mNeighbors.remove(e);
	}

	public int getEdgeCount(){
		return this.mNeighbors.size();
	}

	public String getLabel(){
		return this.mLabel;
	}

	public Page getPage(){
		return this.mPage;
	}

	public int getId(){
		return this.mId;
	}

	public void setChecked(boolean check){
		this.isChecked = check;
	}

	public boolean getChecked(){
		return this.isChecked;
	}

	public String toString(){
		return "VERTEX: " + this.mId + " " + this.mLabel + " NEIGHBORS: " + Arrays.toString(getNeighbors().toArray()) + "\n";
	}

	public int hashCode(){
		return this.mPage.hashCode();
	}

	public boolean equals(Object obj){
		if(!(obj instanceof Vertex))
			return false;

		Vertex v = (Vertex) obj;
		return this.mPage.equals(v.mPage);
	}

	public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.mNeighbors);
    }
}