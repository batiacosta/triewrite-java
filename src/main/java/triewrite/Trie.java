package triewrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Trie implementation for word storage and retrieval to be 
 * used in prefix-based searching in auto-complete functionality.
 */
public class Trie {

	//Inner class for creating nodes for Trie.
	class TrieNode {
		Map<Character, TrieNode> children = new HashMap<>(); //stores child nodes for each character
		boolean isEndOfWord; //true if the node has a character that marks the ends of a word
	}
	
	TrieNode root; 

	//Trie constructor initializes the root.
	Trie() {
		root = new TrieNode();
	}

	/** insert() method inserts word in Trie. It starts from the root
	 * and traverses the trie looking for each character in the word. It adds
	 * the character when not found in trie. 
	 * Please refer to Figure 13.3.1 to see a visual representation of a trie 
	 * to be implemented here.
	 * -----------This method is tested in JUnit test-cases.----------
	 */
	void insert(String word) {
		//write your code here
	}

	/**getWordsWithPrefix() returns a list of all words that have the given prefix. 
	 * -----------This method is tested in JUnit test-cases.----------
	 */
	List<String> getWordsWithPrefix(String prefix) {
		//write your code here
		return null;
	}
}
