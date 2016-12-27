package models;

public class Edge implements Comparable<Edge>{
	private int mWeight;
	private Vertex mFrom, mTo;

	public Edge(Vertex v1, Vertex v2){
		this.mFrom = v1;
		this.mTo = v2;
		this.mWeight = 1;
	}

	public Edge(Vertex v1, Vertex v2, int w){
		this.mFrom = (v1.getLabel().compareTo(v2.getLabel()) <= 0) ? v1 : v2;
        this.mTo = (this.mFrom == v1) ? v2 : v1;
        this.mWeight = w;
	}

	public Vertex getNeighbor(Vertex current){
		if(!(current.equals(mFrom) || current.equals(mTo)))
			return null;

		return (current.equals(mFrom)) ? mTo : mFrom;
	}

	public Vertex getFrom(){
		return this.mFrom;
	}

	public Vertex getTo(){
		return this.mTo;
	}

	public int getWeight(){
		return this.mWeight;
	}

	public void setWeight(int weight){
		this.mWeight = weight;
	}

	public int compareTo(Edge e){
		return this.mWeight - e.mWeight;
	}

	public String toString(){
		return mFrom.getId() + " -> " + mTo.getId();
	}

	public int hashCode(){
        return (mFrom.getLabel() + mTo.getLabel()).hashCode();
    }

	public boolean equals(Object obj){
        if(!(obj instanceof Edge))
            return false;

        Edge e = (Edge)obj;
        return e.mFrom.equals(this.mFrom) && e.mTo.equals(this.mTo);
    }  
}