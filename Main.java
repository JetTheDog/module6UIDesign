package application;
	
import static java.util.stream.Collectors.toMap;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.beans.binding.Bindings;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;

public class Main extends Application {
	
	// Word/Count data list
	static Map<String, Integer> wordFrequency;
	
	@Override
	public void start(Stage primaryStage) {
		
		VBox pageVert = new VBox();
		//pageVert.setPrefSize(650, 550);
		//pageVert.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.80));
		pageVert.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.80));
		
		Scene pageScene = new Scene(pageVert,700,600);
		primaryStage.setScene(pageScene);
		Label pageLabel = new Label("Edgar Allan Poe's 'The Raven' words, sortable");
		TableView tableView = new TableView();

		// Create class to manage columns, pass it word list/count
		MapTable mapTable = new MapTable(wordFrequency);
		tableView = mapTable.getTableView();
		
		tableView.setFixedCellSize(25);
	    tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(0.9)));
		// Show it
		pageLabel.setFont(new Font("Calibri", 24));
		primaryStage.setScene(pageScene);
		primaryStage.show();
		
		pageVert.getChildren().add(pageLabel);

		pageVert.getChildren().add(tableView);	
	}
	
	public static void main(String[] args) throws IOException {
		
		// Inital array of words
		ArrayList<String> poemWords = new ArrayList<String>();
		
		// Local data
		File poemFile = new File("ravenPoem.html");
		FileInputStream fis = new FileInputStream(poemFile);
		byte[] data = new byte[(int) poemFile.length()];
		
		try {
			// Read file
			fis.read(data);
			fis.close();
			String entirePoem = new String(data, "UTF-8");
			int firstSpot = entirePoem.indexOf("<h1>"); // Start here
			int lastSpot = entirePoem.indexOf("<!--end chapter-->"); // End here
			entirePoem = entirePoem.substring(firstSpot, lastSpot); // Shortens the page to just the poem
			
			// Remove unwanted chars
			entirePoem = entirePoem.replaceAll("\\<.*?>",""); // Removes HTML tags
			entirePoem = entirePoem.replaceAll("\\.",""); // Removes period
			entirePoem = entirePoem.replaceAll("!"," "); // Removes !
			entirePoem = entirePoem.replaceAll(","," "); // Removes ,
			entirePoem = entirePoem.replaceAll(";"," "); // Removes ;
			entirePoem = entirePoem.replaceAll("\\?"," "); // Removes ?
			entirePoem = entirePoem.replaceAll("\\n"," "); // Removes \n
			entirePoem = entirePoem.replaceAll("\\r"," "); // Removes \r
			entirePoem = entirePoem.replaceAll("[^\\p{ASCII}]", ""); // Removes non-ASII
			
			// Collect just words
			Scanner scanPoem = new Scanner(entirePoem);
			scanPoem.useDelimiter(" |\\n|-"); // White space
			while(scanPoem.hasNext()) {
				String word = scanPoem.next();
				if(word.length() > 0)
					poemWords.add(word);
			}
			scanPoem.close();
			
			// Create count array
			wordFrequency = poemWords.stream().collect(toMap(s -> s, s -> 1, Integer::sum)); // Maps words and counts occurrences
			
			// Debugging
			System.out.println("Sorted words:");
			System.out.println(wordFrequency);
		   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Create UI
		launch(args);
	}
}