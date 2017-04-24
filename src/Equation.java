
import java.util.*;
import javafx.scene.text.Text;

// This is a class that represents one equation, but it is able to perform functions on a set of equations too. This might be conceptually confusing at first.
public class Equation {

	// the String of the hashmap represents the letter variables of the equation
	// and the Integer represents the coefficient value of that variable
	public TreeMap<String, Integer> map;


	public Equation() {

		map = new TreeMap<>();
	}

	public void reset() {
		map.clear();
	}


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

	// removes redundant equations from an ArrayList of Equations
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

	// Adds missing variables, with a coefficient of 0, to all equations so as
	// to equalize length for linear algebra decomposition solving.
	// Returns the total number of variables among the equations.
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

	// displays equation list and solution set in the equationBox pane
	public static void display(ArrayList<Equation> arrayList, boolean freeVariablesPresent) {

		// clears the pane that displays equations and solutions in preparation
		// for display
		Pseudoku.equationBox.getChildren().clear();

		// if vertex found, notify user
		if(!freeVariablesPresent){
			Pseudoku.equationBox.getChildren().add(new Text("THIS IS A VERTEX!"));
			Pseudoku.equationBox.getChildren().add(new Text(""));
		}

		// display equation list
		Pseudoku.equationBox.getChildren().add(new Text("EQUATIONS:"));
		Pseudoku.equationBox.getChildren().add(new Text(""));

		for (int i = 0; i < arrayList.size(); i++) {

			Pseudoku.equationBox.getChildren().add(new Text(arrayList.get(i).toString()));

		}



	}

	// This function solves a system of linear equations and returns true if
	// free variables are present
	// and returns false if no free variables are present.
	public static boolean solve(ArrayList<Equation> arrayList, HashSet<String> setOfAllVariables) {

		int numberOfVariables = setOfAllVariables.size();

		// create a 2-dimensional array of type double to hold coefficients of
		// the equations

		int numberOfEquations = arrayList.size();

		/*
		if (numberOfVariables < numberOfEquations) {
			System.out.println("number of variables is less than number of equations.");
			display(arrayList);
			System.out.println("Number of variables: " + numberOfVariables);
			System.out.println("Number of equations: " + numberOfEquations);
			printEquations(arrayList);
			System.out.println("---");

		}
		*/

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

		if(numberOfVariables < numberOfEquations){
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
			freeVariablesPresent = containsFreeVariables(coefficients, constants);
		}


		return freeVariablesPresent;

	} // end of solve function

	// This function checks whether the system of equations contains a free
	// variable and
	// returns true if so and false if not. The system of equations is
	// represented by a
	// matrix of coefficients and an array of constants (RHS).
	public static boolean containsFreeVariables(double[][] coefficients_original, double[] constants_original) {

		// make copy of coefficients and constants so as not to
		// modify the original matrices
		double[][] coefficients = new double[coefficients_original.length][coefficients_original[0].length];
		double[] constants = new double[constants_original.length];

		coefficients = copyMatrix(coefficients_original);
		constants = copyArray(constants_original);


		// FIRST, REDUCE MATRIX TO REDUCED ECHELON FORM
		int N = constants.length;

		for (int k = 0; k < N; k++) {
			/** find pivot row **/
			int max = k;
			for (int i = k + 1; i < N; i++)
				if (Math.abs(coefficients[i][k]) > Math.abs(coefficients[max][k]))
					max = i;

			/** swap row in A matrix **/
			double[] temp = coefficients[k];
			coefficients[k] = coefficients[max];
			coefficients[max] = temp;

			/** swap corresponding values in constants matrix **/
			double t = constants[k];
			constants[k] = constants[max];
			constants[max] = t;

			/** pivot within A and B**/
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

	// this function makes a copy of a matrix and returns that copy
	public static double[][] copyMatrix(double[][] matrix){

		double[][] temp = new double[matrix.length][matrix[0].length];

		for(int row = 0; row < matrix.length; row++){
			for(int col = 0; col < matrix[0].length; col++){
				temp[row][col] = matrix[row][col];
			}
		}

		return temp;
	}

	// this function makes a copy of an array and returns that copy
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
	public static void printMatrix(double[][] matrix, double[] constants) {
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				System.out.print(matrix[row][col] + " ");
			}
			System.out.print(" = " + constants[row]);
			System.out.println("");
		}

	}

	// prints equation list, for debugging
	public static void printEquations(ArrayList<Equation> arrayList) {

		for (int i = 0; i < arrayList.size(); i++) {

			System.out.println(arrayList.get(i).toString2());

		}

	}

	// converts equation to string in a way helpful for debugging rather than
	// for
	// user to understand
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

			// does not display the coefficient if it is a 1
			// if (entry.getValue() == 1) {
			// temp = temp + entry.getKey() + " + ";
			// }
			// does not display variable or coefficient if the coefficient is 0
			// else if (entry.getValue() == 0) {
			// do nothing
			// } else {
			temp = temp + entry.getValue() + entry.getKey() + " + ";
			// }
		}

		// remove the last " + " of the string and replace it with a " = 0";
		temp = temp.substring(0, temp.length() - 3);

		temp = temp + " = 0";

		return temp;

		// return (temp.trim());

	}
}
