import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// every instance of the class is one little box on the screen
public class CustomBox {

	private static final int N = Pseudoku.N;

	// the box that displays the initial "0" value
	public Rectangle outline;
	public int row;
	public int column;

	// what is displayed in the outline, initially set to "0"
	public Text text;

	// the small box the holds the letter in the upper right of the outline
	// Rectangle variable
	public Rectangle letterBox;

	// the letter in the upper right of the outline Rectangle variable when
	// lines are drawn, initially blank
	public Text letterText;

	// true of this box is part of a line-series (part of an equation)
	// and false if it is not
	public boolean partOfLine;

	// Holds the value of the letter that is in the upper right of a box.
	// This is to bypass having to reference
	// the text in order to do numeral operations, such as for the
	// auto-generation and processing of randomly generated matrices
	public String letter;

	// "int value" holds the numerical value equal to the CustomBox "text"
	// variable, to bypass having to reference
	// the text in order to do numeral operations, such as for the
	// auto-generation and processing of a
	// large number of randomly generated matrices
	private int value;

	// This variable is used for auto-generation. When true, it indicates that
	// this box's value cannot be changed, because it was specified by the user,
	// directly or indirectly.
	public boolean immutable;

	public CustomBox(double x, double y) {

		value = 0;
		letter = " ";
		partOfLine = false;
		immutable = false;

		outline = new Rectangle(Pseudoku.BOX_HEIGHT, Pseudoku.BOX_HEIGHT);

		outline.setStroke(Color.BLACK);
		outline.setFill(Color.TRANSPARENT);
		outline.setOpacity(.3);

		outline.setX(x);
		outline.setY(y);

		// aligns text position in a visually pleasing way
		text = new Text(x + (Pseudoku.BOX_HEIGHT / 3), (Pseudoku.BOX_HEIGHT + y) - (Pseudoku.BOX_HEIGHT / 3), "0");

		text.setFill(Color.TRANSPARENT);
		text.setStroke(Color.BLACK);

		// sets size and position of letter box in the upper right of each
		// small box
		letterBox = new Rectangle(Pseudoku.BOX_HEIGHT / 3, Pseudoku.BOX_HEIGHT / 3);

		// makes the letter in the upper right invisible, initially
		letterBox.setStroke(Color.TRANSPARENT);
		letterBox.setFill(Color.TRANSPARENT);
		letterBox.setOpacity(.5);

		letterBox.setX(x + Pseudoku.BOX_HEIGHT - (Pseudoku.BOX_HEIGHT / 3));
		letterBox.setY(y);

		// align the little letter in the upper right of the box in a
		// visually pleasing way
		letterText = new Text(letterBox.getX() + 1, letterBox.getY() + Pseudoku.BOX_HEIGHT / 3 - 2, " ");
		letterText.setFill(Color.TRANSPARENT);
		letterText.setStroke(Color.BLACK);

		// create a thin font for the letter box
		Font letterFont = Font.font("Courier New", FontWeight.THIN, FontPosture.REGULAR, 12);
		letterText.setFont(letterFont);

		text.setOnMouseClicked(e -> textClicked());
		outline.setOnMouseClicked(e -> textClicked());
		letterBox.setOnMouseClicked(e -> textClicked());
		letterText.setOnMouseClicked(e -> textClicked());

	}

	// Handle text click
	public void textClicked() {

		// don't change the screen if it is locked
		if(Pseudoku.screenLocked)
			return;

		// cycles through values between 0 and N-1 with every mouse click
		if (this.getValue() < (N - 1)) {

			this.setValue(this.getValue() + 1);

		} else {
			this.setValue(0);
		}

		/**
		 * text.setText(Integer.toString(value));
		 *
		 * Pseudoku.addUpRows(); Pseudoku.addUpColumns();
		 * Pseudoku.addUpBigBoxes();
		 *
		 */

	}

	public int getValue() {
		return value;
	}

	// This method ensures that whenever a value is updated, the summation boxes
	// are also updated
	public void setValue(int x) {

		this.value = x;
		this.text.setText(Integer.toString(x));

		// if this is a summation box ...
		if (this.row == N * N || this.column == N * N) {

			// Then change its color to green if sum is N and to transparent if
			// sum is not N
			if (x == N) {
				this.outline.setFill(Color.GREEN);
			} else {
				this.outline.setFill(Color.TRANSPARENT);
			}
		} else {


			Pseudoku.addUpRow(this.row);
			Pseudoku.addUpColumn(this.column);
			Pseudoku.addUpBigBox(this.row, this.column, false);


		}

	}
}