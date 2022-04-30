/**
 * Class name: Successor
 * 
<<<<<<< HEAD
 * This class represents a successor with tuple of Action, New State (bottleList), cumulativeCost
 * 
 * @author Alberto Vázquez Martínez,
=======
 * This class represents a successor object
 * 
 * @author Alberto Vázquez Martínez
>>>>>>> c6c2eeb721ebc2cfed086d1077967385eaf2e81e
 * @author Javier Villar Asensio
 * @author Álvaro Ramos Cobacho
 * 
 */

/**
 * Class name: Successor
 * 
 * This class represents a successor object
 * 
 * @author Alberto Vázquez Martínez
 * @author Javier Villar Asensio
 * @author Álvaro Ramos Cobacho
 * 
 */

import java.util.List;

public class Successor {

		private Action action;
		private List<Bottle> bottleList;
		private float cumulativeCost;
		
		Successor (List<Bottle> bottleList, Action action, float cumulativeCost){
			this.bottleList = bottleList;
			this.action = action;
			this.cumulativeCost = cumulativeCost;
		}
		
		/* Getters */
		public List<Bottle> getBottleList() {
			return this.bottleList;
		}
		
		public Action getAction() {
			return this.action;
		}
		
		public float getCumulativeCost() {
			return this.cumulativeCost;
		}
}