package triewrite;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/** TrieWrite demonstrates an end-to-end application with text-file as 'Model', 
 * GUI as 'View', and various event-handlers as 'Controllers'  
 */
public class TrieWrite extends Application {

	static final int MAX_MRU_SEARCH_COUNT = 5; //Number of most-recently-used search terms to be displayed

	/** GUI components **/	
	private Stage stage;
	private BorderPane root = new BorderPane(); 	//holds all GUI components
	private StyleClassedTextArea fileTextArea = new StyleClassedTextArea(); //displays the file content
	
	private Label statusLabel = new Label();		//shows the status of various actions
	private BooleanProperty isFileClosed = new SimpleBooleanProperty(false); //enables or disables File-Save and Close options

	/** Search components **/
	private Menu searchMenu = new Menu("Search");
	private MenuItem searchMenuItem = new MenuItem("Search...");
	private SearchHandler searchToolHandler = new SearchHandler();

	private MRUCache searchTerms = new MRUCache(MAX_MRU_SEARCH_COUNT); // Contains most recently used search terms 

	private AutoCompleter autoCompleter = new AutoCompleter(); //will be bound to fileTextArea for auto-completion 

	private FileUtilities fileUtilities = new FileUtilities();  //helper class

	public static void main(String[] args) {
		launch(args);
	}

	//Start the application
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		setScene();
		Scene scene = new Scene(root, 500, 600);
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		stage.setTitle("TrieWrite 1.0");
		stage.setScene(scene);
		stage.show();
	}

	//Setup Scene and its components.
	private void setScene() {
		//create menus
		Menu fileMenu = new Menu("File");
		Menu toolsMenu = new Menu("Tools");
		Menu helpMenu = new Menu("Help");

		//attach File menu items and their event handlers
		MenuItem newFileMenuItem = new MenuItem("New");
		newFileMenuItem.setOnAction(new NewFileHandler());
		MenuItem openFileMenuItem = new MenuItem("Open");
		openFileMenuItem.setOnAction(new OpenFileHandler());
		MenuItem saveFileMenuItem = new MenuItem("Save");
		saveFileMenuItem.setOnAction(new SaveFileHandler());
		MenuItem closeFileMenuItem = new MenuItem("Close");
		closeFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fileTextArea.clear();
				statusLabel.setText("");
				root.setCenter(null);
				isFileClosed.set(true);
			}
		});
		MenuItem exitFileMenuItem = new MenuItem("Exit");
		exitFileMenuItem.setOnAction(actionEvent->Platform.exit());

		//attach Tools menu items and their event handlers
		searchMenu.getItems().add(searchMenuItem);
		searchMenuItem.setOnAction(new SearchHandler());

		MenuItem wordCountToolsMenuItem = new MenuItem("Word Count");
		wordCountToolsMenuItem.setOnAction(new WordCountHandler());
		MenuItem uniqueWordCountToolsMenuItem = new MenuItem("Unique word count");
		uniqueWordCountToolsMenuItem.setOnAction(new UniqueWordCountHandler());

		//set Help menu
		MenuItem aboutHelpMenuItem = new MenuItem("About");
		aboutHelpMenuItem.setOnAction(new AboutHandler());

		//set menubar
		MenuBar menuBar = new MenuBar();

		fileMenu.getItems().addAll(newFileMenuItem, openFileMenuItem, saveFileMenuItem, closeFileMenuItem, new SeparatorMenuItem(), exitFileMenuItem);
		toolsMenu.getItems().addAll(searchMenu, new SeparatorMenuItem(),wordCountToolsMenuItem, uniqueWordCountToolsMenuItem);
		helpMenu.getItems().addAll(aboutHelpMenuItem);
		menuBar.getMenus().addAll(fileMenu, toolsMenu, helpMenu);	

		//set status bar
		statusLabel.setPrefWidth(this.stage.getMaxWidth());
		statusLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

		//set layout 
		root.setTop(menuBar);
		root.setBottom(statusLabel);

		//initialize bindings
		isFileClosed.set(true);
		toolsMenu.disableProperty().bind(isFileClosed);
		saveFileMenuItem.disableProperty().bind(isFileClosed);
		closeFileMenuItem.disableProperty().bind(isFileClosed);

		//load and bind autoCompleter
		autoCompleter.loadWordsFromFile("dictionary.txt");
		autoCompleter.bindToTextArea(fileTextArea);
	}

	//Bound to File -> New menu option
	private class NewFileHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			fileTextArea.clear();
			fileTextArea.setWrapText(true);
			root.setCenter(fileTextArea);
			isFileClosed.set(false);
		}
	}

	//Bound to File -> Open menu option
	private class OpenFileHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File("./")); //this will open the file chooser in the project folder. DO NOT CHANGE THIS.
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Text Files", "*.txt"),
					new ExtensionFilter("All Files", "*.*"));
			File file = null;
			if ((file = fileChooser.showOpenDialog(stage)) != null)	{
				StringBuilder fileContent = fileUtilities.readFile(file.getAbsolutePath());
				fileTextArea.clear();
				fileTextArea.appendText(fileContent.toString());
				fileTextArea.setWrapText(true);
				statusLabel.setText(file.getName());
				root.setCenter(fileTextArea);
				isFileClosed.set(false);
				fileTextArea.moveTo(0);
				fileTextArea.requestFollowCaret();
				fileUtilities.buildWordTree(fileTextArea.getText());  //create tree 
			}
		}
	}

	//Bound to File -> Save menu option
	private class SaveFileHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File(".")); //This will save the file in the project folder. DO NOT CHANGE THIS.
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Text Files", "*.txt"),
					new ExtensionFilter("All Files", "*.*"));
			File file = null;
			if ((file = fileChooser.showSaveDialog(stage)) != null)	{
				statusLabel.setText(fileUtilities.writeFile(file.getAbsolutePath(), fileTextArea.getText()));

			}
		}
	}

	/** getStringInput is a helper method that opens a dialog box to take user input.
	 * The method returns the string entered by the user.
	 * When user presses Cancel, it returns null.
	 */
	private String getStringInput() {   
		String inputString = null;
		TextInputDialog searchTextInputDialog = new TextInputDialog();
		searchTextInputDialog.setTitle("Search");
		searchTextInputDialog.setHeaderText("Search");
		searchTextInputDialog.setContentText("Enter search string ");
		Optional <String> searchStringOptional = searchTextInputDialog.showAndWait();
		if (searchStringOptional.isPresent()) {
			statusLabel.setText("");
			inputString = searchStringOptional.get();
		}
		if (inputString ==  null || inputString.isEmpty()) return null;  //to handle empty string on Cancel
		else return inputString;
	}

	//Bound to Tools -> Search menu option
	private class SearchHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			fileUtilities.buildWordTree(fileTextArea.getText());  //refresh wordTree

			//get search string input
			String searchString;
			if (((MenuItem)event.getSource()).getText().equals("Search...")) {  //if new search string entered in the search dialog box
				searchString = getStringInput();  
			} else {
				searchString = ((MenuItem)event.getSource()).getText();  //else reuse of old search string
			}
			fileTextArea.clearStyle(0, fileTextArea.getLength()); //clear previous highlights

			if (searchString != null) {
				List<Integer> positions = fileUtilities.getWordPositions(searchString);
				if (positions != null) { //if searchString is found
					for (int position: positions ) {
						fileTextArea.setStyleClass(position, position + searchString.length(), "highlight");
					}
					statusLabel.setText(String.format("%s found %d times", searchString, positions.size()));

					/*** Update search queue */
					searchTerms.addSearchTerm(searchString); //add the most recent search term to MRUCache

					if (searchMenu.getItems().size() > 1) //if searchMenu has old search terms ... 
						searchMenu.getItems().remove(1, searchMenu.getItems().size()); //remove all except the first "Search..." menu item

					List<String> updatedSearchTerms = searchTerms.getSearchTerms(); //get search strings from the updated search terms list

					//add updatedSearchTerms to search menu and attach event handler to all of them
					for (int i = 0; i < updatedSearchTerms.size(); i++) {
						MenuItem searchMenuItem = new MenuItem(updatedSearchTerms.get(i));
						searchMenu.getItems().add(searchMenuItem);
						searchMenuItem.setOnAction(searchToolHandler);
					}
					/*** end update queue */
				} else {
					statusLabel.setText(String.format("%s not found", searchString));
				}
			} else {
				statusLabel.setText("Search cancelled");
			}
		}
	}


	//Bound to Tools -> Word Count menu item
	private class WordCountHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			fileUtilities.buildWordTree(fileTextArea.getText());  //refresh wordTree
			statusLabel.setText(String.format("%d words", fileUtilities.countWords(fileTextArea.getText())));
		}
	}

	//Bound to Tools -> Unique word count menu item
	private class UniqueWordCountHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			fileUtilities.buildWordTree(fileTextArea.getText());  //refresh wordTree
			statusLabel.setText(String.format("%d unique words", fileUtilities.countUniqueWords(fileTextArea.getText())));
		}
	}

	//Bound to Help -> About menu item
	private class AboutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("TrieWrite");
			alert.setContentText("Version 1.0 \nRelease 1.0\nCopyLeft Sooner\nDeveloped by a Sooner!");
			Image image = new Image(this.getClass().getResource("Sooner.jpg").toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}
}
