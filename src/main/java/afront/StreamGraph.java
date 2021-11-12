package afront;

import java.util.*;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.step.branch.BranchStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.branch.ChooseStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.branch.OptionalStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.branch.RepeatStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.branch.UnionStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.filter.ConnectiveStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.filter.NotStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.filter.TraversalFilterStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.GroupStep;

public class StreamGraph {
	private static int next_id = 0;
	
	private List<Node> nodes;  //图中所有节点的集合
	private List<Edge> edges;  //图中所有边的集合
	
	private Node head;  //headNode
	private Node tail;  //tailNode
	
	public StreamGraph() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public void show() {
		String mes = "Nodes: \n";
		for(Node node : nodes) {
			mes += node.message() + "\n";
		}
		System.out.println(mes);
	}
	
	/*生成数据流图，也就是确认nodes、edges集合，以及点与点、点与边之间的关系*/
	public void process(Traversal t) {
		List<Step> stepList = t.asAdmin().getSteps();
		int len = stepList.size();
		for(int i=0; i<len; i++) {
			Step step = stepList.get(i);
			String stepName = step.getClass().getSimpleName();
			switch(stepName) {
			case "AndStep":
				processAndStep((ConnectiveStep) step);
				break;
			case "OrStep":
				processOrStep((ConnectiveStep) step);
				break;
			case "NotStep":
				processNotStep((NotStep) step);
				break;
			case "OptionalStep":
				processOptionalStep((OptionalStep) step);
				break;
			case "TraversalFilterStep":  //where
				processWhereStep((TraversalFilterStep) step);
				break;
			case "ChooseStep":
				processChooseStep((ChooseStep) step);
				break;
			case "UnionStep":
				processUnionStep((UnionStep) step);
				break;
			case "GroupStep":
				processGroupStep((GroupStep) step);
				break;
			case "RepeatStep":
				processRepeatStep((RepeatStep) step);
				break;
			case "GraphStep":  //.V
				head = genNode(stepName);
				tail = head;
				break;
			default:  //normal step
				if(i==0) {
					head = genNode(stepName);
					tail = head;
				}
				else {
					Node cur = genNode(stepName);
					genEdge(tail, cur);
					tail = cur;
				}
				
			}
		}
	}
	
	/*------------------------------------------------------------------------------*/
	private void processAndStep(ConnectiveStep step) {
		List<Traversal> t = step.getSubTraversals();
		
		Node copy_node = genNode("copy");
		Node switch_node = genNode("switch");
		Node and_node = genNode("and");
		
		genEdge(tail, copy_node);
		genEdge(copy_node, switch_node);
		genEdge(and_node, switch_node);
		
		for(int i=0; i<t.size(); i++) {
			StreamGraph subGraph = new StreamGraph();
			subGraph.process(t.get(i));
			genEdge(copy_node, subGraph.head);
			genEdge(subGraph.tail, and_node);
			this.merge(subGraph);
		}
		
		tail = switch_node;
	}
	
	private void processOrStep(ConnectiveStep step) {
		List<Traversal> t = step.getSubTraversals();
		
		Node copy_node = genNode("copy");
		Node switch_node = genNode("switch");
		Node or_node = genNode("or");
		
		genEdge(tail, copy_node);
		genEdge(copy_node, switch_node);
		genEdge(or_node, switch_node);
		
		for(int i=0; i<t.size(); i++) {
			StreamGraph subGraph = new StreamGraph();
			subGraph.process(t.get(i));
			genEdge(copy_node, subGraph.head);
			genEdge(subGraph.tail, or_node);
			this.merge(subGraph);
		}
		
		tail = switch_node;
	}
	
	private void processNotStep(NotStep step) {
		Traversal t = step.getSubTraversal();
		
		Node copy_node = genNode("copy");
		Node switch_node = genNode("switch");
		Node not_node = genNode("not");
		
		genEdge(tail, copy_node);
		genEdge(copy_node, switch_node);
		genEdge(not_node, switch_node);
		
		StreamGraph subGraph = new StreamGraph();
		subGraph.process(t);
		genEdge(copy_node, subGraph.head);
		genEdge(subGraph.tail, not_node);
		this.merge(subGraph);
		
		tail = switch_node;
	}
	
	private void processOptionalStep(OptionalStep step) {
		Traversal t = step.getSubTraversal();
		
		Node copy_node1 = genNode("copy");
		Node copy_node2 = genNode("copy");
		Node mux_node = genNode("mux");
		Node toBoolean_node = genNode("toBoolean");
		
		genEdge(tail, copy_node1);
		genEdge(copy_node1, mux_node);
		genEdge(copy_node2, mux_node);
		genEdge(copy_node2, toBoolean_node);
		genEdge(toBoolean_node, mux_node);
		
		StreamGraph subGraph = new StreamGraph();
		subGraph.process(t);
		genEdge(copy_node1, subGraph.head);
		genEdge(subGraph.tail, copy_node2);
		
		tail = mux_node;
	}
	
	private void processWhereStep(TraversalFilterStep step) {
		Traversal t = step.getSubTraversal();
		
		Node copy_node = genNode("copy");
		Node switch_node = genNode("switch");
		
		genEdge(tail, copy_node);
		genEdge(copy_node, switch_node);
		
		StreamGraph subGraph = new StreamGraph();
		subGraph.process(t);
		genEdge(copy_node, subGraph.head);
		genEdge(subGraph.tail, switch_node);
		
		tail = switch_node;
	}
	
	private void processChooseStep(ChooseStep step) {
		List<Traversal> t = step.getChooseTraversals();  //0->branch, 1->true, 2->false
		
		Node copy_node = genNode("copy");
		Node switch_node = genNode("switch");
		Node merge_node = genNode("merge");
		
		genEdge(tail, copy_node);
		genEdge(copy_node, switch_node);
		
		StreamGraph[] subGraphs = new StreamGraph[3];
		for(int i=0; i<3; i++) {
			subGraphs[i] = new StreamGraph();
			subGraphs[i].process(t.get(i));
			this.merge(subGraphs[i]);
		}
		
		genEdge(copy_node, subGraphs[0].head);
		genEdge(subGraphs[0].tail, switch_node);
		genEdge(switch_node, subGraphs[1].head);
		genEdge(switch_node, subGraphs[2].head);
		genEdge(subGraphs[1].tail, merge_node);
		genEdge(subGraphs[2].tail, merge_node);
		
		tail = merge_node;
	}
	
	private void processUnionStep(UnionStep step) {
		List<Traversal> t = step.getUnionTraversals();
		
		Node copy_node = genNode("copy");
		Node merge_node = genNode("merge");
		
		genEdge(tail, copy_node);
		
		for(int i=0; i<t.size(); i++) {
			StreamGraph subGraph = new StreamGraph();
			subGraph.process(t.get(i));
			genEdge(copy_node, subGraph.head);
			genEdge(subGraph.tail, merge_node);
			this.merge(subGraph);
		}
		
		tail = merge_node;
	}
	
	private void processGroupStep(GroupStep step) {
		List<Traversal> t = step.getSubTraversals();  //0->keyTraversal, 1->valueTraversal
		
		Node copy_node = genNode("copy");
		Node map_node = genNode("map");
		Node addAll_node = genNode("addAll");
		
		genEdge(tail, copy_node);
		genEdge(map_node, addAll_node);
		
		for(int i=0; i<2; i++) {
			
			StreamGraph subGraph = new StreamGraph();
			subGraph.process(t.get(i));
			genEdge(copy_node, subGraph.head);
			genEdge(subGraph.tail, map_node);
			this.merge(subGraph);
		}
	}
	
	private void processRepeatStep(RepeatStep step) {
		List<Traversal> t = step.getSubTraversals(); //0->repeatTraversal, 1->untilTraversal
		
		Node copy_node = genNode("copy");
		Node switch_node = genNode("switch");
		
		genEdge(tail, copy_node);
		genEdge(copy_node, switch_node);
		
		StreamGraph[] subGraphs = new StreamGraph[2];
		
		for(int i=0; i<2; i++) {
			subGraphs[i] = new StreamGraph();
			subGraphs[i].process(t.get(i));
			this.merge(subGraphs[i]);
		}
		genEdge(copy_node, subGraphs[1].head);
		genEdge(subGraphs[1].tail, switch_node);
		genEdge(switch_node, subGraphs[0].head);
		genEdge(subGraphs[0].tail, copy_node);
		
		tail = switch_node;
	}
	
	/*------------------------------------------------------------------------------*/
	private Node genNode(String type) {
		Node node = new Node(next_id++, type);
		nodes.add(node);
		return node;
	}
	
	private void genEdge(Node from, Node to) {
		Edge edge = new Edge(next_id++, from, to);
		edges.add(edge);
	}
	
	private void merge(StreamGraph subGraph) {
		for(Node node: subGraph.nodes) {
			this.nodes.add(node);
		}
		for(Edge edge: subGraph.edges) {
			this.edges.add(edge);
		}
	}
}
