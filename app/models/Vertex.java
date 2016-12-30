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
		mPage = p;
		mId = p.getId();
		mLabel = p.getName();
		mNeighbors = new ArrayList<Edge>();
	}

	public void addEdge(Edge e){
		if(!mNeighbors.contains(e))
			mNeighbors.add(e);
	}

	public boolean containsEdge(Edge e){
		return mNeighbors.contains(e);
	}

	public Edge getEdge(int index){
		return mNeighbors.get(index);
	}

	public Edge removeEdge(int index){
		return mNeighbors.remove(index);
	}

	public void removeEdge(Edge e){
		mNeighbors.remove(e);
	}

	public int getEdgeCount(){
		return mNeighbors.size();
	}

	public String getLabel(){
		return mLabel;
	}

	public Page getPage(){
		return mPage;
	}

	public int getId(){
		return mId;
	}

	public void setChecked(boolean check){
		isChecked = check;
	}

	public boolean getChecked(){
		return isChecked;
	}

	public String toString(){
		return "VERTEX: " + mId + " " + mLabel + " NEIGHBORS: " + Arrays.toString(getNeighbors().toArray()) + "\n";
	}

	public int hashCode(){
		return mPage.hashCode();
	}

	public boolean equals(Object obj){
		if(!(obj instanceof Vertex))
			return false;

		Vertex v = (Vertex) obj;
		return mPage.equals(v.mPage);
	}

	public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(mNeighbors);
    }
}