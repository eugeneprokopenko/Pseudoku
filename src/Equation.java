
import java.util.*;
import javafx.scene.text.Text;

/**
 * This is a class that represents one equation, but it is
 * able to perform functions on a set of equations too.
 *
 * @author Eugene Prokopenko
 */
public class Equation {

	// the String of the hashmap represents the letter variables of the equation
	// and the Integer represents the coefficient value of that variable
	public TreeMap<String, Integer> map;


	/**
	 * Constructor for an equation. It creates an empty
	 * TreeMap.
	 */
	public Equation() {

		map = new TreeMap<>();
	}

	/**
	 * A reset method for an equation. It clears the TreeMap.
	 */
	public void reset() {
		map.clear();
	}


	/**
	 * A method that creates a string representation of an
	 * equation, for display purposes.
	 *
	 * @return temp A string representation of an equation's TreeMap.
	 */
	public String toString() {

		String temp = "";

		if (map.isEmpty()) {
			return "Equation is empty";
		}

		// create iterator for the map
		Set<Map.Entry<String, Integer>> mapSet = map.entrySet();
		Iterator<Map.Entry<String, Integer>> itr = mapSet.iterator();
		Map.Entry<String, Integer> entry;

		while (itr.hasNext()) {
			entry = itr.next();

			// does not display the coefficient if it is a 1
			if (entry.getValue() == 1) {
				temp = temp + entry.getKey() + " + ";
			}
			// does not display variable or coefficient if the coefficient is 0
			else if (entry.getValue() == 0) {
				// do nothing
			} else {
				temp = temp + entry.getValue() + entry.getKey() + " + ";
			}
		}

		// remove the last " + " of the string and replace it with a " = 0";
		temp = temp.substring(0, temp.length() - 3);

		temp = temp + " = 0";

		return temp;

		// return (temp.trim());

	}

	/**
	 * Removes redundant equations from an ArrayList of Equations.
	 * This method compares each equation to each other and ensures
	 * that only one unique equation remains.
	 *
	 * @param arrayList contains equations to evaluate.
	 */
	//
	public static void removeRedundantEquations(ArrayList<Equation> arrayList) {

		if (arrayList.size() > 1) {

			for (int i = 0; i < arrayList.size() - 1; i++) {

				for (int j = i + 1; j < arrayList.size(); j++) {

					if (arrayList.get(i).map.equals(arrayList.get(j).map)) {

						arrayList.remove(j);

						// decrease j by one to compensate for the removal of
						// one element in the array list
						j--;

					}

				}

			}
		}
	}

	/**
	 * This method adds missing variables, with a coefficient of 0,
	 * to all equations so as to equalize length.
	 *
	 * @param arrayList contains equations to evaluate.
	 * @return setOfAllVariables set of all the variables among
	 * the equations.
	 */
	public static HashSet<String> equalize(ArrayList<Equation> arrayList) {

		HashSet<String> setOfAllVariables = new HashSet<String>();

		int numberOfEquations = arrayList.size();

		// the for loop creates a set of all variables in all the equations
		for (int i = 0; i < numberOfEquations; i++) {

			Equation eq = arrayList.get(i);

			// create iterator for the equation map
			Set<Map.Entry<String, Integer>> mapSet = eq.map.entrySet();
			Iterator<Map.Entry<String, Integer>> itr = mapSet.iterator();
			// Map.Entry<String, Integer> entry;

			// iterates through the given equation and adds all its variables to
			// the setOfAllVariables
			while (itr.hasNext()) {
				setOfAllVariables.add(itr.next().getKey());
			}

		}

		// this for loop goes through each equation and adds the missing
		// variables to equalize length
		for (int i = 0; i < numberOfEquations; i++) {

			Equation eq = arrayList.get(i);

			Iterator<String> itr = setOfAllVariables.iterator();
			// Map.Entry<String, Integer> entry;

			// iterates through the setOfAllVariables and adds any missing
			// variables to the given equation
			while (itr.hasNext()) {

				String variable = itr.next();

				// if the equation does not contain the variable, then add it
				// with a coefficient of 0
				if (!eq.map.containsKey(variable)) {
					eq.map.put(variable, 0);
				}

			}

		}

		return setOfAllVariables;

	}

	//
	/**
	 * Displays equation list and solution set in the equationBox pane.
	 *
	 *  @param arrayList contains equations to display.
	 *  @param freeVariablesPresent tells the method whether to display
	 *  a message notifying user whether this is a vertex.
	 *
	 */
	public static void display(ArrayList<Equation> arrayList, boolean freeVariablesPresent) {

		// clears the pane that displays equations and solutions in preparation
		// for display
		Pseudoku.equationBox.getChildren().clear();

		// if vertex found, notify user
		if(!freeVariablesPresent){
			Pseudoku.equationBox.getChildren().add(new Text("THIS IS A VERTEX!"));
			Pseudoku.equationBox.getChildren().add(new Text(""));
		}else{

			Pseudoku.equationBox.getChildren().add(new Text("This is not a vertex."));
			Pseudoku.equationBox.getChildren().add(new Text(""));

		}

		// display equation list
		Pseudoku.equationBox.getChildren().add(new Text("EQUATIONS:"));
		Pseudoku.equationBox.getChildren().add(new Text(""));

		for (int i = 0; i < arrayList.size(); i++) {

			Pseudoku.equationBox.getChildren().add(new Text(arrayList.get(i).toString()));

		}



	}

	/**
	 * Solves a system of linear equations and calls the method
	 * containsFreeVariables to determine whether
	 * any free variables are present in the set of equations.
	 *
	 * @param arrayList contains equations to evaluate.
	 * @param setOfAllVariables a set of all the variables among the equations.
	 * @return freeVariablesPresent true if free variables are present,
	 * false otherwise
	 */
	public static boolean solve(ArrayList<Equation> arrayList, HashSet<String> setOfAllVariables) {

		int numberOfVariables = setOfAllVariables.size();

		// create a 2-dimensional array of type double to hold coefficients of
		// the equations

		int numberOfEquations = arrayList.size();


		// Create a square matrix that is as large as needed to hold all
		// variables and equations
		double[][] coefficients = new double[Math.max(numberOfVariables, numberOfEquations)][Math.max(numberOfVariables,
				numberOfEquations)];

		// initialize all values in coefficients matrix to zero
		for (int row = 0; row < coefficients.length; row++) {
			for (int col = 0; col < coefficients[0].length; col++) {
				coefficients[row][col] = 0.0;
			}
		}

		// The for loop adds the coefficients of each equation to the 2D array.
		// Each row represents the coefficients of one equation.
		for (int i = 0; i < numberOfEquations; i++) {

			Equation eq = arrayList.get(i);

			// create iterator for the equation map
			Set<Map.Entry<String, Integer>> mapSet = eq.map.entrySet();
			Iterator<Map.Entry<String, Integer>> itr = mapSet.iterator();

			int column = 0;

			// Iterates through the given equation and adds all its coefficients
			// to a row of the coefficient 2D array
			while (itr.hasNext()) {
				coefficients[i][column] = itr.next().getValue();
				column++;
			}

		}

		// Create an array of constants (RHS of equality) to use for solving
		// equations.
		// The number of constants is equal to the number of equations and all
		// constants are equal to 0.
		double[] constants = new double[Math.max(numberOfVariables, numberOfEquations)];

		// set all constants to N
		for (int i = 0; i < constants.length; i++) {
			//constants[i] = Pseudoku.N;
			constants[i] = 0;
		}


		boolean freeVariablesPresent;


		if(numberOfVariables > numberOfEquations){
			// If there are more variables than equations in the system,
			// then we know that the system is not constrained enough.
			freeVariablesPresent = true;
			//System.out.println("numberOfVariables: " + numberOfVariables);
			//System.out.println("numberOfEquations: " + numberOfEquations);
		}
		else{
			// Find out if system of equations contains free variables
			// by reducing to row echelon form and checking whether one row
			// consists entirely of zeroes

			// check whether at least one equation in the system has no variables that
			// are dependent on any other equation

			freeVariablesPresent = containsFreeVariables(coefficients, constants);
			// boolean equation_lacks_dependency = equationInSystemLacksDependency(arrayList);
		}


		return freeVariablesPresent;

	} // end of solve function


	/**
	 * Checks whether the variables of an equation in the
	 * arrayList of equations lack dependencies in other
	 * equations.
	 * <p>
	 * If an equation has variables, none of which are present
	 * in any other equation, then the variables in that
	 * equation lack dependencies and are therefore free.
	 * THIS METHOD IS NOT CURRENTLY IN USE.
	 *
	 *
	 * @param arrayList contains equations to evaluate.
	 * @return true/false True if any equation's variables
	 * lack dependencies. False, otherwise.
	 */
	//
	public static boolean equationInSystemLacksDependency(ArrayList<Equation> arrayList) {


		// is set to true in the inner for-loop if at least one of
		// a given equation's variables are present in another
		// equation.
		boolean found = false;

		int j;

		if (arrayList.size() > 1) {

			// i is index of equation whose variables are being checked
			for (int i = 0; i < arrayList.size(); i++) {

				// Get the set of all variables from the i equation.
				Set<String> i_variables = arrayList.get(i).map.keySet();

				// reset found to false for a new i equation
				found = false;

				// j is index of equation against which i's variables are
				// being checked
				for (j = 0; j < arrayList.size(); j++) {

					// Get the set of all variables from the j equation.
					Set<String> j_variables = arrayList.get(j).map.keySet();

					// For each variable in i, check whether it is in j.
					// If yes, Set the found variable to true and break out
					// of for-loop to move onto the next i equation.

					// Now we check whether at least one of i's variables is
					// present in j's equation
					for(String i_variable: i_variables){
			            if(i != j && j_variables.contains(i_variable));
			            found = true;
			            break;
			        }



				} // end of j for-loop

				// check if we have checked every j equation but
				// no i variables were found.
				if (found == false && j == arrayList.size()){
					return true;
				}

			} // end of i for-loop

			//
			return false;
		}

		// if equation list is empty, then no variables, including
		// free variables, are present
		return false;
	}

	/**
	 * Checks whether the system of equations contains free variables.
	 * <p>
	 * This method is called by the solve method. The system of equations
	 * is represented by a matrix of coefficients and an array of constants (RHS).
	 *
	 * @param coefficients_original a matrix of coefficients among the equations.
	 * @param constants_original an array of constants (RHS)
	 * @return true/false true if free variables are present,
	 * false otherwise
	 *
	 */
	public static boolean containsFreeVariables(double[][] coefficients_original, double[] constants_original) {

		// make copy of coefficients and constants so as not to
		// modify the original matrices
		double[][] coefficients = new double[coefficients_original.length][coefficients_original[0].length];
		double[] constants = new double[constants_original.length];

		coefficients = copyMatrix(coefficients_original);
		constants = copyArray(constants_original);


		// FIRST, REDUCE MATRIX TO REDUCED ECHELON FORM
		int N = constants.length;

		// The below Gaussian elimination code is a slightly
		// modified version of the code available at:
		// https://ideone.com/plain/fGmEQQ

		for (int k = 0; k < N; k++) {
			/* find pivot row */
			int max = k;
			for (int i = k + 1; i < N; i++)
				if (Math.abs(coefficients[i][k]) > Math.abs(coefficients[max][k]))
					max = i;

			/* swap row in A matrix */
			double[] temp = coefficients[k];
			coefficients[k] = coefficients[max];
			coefficients[max] = temp;

			/* swap corresponding values in constants matrix */
			double t = constants[k];
			constants[k] = constants[max];
			constants[max] = t;

			/* pivot within A and B*/
			for (int i = k + 1; i < N; i++) {

				// Before determining factor by dividing by a number
				// from the kth row, determine whether kth row is all zeroes.
				// If so, then free variables are present. Return true.
				// This also prevents a division by zero error.
				if(rowHasAllZeroes(coefficients, constants, k))
					return true;

				double factor = coefficients[i][k] / coefficients[k][k];
				constants[i] -= factor * constants[k];
				for (int j = k; j < N; j++)
					coefficients[i][j] -= factor * coefficients[k][j];
			}
		}


		// SECOND, EVALUATE EVERY ROW TO SEE IF ANY CONTAINS
		// ALL ZEROES. IF SO, RETURN TRUE. OTHERWISE, FALSE.

		int row;
		for (row = 0; row < coefficients.length; row++) {

			if (rowHasAllZeroes(coefficients, constants, row)){
				return true;
			}
		}

		// No rows of all zeros found, so return false.
		return false;
	}


	/**
	 * Makes a copy of a matrix and returns that copy.
	 *
	 * @param matrix matrix of which to make a copy
	 * @return temp copy of matrix parameter.
	 */
	public static double[][] copyMatrix(double[][] matrix){

		double[][] temp = new double[matrix.length][matrix[0].length];

		for(int row = 0; row < matrix.length; row++){
			for(int col = 0; col < matrix[0].length; col++){
				temp[row][col] = matrix[row][col];
			}
		}

		return temp;
	}

	/**
	 * Makes a copy of an arrya and returns that copy.
	 *
	 * @param array array of which to make a copy
	 * @return temp copy of array parameter.
	 */
	public static double[] copyArray(double[] array){

		double[] temp = new double[array.length];

			for(int i = 0; i < array.length; i++){
				temp[i] = array[i];
			}


		return temp;
	}

	// Function checks a row in a matrix and in a
	// vertical array to see if it is composed
	// entirely of zeroes. If so, returns true. Otherwise, false.

	/**
	 * Checks a row in a matrix to see whether it is composed
	 * entirely of zeroes.
	 *
	 * @param coefficients matrix of all the coefficients
	 * @param constants array of constants (RHS)
	 * @param row number of the row to check
	 * @return true/false true if row has all zeroes, false otherwise
	 */
	public static boolean rowHasAllZeroes(double[][] coefficients, double[] constants, int row){

		int col; // column

		for (col = 0; col < coefficients[0].length; col++) {

			if (coefficients[row][col] != 0) {
				// If row contains a non-zero entry, then we know
				// that this is not a row of all zeroes, so return false.
				return false;
			}
		}
		// if the entire row above was checked without returning,
		// then all coefficients are zero. Now if the RHS is also
		// zero, then return true because a free variable is present.

			if (constants[row] == 0) {
				return true;
			}
			else
				return false;


	}



	// helper function to print matrix, for debugging
	/**
	 * Prints the coefficients of a system of equations
	 * to the console. Used for debugging.
	 *
	 * @param matrix variable coefficients to print
	 * @param constants RHS of equation to print
	 */
	public static void printMatrix(double[][] matrix, double[] constants) {
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				System.out.print(matrix[row][col] + " ");
			}
			System.out.print(" = " + constants[row]);
			System.out.println("");
		}

	}

	/**
	 * Prints a system of equations
	 * to the console. Used for debugging.
	 *
	 * @param arrayList list of equations to print
	 */
	public static void printEquations(ArrayList<Equation> arrayList) {

		for (int i = 0; i < arrayList.size(); i++) {

			System.out.println(arrayList.get(i).toString2());

		}

	}


	/**
	 * A method that creates a string representation of an
	 * equation, for DEBUG display purposes.
	 *
	 * @return temp A string representation of an equation's TreeMap.
	 */
	public String toString2() {

		String temp = "";

		if (map.isEmpty()) {
			return "Equation is empty";
		}

		// create iterator for the map
		Set<Map.Entry<String, Integer>> mapSet = map.entrySet();
		Iterator<Map.Entry<String, Integer>> itr = mapSet.iterator();
		Map.Entry<String, Integer> entry;

		while (itr.hasNext()) {
			entry = itr.next();


			temp = temp + entry.getValue() + entry.getKey() + " + ";

		}

		// remove the last " + " of the string and replace it with a " = 0";
		temp = temp.substring(0, temp.length() - 3);

		temp = temp + " = 0";

		return temp;


	}
}
