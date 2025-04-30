package triewrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestMRUCache {
	
	public static final int MRU_SIZE = 5;
	MRUCache mruCache;

	@Before
	public void setup() throws Exception {
		mruCache = new MRUCache(MRU_SIZE);
		mruCache.addSearchTerm("agate");
		mruCache.addSearchTerm("bloodstone");
		mruCache.addSearchTerm("coral");
		mruCache.addSearchTerm("diamond");
		mruCache.addSearchTerm("emerald");
	}

	//Test size of mruCache
	@Test
	public void testCacheSize() {
		assertEquals(MRU_SIZE, mruCache.capacity);
	}
	
	//Test that addSearchTerm() adds terms in mruCache's cache.
	@Test
	public void testCacheAfterAddSearchTerm() {
		assertTrue(mruCache.cache.containsKey("agate"));
		assertTrue(mruCache.cache.containsKey("bloodstone"));
		assertTrue(mruCache.cache.containsKey("coral"));
		assertTrue(mruCache.cache.containsKey("diamond"));
		assertTrue(mruCache.cache.containsKey("emerald"));
	}
	
	
	//Test that addSearchTerm() adds terms in mruCache's recentList 
	//in order so that the most recent term is at the head.
	@Test
	public void testRecentListAfterAddSearchTerm() {
		MRUCache.MRUNode currentNode = mruCache.recentList.head.next;
		for (int i = 0; i < 5; i++) {
			switch (i) {
			case 0: {
				currentNode.term.equals("emerald"); 
				currentNode = currentNode.next;
				break;
			}
			case 1: {
				currentNode.term.equals("diamond"); 
				currentNode = currentNode.next;
				break;
			}
			case 2: {
				currentNode.term.equals("coral"); 
				currentNode = currentNode.next;
				break;
			}
			case 3: {
				currentNode.term.equals("bloodstone"); 
				currentNode = currentNode.next;
				break;
			}
			case 4: {
				currentNode.term.equals("agate"); 
				currentNode = currentNode.next;
				break;
			}
			default: break;
			
			}
			
		}
	}
	
	//Test the terms returned by mruCache are in correct order
	//with most recent search term as first and least recent as last.
	@Test
	public void testGetSearchTerms( ) {
		List<String> terms = mruCache.getSearchTerms();
		assertTrue(terms.get(0).equals("emerald")); 
		assertTrue(terms.get(1).equals("diamond")); 
		assertTrue(terms.get(2).equals("coral")); 
		assertTrue(terms.get(3).equals("bloodstone")); 
		assertTrue(terms.get(4).equals("agate")); 
	}
	
	
	/************************ Tests for MRULinkedList *************************/
	
	//Test MRULinkedList() constructor initializes the head and tail nodes correctly.
	@Test
	public void testMRULinkedListConstructor() {
		MRUCache.MRULinkedList mruLinkedList = mruCache.new MRULinkedList();
		assertEquals(mruLinkedList.head.next, mruLinkedList.tail);
		assertEquals(mruLinkedList.tail.prev, mruLinkedList.head);
		assertEquals(mruLinkedList.head.term, null);
		assertEquals(mruLinkedList.tail.term, null);
	}
	
	//Test addToFront() method by creating and adding nodes and checking if 
	//they are added in correct order, with head, tail, prev, and next pointers
	//set correctly after each addition.
	@Test 
	public void testAddToFront() {
		MRUCache.MRULinkedList mruLinkedList = mruCache.new MRULinkedList();
		//Create and add first node
		MRUCache.MRUNode mruNode1 = mruCache.new MRUNode("agate");
		mruLinkedList.addToFront(mruNode1);
		assertEquals(mruLinkedList.head.next, mruNode1);
		assertEquals(mruNode1.next, mruLinkedList.tail);
		
		//Create and add second node
		MRUCache.MRUNode mruNode2 = mruCache.new MRUNode("bloodstone");
		mruLinkedList.addToFront(mruNode2);
		assertEquals(mruLinkedList.head.next, mruNode2);
		assertEquals(mruNode2.next, mruNode1);
		assertEquals(mruNode2.prev, mruLinkedList.head);
		assertEquals(mruNode1.prev, mruNode2);
	}
	
	//Test removeLast() by first adding two nodes and then removing last. 
	@Test
	public void testRemoveLast() {
		MRUCache.MRULinkedList mruLinkedList = mruCache.new MRULinkedList();
		//Create and add first term
		MRUCache.MRUNode mruNode1 = mruCache.new MRUNode("agate");
		mruLinkedList.head.next = mruNode1;
		mruNode1.next = mruLinkedList.tail;
		mruNode1.prev = mruLinkedList.head;
		mruLinkedList.tail.prev = mruNode1;
		
		//Create and add second term
		MRUCache.MRUNode mruNode2 = mruCache.new MRUNode("bloodstone");
		mruLinkedList.head.next = mruNode2;
		mruNode2.next = mruNode1;
		mruNode2.prev = mruLinkedList.head;
		mruNode1.prev = mruNode2;
		
		assertEquals(mruNode1, mruLinkedList.removeLast()); //this should remove the node with agate
		assertEquals(mruNode2, mruLinkedList.removeLast()); //this should remove the node with bloodstone
		assertEquals(null, mruLinkedList.removeLast()); //this should return null
		
	}
	
	//Test moveToFront() by first adding two nodes and then moving last one to front. 
	@Test
	public void testMoveToFront() {
		MRUCache.MRULinkedList mruLinkedList = mruCache.new MRULinkedList();
		//Create and add first term
		MRUCache.MRUNode mruNode1 = mruCache.new MRUNode("agate");
		mruLinkedList.head.next = mruNode1;
		mruNode1.next = mruLinkedList.tail;
		mruNode1.prev = mruLinkedList.head;
		mruLinkedList.tail.prev = mruNode1;
		
		//Create and add second term
		MRUCache.MRUNode mruNode2 = mruCache.new MRUNode("bloodstone");
		mruLinkedList.head.next = mruNode2;
		mruNode2.next = mruNode1;
		mruNode2.prev = mruLinkedList.head;
		mruNode1.prev = mruNode2;
		
		//Now move "agate" node to front
		mruLinkedList.moveToFront(mruNode1);
		
		assertEquals(mruLinkedList.head.next, mruNode1); //agate as moved towards head
		assertEquals(mruLinkedList.tail.prev, mruNode2); //bloodstone has moved towards tail
		
	}
}




