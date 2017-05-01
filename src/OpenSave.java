
import java.util.Scanner;
import java.io.*;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

/**
 * @author Eugene Prokopenko
 */
public class OpenSave {

	/**
	 * Opens a file dialog allowing user to choose a directory.
	 * <p>
	 * @return the selected directory.
	 */
	public static File getDirectory() {

		Stage mainStage = new Stage();

		DirectoryChooser chooser = new DirectoryChooser();

		chooser.setTitle("Choose Folder");

		File selectedDirectory = chooser.showDialog(mainStage);

		return selectedDirectory;

	}

	/**
	 * Calls the saveMatrix method when user chooses to save a
	 * matrix displayed on the screen.
	 */
	public static void saveMatrixByUser() {

		File filePath = getSaveFilePath();

		if (filePath != null) {
			saveMatrix(filePath);
		}

	}

	/**
	 * Opens a file dialog allowing user to open a file.
	 * <p>
	 * @return path where to save the file.
	 */
	public static File getSaveFilePath() {

		Stage mainStage = new Stage();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Matrix File");

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		File filePath = fileChooser.showSaveDialog(mainStage);

		return filePath;
	}


	/**
	 * Saves the matrix displayed on the screen as a text file by copying
	 * contents of all textboxes from the equationBox pane into a txt file.
	 * <p>
	 * @param filePath the path where file should be saved.
	 */
	public static void saveMatrixByCopyingEquationBox(File filePath) {

		PrintWriter fileOutput = null;

		// java forces handling of the FileNotFoundException here
		try {
			fileOutput = new PrintWriter(filePath);

			// should have a warning here eventually if overwriting an existing
			// file

		} catch (FileNotFoundException e) {

			try {
				fileOutput = new PrintWriter(new FileOutputStream(filePath), true);
			} catch (FileNotFoundException t) {
				System.out.println("PrintWriter FileNotFoundException occured when trying to save file");
			}
		}

		// save the grid
		for (int i = 0; i < Pseudoku.twoDArray.length - 1; i++) {

			for (int j = 0; j < Pseudoku.twoDArray[i].length - 1; j++) {

				fileOutput.print(Pseudoku.twoDArray[i][j].text.getText());
				fileOutput.print(" ");

			}

			fileOutput.print(System.getProperty("line.separator"));
		}

		fileOutput.print(System.getProperty("line.separator"));

		// save Equations and Solutions
		for (Node t : Pseudoku.equationBox.getChildren()) {

			Text temp = (Text) t;
			fileOutput.print(temp.getText());
			fileOutput.print(System.getProperty("line.separator"));
		}

		fileOutput.close();

	}

	/**
	 * Saves the matrix displayed on the screen as a text file by copying
	 * contents of program variables into the txt file.
	 * <p>
	 * @param filePath the path where file should be saved.
	 */
	public static void saveMatrix(File filePath) {

		// System.out.println(filePath.getAbsolutePath());

		PrintWriter fileOutput = null;

		// java forces handling of the FileNotFoundException here
		try {
			fileOutput = new PrintWriter(filePath);

			// should have a warning here eventually if overwriting an existing
			// file

		} catch (FileNotFoundException e) {

			try {
				fileOutput = new PrintWriter(new FileOutputStream(filePath), true);
			} catch (FileNotFoundException t) {
				System.out.println("PrintWriter FileNotFoundException occured when trying to save file");
			}
		}

		// save the grid
		for (int i = 0; i < Pseudoku.twoDArray.length - 1; i++) {

			for (int j = 0; j < Pseudoku.twoDArray[i].length - 1; j++) {

				fileOutput.print(Pseudoku.twoDArray[i][j].text.getText());
				fileOutput.print(" ");

			}

			fileOutput.print(System.getProperty("line.separator"));
		}

		fileOutput.print(System.getProperty("line.separator"));


		// SEE IF YOU CAN PRINT AN ERROR HERE IF THE POOL OF CHARACTERS RAN OUT


		// save equation list
		fileOutput.print("EQUATIONS:");
		fileOutput.print(System.getProperty("line.separator"));
		fileOutput.print(System.getProperty("line.separator"));
		for (int i = 0; i < Pseudoku.equationList.size(); i++) {

			fileOutput.print(Pseudoku.equationList.get(i).toString());
			fileOutput.print(System.getProperty("line.separator"));

		}



		fileOutput.close();

	}

/**
 * Calls the getOpenFileName method to open a matrix file
 * chosen by the user.
 */
	public static void openMatrixByUser() {

		File fileName = getOpenFileName();

		if (fileName != null) {
			openMatrix(fileName);
		}

	}

	/**
	 * Displays a file dialog allowing user to choose a file to open.
	 * <p>
	 * @return name of the file to open, including the path.
	 */
	public static File getOpenFileName() {

		Stage mainStage = new Stage();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Matrix File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		File fileName = fileChooser.showOpenDialog(mainStage);

		return fileName;
	}

	/**
	 * Saves the matrix displayed on the screen as a text file.
	 */
	public static void openMatrix(File fileName) {

		// variable declarations
		Scanner fileInput = null;

		try {
			fileInput = new Scanner(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Scanner FileNotFoundException exception occured while trying to open file");
		}

		for (int i = 0; i < Pseudoku.twoDArray.length - 1; i++) {

			for (int j = 0; j < Pseudoku.twoDArray[i].length - 1; j++) {

				if (fileInput.hasNext()) {

					String token = fileInput.next();
					Pseudoku.twoDArray[i][j].text.setText(token);
					Pseudoku.twoDArray[i][j].setValue(Integer.parseInt(token));

				}

			}


		}

		fileInput.close();

		// Pseudoku.addUpRows();
		// Pseudoku.addUpColumns();
		// Pseudoku.addUpBigBoxes();

	}

}
