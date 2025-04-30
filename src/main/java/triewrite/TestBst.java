package triewrite;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestBst {

	BinarySearchTree<Integer> numberTree = new BinarySearchTree<>();
	BinarySearchTree<Integer> emptyTree = new BinarySearchTree<>();

	@Before
	public void setup() throws Exception {
		numberTree.insert(50);
		numberTree.insert(30);
		numberTree.insert(80);
		numberTree.insert(20);
		numberTree.insert(100);
		numberTree.insert(40);
		numberTree.insert(70);
		numberTree.insert(55);
		numberTree.insert(35);
		numberTree.insert(85);
		numberTree.insert(25);
		numberTree.insert(105);
		numberTree.insert(45);
		numberTree.insert(75);
	}
	

	@Test
	public void testSize() {
		assertEquals("Test size on numberTree", 14, numberTree.size());
		assertEquals("Test size on emptyTree", 0, emptyTree.size());
	}
	
	@Test
	public void testRoot() {
		assertEquals("Test root on numberTree", Integer.valueOf(50), numberTree.root().getElement());
		assertEquals("Test root on emptyTree", null, emptyTree.root());
	}
	@Test
	public void testIsEmpty() {
		assertEquals("Test isEmpty on numberTree", false, numberTree.isEmpty());
		assertEquals("Test isEmpty on emptyTree", true, emptyTree.isEmpty());
	}
	
	@Test
	public void testChildren() {
		assertEquals("Test children on numberTree", Integer.valueOf(30), numberTree.children(numberTree.root()).iterator().next().getElement());
		assertEquals("Test children on emptyTree", null, emptyTree.children(emptyTree.root()));
	}
	
	@Test
	public void testHeight() {
		assertEquals("Test height on numberTree root node", 3, numberTree.height(numberTree.root()));
		assertEquals("Test height on numberTree leaf node", 0, numberTree.height(numberTree.find(25)));
		assertEquals("Test height on emptyTree", 0, emptyTree.height(emptyTree.root()));
	}
	
	@Test
	public void testDepth() {
		assertEquals("Test depth on numberTree", 0, numberTree.depth(numberTree.root()));
		assertEquals("Test depth on numberTree", 3, numberTree.depth(numberTree.find(25)));
		assertEquals("Test depth on emptyTree", 0, emptyTree.height(emptyTree.root()));
	}
	
	@Test
	public void testFindMin() {
		assertEquals("Test find minimum on numberTree", Integer.valueOf(20), numberTree.findMin(numberTree.root()).getElement());
	}
	
	@Test 
	public void testFind() {
		assertEquals("Test find an existing number on numberTree", Integer.valueOf(20), numberTree.find(Integer.valueOf(20)).getElement());
		assertEquals("Test find a non-existent number on numberTree", null, numberTree.find(Integer.valueOf(2)));
	}
	
	@Test
	public void testDelete() {
		numberTree.delete(numberTree.find(20));
		assertEquals("Test deleting node with single child", null, numberTree.find(20));
		assertEquals("Test child parent update after parent is deleted", Integer.valueOf(30), numberTree.find(25).getParent().getElement());
		assertEquals("Test parent update after child is deleted and grandchild attached", Integer.valueOf(25), numberTree.find(30).getLeft().getElement());
		
		numberTree.delete(numberTree.find(25));
		assertEquals("Test deleting node with no children", null, numberTree.find(25));
		assertEquals("Test size after deleting one node", 12, numberTree.size());
		assertEquals("Test parent update after deleting node", null, numberTree.find(30).getLeft());	
		
		numberTree.delete(numberTree.find(50));
		assertEquals("Test new root after root delete", 55, numberTree.root().getElement().intValue());
	}
	
	@Test
	public void testInOrder() {
		List<BinarySearchTree.Node <Integer>> inorderList = numberTree.inorder(numberTree.root());
		Iterator<BinarySearchTree.Node<Integer>> iter = inorderList.iterator();
		assertEquals("Test inorder traversal", Integer.valueOf(20), iter.next().getElement());
		assertEquals("Test inorder traversal", Integer.valueOf(25), iter.next().getElement());
		assertEquals("Test inorder traversal", Integer.valueOf(30), iter.next().getElement());
		assertEquals("Test inorder traversal", Integer.valueOf(35), iter.next().getElement());
	}

}
