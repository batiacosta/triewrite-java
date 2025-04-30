package triewrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestTrie {
	
	Trie trie;
	
	@Before
	public void setup() throws Exception {
		//setup
		trie = new Trie();
	}

	//Test inserting one word and walk through the trie, test value at each node,
	//till the last node which should have isEndOfWord set to true.
	@Test
	public void testInsertOneWord() {
		//test from root - level 0
		trie.insert("apple");
		assertEquals(1, trie.root.children.size()); //only one child with 'a'
		assertTrue(trie.root.children.containsKey('a'));
		
		//move to level 1 and test
		Trie.TrieNode nextLevelNode = trie.root.children.get('a'); 
		assertEquals(1, nextLevelNode.children.size());
		assertTrue(nextLevelNode.children.containsKey('p'));
		assertFalse(nextLevelNode.isEndOfWord);
		
		//move to level 2 and test
		nextLevelNode = nextLevelNode.children.get('p'); 
		assertTrue(nextLevelNode.children.containsKey('p'));
		assertFalse(nextLevelNode.isEndOfWord);
		
		//move to level 3 and test
		nextLevelNode = nextLevelNode.children.get('p');
		assertTrue(nextLevelNode.children.containsKey('l'));
		assertFalse(nextLevelNode.isEndOfWord);
		
		//move to level 4 and test
		nextLevelNode = nextLevelNode.children.get('l');
		assertTrue(nextLevelNode.children.containsKey('e'));
		assertFalse(nextLevelNode.isEndOfWord);
		
		//move to level 5 and test
		nextLevelNode = nextLevelNode.children.get('e');
		assertTrue(nextLevelNode.isEndOfWord);
	}
	
	//Test inserting two words with no common prefix. 
	//Test trie size, and node contents
	@Test
	public void testInsertTwoWordsWithNoCommonPrefix() {
		trie.insert("apple");
		trie.insert("banana");
		assertEquals(2, trie.root.children.size()); //root has two children 'a' and 'b'
		assertTrue(trie.root.children.containsKey('a'));
		assertTrue(trie.root.children.containsKey('b'));
	}
	
	
	//Test inserting two words with common prefix
	@Test
	public void testInsertTwoWordsWithCommonPrefix() {
		
		//test another word with same first character
		trie.insert("apple");
		trie.insert("apply");
		assertEquals(1, trie.root.children.size()); //root has two children 'a' and 'b'
		
		Trie.TrieNode nextLevelNode = trie.root.children.get('a'); 
		nextLevelNode = nextLevelNode.children.get('p'); 
		nextLevelNode = nextLevelNode.children.get('p'); 
		nextLevelNode = nextLevelNode.children.get('l'); 
		
		assertEquals(2, nextLevelNode.children.size());  //Two branches from here
		assertTrue(nextLevelNode.children.containsKey('e'));
		assertTrue(nextLevelNode.children.containsKey('y'));
	}


	//Test the number of words in trie with different prefixes.
	@Test
	public void testGetWordsWithPrefix() {

		//setup
		Trie trie = new Trie();

		trie.insert("apple");
		trie.insert("apply");
		trie.insert("application");
		trie.insert("applicable");
		trie.insert("ban");
		trie.insert("banana");

		//test
		assertEquals(4, trie.getWordsWithPrefix("app").size());
		assertEquals(4, trie.getWordsWithPrefix("appl").size());
		assertEquals(1, trie.getWordsWithPrefix("apple").size());
		assertEquals(1, trie.getWordsWithPrefix("application").size());
		assertEquals(2, trie.getWordsWithPrefix("ban").size());
		assertEquals(0, trie.getWordsWithPrefix("abc").size());
		assertEquals(0, trie.getWordsWithPrefix("xyz").size());
	}

}