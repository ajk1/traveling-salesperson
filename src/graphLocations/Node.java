/**
 * 
 */
package graphLocations;

/**
 * @author Alex
 * linked node holds integer and is implemented in Stack
 */
public class Node {
	int value;
	Node next;
	
	public Node(int value, Node next) {
		this.value = value;
		this.next = next;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Node getNext() {
		return next;
	}
	public void setNext(Node next) {
		this.next = next;
	}
}
