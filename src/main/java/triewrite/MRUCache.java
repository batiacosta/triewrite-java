package triewrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** MRUCache (Most recently used cache) class stores a fixed number of the most recently used 
 * search terms. It supports insertion, update, and deletion using a combination of:
 * - Doubly Linked List (to maintain MRU order)
 * - HashMap (for O(1) access to nodes)
 */
class MRUCache {
	int capacity;  //max number of search terms to store
	HashMap<String, MRUNode> cache;  // Maps search terms to nodes in the linked list
	MRULinkedList recentList; // Maintains order of terms from MRU (head) to LRU (tail)

	// Constructor to initialize cache, recentList, and capacity
	MRUCache(int capacity) {
		this.capacity = capacity;
		this.cache = new HashMap<>();
		this.recentList = new MRULinkedList();
	}

	/** addSearchTerm() adds a search term to the MRU cache.
     * It moves the term to the front if it already exists.
     * It removes the least recently used item if capacity is exceeded.
     * -----------This method is tested in JUnit test-cases.----------
     */
	void addSearchTerm(String term) {
		//write your code here
		if (cache.containsKey(term)) {
            // If the term already exists, move it to the front
            MRUNode node = cache.get(term);
            recentList.moveToFront(node);
        } else {
            // If the term is new, create a new node
            MRUNode newNode = new MRUNode(term);
            recentList.addToFront(newNode);
            cache.put(term, newNode);

            // If capacity is exceeded, remove the least recently used term
            if (cache.size() > capacity) {
                MRUNode lruNode = recentList.removeLast();
                cache.remove(lruNode.term);
            }
        }
	}

	
	/** getSearchTerms() returns a list of all search terms in MRU order
	 * with the most recent term as the first element in the list.
	 * -----------This method is tested in JUnit test-cases.----------
	 */
	List<String> getSearchTerms() {
		
		//write your code here
		List<String> terms = new ArrayList<>();
        MRUNode current = recentList.head.next;
        while (current != recentList.tail) {
            terms.add(current.term);
            current = current.next;
        }
        return terms;
	}

	/** size() returns the size of cache.
	 */
	int size() {
		return cache.size();
	}

	
	/************************************ MRULinkedList implementation below************************/

	/** MRUNode is an inner class that has a string term, and pointers to 
	 * its next and previous terms in a doubly linked list MRULinkedList.
	 */
	class MRUNode {
		String term;
		MRUNode prev, next;

		public MRUNode(String term) {
			this.term = term;
		}
	}

	/** MRULinkedList is an inner class used in MRUCache
	 * to maintain order of search terms. It uses MRUNode.
	 */
	class MRULinkedList {
		MRUNode head, tail;

		
		//Initialize head and tail nodes with term as null in both.
		//The head's next will point to tail and
		//the tail's prev will point to head.
		//-----------This method is tested in JUnit test-cases.----------
		MRULinkedList() {
			//write your code here
			head = new MRUNode(null);
            tail = new MRUNode(null);
            head.next = tail;
            tail.prev = head;
		}

		// Add a new node to the front
		//-----------This method is tested in JUnit test-cases.----------
		void addToFront(MRUNode node) {
			//write your code here
			node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
		}

		// Move an existing node to the front
		//-----------This method is tested in JUnit test-cases.----------
		void moveToFront(MRUNode node) {
			//write your code here
			node.prev.next = node.next;
            node.next.prev = node.prev;

            addToFront(node);
		}


		// Remove the last node (least recently used)
		//-----------This method is tested in JUnit test-cases.----------
		MRUNode removeLast() {
			//write your code here
			if (tail.prev == head) {
                return null; // List is empty
            }
            MRUNode lruNode = tail.prev;
            lruNode.prev.next = tail;
            tail.prev = lruNode.prev;
            return lruNode;
		}

	}
}
