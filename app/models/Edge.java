package models;

public class Edge implements Comparable<Edge>{
	private int mWeight;
	private Vertex mFrom, mTo;

	public Edge(Vertex v1, Vertex v2){
		mFrom = v1;
		mTo = v2;
		mWeight = 1;
	}

	public Edge(Vertex v1, Vertex v2, int w){
		mFrom = (v1.getLabel().compareTo(v2.getLabel()) <= 0) ? v1 : v2;
        mTo = (mFrom == v1) ? v2 : v1;
        mWeight = w;
	}

	public Vertex getNeighbor(Vertex current){
		if(!(current.equals(mFrom) || current.equals(mTo)))
			return null;

		return (current.equals(mFrom)) ? mTo : mFrom;
	}

	public Vertex getFrom(){
		return mFrom;
	}

	public Vertex getTo(){
		return mTo;
	}

	public int getWeight(){
		return mWeight;
	}

	public void setWeight(int weight){
		mWeight = weight;
	}

	public int compareTo(Edge e){
		return mWeight - e.mWeight;
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
        return e.mFrom.equals(mFrom) && e.mTo.equals(mTo);
    }  
}