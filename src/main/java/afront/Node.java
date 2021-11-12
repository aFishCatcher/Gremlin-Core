package afront;

import java.util.*;

public class Node {
	private int id;  //id号
	private String type;  //节点的类型
	
	List<Node> out_nodes;  //出节点
	List<Node> in_nodes;  //入节点
	List<Edge> out_edges;  //出边
	List<Edge> in_edges;  //入边
	
	public Node(int id, String type) {
		this.id = id;
		this.type = type;
		
		out_nodes = new ArrayList<>();
		in_nodes = new ArrayList<>();
		out_edges = new ArrayList<>();
		in_edges = new ArrayList<>();
	}
	
	public String message() {
		String mes = "";
		mes += this.id + ":" + this.type + " -> ";
		for(Node node : out_nodes) {
			mes += node.id + ",";
		}
		return mes;
	}
	
	public void addNode(Node node, Direction dir) {
		List<Node> nodes;
		if(dir.equals(Direction.in)) nodes = in_nodes;
		else nodes = out_nodes;
		
		nodes.add(node);
	}
	
	public void addEdge(Edge edge, Direction dir) {
		List<Edge> edges;
		if(dir.equals(Direction.in)) edges = in_edges;
		else edges = out_edges;
		
		edges.add(edge);
	}
}
