package afront;

public class Edge {
	private int id;  //id号
	private String type;  //类型
	
	private Node from;  //fromNode
	private Node to;  //toNode
	
	public Edge(int id, Node from, Node to) {
		this.id = id;
		this.type = "null";
		
		from.addNode(to, Direction.out);
		to.addNode(from, Direction.in);
		
		from.addEdge(this, Direction.out);
		to.addEdge(this, Direction.in);
		
		this.from = from;
		this.to = to;
	}
	public Edge(int id, String type, Node from, Node to) {
		this(id, from, to);
		this.type = type;
	}
}
