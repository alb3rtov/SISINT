/**
 * Class name: Node
 * 
 * This class represents a node object with attributes: ID, cost, state, IDParent, action, depth, heuristic and value
 * 
 * @author Alberto VÃ¡zquez MartÃ­nez
 * @author Javier Villar Asensio
 * @author Ã�lvaro Ramos Cobacho
 * 
 */

import java.util.List;

public class Node {
	private int ID;
	private float cost;
	private List<Bottle> state;
	private Node parent;
	private Action action;
	private int depth;
	private float heuristic;
	private float value;
	
	Node(int ID, float cost,  List<Bottle> state, Node parent, Action action, int depth, float heuristic, float value){
		this.ID = ID;
		this.cost = cost;
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.depth = depth;
		this.heuristic = heuristic;
		this.value = value;
	}
	
	//------------------GETTERS---------------------//
	
	public int getID () {
		return ID;
	}
	public float getCost() {
		return cost;
	}
	public List<Bottle> getState(){
		return state;
	}
	public Node getParent() {
		return parent;
	}
	public Action getAction() {
		return action;
	}
	public int getDepth() {
		return depth;
	}
	public float getHeuristic() {
		return heuristic;
	}
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
}