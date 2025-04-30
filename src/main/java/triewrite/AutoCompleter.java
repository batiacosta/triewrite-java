package triewrite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCode;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


/** AutoCompleter is a helper class to provide autocomplete functionality
 * for a javaFX-based text editor using a Trie for work lookup
 */
public class AutoCompleter {
    private final Trie trie;  //Trie for word searching
    private final PopupControl suggestionPopup; //Popup to display autocomplete suggestions 
    private final ListView<String> suggestionList; //Listview to show suggestions in the popup
    private final ObservableList<String> suggestions; //Observable list to dynamically update suggestions
    private StyleClassedTextArea textArea; //Reference to the text editor

    //Initialize AutoCompleter components
    AutoCompleter() {
        trie = new Trie();
        suggestions = FXCollections.observableArrayList();
        suggestionList = new ListView<>(suggestions);
        suggestionPopup = new PopupControl();
        suggestionPopup.setAutoHide(true);
        suggestionPopup.getScene().setRoot(suggestionList);
        setupSuggestionSelection();
    }

    //Bind autocomplete functionality to the search area
    void bindToTextArea(StyleClassedTextArea textArea) {
        this.textArea = textArea;
        
        //Handle keyboard events
        textArea.setOnKeyPressed(event -> {
            if (suggestionPopup.isShowing()) {
                if (event.getCode() == KeyCode.DOWN) {  //Move down in selection list when down key pressed
                    suggestionList.requestFocus();
                    suggestionList.getSelectionModel().select(0);
                    event.consume();
                } else if (event.getCode() == KeyCode.ESCAPE) { //hide the popup whe Esc key pressed
                    suggestionPopup.hide();
                }
            }
        });

        //Listen for type characters and trigger autocomplete
        textArea.setOnKeyTyped(event -> {
            String currentWord = getCurrentWord(); //extract the current work being typed
            if (!currentWord.isEmpty()) {
                List<String> matches = trie.getWordsWithPrefix(currentWord.toLowerCase()); //Fetch matching words from Trie
                if (!matches.isEmpty()) {
                    showSuggestions(matches); //Display matching suggestions in the popup
                } else {
                    suggestionPopup.hide(); //else hide the popup if no match
                }
            } else {
                suggestionPopup.hide();
            }
        });
    }

    
    //Load words from external file to the Trie
    void loadWordsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                trie.insert(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Failed to load words: " + e.getMessage());
        }
    }
    
    //Show autocomplete suggestions
    private void showSuggestions(List<String> matches) {
        suggestions.setAll(matches); //Update the suggestion list
        if (!suggestionPopup.isShowing()) {
            Bounds caretBounds = textArea.getCaretBounds().orElse(null);  //get current cursor position
            if (caretBounds != null) {
            	//show the popup near the cursor position
                suggestionPopup.show(textArea, caretBounds.getMinX() + textArea.getLayoutX(),
                        caretBounds.getMaxY() + textArea.getLayoutY());
            }
        }
    }

    //Setup keyboard and mouse event handlers for selecting autocomplete suggestions
    private void setupSuggestionSelection() {
    	
    	//Handle selection using keyboard
        suggestionList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                insertSelectedSuggestion();
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                suggestionPopup.hide();
            }
        });

        //Handle selection using mouse click
        suggestionList.setOnMouseClicked(event -> insertSelectedSuggestion());
    }

    //Insert the selected autocomplete suggestion in the text editor
    private void insertSelectedSuggestion() {
        String selected = suggestionList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String currentWord = getCurrentWord(); //get the current word being typed
            int start = textArea.getCaretPosition(); //find start position of the word;
            textArea.replaceText(start, textArea.getCaretPosition(), selected.substring(currentWord.length())); //Replace typed word with suggestion
            suggestionPopup.hide(); //Close the popup after suggestion
        }
    }

    //Extract the current word being typed
    private String getCurrentWord() {
        int caretPos = textArea.getCaretPosition();
        String text = textArea.getText().substring(0, caretPos);
        int lastSpaceIndex = Math.max(text.lastIndexOf(" "), text.lastIndexOf("\n")); //find last space or newline for the word beginning
        return text.substring(lastSpaceIndex + 1);
    }
}

