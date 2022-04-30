/**
 * Class name: Problem
 * 
 * This class represents a problem object
 * 
 * @author Alberto V�zquez Mart�nez
 * @author Javier Villar Asensio
 * @author �lvaro Ramos Cobacho
 * 
 */

public class Problem {

	private String id;
	private int bottleSize;
	private String initState;
	
	public Problem(String id, int bottleSize, String initState) {
		this.id = id;
		this.bottleSize = bottleSize;
		this.initState = initState;
	}
	
	/* Getters */
	public String getId() {
		return this.id;
	}
	
	public int getBottleSize() {
		return this.bottleSize;
	}
	
	public String getInitState() {
		return this.initState;
	}
}
