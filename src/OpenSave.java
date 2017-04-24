
import java.util.Scanner;
import java.io.*;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

public class OpenSave {

	public static File getDirectory() {

		Stage mainStage = new Stage();

		DirectoryChooser chooser = new DirectoryChooser();

		chooser.setTitle("Choose Folder");

		File selectedDirectory = chooser.showDialog(mainStage);

		return selectedDirectory;

	}

	public static void saveMatrixByUser() {

		File filePath = getSaveFilePath();

		if (filePath != null) {
			saveMatrix(filePath);
		}

	}

	public static File getSaveFilePath() {

		Stage mainStage = new Stage();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Matrix File");

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		File filePath = fileChooser.showSaveDialog(mainStage);

		return filePath;
	}


	// Saves the matrix displayed on the screen as a text file by copying
	// contents of all textboxes from the equationBox pane into a txt file.
	// NOTE: This method is not currently being used by the program.
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

	// saves the matrix displayed on the screen as a text file by copying
	// contents of program variables into the txt file
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

	public static void openMatrixByUser() {

		File fileName = getOpenFileName();

		// need a failsafe test to make sure the opened file is the same size as
		// the current N

		if (fileName != null) {
			openMatrix(fileName);
		}

	}

	public static File getOpenFileName() {

		Stage mainStage = new Stage();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Matrix File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		File fileName = fileChooser.showOpenDialog(mainStage);

		return fileName;
	}

	// saves the matrix displayed on the screen as a text file
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
