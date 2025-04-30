package triewrite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/** FileUtilities class provides basic methods to read and write a file, 
 * and search and count words. It uses Binary Search Tree to carry out these functions. 
 */
public class FileUtilities {


	BinarySearchTree<Word> wordTree = new BinarySearchTree<>();  //To store words in the document

	/**readFile() opens the file with filename, 
	 * extracts content into a StringBuilder
	 * and returns the StringBuilder
s	 */
	StringBuilder readFile(String fileName)  {
		File file = new File(fileName);
		StringBuilder fileContent = new StringBuilder();
		try {
			Scanner input = new Scanner (file);
			while (input.hasNextLine()) {
				fileContent.append(input.nextLine() + "\n");
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return fileContent;				
	}
	
	/** writeFile() takes fileName and fileContent
	 * and writes it to the disk in the project folder.
	 */
	String writeFile(String fileName, String fileContent) {
		try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName))) {
			br.write(fileContent );
			return "File saved";

		} catch (IOException e) {
			return "Could not save file";
		}
	}

	/**buildWordTree() takes a text string, tokenizes it into words 
	 * and adds these words into the wordTree.
	 * The regex to find delimiters to tokenize is " .,!?;:\"()[]{}<>-\n"
	 * -----------This method is tested in JUnit test-cases.----------
	 */
	void buildWordTree(String text) {
		//write your code here
		String[] words = text.split("[ .,!?;:\"()\\[\\]{}<>\\-\\n]+"); // Tokenize the text
		System.out.println("Total words: " + words.length);

		int position = 0; // Track the position of each character in the text
		for (String word : words) {
			if (!word.isBlank()) { // Ensure the word is not empty
				// Find the starting position of the word
				position = text.indexOf(word, position);

				Word wordObj = new Word(word.toLowerCase(), position);
				BinarySearchTree.Node<Word> existingNode = wordTree.find(wordObj);

				if (existingNode != null) {
					// If the word already exists, add the position to its list
					existingNode.getElement().positions.add(position);
				} else {
					// If the word does not exist, insert it into the tree
					wordTree.insert(wordObj);
				}

				// Move the position forward for the next word
				position += word.length(); // Ensure position moves past the current word
			}
		}

		System.out.println("Word tree size: " + wordTree.size());
	}

	/** getWordPositions() takes a search string,
	 * searches for it in the words in the wordTree, and if found, 
	 * returns its positions in the document as a List.
	 * -----------This method is tested in JUnit test-cases.----------
	 */
	List<Integer> getWordPositions(String searchString) {
		//write your code here
		Word searchWord = new Word(searchString.toLowerCase(), 0);
        BinarySearchTree.Node<Word> node = wordTree.find(searchWord);
        if (node != null) {
            return node.getElement().positions;
        }
        return new ArrayList<>();
	}


	/** wordCount() takes a string, builds its wordTree,
	 * and returns the total number of words in
	 * the document.  
	 * -----------This method is tested in JUnit test-cases.---------- 
	 */
	int countWords(String fileContent) {
		//write your code here;
		wordTree = new BinarySearchTree<>();
		buildWordTree(fileContent);

		int count = 0;
		List<BinarySearchTree.Node<Word>> nodes = wordTree.inorder(wordTree.root());
		for (BinarySearchTree.Node<Word> node : nodes) {
			count += node.getElement().positions.size();
		}
		return count;
	}

	/** countUniqueWords() takes a string, builds its wordTree,
	 * and returns unique words in the tree.
	 *-----------This method is tested in JUnit test-cases.----------
	 */
	int countUniqueWords(String fileContent) {
		//write your cide here
		buildWordTree(fileContent);
        return wordTree.size();
	}

}
