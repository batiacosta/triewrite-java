package triewrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestFileUtilities {
 
	FileUtilities fileUtilities = new FileUtilities();
	StringBuilder fileContent;

	@Before
	public void setup() throws Exception {
		fileContent = fileUtilities.readFile("sample.txt");
		System.out.println(fileContent);
		fileUtilities.buildWordTree(fileContent.toString());
	}
	

	//Test if all words are captured.
	@Test
	public void testWordTreeSize() {
		assertEquals(23, fileUtilities.wordTree.size());

	}
	
	//Test if all the count of recurrence of words is counted correctly.
	@Test
	public void testWordCountOfPositions() {
		List<BinarySearchTree.Node<Word>> words = fileUtilities.wordTree.inorder(fileUtilities.wordTree.root());
		for (BinarySearchTree.Node<Word> word : words) {
			switch (word.getElement().word.toLowerCase()) {
			case "This": assertEquals(2, word.getElement().positions.size()); break;
			case "sample": assertEquals(1, word.getElement().positions.size()); break;
			case "text": assertEquals(3, word.getElement().positions.size()); break;
			}
		}
	}
	
	
	//Test if the character positions of each word are captured correctly. 
	//Note that position count starts from 0 and newline characters are hidden but are counted.
	@Test
	public void testWordPositions() {
		List<BinarySearchTree.Node<Word>> words = fileUtilities.wordTree.inorder(fileUtilities.wordTree.root());
		for (BinarySearchTree.Node<Word> word : words) {
			switch (word.getElement().word.toLowerCase()) {
			case "this": assertTrue(word.getElement().positions.contains(0) && word.getElement().positions.contains(23)); break;
			case "file": assertTrue(word.getElement().positions.contains(17) && word.getElement().positions.contains(28)); break;
			default: break;
			}
		}
	}
	
	//Test getWordPositions() method
	@Test
	public void testGetWordPositions() {
		List<Integer> positions = fileUtilities.getWordPositions("text");
		assertEquals(3, positions.size());
		assertTrue(positions.contains(70));
		assertTrue(positions.contains(87));
		assertTrue(positions.contains(112));
	}
	
	//Test countWords() method
	@Test
	public void testCountWords() {
		assertEquals(27, fileUtilities.countWords(fileContent.toString()));
	}
	
	//Test countUniqueWords() method
	@Test
	public void testCountUniqueWords() {
		assertEquals(23, fileUtilities.countUniqueWords(fileContent.toString()));
	}
	
}
