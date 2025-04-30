package triewrite;

import java.util.ArrayList;
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
		TrieNode current = root;
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.isEndOfWord = true; // Mark the end of the word
	}

	/**getWordsWithPrefix() returns a list of all words that have the given prefix. 
	 * -----------This method is tested in JUnit test-cases.----------
	 */
	List<String> getWordsWithPrefix(String prefix) {
		//write your code here
		List<String> result = new ArrayList<>();
        TrieNode current = root;

        // Traverse the Trie to find the node corresponding to the prefix
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return result; // If prefix is not found, return an empty list
            }
            current = current.children.get(c);
        }

        // Perform a DFS from the prefix node to collect all words
        collectWords(current, new StringBuilder(prefix), result);
        return result;
	}

	private void collectWords(TrieNode node, StringBuilder prefix, List<String> result) {
        if (node.isEndOfWord) {
            result.add(prefix.toString());
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            collectWords(entry.getValue(), prefix, result);
            prefix.deleteCharAt(prefix.length() - 1); // Backtrack
        }
    }
}
