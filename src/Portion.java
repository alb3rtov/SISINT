/**
 * Class name: Portion
 * 
 * This class represents a portion made up colour and quantity
 * 
 * @author Javier Villar Asensio
 * @author Alberto Vázquez Martínez
 * @author Álvaro Ramos Cobacho
 *
 */

public class Portion {
	
	private int colour;
	private int quantity;
	
	public Portion(int colour, int quantity) {
		this.colour = colour;
		this.quantity = quantity;
	}
	
	/* Getters */
	public int getColour() {
		return colour;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	/* Setters */
	public void setQuantity(int newQuantity) {
		quantity = newQuantity;
	}
	
	public void setColour(int newColour) {
		colour = newColour;
	}
	
	/* Modifiers */
	public void decrease() {
		this.quantity = quantity - 1;
	}
	
	public void increase() {
		this.quantity = quantity + 1;
	}
}
