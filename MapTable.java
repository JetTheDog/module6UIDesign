package application;

import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode; 

// Class to link a 2-entry Map into a TableView with 2 columns

// Initial implementation borrowed from https://www.mashen.zone/thread-1911531.htm?user=12
// Then modified for my purposes

public class MapTable 
{ 
	// Class data
	private final TableView<Map.Entry<String, Integer>> table; 
	private final ObservableList<Map.Entry<String, Integer>> data; 
	
	public MapTable(Map map) 
    {
		// Create a TableView and save it
		this.table = new TableView<>(); 
		
		// Link local list to passed-in map
		this.data = FXCollections.observableArrayList(map.entrySet());  
		
		// Create columns
		setUpTable();  
	} 
	
	private void setUpTable()
	{ 
		this.table.setEditable(false);
		this.table.setItems(this.data);
		this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// use fully detailed type for Map.Entry 
		// Column 1 is the word
		TableColumn<Map.Entry<String, Integer>, String> column1 = 
				new TableColumn<Map.Entry<String, Integer>, String>("Word"); 
		column1.setMinWidth(125);
		column1.setSortable(true);
		column1.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> p) 
				-> new SimpleStringProperty(p.getValue().getKey())); 

		// Using Number for Column 2, makes sorting by int work, Integer had issues
		TableColumn<Map.Entry<String, Integer>, Number> column2 = 
				new TableColumn<Map.Entry<String, Integer>, Number>("Count"); 
		column2.setMinWidth(125);
		column2.setSortable(true);
		column2.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Integer>, Number> p) 
				-> new SimpleIntegerProperty(p.getValue().getValue())); 
		
		// Add columns to the table
		this.table.getColumns().setAll(column1, column2);
	} 
	
	// Return the TableView created
	public TableView getTableView()
	{ 
		    return this.table; 
    } 
}