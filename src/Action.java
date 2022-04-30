/**
 * Class name: Action
 * 
 * This class that represents an action
 * 
 * @author Alberto V�zquez Mart�nez,
 * @author Javier Villar Asensio
 * @author �lvaro Ramos Cobacho
 * 
 */

public class Action {
	private Bottle sourceBottle;
	private Bottle destinationBottle;
	private int quantity;
	
	Action (Bottle sourceBottle, Bottle destinationBottle, int quantity){
		this.sourceBottle = sourceBottle;
		this.destinationBottle = destinationBottle;
		this.quantity = quantity;
	}
	
	/* Getters */
	public Bottle getSource_bottle () {
		return sourceBottle;
	}
	
	public Bottle getDestination_bottle() {
		return destinationBottle;
	}
	
	public int getQuantity () {
		return quantity;
	}
	
	/**
	 * If the action is possible, then delete the quantity in origin bottle and adds in destination bottle
	 * @param origin
	 * @param destination
	 * @param quantity
	 */
	public boolean generateNewState() {
		boolean valid = false;
		
		if (isPossible(sourceBottle, destinationBottle, quantity)) {
			pour(sourceBottle, destinationBottle, quantity);
			valid = true;
		}
		
		return valid;
	}
	
	/**
	 * Check if the action of moving a liquid is possible
	 * @param origin
	 * @param destination
	 * @param quantity
	 * @return
	 */
	public boolean isPossible(Bottle sourceBottle, Bottle destinationBottle, int quantity) {
		return ((hasEnough(sourceBottle, quantity) && 
				fitsIn(destinationBottle, quantity) && 
				sameColour(sourceBottle, destinationBottle)) ? true : false);
	}
	
	/**
	 * Check if origin bottle have any portion
	 * @param bottle
	 * @param required
	 * @return
	 */
	public boolean hasEnough(Bottle bottle, int required) {
		return ((bottle.getQuantityFilled() >= required) ? true : false);
	}
	
	/**
	 * Check if destination bottle have enough space
	 * @param bottle
	 * @param fill
	 * @return
	 */
	public boolean fitsIn (Bottle bottle, int fill) {
		return (((bottle.getMAXquantity()-bottle.getQuantityFilled()) >= fill) ? true : false);
	}
	
	/**
	 * Check if top colour of source bottle is equal to top colour of destination bottle
	 * @param origin
	 * @param destination
	 * @return
	 */
	public static boolean sameColour (Bottle sourceBottle, Bottle destinationBottle) {
		boolean same = false;
		int sourceColor = sourceBottle.getTopColour();
		int destinationColor = destinationBottle.getTopColour();
		
		if (sourceColor != -1) {
			if ((sourceColor == destinationColor) || destinationColor == -1) {
				same = true;
			}
		}

		return same;
	}
	
	/**
	 * Move the portion to destination bottle and remove it in origin bottle
	 * @param origin
	 * @param destination
	 * @param pour
	 * @param bottleList
	 * @return
	 */
	public void pour(Bottle sourceBottle, Bottle destinationBottle, int pour) {
		
		for (int i = 0; i < pour; i++) {
			int originColour = sourceBottle.getTopColour();
			sourceBottle.spill();	
			destinationBottle.receive(originColour);		
		}
	}
}
