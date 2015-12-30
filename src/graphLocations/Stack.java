package graphLocations;
import graphLocations.*;


/**
 * @author Alex
 * Stack of non-negative integer Nodes (linked list structure)
 */
public class Stack {
	Node top; 
	
	public Stack() {
		top = null;
	}

	public boolean isEmpty() {
		if (top == null) return true;
		else return false;
	}
	
	public int pop() {
		if (this.isEmpty()) return -1;
		else {
			Node toRemove = top;
			top = top.getNext(); //moves front position forward 1
			return toRemove.getValue(); //returns object in previous position of front 
		}
	}
	
	public void push(int x) {
		top = new Node(x, top);
	}
	
	public int peek() {
		if (top == null) return -1;
		else return top.getValue();
	}
	
	public int size() {
		Node index = top;
		int count = 0;
		while (index != null) {
			count++;
			index = index.getNext();
		}
		return count;
	}
	
	public int[] toArray() {
		int size = this.size();
		int[] asArray = new int[size];
		Node index = top;
		for (int i=0; i<size; i++) {
			asArray[i] = index.getValue();
			index = index.getNext();
		}
		return asArray;
	}
	
	public int[] toArrayReversed() {
		int size = this.size();
		int[] asArray = new int[size];
		Node index = top;
		for (int i=size-1; i>=0; i--) {
			asArray[i] = index.getValue();
			index = index.getNext();
		}
		return asArray;
	}
	
	public boolean contains(int x) {
		Node index = top;
		while (index != null) {
			if (index.getValue() == x) {
				return true;
			}
			index = index.getNext();
		}
		return false;
	}
	
	public String toString() {
		String s = new String();
		Node index = top;
		while (index != null) {
			s = s + "\n" + index.getValue();
			index = index.getNext();
		}
		return s;
	}
}
