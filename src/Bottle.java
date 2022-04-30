/**
 * Class name: Bottle
 * 
 * This class represents a bottle object with attribute portions
 * 
 * @author Alberto Vázquez Martínez
 * @author Javier Villar Asensio
 * @author Alvaro Ramos Cobacho
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Iterator;

public class Bottle {
	
	private Stack<Portion> portions = new Stack<Portion>();
	private int MAXquantity;
	
	public Bottle(List<Portion> portions, int MAXquantity) {
		this.portions = generateStackfromList(portions); /* Transform list to stack of portions */
		this.MAXquantity = MAXquantity;
	}
	
	/* Getters */
	public Stack<Portion> getPortions() {
		return this.portions;
	}
	
	public int getTopColour() {
		int colour = 0;
		if (portions.empty()) { /* Check if bottle is empty */
			colour = -1;
		} else {
			colour = portions.peek().getColour();
		}
		return colour;
	}
	
	public int getTopQuantity() {
		return ((!this.portions.isEmpty()) ? portions.peek().getQuantity() : -1);
	}
	
	public int getMAXquantity() {
		return MAXquantity;
	}
	
	public int getQuantityFilled() {
		int filled = 0;
		for (int i = 0; i < portions.size(); i++) {
			filled += portions.get(i).getQuantity();
		}
		return filled;
	}
	
	public List<Integer> getColours() {
		List<Integer> colours = new ArrayList<Integer>();
		for (int i = 0; i < portions.size(); i++) {
			colours.add(portions.get(i).getColour());
		}
		return colours;	
	}
	
	public List<Integer> getAmounts() {
		List<Integer> amounts = new ArrayList<Integer>();
		for (int i = 0; i < portions.size(); i++) {
			amounts.add(portions.get(i).getQuantity());
		}
		return amounts;
	}
	
	/* Setters */
	public void setPortions(Stack<Portion> portions) {
		this.portions = portions;
	}
	public void addPortion(Portion port) {
		portions.add(port);
	}
	
	public void clearBottle() {
		portions.clear();
	}
	
	/**
	 * Transform a list of portions into a stack of portions
	 * @param portions
	 * @return
	 */
	public Stack<Portion> generateStackfromList(List<Portion> portions) {
		Stack<Portion> stackPortions = new Stack<Portion>();
		
		for (int i = portions.size()-1; i >= 0; i--) {
			stackPortions.push(portions.get(i));
		}
		
		return stackPortions;
	}
	
	/**
	 * Remove one part of the portion in origin bottle
	 */
	public void spill() {
		if(this.getTopQuantity() == 1) {
			this.portions.pop();
		}
		else {
			this.portions.peek().decrease();
		}
	}
	
	/**
	 * Add one part of the portion in destination bottle
	 * @param colour
	 */
	public void receive(int colour) {
		if (this.portions.empty()) {
			Portion p = new Portion(colour, 1);
			this.portions.add(p);
		} else if (this.portions.peek().getColour() == colour) {
			this.portions.peek().increase();
		} else {
			Portion p = new Portion(colour, 1);
			this.portions.add(p);
		}
	}
	
	/**
	 * Check if a bottle has only one color
	 * @return
	 */
	public boolean oneColour() {
		Iterator<Portion> it = portions.iterator();
		boolean one = true;
		Portion portion;
		
		if (it.hasNext()) {
			portion = it.next();
			int colour = portion.getColour();
			while (it.hasNext()) {
				portion = it.next();
				if (portion.getColour()!=colour) {
					one = false;
					break;
				}
			}
		}
		return one;
	}
}
