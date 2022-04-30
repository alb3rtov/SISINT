/**
 * Class name: Main
 * 
 * This class generate an artifact from a json file and vice versa
 * 
 * @author Alberto Vazquez Martinez,
 * @author Javier Villar Asensio
 * @author Alvaro Ramos Cobacho
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


public class Main {
	/* Constants */
	final static String FILE = "data/Estados.txt";
	final static int DEPTH = 0;
	final static int BREADTH = 1;
	final static int UNIFORM = 2;
	final static int GREEDY = 3;
	final static int A = 4;
	final static Scanner KEYBOARD = new Scanner(System.in);
	
	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		
		int option = displayMenu();
		
		Problem p = importProblem("data/p5.json");
		List<Bottle> state = generateBottleList(p.getInitState(), p.getBottleSize());
		Node node = search(state, option);
		
		if (node == null) {
			System.out.println("\n Solution file will not be generated");
		} else {
			generateFileSolution(p.getId(), node, option);
		}
	}
	
	/**
	 * Generate file with the solution path of nodes
	 * @param problemId
	 * @param node
	 * @param idStrategy
	 */
	public static void generateFileSolution(String problemId, Node node, int idStrategy) {
		String strategy = "";
		
		switch (idStrategy) {
			case 0:
				strategy = "Depth";
				break;
			case 1:
				strategy = "Breadth";
				break;
			case 2:
				strategy = "Uniform";
				break;
			case 3:
				strategy = "Greedy";
				break;
			case 4:
				strategy = "A";
				break;
		}
		
		String filename = problemId + "_" + strategy + ".txt";
		Stack<String> path = generatePath(node);
		
		File file = new File("data/" + filename);
		
		/* Write file */
		try {
			/* If file exists then delete */
			if (!file.createNewFile()) {
				file.delete();
			}
			
			FileWriter myWriter = new FileWriter(file.getAbsoluteFile());
			
			while (!path.isEmpty()) {
				myWriter.write(path.pop() + "\n");
			}
			
			myWriter.close();
			
			System.out.println("\nSolucion generada.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Generate a stack of the path to the solution
	 * @param node
	 * @return
	 */
	public static Stack<String> generatePath(Node node) {
		Stack<String> path = new Stack<String>();
		boolean hasParent = true;
		
		do {
			Node nodeParent = node.getParent();
			String idParent, action;
		
			if (nodeParent == null) {
				
				hasParent = false;
				idParent = "None";
				action = "None";
			} else {
				idParent = "" + nodeParent.getID();
				List<Bottle> currentState = node.getState();
				action = "(" + currentState.indexOf(node.getAction().getSource_bottle()) + ", " + 
							currentState.indexOf(node.getAction().getDestination_bottle()) + ", " + 
							node.getAction().getQuantity() + ")";
			}
			
			int id = node.getID();
			float cost = node.getCost();
			String hash = getHashState(node.getState());
			int depth = node.getDepth();
			float heuristic = node.getHeuristic();
			float value = node.getValue();
			float roundDbl = (float) (Math.round(value*100.0)/100.0);
			
			String s = "[" + id + "]" + "[" + cost + "," + hash + "," + idParent + 
						"," + action + "," + depth + "," + heuristic + "," + roundDbl + "]";
			
			path.push(s);
			node = nodeParent;
		    
		} while (hasParent);
		
		return path;
	}
	
	/**
	 * Display a simple menu for select the strategy
	 */
	public static int displayMenu() {
		boolean notString = false;
		boolean inRange = false;
		int option = 0;
		
		System.out.println("[*] ------ Estrategia a utilizar ------ [*]");
		System.out.println("1.- Depth");
		System.out.println("2.- Breadth");
		System.out.println("3.- Uniform");
		System.out.println("4.- Greedy");
		System.out.println("5.- A*");
		System.out.println("Selecciona una de las opciones (1-5): ");

		do {
			try {
				option = KEYBOARD.nextInt();
				notString = true;

				if (option > 0 && option < 6) {
					inRange = true;
				} else {
					System.out.println("\nIntroduce un número entre 1 y 5: \n");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nError leyendo entero. Reintroducir el dato (1-5): ");
				KEYBOARD.next();
			}

		} while (!notString || !inRange);

		return option-1;
	}
	
	/**
	 * Generate a list of bottles from json arrays (state)
	 * @param array
	 * @param arrayBottles
	 * @param jsonBottle
	 * @param attr
	 * @param jsonParser
	 * @return List 
	 */
	public static List<Bottle> generateBottleList(String array, int bottleSize) {
		
		JsonParser jsonParser = new JsonParser();
		JsonArray arrayBottles = null;
		JsonArray jsonBottle = null;
		JsonArray attr = null;
		
		List<Bottle> bottleList = new ArrayList<Bottle>();
		
		arrayBottles = (JsonArray) jsonParser.parse(array);  /* Treat each state as a jsonarray */	
		for (int i = 0; i<arrayBottles.size(); i++) {
			jsonBottle = (JsonArray) arrayBottles.get(i);

			List<Portion> portions = new ArrayList<Portion>();
			
			for (int j = 0; j<jsonBottle.size(); j++) {
				attr = (JsonArray) jsonBottle.get(j); /* Treat each bottle as a jsonarray */
				Portion auxPortion = new Portion(attr.get(0).getAsInt(), attr.get(1).getAsInt());
				portions.add(auxPortion);						
			}
			
			Bottle b = new Bottle(portions, bottleSize);
			bottleList.add(b);

		}
		
		return bottleList;
	}

	/**
	 * Generate all objects and lists of objects (states) from a representation in a json file
	 * @return
	 */
	public static List<List<Bottle>> generateObjectsFromJson(int bottleSize){
		
		List<List<Bottle>> states = new ArrayList<List<Bottle>>();
		
		String array;
		
		try {
			File f = new File(FILE);
			Scanner r = new Scanner(f);
			
			while (r.hasNextLine()) {
				array = r.nextLine();
				states.add(generateBottleList(array, bottleSize));
			}
			
			/* Print all states */
			for (int i = 0; i < states.size(); i++) {
				System.out.println(states.get(i));
			}
			r.close();
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return states;
	}
	
	/**
	 * Parse a state (list of bottles) to JsonArray
	 * @return JsonArray
	 */
	
	public static JsonArray parseJsonBottles(List<Bottle> state) {
		JsonArray attr = new JsonArray(2), jsonBottle = new JsonArray(), arrayBottles = new JsonArray();
		
		for(Bottle bottle: state) {		/*Iterate each bottle*/
			Stack<Portion> portions = bottle.getPortions();		/*Get every portion contained inside a bottle*/
			Iterator<Portion> iter = portions.iterator();
			
			while(iter.hasNext()) {		/*Iterate each portion and get attributes (color and amount)*/
				Portion portion = portions.pop();	/*Pop the portion once it is processed*/
				
				attr.add(portion.getColour());
				attr.add(portion.getQuantity());
				
				jsonBottle.add(attr);		/*Add the portion to it's bottle*/
				attr = new JsonArray(2);		/*Clear variable "attr"*/
			}
			
			arrayBottles.add(jsonBottle);		/*Add the bottle to it's state*/
			jsonBottle = new JsonArray();		/*Clear variable "jsonBottle"*/
		}
		
		return arrayBottles;
	}

	
	/**
	 * Convert the objects back to a json array and output it 
	 * @return
	 */
	public static void generateJsonFromObjects(List<List<Bottle>> states) {
		//JsonArray arrayStates = new JsonArray(), arrayBottles = new JsonArray(), jsonBottle = new JsonArray(), attr = new JsonArray(2);
		JsonArray arrayStates = new JsonArray();
		File file = new File("data/Estados2.txt");
		
		for(List<Bottle> state: states) {	/*Iterate each state*/
			arrayStates.add(parseJsonBottles(state));		//Add the state to the states array
		}
		
		/* Write file */
		try {
			/* If file exists then delete */
			if (!file.createNewFile()) {
				file.delete();
			}
			
			FileWriter myWriter = new FileWriter(file.getAbsoluteFile());
			
			for(int i = 0; i < arrayStates.size(); i++) {
				myWriter.write(arrayStates.get(i) + "\n");
			}
			myWriter.close();
			
			System.out.println("\nArchivo JSON generado.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate MD5 hash from state string
	 * @param state
	 * @return
	 */
	public static String getHashState(List<Bottle> state) {
		String stringState = "" + parseJsonBottles(state), hashtext = "";
		byte[] messageDigest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			messageDigest = md.digest(stringState.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashtext;
	}
	
	/**
	 * Check if a given state is goal that is all bottles contains only one type of liquid 
	 * @param state
	 * @return
	 */
	public static boolean isGoal(List<Bottle> state) {
		boolean isGoal = true;
		int colorBottles[] = new int[state.size()];
		
		for (int i = 0; i < state.size(); i++) {
			colorBottles[i] = state.get(i).getTopColour();
			Bottle b = state.get(i);
			
			if (!b.getPortions().isEmpty()) {
				int color = b.getPortions().get(0).getColour();
				
				for (int j = 1; j < b.getPortions().size(); j++) {
					if (color != b.getPortions().get(j).getColour()) {
						isGoal = false;
						break;
					}
				}
			}
		}
		
		/* Check if each color is only in one bottle */
		if (isGoal) {
			for (int i = 0; i < colorBottles.length; i++) {
				int color1 = colorBottles[i];
				for (int j = 0; j < colorBottles.length; j++) {
					int color2 = colorBottles[j];
					if (i != j && color1 != -1 && color2 != -1) {
						if (color1 == color2) {
							isGoal = false;
							break;
						}
					}
				}
			}
		}
		
		return isGoal;
	}
	
	/**
	 * Generate a object problem from a json file
	 * @param filename
	 * @return
	 */
	public static Problem importProblem(String filename) {
		BufferedReader br;
		Problem p = null;
		
		try {
			br = new BufferedReader(new FileReader(filename));
			p = new Gson().fromJson(br, Problem.class);  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	/**
	 * Generate a json file from a problem object
	 * @param p
	 */
	public static void exportProblem(Problem p) {
		Gson gson = new Gson();

		try {
			Writer writer = Files.newBufferedWriter(Paths.get("data/problem.json"));
			gson.toJson(p, writer);
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clone a list with no referenced objects
	 * @param list1
	 * @param list2
	 */
	public static void cloneList(List<Bottle> list1, List<Bottle> list2) {
		int maxSize = list1.get(0).getMAXquantity();
		for (int i = 0; i < list1.size(); i++) {
			Stack<Portion> portions = new Stack<Portion>();
			for (int j = list1.get(i).getPortions().size()-1; j >= 0 ; j--) {
				int colour = list1.get(i).getPortions().get(j).getColour();
				int quantity = list1.get(i).getPortions().get(j).getQuantity();
				Portion p = new Portion(colour, quantity);
				portions.push(p);
			}
			Bottle b = new Bottle(portions,maxSize);
			list2.add(b);
		}
	}
	
	/**
	 * Generate all valid successors of a given state
	 * @param initState
	 * @return
	 */
	public static List<Successor> getSuccessors(List<Bottle> initState) {
		List<Successor> successors = new ArrayList<Successor>();
		List<Bottle> currentState = new ArrayList<Bottle>();
		List<Bottle> newState = new ArrayList<Bottle>();
		Successor successor;
		Bottle bottle;
		int sourceBottle;
		int amountLiquid;
		
		cloneList(initState, currentState);
		Iterator<Bottle> bottListIt = currentState.iterator();

		while (bottListIt.hasNext()) {
			bottle = bottListIt.next();
			sourceBottle = currentState.indexOf(bottle);
			amountLiquid = currentState.get(sourceBottle).getTopQuantity();
			
			if (amountLiquid != -1) {
			
				for (int j = 0; j < currentState.size(); j++) {
	
					if (j != sourceBottle) {
	
						cloneList(currentState, newState);
						Action action = new Action(newState.get(sourceBottle), newState.get(j), amountLiquid);
						boolean valid = action.generateNewState();
	
						if(valid) {
							successor = generateSuccessor(newState, sourceBottle, j, amountLiquid);
							successors.add(successor);
						}				
					}
					
					newState.clear();
				}
			}
		}
		
		return successors;
	}

	/**
	 * Generate an object succesor
	 * @param init_state
	 * @param quantity
	 * @param source_bottle
	 * @param destination_bottle
	 * @return
	 */
	public static Successor generateSuccessor(List<Bottle> newState, int sourceBottle, int destinationBottle, int quantity) {
		List<Bottle> stateCopy = new ArrayList<Bottle>();
		cloneList(newState, stateCopy);
		Action action = new Action(stateCopy.get(sourceBottle), stateCopy.get(destinationBottle), quantity);
		Successor successor = new Successor(stateCopy, action, 1);

		return successor;
	}
	
	/**
	 * Main searching algorithm. Given the state the fringe and the strategy,
	 * it finds the path to solve the problem
	 * @param state
	 * @param fringe
	 * @param strategy
	 * @return ending node
	 */
	public static Node search(List<Bottle> state, int strategy) {
		List<List<Bottle>> visitedList = new ArrayList<List<Bottle>>();
		int numNodes = 1;
		boolean end = false;
		Node stepNode = null;
		int iteracion = 0;
		Border border = new Border();
		Node initNode = new Node(0, 0, state, null, null, 0, calculateHeuristic(state), 0);
		calculateValue(initNode, strategy);
		border.push(initNode);
		
		while(!end) {
			iteracion++;
			if (border.isEmpty()) {
				System.out.println("Error in search");
				stepNode = null;
				end = true;
			} else {
				stepNode = border.pop();
				if (isGoal(stepNode.getState())) {
					end = true;
				} else {
					if (visitedList.isEmpty() || !isVisited(visitedList, stepNode.getState(), iteracion)) {
						//System.out.println(iteracion);
						visitedList.add(stepNode.getState());
						numNodes = expand(border, stepNode, numNodes, strategy);
					}			
				}
			}
		}
		return stepNode;
	}
	
	/**
	 * Check if a given state is visited or not, checking a list of visited states
	 * @param visitedList
	 * @param state
	 * @param iteracion
	 * @return
	 */
	public static boolean isVisited(List<List<Bottle>> visitedList, List<Bottle> state, int iteracion) {
		boolean visited = false;
		boolean visitState = true;
		int currentQuantity = 0;
		int quantity = 0;
		
		for (int i = 0; i < state.size(); i++) {
			currentQuantity += state.get(i).getQuantityFilled();
		}
		
		for (int i = 0; i < visitedList.size(); i++) {
			List<Bottle> auxList = visitedList.get(i);
			visitState = true;
			quantity = 0;
			for (int j = 0; j < auxList.size() && visitState; j++) {
				Bottle auxBottle = auxList.get(j);
				if (auxBottle.getPortions().size() == state.get(j).getPortions().size()) {
					for (int k = 0; k < auxBottle.getPortions().size(); k++) {
						if (auxBottle.getPortions().get(k).getColour() == state.get(j).getPortions().get(k).getColour() &&
								auxBottle.getPortions().get(k).getQuantity() == state.get(j).getPortions().get(k).getQuantity()) {
								quantity += auxBottle.getPortions().get(k).getQuantity();
						}
					}
					
				} else {
					visitState = false;
				}
			}
			
			if (quantity == currentQuantity) {
				visited = true;
				break;
			}
		}

		return visited;
	}
	
	/**
	 * Calculates the value of the node depeding on the strategy
	 * @param node
	 * @param strategy
	 */
	public static void calculateValue(Node node, int strategy) {
		switch (strategy) {
			case DEPTH:
				node.setValue((float)1/(node.getDepth()+1));
				break;
			case BREADTH:
				node.setValue(node.getDepth());
				break;
			case UNIFORM:
				node.setValue(node.getCost());
				break;
			case GREEDY:
				node.setValue(node.getHeuristic());
				break;
			case A:
				node.setValue(node.getCost()+node.getHeuristic());
				break;
		}
	}
	
	/**
	 * Inserts in the fringe the successor of a given node
	 * @param fringe
	 * @param step_node
	 * @param n_nodes
	 * @param strategy
	 * @return number of nodes-1 we have in the tree
	 * @throws InterruptedException 
	 */
	public static int expand(Border border, Node stepNode, int numNodes, int strategy) {
		List<Bottle> state = stepNode.getState();
		List<Successor> successors = getSuccessors(state);
		Iterator<Successor> it = successors.iterator();
		Node node;
		Successor successor;

		while(it.hasNext()) {
			successor = it.next();
			
			node = new Node(numNodes, stepNode.getCost() + successor.getCumulativeCost(), successor.getBottleList(), 
							stepNode, successor.getAction(), stepNode.getDepth() + 1, calculateHeuristic(successor.getBottleList()), 0);
			
			calculateValue(node, strategy);
			
			numNodes++;
			border.push(node);

		}
		return numNodes;
	}
	
	/**
	 * Function that calculates the heuristic of each node
	 * @param state
	 * @return
	 */
	public static float calculateHeuristic(List<Bottle> state) {
		List<Integer> foundColours = new ArrayList<Integer>();
		Bottle bottle;
		Iterator<Bottle> it = state.iterator();
		int h = 0;
		
		while (it.hasNext()) {
			bottle = it.next();
			if (bottle.getTopColour() != -1) {
				if (foundColours.contains(bottle.getTopColour())) {
					h++;
				}
				h += bottle.getPortions().size();
				foundColours.add(bottle.getTopColour());
			} else {
				h++;
			}
		}

		return h-state.size();		
	}
}
