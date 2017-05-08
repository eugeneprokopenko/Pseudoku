import static org.junit.Assert.*;

import org.junit.Test;

public class EquationTest {

	@Test
	public void rowHasAllZeroesTest() {

		Equation junit = new Equation();

		double[][] coefficients = {{1,1}, {1, 0}, {0, 0}};
		double[] constants = {0, 0, 0};
		int row = 2;

		boolean result = junit.rowHasAllZeroes(coefficients, constants, row);

		// test for true condition
		assertEquals(result, true);

		row = 1;

		result = junit.rowHasAllZeroes(coefficients, constants, row);

		// test for false condition
		assertEquals(result, false);

	}

}
