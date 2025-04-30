package triewrite;

import java.util.ArrayList;
import java.util.List;

//Binary Search Tree is same as Lab 7
public class BinarySearchTree<E extends Comparable <? super E>> {

	private Node<E> root = null;
	private int size = 0;

	public static class Node<E> {
		private E element;
		private Node<E> parent;
		private Node<E> left;
		private Node<E> right;

		Node(E element) {
			//write your code here
			this.element = element;
		}

		/**accessor methods*/
		E getElement() {
			//write your code here
			if(element == null) return null;
			return element;
		}
		Node<E> getParent() {
			//write your code here
			if(parent == null) return null;
			return parent;
		}
		Node<E> getLeft() {
			//write your code here
			if(left == null) return null;
			return left;
		}
		Node<E> getRight() {
			//write your code here
			if(right == null) return null;
			return right;
		}

		/**update methods */
		void setElement(E e) throws IllegalArgumentException {
			//write your code here
			if(e == null) throw new IllegalArgumentException("Element cannot be null");
			element = e;
		}
		void setParent(Node<E> parentNode) {
			//write your code here
			parent = parentNode;
		}
		void setLeft(Node<E> left) {
			//write your code here
			this.left = left;
		}
		void setRight(Node<E> right) {
			//write your code here
			this.right = right;
		}

		/**misc methods */
		int numChildren() {
			//write your code here
			int count = 0;
			if(left != null) count++;
			if(right != null) count++;
			return count;
		}

	} //end Node class

	Node<E> root() {
		//write your code here
		if(root == null) return null;
		return root;
	}

	int height(Node<E> node) {
		//write your code here
		if(node == root && size == 0) return 0;
		if(node == null) return -1;
		return Math.max(height(node.getLeft()), height(node.getRight())) + 1;
	}

	int depth(Node<E> node) {
		//write your code here
		if(node == null || node == root) return 0;
		return depth(node.getParent()) + 1;
	}

	/** Returns a list of nodes that are children of the given node */
	List<Node<E>> children(Node<E> node) {
		//write your code here
		List<Node<E>> children = new ArrayList<>();
		if(node == null ) return null;
		if(node.getLeft() != null) children.add(node.getLeft());
		if(node.getRight() != null) children.add(node.getRight());
		return children;
	}

	/**Returns the node with minimum value starting downward from a node */
	Node<E> findMin(Node<E> node) {
		//write your code here
		if(node == null) return null;
		while (node.getLeft() != null) {
			node = node.getLeft();
		}
		return node;

	}

	public boolean isEmpty() {
		//write your code here
		return size == 0;
	}

	public int size() {
		//write your code here
		return size;
	}

	/** Inserts a new node with the element. Throws IllegalArgumentException if it already exists */
	public void insert (E element) throws IllegalArgumentException {
		//write your code here
		if(element == null) throw new IllegalArgumentException("Element cannot be null");
		Node<E> newNode = new Node<>(element);
		if(root == null) {
			root = newNode;
			size++;
			return;
		}
		Node<E> current = root;
		Node<E> parent = null;
		while(current != null) {
			parent = current;
			if(element.compareTo(current.getElement()) < 0) {
				current = current.getLeft();
			} else if(element.compareTo(current.getElement()) > 0) {
				current = current.getRight();
			} else {
				throw new IllegalArgumentException("Element already exists in the tree");
			}
		}
		newNode.setParent(parent);
		if(element.compareTo(parent.getElement()) < 0) {
			parent.setLeft(newNode);
		} else {
			parent.setRight(newNode);
		}
		size++;
	}

	/** Deletes a node */
	public void delete(Node<E> node) {
		//write your code here
		if(node == null) return;
		if(node.numChildren() == 0) { // leaf node
			if(node.getParent() != null) {
				if(node.getParent().getLeft() == node) {
					node.getParent().setLeft(null);
				} else {
					node.getParent().setRight(null);
				}
			} else {
				root = null;
			}
		} else if(node.numChildren() == 1) { // one child
			Node<E> child = (node.getLeft() != null) ? node.getLeft() : node.getRight();
			if(node.getParent() != null) {
				if(node.getParent().getLeft() == node) {
					node.getParent().setLeft(child);
				} else {
					node.getParent().setRight(child);
				}
			} else {
				root = child;
			}
			child.setParent(node.getParent());
		} else { // two children
			Node<E> successor = findMin(node.getRight());
			node.setElement(successor.getElement());
			delete(successor);
		}
		size--;
	}



	/** Returns the node containing the element if found, else null */
	public Node<E> find(E element) {
		//write your code here
		Node<E> current = root;
		while(current != null) {
			if(element.compareTo(current.getElement()) < 0) {
				current = current.getLeft();
			} else if(element.compareTo(current.getElement()) > 0) {
				current = current.getRight();
			} else {
				return current;
			}
		}
		return null;
	}

	/** Returns a list of nodes using in-order traversal given the root node of the tree */
	public List <Node <E>> inorder(Node<E> root) {
		//write your code here
		List<Node<E>> snapshot = new ArrayList<>();
		inorderSubtree(root, snapshot);
		return snapshot;
	}

	/** Adds children of the subtree rooted at node to the given snapshot in inorder */
	private void inorderSubtree(Node<E> node, List<Node<E>> snapshot) {
		//write your code here
		if(node == null) return;
		inorderSubtree(node.getLeft(), snapshot);
		snapshot.add(node);
		inorderSubtree(node.getRight(), snapshot);
	}
}

