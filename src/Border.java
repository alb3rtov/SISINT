/**
 * Class name: Border
 * 
 * This class represent the fringe of nodes of the search tree
 * 
 * @author Alberto Vazquez Martinez,
 * @author Javier Villar Asensio
 * @author Alvaro Ramos Cobacho
 * 
 */

import java.util.*;

public class Border {
	private List<Node> border;
	
	public Border() {
		this.border = new ArrayList<Node>();
	}
	
	public Node pop() {
		return this.border.remove(0);
	}
	
	public List<Node> getBorder() {
		return this.border;
	}
	
	public void push(Node node) {
		if (this.border.isEmpty()) {
			this.border.add(node);
		} else {
			
			int size = this.border.size();
			
			for (int i = 0; i < size; i++) {
				if (node.getValue() < this.border.get(i).getValue()) {
					this.border.add(i, node);
					break;
				} 
				else if (node.getValue() == this.border.get(i).getValue()) {
					if (node.getID() < this.border.get(i).getID()) {
						this.border.add(i, node);
						break;
					}
				}
			}
			
			if (size == this.border.size()) {
				this.border.add(size, node);
			}
		}
	}
	
	public boolean isEmpty() {
		return ((this.border.isEmpty()) ? true : false);
	}
}
